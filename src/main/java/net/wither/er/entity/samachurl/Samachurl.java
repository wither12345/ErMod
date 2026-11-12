package net.wither.er.entity.samachurl;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.wither.er.init.ErAttributeRegister;
import org.jetbrains.annotations.NotNull;

public abstract class Samachurl extends PathfinderMob implements Enemy {
    private static final EntityDataAccessor<Integer> ANIMATION = SynchedEntityData.defineId(Samachurl.class, EntityDataSerializers.INT);


    private Action action = Action.NORMAL;
    public float startTick = 0;
    public Action lastAction = Action.NORMAL;

    protected Samachurl(EntityType<? extends Samachurl> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.MAX_HEALTH, 20.0D);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
        builder = builder.add(ErAttributeRegister.ANEMO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.CRYO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.GEO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.DENDRO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.PYRO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.HYDRO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.ELECTRO_RES.get(), 10);
        builder = builder.add(ErAttributeRegister.PHYSICAL_RES.get(), 10);
        return builder;
    }

    public Action getAction() {
        return this.action;
    }

    protected void setAction(Action action) {
        this.entityData.set(ANIMATION, action.ordinal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION, 0);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        if(accessor == ANIMATION) {
            this.action = Action.byInt(entityData.get(ANIMATION));
            this.startTick = this.level().getGameTime();
        }
    }

    public enum Action{
        NORMAL,
        CONJURE,
        CONJURE_CONSTANT,
        MELEE;

        public static Action byInt(int i) {
            return values()[i];
        }
    }
}
