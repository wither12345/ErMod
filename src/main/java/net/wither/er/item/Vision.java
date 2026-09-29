package net.wither.er.item;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.elements.Element;
import net.wither.er.item.data.DelusionData;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Vision extends Item {
    public static final TagKey<Item> VISION_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(ErMod.MODID, "vision"));
    private final Element.Category category;
    @Nullable private final ICapabilityProvider provider;

    public Vision(Element.Category category) {
        this(category, null);
    }

    public Vision(Element.Category category, @Nullable ICapabilityProvider provider){
        super(new Properties().stacksTo(1));
        this.category = category;
        this.provider = provider;
    }

    public static LazyOptional<DelusionData> getOptDelusion(ItemStack itemStack){
        return itemStack.getCapability(DelusionData.DELUSION);
    }

    public static LazyOptional<DelusionData> getOptDelusion(LivingEntity entity){
        if(entity instanceof Player player){
            return getOptDelusion(player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables()).Vision);
        }
        return LazyOptional.empty();
    }

    public static boolean isDelusion(ItemStack stack){
        return getOptDelusion(stack).isPresent();
    }

    public Element.Category getCategory() {
        return category;
    }

    public @NotNull String getDescriptionId(@NotNull ItemStack itemStack) {
        if(itemStack.getOrCreateTag().getInt("frame") == Frame.MOON_WHEEL.ordinal()) return "item.er." + category.toString().toLowerCase() + "_moon_wheel";
        return super.getDescriptionId(itemStack);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return this.provider;
    }

    public enum Frame{
        MONDSTADT,
        LIYUE,
        MOON_WHEEL,
        SUMERU;

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
