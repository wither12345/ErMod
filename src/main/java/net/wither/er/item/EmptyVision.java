package net.wither.er.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmptyVision extends Item {
    @Nullable private final ICapabilityProvider provider;
    public EmptyVision() {
        this(null);
    }

    public EmptyVision(@Nullable ICapabilityProvider provider){
        super(new Properties().stacksTo(1));
        this.provider = provider;
    }

    public @NotNull String getDescriptionId(ItemStack itemStack) {
        if(itemStack.getOrCreateTag().getInt("frame") == Vision.Frame.MOON_WHEEL.ordinal()) return "item.er.unowned_moon_wheel";
        return super.getDescriptionId(itemStack);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return this.provider;
    }
}
