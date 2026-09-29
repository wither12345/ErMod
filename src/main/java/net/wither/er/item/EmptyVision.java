package net.wither.er.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.DelusionData;
import org.jetbrains.annotations.NotNull;

public class EmptyVision extends Item {
    public EmptyVision() {
        super(new Properties().stacksTo(1)
                .component(DataComponentsRegister.VISION_FRAME, Vision.Frame.MONDSTADT));
    }

    public EmptyVision(DelusionData data) {
        super(new Properties().stacksTo(1)
                .component(DataComponentsRegister.VISION_FRAME, Vision.Frame.MONDSTADT)
                .component(DataComponentsRegister.DELUSION, data));
    }

    public @NotNull String getDescriptionId(ItemStack itemStack) {
        if(itemStack.get(DataComponentsRegister.VISION_FRAME.get()) == Vision.Frame.MOON_WHEEL) return "item.er.unowned_moon_wheel";
        return super.getDescriptionId(itemStack);
    }
}
