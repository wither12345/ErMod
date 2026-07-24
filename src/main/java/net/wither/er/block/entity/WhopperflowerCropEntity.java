package net.wither.er.block.entity;

import net.mcreator.er.init.ErModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class WhopperflowerCropEntity extends BlockEntity implements TraceableEntity {
    @Nullable
    private UUID ownerUUID;
    @Nullable private LivingEntity cachedOwner;

    public WhopperflowerCropEntity(BlockPos position, BlockState state) {
        super(ErModBlockEntities.WHOPPERFLOWER_CROP.get(), position, state);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
            this.cachedOwner = null;
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.ownerUUID != null) tag.putUUID("Owner", this.ownerUUID);
    }

    public void setOwner(@Nullable LivingEntity entity) {
        if (entity != null) {
            this.ownerUUID = entity.getUUID();
            this.cachedOwner = entity;
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else {
            if (this.ownerUUID != null) {
                Level var2 = this.getLevel();
                if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedOwner = serverlevel.getEntity(this.ownerUUID) instanceof LivingEntity living ? living : null;
                    return this.cachedOwner;
                }
            }
            return null;
        }
    }
}