package net.mcreator.er.item;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.wither.er.item.fortuna.StellaFortuna;
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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.client.animations.TravelerAnimation;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.entity.EnergyOrb;
import net.wither.er.init.ElementRegistry;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.network.ErCombatVariables;

public class MemoryofRovingGalesItem extends StellaFortuna {
    private static final ResourceLocation TRAVELER_SKILL = new ResourceLocation(ErMod.MODID, "traveler_skill");
    public static final ResourceLocation TRAVELER_BURST = new ResourceLocation(ErMod.MODID, "traveler_burst");

	public MemoryofRovingGalesItem() {
		super(new Properties().stacksTo(1).rarity(Rarity.RARE), 20, 2.5f, 57.23f, true);
	}

    @Override
	public void elementalSkillStart(LivingEntity entity) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (vars.skillCooldown <= 0) {
			vars.animationTime = 1000;
			vars.animationId = 20;
		}
		vars.syncWithId(entity, 0b11);
	}

    @Override
	public void elementalSkillEnd(LivingEntity entity) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (vars.animationTime > 0 && vars.animationId == 20) {
			if (vars.animationTime < 990) {
				entity.getPersistentData().putBoolean("skillPressed", true);
				vars.skillCooldown = 8;
				vars.stackedMaxSkillCooldown = 8;
			} else {
				vars.skillCooldown = 5;
				vars.stackedMaxSkillCooldown = 5;
				entity.getPersistentData().putBoolean("skillPressed", false);
			}
            vars.animationTime = 14;
            vars.animationId = 21;
            CompoundTag message = new CompoundTag();
            message.putInt("elementalAbsorption", 0);
            sendMessage(entity, message);
            vars.syncWithId(entity, 0b00_0011_0011);
		}
	}

    @Override
	public void elementalBurstStart(LivingEntity entity) {
		ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		if (vars.burstCooldown <= 0 && vars.energyAmount >= getEnergyCost(entity)) {
			vars.burstCooldown = 15;
			vars.animationTime = 18;
			vars.animationId = 30;
			vars.stackedMaxBurstCooldown = 15;
            vars.energyAmount = 0;
			if (entity.level() instanceof ServerLevel _level) {
				Entity entityToSpawn = ErModEntities.TRAVELER_TORNADO.get().spawn(_level, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), MobSpawnType.MOB_SUMMONED);
				onBurst(entity);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(entity.getYRot());
					if (entityToSpawn instanceof TravelerTornadoEntity _datEntSetS)
                        _datEntSetS.setOwner(entity);
				}
			}
            vars.syncWithId(entity, 0b110100_0011);
		}
	}

    @Override
	public void elementalBurstEnd(LivingEntity entity) {
	}

    @Override
	public float getEnergyCost(LivingEntity entity) {
		return 60;
	}

    @Override
	public void receiveMessage(LivingEntity entity, CompoundTag message) {
		entity.getPersistentData().putInt("elementalAbsorption", message.getInt("elementalAbsorption"));
	}

    @Override
	public AnimationDefinition getAnimation(int animationId) {
        return switch (animationId){
            case 0 -> TravelerAnimation.NORMAL_1;
            case 1 -> TravelerAnimation.NORMAL_2;
            case 2 -> TravelerAnimation.NORMAL_3;
            case 3 -> TravelerAnimation.NORMAL_4;
            case 4 -> TravelerAnimation.NORMAL_5;
            case 10 -> travelerAnimation.ChargedAttack;
            case 20 -> travelerAnimation.ElementalSkillAnemoStart;
            case 21 -> travelerAnimation.ElementalSkillAnemoEnd;
            case 30 -> travelerAnimation.ElementalBurstAnemo;
            default -> TravelerAnimation.NORMAL_1;
        };
	}

    @Override
	public int getNormal(LivingEntity entity) {
		return 5;
	}

	public boolean hasAnimation(LivingEntity entity) {
		return entity.getMainHandItem().getItem() instanceof SwordItem;
	}

	public float getAnimationTick(LivingEntity entity, int animationId) {
        if (animationId == 4)
            return 64;
        if (animationId == 3)
            return 46;
		if (animationId == 2)
			return 48;
		return 45;
	}

    @Override
	public float getFinishTick(LivingEntity entity, int animationId) {
        return switch (animationId){
            case 20 -> 0;
            case 21 -> 6;
            case 4 -> 34;
            case 3 -> 27;
            case 2 -> 28;
            case 1 -> 31;
            case 0 -> 29;
            default -> 20;
        };
	}

	public int getChargedAttackCost(LivingEntity entity) {
		return 20;
	}

    @Override
	public void animationTicking(LivingEntity entity, int animationId, float time, float speed) {
		if (entity.level() instanceof ServerLevel) {
            switch (animationId){
                case 0 -> {
                    if(test(time, speed, 37))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0));
                }
                case 1 -> {
                    if(test(time, speed, 40))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0));
                }
                case 2 -> {
                    if(test(time, speed, 38))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0));
                }
                case 3 -> {
                    if(test(time, speed, 34))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0));
                }
                case 4 -> {
                    if(test(time, speed, 56))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0));
                }
            }
			if (animationId == 10 && test(time, speed, 25))
				PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0));
		}

		if (time <= this.getFinishTick(entity, animationId) && animationId <= this.getNormal(entity) && entity.getPersistentData().getBoolean("WaitingChargeAttack")) {
			ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
            AttributeInstance instance = entity.getAttribute(ErAttributeRegister.STAMINA_COST.get());

            double multi = instance == null ? 1 : instance.getValue();
            if (vars.stamina >= this.getChargedAttackCost(entity) * multi) {
				vars.stamina -= this.getChargedAttackCost(entity) * multi;
				vars.animationId = 10;
				vars.animationTime = this.getAnimationTick(entity, 10);
				vars.staminaRecoveryCooldown = 50;
				vars.syncWithId(entity, 0b1111);
				entity.getPersistentData().putBoolean("WaitingChargeAttack", false);
			}
		}
		if (animationId == 20) {
			Level world = entity.level();
			float yaw = entity.getYRot();
			Vec3 lookVec = new Vec3(-Math.sin(yaw * Math.PI / 180), 0, Math.cos(yaw * Math.PI / 180)).normalize().scale(2);
			final Vec3 _center = (new Vec3(entity.getX(), entity.getY() + 1.2, entity.getZ())).add(lookVec);
			int absorption = entity.getPersistentData().getInt("elementalAbsorption");
            if (absorption == 0 && world instanceof ServerLevel) {
                for (Entity ent : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3), e -> true)) {
                    if(ent instanceof IAuraContainerOwner containerOwner){
                        if(!containerOwner.er$getAuraContainer().getAura().get(Element.Category.ELECTRO.getId()).isEmpty())
                            absorption = Math.max(absorption, 1);
                        if(!containerOwner.er$getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty())
                            absorption = Math.max(absorption, 2);
                        if(!containerOwner.er$getAuraContainer().getAura().get(Element.Category.PYRO.getId()).isEmpty())
                            absorption = Math.max(absorption, 3);
                        if(!containerOwner.er$getAuraContainer().getAura().get(Element.Category.CRYO.getId()).isEmpty())
                            absorption = 4;
                    }
                }
                if (absorption != 0) {
                    CompoundTag message = new CompoundTag();
                    message.putInt("elementalAbsorption", absorption);
                    sendMessage(entity, message);
                }
            }

			if (test(time, speed, 993) || test(time, speed, 995)) {
                Holder<DamageType> type = world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortuna.SKILL);
                int finalAbsorption = absorption;
                world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1), e -> true).forEach(ent -> {
                    if (ent instanceof LivingEntity && EntityHurtEvent.shouldHurt(ent, entity)) {
                        entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
                        if(finalAbsorption != 0)
                            ent.hurt(ElementSource.createDamageSource(type, entity, absorbElement(finalAbsorption)),
                                    (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(20, 0) * 0.25));
                        ent.hurt(
                                ElementSource.createDamageSource(type, entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL , 1, false)),
                                (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(20, 0)));
                    }
                });
			}

            if (test(time, speed, 983) || test(time, speed, 973) || test(time, speed, 970)) {
                Holder<DamageType> type = world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortuna.SKILL);
                int finalAbsorption = absorption;
                world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(1), e -> true).forEach(ent -> {
                    if (ent instanceof LivingEntity && EntityHurtEvent.shouldHurt(ent, entity)) {
                        entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
                        if(finalAbsorption != 0)
                            ent.hurt(ElementSource.createDamageSource(type, entity, absorbElement(finalAbsorption)),
                                    (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(20, 1) * 0.25));
                        ent.hurt(
                                ElementSource.createDamageSource(type, entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL , 1, false)),
                                (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(20, 1)));
                    }
                });
			}
			if (time < 970) {
				elementalSkillEnd(entity);
			}
			world.addParticle(getParticle(absorption), _center.x, _center.y, _center.z, 0, 0, 0);
		}
		if (animationId == 21) {
			if (time > 9)
				entity.push(0.1f * Math.sin((entity.getYRot() / 180d) * Math.PI), 0, -0.1f * Math.cos((entity.getYRot() / 180d) * Math.PI));
			if (time == 9) {
				float yaw = entity.getYRot();
				Vec3 lookVec = new Vec3(-Math.sin(yaw * Math.PI / 180), 0, Math.cos(yaw * Math.PI / 180)).normalize().scale(2);
				final Vec3 _center = (new Vec3(entity.getX(), entity.getY() + 1.2, entity.getZ())).add(lookVec);
				Level world = entity.level();
                world.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(3), e -> true).forEach(ent -> {
                    if (EntityHurtEvent.shouldHurt(ent, entity)) {
                        ent.hurt(ElementSource.createDamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortuna.SKILL), entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL, 1, false)),
                                (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(21, entity.getPersistentData().getBoolean("skillPressed") ? 1 : 0)));
                        entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
                    }
                });

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
		if (animationId == 30) {
			if (time >= 15) {
				entity.push(0, 0.25, 0);
			} else if (time >= 4) {
				entity.push(0, 0.02, 0);
			}
		}
	}

	public float DamageMulti(int animationId, int index) {
		if (animationId == 0)
			return 0.445f;
		else if (animationId == 1)
			return 0.434f;
		else if (animationId == 2)
			return 0.53f;
		else if (animationId == 3)
			return 0.583f;
		else if (animationId == 4)
			return 0.708f;
		else if (animationId == 10)
			return index == 0 ? 0.559f : 0.772f;
		else if (animationId == 20)
			return index == 0 ? 0.12f : 0.168f;
		else if (animationId == 21)
			return index == 0 ? 1.76f : 1.92f;
		else if (animationId == 30)
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

	public Element.Category elementType() {
		return Element.Category.ANEMO;
	}
}