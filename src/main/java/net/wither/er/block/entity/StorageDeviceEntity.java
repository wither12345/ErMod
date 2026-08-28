package net.wither.er.block.entity;

import net.mcreator.er.init.ErModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.wither.er.world.inventory.StorageDeviceMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StorageDeviceEntity extends DispenserBlockEntity {
    public StorageDeviceEntity(BlockPos blockPos, BlockState blockState) {
        super(ErModBlockEntities.STORAGE_DEVICE.get(), blockPos, blockState);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundtag = super.getUpdateTag();
        ContainerHelper.saveAllItems(compoundtag, this.getItems());
        return compoundtag;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.getItems().clear();
        ContainerHelper.loadAllItems(tag, this.getItems());
    }

    @Override
    public void setChanged() {
        if (this.getLevel() instanceof ServerLevel)
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        super.setChanged();
    }

    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.er.storage_device");
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack itemStack) {
        return !(Block.byItem(itemStack.getItem()) instanceof ShulkerBoxBlock) && itemStack.getItem().canFitInsideContainerItems();
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        return new StorageDeviceMenu(i, inventory, this);
    }
}
