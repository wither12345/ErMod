package net.wither.er.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LunarChargedCloud extends Entity{
    private int activeTime = 0;

    public LunarChargedCloud(EntityType<?> type, Level level) {
        super(type, level);
    }

    public void refresh(){
        this.activeTime = 0 ;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        activeTime = compoundTag.getInt("activeTime");

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putInt("activeTime", activeTime);
    }

    @Override
    public void tick() {
        super.tick();
        activeTime ++ ;
        if(activeTime > 200){
            this.discard();
        }
    }
}
