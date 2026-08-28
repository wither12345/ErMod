package net.wither.er.item;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.world.item.BlockItem;

public class StorageDeviceItem extends BlockItem {
    public StorageDeviceItem(){
        super(ErModBlocks.STORAGE_DEVICE_BASE.get(), new Properties().stacksTo(1));
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }
}
