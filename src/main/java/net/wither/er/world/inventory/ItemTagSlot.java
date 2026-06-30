package net.wither.er.world.inventory;

import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemTagSlot extends Slot {
    private final TagKey<Item> tag ;

    public ItemTagSlot(TagKey<Item> tag, Container container, int id, int x, int y) {
        super(container, id, x, y);
        this.tag = tag;

    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(this.tag);
    }
}
