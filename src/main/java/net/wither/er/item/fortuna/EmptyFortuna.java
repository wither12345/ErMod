package net.wither.er.item.fortuna;

import net.mcreator.er.client.model.animations.travelerAnimation;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.SwordItem;
import net.wither.er.client.animations.TravelerAnimation;
import net.wither.er.elements.Element;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.data.FortunaData;
import net.wither.er.item.data.SkillData;
import net.wither.er.network.ErCombatVariables;
import org.jetbrains.annotations.Nullable;

public class EmptyFortuna extends StellaFortuna {
    public EmptyFortuna() {
        super(20, 2.5f, 57.23f, true);
    }

    @Override
    public void elementalSkillStart(LivingEntity entity) {
    }

    @Override
    public void elementalSkillEnd(LivingEntity entity) {
    }

    @Override
    public void elementalBurstStart(LivingEntity entity) {
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
    }

    @Override
    public boolean hasSkill() {
        return false;
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

    @Override
    public boolean hasAnimation(LivingEntity entity) {
        return entity.getMainHandItem().getItem() instanceof SwordItem;
    }

    @Override
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
            case 3 -> 27;
            case 2 -> 28;
            case 1 -> 31;
            case 0, 4 -> 29;
            default -> 20;
        };
    }

    public int getChargedAttackCost(LivingEntity entity) {
        return 20;
    }

    @Override
    public void animationTicking(LivingEntity entity, int animationId, float time, float speed, FortunaData data) {
        if (entity.level() instanceof ServerLevel) {
            switch (animationId){
                case 0 -> {
                    if(test(time, speed, 37))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data.skills()));
                }
                case 1 -> {
                    if(test(time, speed, 40))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data.skills()));
                }
                case 2 -> {
                    if(test(time, speed, 38))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data.skills()));
                }
                case 3 -> {
                    if(test(time, speed, 34))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data.skills()));
                }
                case 4 -> {
                    if(test(time, speed, 56))
                        PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data.skills()));
                }
            }
            if (animationId == 10 && test(time, speed, 25))
                PerformAttack(entity, 1, 2.2, 3, entity.getEyePosition(-0.3f), DamageMulti(animationId, 0, data.skills()));
        }

        if (time <= this.getFinishTick(entity, animationId) && animationId <= this.getNormal(entity) && entity.getPersistentData().getBoolean("WaitingChargeAttack")) {
            ErCombatVariables.PlayerVariables vars = entity.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElse(new ErCombatVariables.PlayerVariables());
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
    }

    public float DamageMulti(int animationId, int index, SkillData data) {
        return switch (animationId){
            case 0 -> 0.445f;
            case 1 -> 0.434f;
            case 2 -> 0.53f;
            case 3 -> 0.583f;
            case 4 -> 0.708f;
            case 10 -> 0.559f;
            case 20 -> index == 0 ? 0.12f : 0.168f;
            case 21 -> index == 0 ? 1.76f : 1.92f;
            case 30 -> index == 0 ? 0.808f : 0.248f;
            default -> 1f;
        };
    }

    @Nullable
    @Override
    public Element.Category elementType() {
        return Element.Category.ANEMO;
    }
}
