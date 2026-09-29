package net.wither.er.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
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
import net.wither.er.item.data.DelusionData;
import org.jetbrains.annotations.NotNull;

public class VisionFrameRecipe extends CustomRecipe {
    private final Vision.Frame frame;
    private final TagKey<Item> tag ;
    private final boolean enableDelusion;

    public VisionFrameRecipe(CraftingBookCategory category, Vision.Frame frame, boolean enableDelusion) {
        super(category);
        this.frame = frame;
        this.tag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, frame.getName()));
        this.enableDelusion = enableDelusion;
    }

    public Vision.Frame getFrame() {
        return frame;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ErModItems.UNOWNED_VISION));
        ingredients.add(Ingredient.of(tag));
        for (int i = 2; i < 9; i++) {
            ingredients.add(Ingredient.EMPTY);
        }
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registryAccess) {
        ItemStack result = new ItemStack(ErModItems.UNOWNED_VISION.get());
        result.set(DataComponentsRegister.VISION_FRAME.get(), frame);
        return result;
    }

    public boolean isEnableDelusion(){
        return this.enableDelusion;
    }

    public boolean matches(CraftingInput craftingInput, @NotNull Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < craftingInput.size(); ++k) {
            ItemStack itemstack = craftingInput.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(Vision.TAG) && (this.enableDelusion || !itemstack.has(DataComponentsRegister.DELUSION))) {
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
            if (itemStack.is(Vision.TAG)) {
                vision = itemStack.copy();

                DelusionData data = itemStack.get(DataComponentsRegister.DELUSION);
                if(data != null)
                    vision.set(DataComponentsRegister.DELUSION, data);
                break;
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
                        Vision.FRAME_CODEC.fieldOf("frame").forGetter(VisionFrameRecipe::getFrame),
                        Codec.BOOL.optionalFieldOf("enable_delusion", true).forGetter(VisionFrameRecipe::isEnableDelusion)
                ).apply(instance, VisionFrameRecipe::new)
        );
        private static final  StreamCodec<RegistryFriendlyByteBuf, VisionFrameRecipe> streamCodec = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, VisionFrameRecipe::category,
                Vision.FRAME_STREAM_CODEC, VisionFrameRecipe::getFrame,
                ByteBufCodecs.BOOL, VisionFrameRecipe::isEnableDelusion,
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
