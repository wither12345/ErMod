package net.wither.er.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EmptyVision extends Item {
    public EmptyVision() {
        super(new Properties().stacksTo(1));
    }

    public @NotNull String getDescriptionId(ItemStack itemStack) {
        if(itemStack.getOrCreateTag().getInt("frame") == Vision.Frame.MOON_WHEEL.ordinal()) return "item.er.unowned_moon_wheel";
        return super.getDescriptionId(itemStack);
    }
}
