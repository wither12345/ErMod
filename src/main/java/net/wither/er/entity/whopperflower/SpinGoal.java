package net.wither.er.entity.whopperflower;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;

import java.util.EnumSet;

public class SpinGoal extends Goal {
    private final Whopperflower whopperflower;
    private Entity target;
    private final float damageScale;
    private int time ;
    private final Element element;

    public SpinGoal(Whopperflower whopperflower, float damageScale, Element element) {
        this.whopperflower = whopperflower;
        this.damageScale = damageScale;
        this.element = element;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if(this.whopperflower.cd > 0 ||
                this.whopperflower.spinCd > 0 ||
                this.whopperflower.getAction() != Whopperflower.Action.NORMAL) return false;
        this.target = this.whopperflower.getTarget();
        return this.target != null && this.target.isAlive() && this.target.distanceToSqr(this.whopperflower) <= 9;
    }

    @Override
    public boolean canContinueToUse() {
        return this.time < 18;
    }

    @Override
    public void stop() {
        this.whopperflower.setAction(Whopperflower.Action.NORMAL);
        this.whopperflower.spinCd = 80;
        this.whopperflower.cd = 20;
    }

    @Override
    public void start() {
        this.time = 0;
        this.whopperflower.setAction(Whopperflower.Action.SPIN);
    }

    @Override
    public void tick() {
        this.time ++ ;
        if(this.time >= 6 && this.time <= 8){
            ErMod.LOGGER.info("ddd");
            Level level = this.whopperflower.level();
            final Vec3 _center = new Vec3(whopperflower.getX(), whopperflower.getY(), whopperflower.getZ());
            level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(3), e -> true)
                    .stream().filter(e -> EntityHurtEvent.shouldHurt(e, this.whopperflower)).forEach(
                            e -> e.hurt(
                                    ElementSource.createDamageSource(
                                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                            this.whopperflower,
                                            new ElementSource(element, new ResourceLocation("er:whopperflower.spin") , 2, true)
                                    ),  damageScale * (float)this.whopperflower.getAttributeValue(Attributes.ATTACK_DAMAGE))
                    );
        }
    }
}
