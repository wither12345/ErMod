package net.wither.er.entity;

import net.mcreator.er.init.ErModBlocks;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.wither.er.block.LinkMechanismBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class LinkMechanismTelpher extends Entity {
    @Nullable Entity owner;
    @Nullable private Vec3 destination = null;
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_DESTINATION = SynchedEntityData.defineId(LinkMechanismTelpher.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    public LinkMechanismTelpher(EntityType<?> type, Level level) {
        super(type, level);
    }

    public LinkMechanismTelpher(Entity owner, BlockPos pos){
        this(ErModEntities.LINK_MECHANISM_TELPHER.get(), owner.level());
        this.owner = owner;
        this.setPos(owner.position());
        this.owner.startRiding(this);
        this.setDestination(pos);
    }

    public void setDestination(@NotNull BlockPos blockPos) {
        BlockState state = this.level().getBlockState(blockPos);
        if(state.is(ErModBlocks.LINK_MECHANISM_BASE)) {
            this.entityData.set(DATA_DESTINATION, Optional.of(blockPos));
            this.destination = LinkMechanismBase.getCenter(blockPos, state.getValue(BlockStateProperties.HORIZONTAL_FACING));
        }
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if(accessor == DATA_DESTINATION){
           this.entityData.get(DATA_DESTINATION).ifPresent(
                    pos -> this.destination = LinkMechanismBase.getCenter(pos,
                            this.level().getBlockState(pos).getValue(BlockStateProperties.HORIZONTAL_FACING)));

        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.destination != null){
            this.setDeltaMovement(Vec3.ZERO);
            Vec3 d = destination.subtract(this.position());
            if(d.lengthSqr() < 1 || d.lengthSqr() > 1024)
                destination = null;
            else{
                d = d.normalize();
                this.move(MoverType.SELF, d);
            }
        }
        if(!this.level().isClientSide() && (this.owner == null || this.owner.getVehicle() != this))
            this.discard();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(DATA_DESTINATION, Optional.empty());
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }
}
