package net.wither.er.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ErMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.RecipeSerializerRegister;
import net.wither.er.item.Vision;
import org.jetbrains.annotations.NotNull;

public class VisionFrameRecipe extends CustomRecipe {
    public final static ResourceLocation UID = ResourceLocation.parse("er:vision_frame_recipe");

    private final Vision.Frame frame;
    private final TagKey<Item> tag ;

    public VisionFrameRecipe(CraftingBookCategory category, Vision.Frame frame) {
        super(category);
        this.frame = frame;
        this.tag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, frame.getName()));
    }

    public static final RecipeType<VisionFrameRecipe> TYPE = RecipeType.simple(UID);

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public Vision.Frame getFrame() {
        return frame;
    }

    public boolean matches(CraftingInput craftingInput, @NotNull Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < craftingInput.size(); ++k) {
            ItemStack itemstack = craftingInput.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof Vision) {
                    ++i;
                } else {
                    if (!itemstack.is(tag)) {
                        return false;
                    }

                    ++j;
                }

                if (j > 1 || i > 1) {
                    return false;
                }
            }
        }

        return i == 1 && j == 1;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.@NotNull Provider provider) {
        ItemStack vision = ItemStack.EMPTY;

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.getItem() instanceof Vision) {
                Item item = itemStack.getItem();
                if (item instanceof Vision) {
                    vision = itemStack.copy();
                    break;
                }
            }
        }
        vision.set(DataComponentsRegister.VISION_FRAME, frame);
        return vision;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegister.VISION_FRAME.get();
    }

    public static class Serializer implements RecipeSerializer<VisionFrameRecipe> {
        private static final MapCodec<VisionFrameRecipe> codec = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(VisionFrameRecipe::category),
                        Vision.FRAME_CODEC.fieldOf("frame").forGetter(VisionFrameRecipe::getFrame)
                ).apply(instance, VisionFrameRecipe::new)
        );
        private static final  StreamCodec<RegistryFriendlyByteBuf, VisionFrameRecipe> streamCodec = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, VisionFrameRecipe::category,
                Vision.FRAME_STREAM_CODEC, VisionFrameRecipe::getFrame,
                VisionFrameRecipe::new
        );


        @Override
        public @NotNull MapCodec<VisionFrameRecipe> codec() {
            return codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, VisionFrameRecipe> streamCodec() {
            return streamCodec;
        }
    }
}
