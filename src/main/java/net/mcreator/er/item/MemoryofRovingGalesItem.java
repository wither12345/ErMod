package net.mcreator.er.item;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.StellaFortunas;
import net.mcreator.er.client.model.animations.travelerAnimation;
import net.mcreator.er.entity.TravelerTornadoEntity;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModParticleTypes;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.entity.EnergyOrb;
import net.wither.er.init.ElementRegistry;
import net.wither.er.network.ErCombatVariables;

import java.util.Comparator;

public class MemoryofRovingGalesItem extends StellaFortunas {
    private static final ResourceLocation TRAVELER_SKILL = new ResourceLocation(ErMod.MODID, "traveler_skill");
    public static final ResourceLocation TRAVELER_BURST = new ResourceLocation(ErMod.MODID, "traveler_burst");

	public MemoryofRovingGalesItem() {
		super(new Properties().stacksTo(1).rarity(Rarity.RARE), 20, 2.5f, 57.23f, true);
	}

	public void onTick(LevelAccessor world, LivingEntity entity, double x, double y, double z) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
	}

	public void ElementalSkillStart(LivingEntity entity) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (vars.skillCooldown <= 0) {
			vars.animationTime = 1000;
			vars.animationId = 20;
		}
		vars.syncPlayerVariables(entity);
	}

	public void ElementalSkillEnd(LivingEntity entity) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (vars.animationTime > 0) {
			vars.animationTime = 14;
			vars.animationId = 21;
			Level world = entity.level();
			if (vars.animationTime < 990) {
				entity.getPersistentData().putBoolean("skillPressed", true);
				vars.skillCooldown = 8;
				vars.stackedMaxSkillCooldown = 8;
			} else {
				vars.skillCooldown = 5;
				vars.stackedMaxSkillCooldown = 5;
				entity.getPersistentData().putBoolean("skillPressed", false);
			}
		}
		CompoundTag message = new CompoundTag();
		message.putInt("elementalAbsorption", 0);
		sendMessage(entity, message);
		vars.syncPlayerVariables(entity);
	}

	public void ElementalBurstStart(LivingEntity entity) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (vars.burstCooldown <= 0 && vars.energyAmount >= getEnergyCost(entity)) {
			vars.burstCooldown = 15;
			vars.animationTime = 18;
			vars.animationId = 30;
			vars.stackedMaxBurstCooldown = 15;
			if (entity.level() instanceof ServerLevel _level) {
				Entity entityToSpawn = ErModEntities.TRAVELER_TORNADO.get().spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
				vars.energyAmount = 0;
				vars.syncPlayerVariables(entity);
				onBurst(entity);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(entity.getYRot());
					//entityToSpawn.getPersistentData().putString("owner", entity.getStringUUID());
					if (entityToSpawn instanceof TravelerTornadoEntity _datEntSetS)
                        _datEntSetS.setOwner(entity);
				}
			}
		}
		vars.syncPlayerVariables(entity);
	}

	public void ElementalBurstEnd(LivingEntity entity) {
	}

	public float getEnergyCost(LivingEntity entity) {
		return 60;
	}

	@Override
	public float getSpeed(LivingEntity entity, int combo) {
		if (combo < this.getMaxCombo(entity) || combo == 10) {
			return (float) (entity.getAttribute(Attributes.ATTACK_SPEED).getValue());
		}
		return 1f;
	}

	public void receiveMessage(LivingEntity entity, CompoundTag message) {
		entity.getPersistentData().putInt("elementalAbsorption", message.getInt("elementalAbsorption"));
	}

	public AnimationDefinition getAnimation(int combo) {
		if (combo == 20)
			return travelerAnimation.ElementalSkillAnemoStart;
		if (combo == 21)
			return travelerAnimation.ElementalSkillAnemoEnd;
		if (combo == 30)
			return travelerAnimation.ElementalBurstAnemo;
		if (combo == 10)
			return travelerAnimation.ChargedAttack;
		if (combo % 4 == 0)
			return travelerAnimation.NormalAttack1;
		if (combo % 4 == 1)
			return travelerAnimation.NormalAttack2;
		if (combo % 4 == 2)
			return travelerAnimation.NormalAttack3;
		return travelerAnimation.NormalAttack4;
	}

	public int getMaxCombo(LivingEntity entity) {
		return 5;
	}

	public boolean hasAnimation(LivingEntity entity) {
		return entity.getMainHandItem().getItem() instanceof SwordItem;
	}

	public int getAnimationTick(LivingEntity entity, int combo, float speed) {
		if (combo == 2)
			return (int) (29 / speed) + 1;
		return (int) (27 / speed) + 1;
	}

	public int getFinishTick(LivingEntity entity, int combo, float speed) {
		if (combo == 20)
			return 0;
		if (combo == 21)
			return 6;
		if (combo == 2)
			return (int) (18 / speed) + 1;
		return (int) (19 / speed) + 1;
	}

	public int getChargedAttackCost(LivingEntity entity) {
		return 20;
	}

	public void AnimationTicking(LivingEntity entity, int combo, int time, float speed) {
		if (entity.level() instanceof ServerLevel) {
			if (combo == 10 && time == (int) (24 / speed) + 1) {
				PerformAttack(entity, 1, 2.2, 2.5, entity.getEyePosition(), DamageMulti(combo, 0));
			}
			if (combo == 2 & time == (int) (21 / speed) + 1)
				PerformAttack(entity, 1, 2.2, 2.5, entity.getEyePosition(), DamageMulti(2, 0));
			if (combo != 2 && time == (int) (22 / speed) + 1) {
				PerformAttack(entity, 1, 2.2, 2.5, entity.getEyePosition(), DamageMulti(combo, 1));
			}
		}
		if (time <= this.getFinishTick(entity, combo, speed) && combo <= this.getMaxCombo(entity) && entity.getPersistentData().getBoolean("WaitingChargeAttack")) {
			ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
			if (vars.stamina >= this.getChargedAttackCost(entity)) {
				vars.stamina -= this.getChargedAttackCost(entity);
				vars.animationId = 10;
				vars.animationTime = this.getAnimationTick(entity, 10, speed);
				vars.staminaRecoveryCooldown = 50;
				vars.syncAnimation(entity);
				entity.getPersistentData().putBoolean("WaitingChargeAttack", false);
			}
		}
		if (combo == 20) {
			Level world = entity.level();
			float yaw = entity.getYRot();
			Vec3 lookVec = new Vec3(-Math.sin(yaw * Math.PI / 180), 0, Math.cos(yaw * Math.PI / 180)).normalize().scale(2);
			final Vec3 _center = (new Vec3(entity.getX(), entity.getY() + 1.2, entity.getZ())).add(lookVec);
			int absorption = entity.getPersistentData().getInt("elementalAbsorption");
            if (absorption == 0 && world instanceof ServerLevel) {
                for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3), e -> true).stream().toList()) {
                    if(entityiterator instanceof AuraContainerInterface auraContainerInterface){
                        if(!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.ELECTRO.getId()).isEmpty())
                            absorption = Math.max(absorption, 1);
                        if(!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty())
                            absorption = Math.max(absorption, 2);
                        if(!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.PYRO.getId()).isEmpty())
                            absorption = Math.max(absorption, 3);
                        if(!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.CRYO.getId()).isEmpty())
                            absorption = 4;
                    }
                }
                if (absorption != 0) {
                    CompoundTag message = new CompoundTag();
                    message.putInt("elementalAbsorption", absorption);
                    sendMessage(entity, message);
                }
            }
			if (time == 993 || time == 995) {
                Holder<DamageType> type = world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortunas.SKILL);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1), e -> true).stream().toList()) {
					if (entityiterator instanceof LivingEntity living && EntityHurtEvent.shouldHurt(entityiterator, entity)) {
						entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
                        entityiterator.hurt(ElementSource.createDamageSource(type, entity, absorbElement(absorption)),
                                (float) (entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * DamageMulti(20, 0) * 0.25));
                        entityiterator.hurt(
                                ElementSource.createDamageSource(type, entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL , 1, false)),
                                (float) (entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * DamageMulti(20, 0)));
					}
				}
			}
			if (time == 983 || time == 973 || time == 970) {
                Holder<DamageType> type = world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortunas.SKILL);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (entityiterator instanceof LivingEntity living && EntityHurtEvent.shouldHurt(entityiterator, entity)) {
						entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
						entityiterator.hurt(ElementSource.createDamageSource(type, entity, absorbElement(absorption)),
                                (float) (entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * DamageMulti(20, 1) * 0.25));
						entityiterator.hurt(
                                ElementSource.createDamageSource(type, entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL , 1, false)),
								(float) (entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * DamageMulti(20, 1)));
					}
				}
			}
			if (time < 970) {
				ElementalSkillEnd(entity);
			}
			world.addParticle(getParticle(absorption), _center.x, _center.y, _center.z, 0, 0, 0);
		}
		if (combo == 21) {
			if (time > 9)
				entity.push(0.1f * Math.sin((entity.getYRot() / 180d) * Math.PI), 0, -0.1f * Math.cos((entity.getYRot() / 180d) * Math.PI));
			if (time == 9) {
				float yaw = entity.getYRot();
				Vec3 lookVec = new Vec3(-Math.sin(yaw * Math.PI / 180), 0, Math.cos(yaw * Math.PI / 180)).normalize().scale(2);
				final Vec3 _center = (new Vec3(entity.getX(), entity.getY() + 1.2, entity.getZ())).add(lookVec);
				Level world = entity.level();
				for (LivingEntity entityiterator : world.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(3), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (EntityHurtEvent.shouldHurt(entityiterator, entity)) {
						entityiterator.hurt(
								ElementSource.createDamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortunas.SKILL), entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL, 1, false)),
								(float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(21, entity.getPersistentData().getBoolean("skillPressed") ? 1 : 0)));
						entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
					}
				}
				if (world instanceof ServerLevel _level && entity.getPersistentData().getBoolean("elementalSkillDamaged")) {
					entity.getPersistentData().putBoolean("elementalSkillDamaged", false);
					for (int i = getParticlesCount(entity.getPersistentData().getBoolean("skillPressed")); i > 0; i--) {
						Entity entityToSpawn = ErModEntities.ENERGY_ORB.get().spawn(_level, BlockPos.containing(_center.x, _center.y, _center.z), MobSpawnType.MOB_SUMMONED);
						if (entityToSpawn instanceof EnergyOrb orb) {
							orb.setType(1, 3);
							orb.push(0.1 - Math.random() * 0.2, 0.3, 0.1 - Math.random() * 0.2);
						}
					}
				}
			}
		}
		if (combo == 30) {
			if (time >= 15) {
				entity.push(0, 0.25, 0);
			} else if (time >= 4) {
				entity.push(0, 0.02, 0);
			}
		}
	}

	public float DamageMulti(int combo, int index) {
		if (combo == 0)
			return 0.445f;
		else if (combo == 1)
			return 0.434f;
		else if (combo == 2)
			return 0.53f;
		else if (combo == 3)
			return 0.583f;
		else if (combo == 4)
			return 0.708f;
		else if (combo == 10)
			return index == 0 ? 0.559f : 0.772f;
		else if (combo == 20)
			return index == 0 ? 0.12f : 0.168f;
		else if (combo == 21)
			return index == 0 ? 1.76f : 1.92f;
		else if (combo == 30)
			return index == 0 ? 0.808f : 0.248f;
		return 1;
	}

	private static SimpleParticleType getParticle(int i) {
        return switch (i){
            case 1 -> ErModParticleTypes.ELECTRO_VORTEX.get();
            case 2 -> ErModParticleTypes.HYDRO_VORTEX.get();
            case 3 -> ErModParticleTypes.PYRO_VORTEX.get();
            case 4 -> ErModParticleTypes.CRYO_VORTEX.get();
            default -> ErModParticleTypes.ANEMO_VORTEX.get();
        };
	}

	public static ElementSource absorbElement(int i) {
        return switch (i){
            case 1 -> new ElementSource(ElementRegistry.ELECTRO.get(), TRAVELER_SKILL, 1, true);
            case 2 -> new ElementSource(ElementRegistry.HYDRO.get(), TRAVELER_SKILL, 1, true);
            case 3 -> new ElementSource(ElementRegistry.PYRO.get(), TRAVELER_SKILL, 1, true);
            case 4 -> new ElementSource(ElementRegistry.CRYO.get(), TRAVELER_SKILL, 1, true);
            default -> new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL, 1, false);
        };
	}

	private int getParticlesCount(boolean pressed) {
		if (pressed) {
			if (Math.random() <= 0.33) {
				return 4;
			}
			return 3;
		}
		return 2;
	}

	public int elementType() {
		return 1;
	}
}