package net.wither.er.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.NotNull;

public class Vision extends Item {
    private final Element.Category category;

    public Vision(Element.Category category) {
        super(new Properties().stacksTo(1));
        this.category = category;
    }

    public Element.Category getCategory() {
        return category;
    }

    public @NotNull String getDescriptionId(@NotNull ItemStack itemStack) {
        if(itemStack.getOrCreateTag().getInt("frame") == Frame.MOON_WHEEL.ordinal()) return "item.er." + category.toString().toLowerCase() + "_moon_wheel";
        return super.getDescriptionId(itemStack);
    }

    public enum Frame{
        MONDSTADT,
        LIYUE,
        MOON_WHEEL;

        public static Frame fromId(int i){
            return Frame.values()[i];
        }

        public static int getId(ItemStack stack){
            Frame frame = fromId(stack.getOrCreateTag().getInt("frame"));
            return frame == null ? 0 : frame.ordinal();
        }

        public static Frame fromString(String s){
            for(Frame f : Frame.values())
                if(f.name().toLowerCase().equals(s))
                    return f;
            return MONDSTADT;
        }
    }
}
