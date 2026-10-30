package net.wither.er.item.fortuna;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.client.model.animations.travelerAnimation;
import net.mcreator.er.entity.TravelerTornadoEntity;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModParticleTypes;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.client.animations.TravelerAnimation;
import net.wither.er.player.combat.VacuumField;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.entity.EnergyOrb;
import net.wither.er.init.ElementRegistry;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.data.FortunaData;
import net.wither.er.network.ErCombatVariables;

import java.util.List;
import java.util.UUID;

public class MemoryOfRovingGalesItem extends StellaFortuna {
    private static final ResourceLocation TRAVELER_SKILL = new ResourceLocation(ErMod.MODID, "traveler_skill");
    public static final ResourceLocation TRAVELER_BURST = new ResourceLocation(ErMod.MODID, "traveler_burst");
    private static final UUID ASCENSION_DMG = UUID.fromString("A231B9A6-4076-21D5-27F2-D1B9ED53E053");
    private static final AttributeModifier ENERGY_RECHARGE = new AttributeModifier(UUID.fromString("C23CB9A6-4076-21D5-27F2-D1B9ED53E053"), "energy_recharge", 0.12, AttributeModifier.Operation.MULTIPLY_BASE);

	public MemoryOfRovingGalesItem() {
		super(20, 2.5f, 57.23f, true);
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
    public void applyAttr(LivingEntity entity, FortunaData data) {
        super.applyAttr(entity, data);

        AttributeInstance dmg = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if(dmg != null) {
            dmg.removeModifier(ASCENSION_DMG);
            dmg.addPermanentModifier(new AttributeModifier(ASCENSION_DMG, "ascension_damage", 0.06 * data.ascension(), AttributeModifier.Operation.MULTIPLY_BASE));
        }

        AttributeInstance energyRecharge = entity.getAttribute(ErModAttributes.ENERGY_RECHARGE.get());
        if(data.constellation() >= 2 && energyRecharge != null) {
            energyRecharge.removeModifier(ENERGY_RECHARGE);
            energyRecharge.addPermanentModifier(ENERGY_RECHARGE);
        }
    }

    @Override
    public void removeAttr(LivingEntity entity) {
        super.removeAttr(entity);
        AttributeInstance dmg = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if(dmg != null)
            dmg.removeModifier(ASCENSION_DMG);

        AttributeInstance energyRecharge = entity.getAttribute(ErModAttributes.ENERGY_RECHARGE.get());
        if(energyRecharge != null)
            energyRecharge.removeModifier(ENERGY_RECHARGE);
    }

    @Override
    protected void addAttributeDescr(List<Component> list, int level, int ascension) {
        super.addAttributeDescr(list, level, ascension);
        if(ascension > 0)
            list.add(Component.literal(Component.translatable("attribute.name.generic.attack_damage").getString() + ": +" + ascension * 6 + "%"));
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
    public void beAttacked(LivingEntity self, DamageSource source, EntityHurtEvent.DamageModifier modifier, float dmgAmount, FortunaData data) {
        if(data.constellation() >= 4 && self.getCapability(ErCombatVariables.PLAYER_VARIABLES).map(var -> var.animationTime == 20).orElse(false))
            modifier.common_multiply -= 0.1f;
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

    @Override
    public float getAnimationTick(LivingEntity entity, int animationId) {
        if (animationId == 10)
            return 30;
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
	public void animationTicking(LivingEntity entity, int animationId, float time, float speed, FortunaData data) {
        switch (animationId) {
            case 0 -> {
                if (test(time, speed, 37))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data));
            }
            case 1 -> {
                if (test(time, speed, 40))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data));
            }
            case 2 -> {
                if (test(time, speed, 38))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data));
            }
            case 3 -> {
                if (test(time, speed, 34))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data));
            }
            case 4 -> {
                if (test(time, speed, 56))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data));
            }
            case 10 -> {
                if (test(time, speed, 24))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data));
                if (test(time, speed, 20))
                    PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 1, data));
            }
            case 20 -> {
                Level level = entity.level();
                float yaw = entity.getYRot();
                Vec3 lookVec = new Vec3(-Math.sin(yaw * Math.PI / 180), 0, Math.cos(yaw * Math.PI / 180)).normalize().scale(2);
                final Vec3 _center = (new Vec3(entity.getX(), entity.getY() + 1.2, entity.getZ())).add(lookVec);
                int absorption = entity.getPersistentData().getInt("elementalAbsorption");
                if (absorption == 0) {
                    for (Entity ent : level.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3), e -> true)) {
                        if (ent instanceof IAuraContainerOwner containerOwner) {
                            if (!containerOwner.er$getAuraContainer().getAura().get(Element.Category.ELECTRO.getId()).isEmpty())
                                absorption = Math.max(absorption, 1);
                            if (!containerOwner.er$getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty())
                                absorption = Math.max(absorption, 2);
                            if (!containerOwner.er$getAuraContainer().getAura().get(Element.Category.PYRO.getId()).isEmpty())
                                absorption = Math.max(absorption, 3);
                            if (!containerOwner.er$getAuraContainer().getAura().get(Element.Category.CRYO.getId()).isEmpty())
                                absorption = 4;
                        }
                    }
                    if (absorption != 0) {
                        CompoundTag message = new CompoundTag();
                        message.putInt("elementalAbsorption", absorption);
                        sendMessage(entity, message);
                    }
                }

                if (data.constellation() >= 1)
                    VacuumField.vacuum(level, _center, entity, 4, 0.2);

                if (test(time, speed, 993) || test(time, speed, 995))
                    cuttingDamage(level, _center, entity, data, absorption, 0);

                if (test(time, speed, 983) || test(time, speed, 973) || test(time, speed, 970))
                    cuttingDamage(level, _center, entity, data, absorption, 1);

                if (time < 970)
                    elementalSkillEnd(entity);

                if (level.isClientSide())
                    level.addParticle(getParticle(absorption), _center.x, _center.y, _center.z, 0, 0, 0);
            }
        }

        if (time <= this.getFinishTick(entity, animationId) && animationId < this.getNormal(entity) && entity.getPersistentData().getBoolean("WaitingChargeAttack")) {
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
                                (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(21, entity.getPersistentData().getBoolean("skillPressed") ? 1 : 0, data)));
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

    /** 战技切割伤害
     *
     * @param level 对应维度
     * @param _center 伤害中心
     * @param entity 伤害实体
     * @param data 命运数据
     * @param absorption 染色
     * @param index 序列 0最小切割伤害 1最大切割伤害
     */
    private static void cuttingDamage(Level level, Vec3 _center, LivingEntity entity, FortunaData data, int absorption, int index){
        Holder<DamageType> type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortuna.SKILL);
        level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(1), e -> EntityHurtEvent.shouldHurt(e, entity)).forEach(ent -> {
            entity.getPersistentData().putBoolean("elementalSkillDamaged", true);
            if(absorption != 0)
                ent.hurt(ElementSource.createDamageSource(type, entity, absorbElement(absorption)),
                        (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(20, index, data) * 0.25));
            ent.hurt(
                    ElementSource.createDamageSource(type, entity, new ElementSource(ElementRegistry.ANEMO.get(), TRAVELER_SKILL , 1, false)),
                    (float) (entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti(20, index, data)));
        });
    }

    /** 获取伤害倍率
     * @param animationId 动画ID
     *                    普通攻击第一至五段对应0-4
     *                    重击对应10
     *                    战技对应20-21
     *                    爆发对应30
     * @param index 攻击编号
     * @param data 命运数据
     * @return 伤害倍率
     * */
    public static float DamageMulti(int animationId, int index, FortunaData data) {
        return switch (animationId){
            case 0 -> getNormal1(data.normal());
            case 1 -> getNormal2(data.normal());
            case 2 -> getNormal3(data.normal());
            case 3 -> getNormal4(data.normal());
            case 4 -> getNormal5(data.normal());
            case 10 -> index == 0 ? getCharged1(data.normal()) : getCharged2(data.normal());
            case 20 -> index == 0 ? getSkillDamage1(data.skill(5)) : getSkillDamage2(data.skill(5));
            case 21 -> index == 0 ? getSkillDamage3(data.skill(5)) : getSkillDamage4(data.skill(5));
            case 30 -> index == 0 ? getBurstDamage1(data.burst(3)) : getBurstDamage2(data.burst(3));
            default -> 1f;
        };
    }

    /** 获取普通攻击第一下随等级的伤害倍率
     * 随等级:44.5% 48.1%	51.7%	56.9%	60.5%	64.6%	70.3%	76.0%	81.7%	87.9%	94.1%
     * @param index 普通攻击等级
     * @return 伤害倍率
     * */
    private static float getNormal1(int index){
        return switch (index){
            case 1 -> 0.445f;
            case 2 -> 0.481f;
            case 3 -> 0.517f;
            case 4 -> 0.569f;
            case 5 -> 0.605f;
            case 6 -> 0.646f;
            case 7 -> 0.703f;
            case 8 -> 0.760f;
            case 9 -> 0.817f;
            case 10 -> 0.879f;
            case 11 -> 0.941f;
            default -> 0.941f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取普通攻击第二下随等级的伤害倍率
     * 随等级:	43.4%	47.0%	50.5%	55.6%	59.1%	63.1%	68.7%	74.2%	79.8%	85.9%	91.9%
     * @param index 普通攻击等级
     * @return 伤害倍率
     * */
    private static float getNormal2(int index){
        return switch (index){
            case 1 -> 0.434f;
            case 2 -> 0.47f;
            case 3 -> 0.505f;
            case 4 -> 0.556f;
            case 5 -> 0.591f;
            case 6 -> 0.631f;
            case 7 -> 0.687f;
            case 8 -> 0.742f;
            case 9 -> 0.798f;
            case 10 -> 0.859f;
            case 11 -> 0.919f;
            default -> 0.919f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取普通攻击第三下随等级的伤害倍率
     * 随等级:	53.0%	57.3%	61.6%	67.8%	72.1%	77.0%	83.8%	90.6%	97.3%	104.7%	112.1%
     * @param index 普通攻击等级
     * @return 伤害倍率
     * */
    private static float getNormal3(int index){
        return switch (index){
            case 1 -> 0.530f;
            case 2 -> 0.573f;
            case 3 -> 0.616f;
            case 4 -> 0.678f;
            case 5 -> 0.721f;
            case 6 -> 0.770f;
            case 7 -> 0.838f;
            case 8 -> 0.906f;
            case 9 -> 0.973f;
            case 10 -> 1.047f;
            case 11 -> 1.121f;
            default -> 1.121f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取普通攻击第四下随等级的伤害倍率
     * 随等级:	58.3%	63.1%	67.8%	74.6%	79.3%	84.8%	92.2%	100%	107%	115%	123%
     * @param index 普通攻击等级
     * @return 伤害倍率
     * */
    private static float getNormal4(int index){
        return switch (index){
            case 1 -> 0.583f;
            case 2 -> 0.631f;
            case 3 -> 0.678f;
            case 4 -> 0.746f;
            case 5 -> 0.793f;
            case 6 -> 0.848f;
            case 7 -> 0.929f;
            case 8 -> 1f;
            case 9 -> 1.07f;
            case 10 -> 1.15f;
            case 11 -> 1.23f;
            default -> 1.23f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取普通攻击第五下随等级的伤害倍率
     * 随等级:		70.8%	76.5%	82.3%	90.5%	96.3%	106%	112%	121%	130%	140%	150%
     * @param index 普通攻击等级
     * @return 伤害倍率
     * */
    private static float getNormal5(int index){
        return switch (index){
            case 1 -> 0.708f;
            case 2 -> 0.765f;
            case 3 -> 0.823f;
            case 4 -> 0.905f;
            case 5 -> 0.963f;
            case 6 -> 1.06f;
            case 7 -> 1.12f;
            case 8 -> 1.21f;
            case 9 -> 1.3f;
            case 10 -> 1.4f;
            case 11 -> 1.5f;
            default -> 1.5f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取重击第一下随等级的伤害倍率
     * 随等级:		55.9%	60.5%	65.0%	71.5%	76.1%	81.3%	88.4%   96%    103%	    111%	118%
     * @param index 重击等级
     * @return 伤害倍率
     * */
    private static float getCharged1(int index){
        return switch (index){
            case 1 -> 0.559f;
            case 2 -> 0.605f;
            case 3 -> 0.650f;
            case 4 -> 0.715f;
            case 5 -> 0.761f;
            case 6 -> 0.813f;
            case 7 -> 0.884f;
            case 8 -> 0.96f;
            case 9 -> 1.03f;
            case 10 -> 1.11f;
            case 11 -> 1.18f;
            default -> 1.18f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取重击第二下随等级的伤害倍率
     * 随等级:		72.2%	78.1%	84.0%	92.4%	98.3%	105%	114%   123%    133%	    143%	153%
     * @param index 重击等级
     * @return 伤害倍率
     * */
    private static float getCharged2(int index){
        return switch (index){
            case 1 -> 0.722f;
            case 2 -> 0.781f;
            case 3 -> 0.840f;
            case 4 -> 0.924f;
            case 5 -> 0.983f;
            case 6 -> 1.05f;
            case 7 -> 1.14f;
            case 8 -> 1.23f;
            case 9 -> 1.33f;
            case 10 -> 1.43f;
            case 11 -> 1.53f;
            default -> 1.53f * (float)Math.pow(1.0778, index - 11);
        };
    }

    /** 获取战技初始切割伤害随等级的伤害倍率
     * 随等级:		12.0%	12.9%	13.8%	15.0%	15.9%	16.8%	18.0%	19.2%	20.4%	21.6%	22.8%	24.0%	25.5%
     * @param index 战技初始切割伤害等级
     * @return 伤害倍率
     * */
    private static float getSkillDamage1(int index){
        return switch (index){
            case 1 -> 0.120f;
            case 2 -> 0.129f;
            case 3 -> 0.138f;
            case 4 -> 0.150f;
            case 5 -> 0.159f;
            case 6 -> 0.168f;
            case 7 -> 0.180f;
            case 8 -> 0.192f;
            case 9 -> 0.204f;
            case 10 -> 0.216f;
            case 11 -> 0.228f;
            case 12 -> 0.240f;
            case 13 -> 0.255f;
            default -> 0.255f * (float)Math.pow(1.0778, index - 13);
        };
    }

    /** 获取战技最大切割伤害随等级的伤害倍率
     * 随等级:		16.8%	18.1%	19.3%	21.0%	22.3%	23.5%	25.2%	26.9%	28.6%	30.2%	31.9%	33.6%	35.7%
     * @param index 战技最大切割伤害等级
     * @return 伤害倍率
     * */
    private static float getSkillDamage2(int index){
        return switch (index){
            case 1 -> 0.168f;
            case 2 -> 0.181f;
            case 3 -> 0.193f;
            case 4 -> 0.210f;
            case 5 -> 0.223f;
            case 6 -> 0.235f;
            case 7 -> 0.252f;
            case 8 -> 0.269f;
            case 9 -> 0.286f;
            case 10 -> 0.302f;
            case 11 -> 0.319f;
            case 12 -> 0.336f;
            case 13 -> 0.357f;
            default -> 0.357f * (float)Math.pow(1.0778, index - 12);
        };
    }

    /** 获取战技初始爆风伤害随等级的伤害倍率
     * 随等级:		176%	189%	202%	220%	233%	246%	264%	282%	299%	317%	334%	352%	374%
     * @param index 战技初始爆风伤害等级
     * @return 伤害倍率
     * */
    private static float getSkillDamage3(int index){
        return switch (index){
            case 1 -> 1.76f;
            case 2 -> 1.89f;
            case 3 -> 2.02f;
            case 4 -> 2.20f;
            case 5 -> 2.33f;
            case 6 -> 2.46f;
            case 7 -> 2.64f;
            case 8 -> 2.82f;
            case 9 -> 2.99f;
            case 10 -> 3.17f;
            case 11 -> 3.34f;
            case 12 -> 3.52f;
            case 13 -> 3.74f;
            default -> 3.74f * (float)Math.pow(1.0778, index - 13);
        };
    }

    /** 获取战技最大爆风伤害随等级的伤害倍率
     * 随等级:		192%	206%	221%	240%	254%	269%	288%	307%	326%	346%	365%	384%	408%
     * @param index 战技最大爆风伤害等级
     * @return 伤害倍率
     * */
    private static float getSkillDamage4(int index){
        return switch (index){
            case 1 -> 1.92f;
            case 2 -> 2.06f;
            case 3 -> 2.21f;
            case 4 -> 2.40f;
            case 5 -> 2.54f;
            case 6 -> 2.69f;
            case 7 -> 2.88f;
            case 8 -> 3.07f;
            case 9 -> 3.26f;
            case 10 -> 3.46f;
            case 11 -> 3.65f;
            case 12 -> 3.84f;
            case 13 -> 4.08f;
            default -> 4.08f * (float)Math.pow(1.0778, index - 13);
        };
    }

    /** 获取爆发龙卷风伤害随等级的伤害倍率
     * 随等级:		80.8%	86.9%	92.9%	101%	107%	113%	121%	129%	137%	145%	154%	162%	172%	182%
     * @param index 爆发龙卷风伤害等级
     * @return 伤害倍率
     * */
    private static float getBurstDamage1(int index){
        return switch (index){
            case 1 -> 0.808f;
            case 2 -> 0.869f;
            case 3 -> 0.929f;
            case 4 -> 1.01f;
            case 5 -> 1.07f;
            case 6 -> 1.13f;
            case 7 -> 1.21f;
            case 8 -> 1.29f;
            case 9 -> 1.37f;
            case 10 -> 1.45f;
            case 11 -> 1.562f;
            case 12 -> 1.72f;
            case 13 -> 1.82f;
            default -> 1.82f * (float)Math.pow(1.0778, index - 13);
        };
    }

    /** 获取爆发附加元素伤害随等级的伤害倍率
     * 随等级:			24.8%	26.7%	28.5%	31%	32.9%	34.7%	37.2%	39.7%	42.2%	44.6%	47.1%	49.6%	52.7%	55.8%
     * @param index 爆发附加元素伤害等级
     * @return 伤害倍率
     * */
    private static float getBurstDamage2(int index){
        return switch (index){
            case 1 -> 0.248f;
            case 2 -> 0.267f;
            case 3 -> 0.285f;
            case 4 -> 0.31f;
            case 5 -> 0.329f;
            case 6 -> 0.347f;
            case 7 -> 0.372f;
            case 8 -> 0.397f;
            case 9 -> 0.422f;
            case 10 -> 0.446f;
            case 11 -> 0.471f;
            case 12 -> 0.476f;
            case 13 -> 0.558f;
            default -> 0.558f * (float)Math.pow(1.0778, index - 13);
        };
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
			if (Math.random() <= 0.33)
				return 4;
			return 3;
		}
		return 2;
	}

	public Element.Category elementType() {
		return Element.Category.ANEMO;
	}
}