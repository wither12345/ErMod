package net.wither.er.entity.slimes;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;
import net.wither.er.elements.ElementSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

import static net.wither.er.init.ElementRegistry.DENDRO;

public class DendroSlime extends ElementalSlime{
    private static final UUID hideNoKB = UUID.fromString("DDB8FE12-CD5A-E1E2-9269-EB931FB13C99");

    private static final EntityDataAccessor<Boolean> DATA_HAS_GRASS = SynchedEntityData.defineId(DendroSlime.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_HIDING = SynchedEntityData.defineId(DendroSlime.class, EntityDataSerializers.BOOLEAN);

    public DendroSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.hasElement();
        if(!this.level().isClientSide() && this.getEntityData().get(DATA_HAS_GRASS) ^ flag){
            this.getEntityData().set(DATA_HAS_GRASS, flag);
            if(!flag)
                this.hide(false);
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if(this.getTarget() != target && this instanceof AuraContainerInterface containerInterface)
            containerInterface.getAuraContainer().addAura(new ElementSource(this.getElement(), null, 2, true, true));
        super.setTarget(target);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new DendroSlimePanicGoal(this, 2));
        this.goalSelector.addGoal(2, new DendroSlimeHideGoal(this));
        this.goalSelector.addGoal(2, new DendroSlimeStalkGoal(this,1));
    }

    public boolean hasGrass(){
        return this.getEntityData().get(DATA_HAS_GRASS);
    }

    public boolean isHiding(){
        return this.getEntityData().get(DATA_HIDING);
    }

    public void hide(boolean flag){
        this.entityData.set(DATA_HIDING, flag);
        if(flag)
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier(hideNoKB, "hide" , 100 , AttributeModifier.Operation.ADDITION));
        else
            this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(hideNoKB);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_HAS_GRASS, false);
        this.entityData.define(DATA_HIDING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DATA_HAS_GRASS", this.entityData.get(DATA_HAS_GRASS));
        compound.putBoolean("DARA_HIDING", this.entityData.get(DATA_HIDING));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("DATA_HAS_GRASS"))
            this.entityData.set(DATA_HAS_GRASS, compound.getBoolean("DATA_HAS_GRASS"));
        if (compound.contains("DATA_HIDING"))
            this.hide(compound.getBoolean("DATA_HIDING"));
    }

    @Override
    Element getElement() {
        return ElementRegistry.SLIME_DENDRO.get();
    }

    @Override
    boolean isTiny() {
        return true;
    }

    private boolean isBurning(){
        if(this instanceof AuraContainerInterface containerInterface)
            return containerInterface.getAuraContainer().getAura().get(Element.Category.PYRO.getId()).hasElement(ElementRegistry.BURNING.get());
        return false;
    }

    @Override
    protected float getJumpPower() {
        return super.getJumpPower() * (this.isBurning() ? 0.5f: 1);
    }

    static class DendroSlimeStalkGoal extends Goal{
        private final DendroSlime slime;
        private LivingEntity target ;
        private final double speedModifier;
        private int prepareBump ;
        private ElementalSlimeMoveControl control ;

        public DendroSlimeStalkGoal(DendroSlime slime, double speedModifier) {
            this.slime = slime;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.LOOK,Flag.TARGET,Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (this.slime.hasGrass() && livingentity != null && this.slime.getMoveControl() instanceof ElementalSlimeMoveControl moveControl && !moveControl.isActive()) {
                this.target = livingentity;
                control = moveControl;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            super.start();
            prepareBump = -1;
        }

        @Override
        public void tick() {
            this.slime.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if(this.slime.getLookAngle().dot(this.target.getLookAngle()) > 0) {
                if (prepareBump == -1) {
                    if(this.target.distanceToSqr(slime) >= 9)
                        this.slime.getNavigation().moveTo(target, this.speedModifier);
                    else if(this.slime.getCd() <= 0){
                        this.prepareBump = 10 ;
                        this.slime.setCd(60);
                        control.bump();
                        this.slime.getNavigation().stop();
                    }
                }
                else if(this.prepareBump > 0){
                    if(-- this.prepareBump == 0){
                        this.slime.getLookControl().setLookAt(target);
                        this.slime.yHeadRot = this.slime.getYRot();
                        this.slime.yBodyRot = this.slime.getYRot();
                        this.slime.setDeltaMovement(this.slime.getLookAngle().multiply(2,0, 2).normalize().add(0,0.35,0));
                    }
                }
                else if(this.prepareBump == 0){
                    if(this.slime.distanceToSqr(target) < 1){
                        this.prepareBump = -1 ;
                        this.target.hurt(
                                ElementSource.createDamageSource(this.slime.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                        this.slime, new ElementSource(DENDRO.get(),
                                                new ResourceLocation("er:mob_attack"), 1, true)), (float) this.slime.getAttributeValue(Attributes.ATTACK_DAMAGE)
                        );
                        this.slime.setDeltaMovement(Vec3.ZERO);
                    }
                    else if(this.slime.getDeltaMovement().y < 0) {
                        this.slime.setDeltaMovement(this.slime.getDeltaMovement().multiply(-1, 1, -1));
                        this.prepareBump = -1 ;
                    }
                }
            }
            else {
                this.slime.getNavigation().stop();
                this.prepareBump = -1 ;
            }
        }
    }

    static class DendroSlimeHideGoal extends Goal {
        private final DendroSlime slime;
        private boolean jumped ;
        private int timer ;
        private ElementalSlimeMoveControl control;

        public DendroSlimeHideGoal(DendroSlime slime){
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK,Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if(this.slime.getMoveControl() instanceof ElementalSlimeMoveControl moveControl) {
                this.control = moveControl;
                return !this.slime.isHiding() && this.slime.hasGrass() && !this.slime.isBurning();
            }
            return false;
        }

        @Override
        public void start() {
            this.timer = 0 ;
            this.jumped = false ;
            this.control.setActive(false);
        }

        @Override
        public boolean canContinueToUse() {
            return (this.slime.onGround() && !this.jumped) || this.timer < 5;
        }

        @Override
        public void stop() {
            this.slime.hide(true);
        }

        @Override
        public void tick() {
            if(this.timer ++ > 10 && this.slime.onGround() && !this.jumped) {
                this.jumped = true ;
                this.slime.jumpControl.jump();
                this.timer = 0 ;
            }
        }
    }

    static class DendroSlimePanicGoal extends PanicGoal{
        AuraContainerInterface containerInterface;
        ElementalSlimeMoveControl control;
        final DendroSlime slime;

        public DendroSlimePanicGoal(DendroSlime slime, double modifier) {
            super(slime, modifier);
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.MOVE,Flag.LOOK,Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if(this.mob instanceof AuraContainerInterface auraContainerInterface && this.mob.getMoveControl() instanceof ElementalSlimeMoveControl slimeMoveControl) {
                this.containerInterface = auraContainerInterface;
                this.control = slimeMoveControl;
                return super.canUse();
            }
            return false;
        }

        @Override
        public void start() {
            super.start();
            this.control.setSpeed(speedModifier);
            this.control.setDestination(new Vec3(this.posX,this.posY,this.posZ));
            this.control.setActive(true);
            this.slime.hide(false);
        }

        @Override
        public void stop() {
            super.stop();
        }

        @Override
        public boolean canContinueToUse() {
            return this.control != null && !this.control.isDone();
        }

        @Override
        protected boolean shouldPanic() {
            return this.slime.isBurning();
        }
    }
}
