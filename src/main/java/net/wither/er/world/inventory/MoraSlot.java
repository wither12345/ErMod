package net.wither.er.world.inventory;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MoraSlot extends Slot {
    private static final ResourceLocation EMPTY = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "item/mora_slot_empty");
    public MoraSlot(Container container, int id, int x, int y) {
        super(container, id, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return ErModItems.MORA_BAG.get() == stack.getItem();
    }
}
