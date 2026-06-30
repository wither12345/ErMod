package net.wither.er.world.inventory;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MoraSlot extends Slot {
    public MoraSlot(Container container, int id, int x, int y) {
        super(container, id, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return ErModItems.MORA_BAG.get() == stack.getItem();
    }
}
