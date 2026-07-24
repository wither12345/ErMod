package net.wither.er.entity.whopperflower;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;

import java.util.EnumSet;

public class PopGoal extends Goal {
    private final Whopperflower whopperflower;
    private int time ;
    private final Element element;

    public PopGoal(Whopperflower whopperflower, Element element) {
        this.whopperflower = whopperflower;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
        this.element = element;
    }

    @Override
    public boolean canUse() {
        return whopperflower.isDisguise() && whopperflower.getTarget() != null && whopperflower.distanceToSqr(whopperflower.getTarget()) < 9;
    }

    @Override
    public void start() {
        this.whopperflower.setAction(Whopperflower.Action.UP);
        this.time = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.time < 10;
    }

    @Override
    public void stop() {
        this.whopperflower.setAction(Whopperflower.Action.NORMAL);
        this.whopperflower.setFruitCount(3);
        this.whopperflower.cd = 70;
    }

    @Override
    public void tick() {
        this.time ++ ;
        if(this.time == 3){
            this.whopperflower.setDisguisedBlock(Blocks.AIR.defaultBlockState());
            this.whopperflower.setAction(Whopperflower.Action.OPENING);
            Level level = this.whopperflower.level();
            final Vec3 _center = new Vec3(whopperflower.getX(), whopperflower.getY(), whopperflower.getZ());
            level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(3), e -> true)
                    .stream().filter(e -> EntityHurtEvent.shouldHurt(e, this.whopperflower)).forEach(
                            e -> e.hurt(
                                    ElementSource.createDamageSource(
                                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                            this.whopperflower,
                                            new ElementSource(element, ResourceLocation.parse("er:whopperflower.pop") , 2, true)
                                    ), (float) (2 * this.whopperflower.getAttributeValue(Attributes.ATTACK_DAMAGE)))
                    );
        }
    }
}
