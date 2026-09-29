package net.wither.er.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.wither.er.elements.Element;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.DelusionData;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Vision extends Item {
    public static final Codec<Frame> FRAME_CODEC = Codec.STRING.xmap(Frame::fromString, Frame::getName);
    public static final StreamCodec<ByteBuf, Frame> FRAME_STREAM_CODEC = ByteBufCodecs.INT.map(Frame::fromId, Frame::ordinal);
    public static final TagKey<Item> TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "vision"));

    private final Element.Category category;

    public Vision(Element.Category category) {
        super(new Item.Properties().stacksTo(1)
                .component(DataComponentsRegister.VISION_FRAME, Frame.MONDSTADT)
        );
        this.category = category;
    }

    public Vision(Element.Category category, DelusionData data) {
        super(new Item.Properties().stacksTo(1)
                .component(DataComponentsRegister.VISION_FRAME, Frame.MONDSTADT)
                .component(DataComponentsRegister.DELUSION, data)
        );
        this.category = category;
    }

    public @NotNull String getDescriptionId(ItemStack itemStack) {
        if(itemStack.get(DataComponentsRegister.VISION_FRAME.get()) == Frame.MOON_WHEEL) return "item.er." + category.toString().toLowerCase() + "_moon_wheel";
        return super.getDescriptionId(itemStack);
    }


    @Nullable
    public static DelusionData getDelusion(Entity entity){
        if(entity instanceof Player player)
            return player.getData(ErItemVariables.PLAYER_VARIABLES).Vision.get(DataComponentsRegister.DELUSION.get());
        return null;
    }

    public Element.Category getCategory() {
        return category;
    }

    public enum Frame{
        MONDSTADT("mondstadt"),
        LIYUE("liyue"),
        MOON_WHEEL("moon_wheel"),
        SUMERU("sumeru");

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
