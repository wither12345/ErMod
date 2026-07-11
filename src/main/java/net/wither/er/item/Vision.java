package net.wither.er.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.wither.er.elements.Element;
import net.wither.er.init.DataComponentsRegister;
import org.jetbrains.annotations.NotNull;

public class Vision extends Item {
    public static final Codec<Frame> FRAME_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("frame").forGetter(Frame::getName)
            ).apply(instance, Frame::fromString)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, Frame> FRAME_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, Frame::ordinal,
            Frame::fromId
    );
    private final Element.Category category;

    public Vision(Element.Category category) {
        super(new Item.Properties().stacksTo(1).component(DataComponentsRegister.VISION_FRAME, Frame.MOON_WHEEL));
        this.category = category;
    }

    public @NotNull String getDescriptionId(ItemStack itemStack) {
        if(itemStack.get(DataComponentsRegister.VISION_FRAME.get()) == Frame.MOON_WHEEL) return "item.er." + category.toString().toLowerCase() + "_moon_wheel";
        return super.getDescriptionId(itemStack);
    }

    public Element.Category getCategory() {
        return category;
    }

    public enum Frame{
        MONDSTADT("mondstadt"),
        LIYUE("liyue"),
        MOON_WHEEL("moon_wheel");

        private final String name ;

        Frame(String name){
            this.name = name;
        }

        public static Frame fromId(int i){
            return Frame.values()[i];
        }

        public static int getId(ItemStack stack){
            Frame frame = stack.get(DataComponentsRegister.VISION_FRAME.get());
            return frame == null ? 0 : frame.ordinal();
        }

        public String getName() {
            return name;
        }

        public static Frame fromString(String s){
            for(Frame f : Frame.values()){
                if(f.getName().equals(s))
                    return f;
            }
            return MONDSTADT;
        }
    }
}
