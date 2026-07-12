package net.wither.er.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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

public class VisionElementRecipe extends CustomRecipe {
    private final ItemStack result ;
    private final Ingredient ingredient;
    private static final TagKey<Item> VISION_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "vision"));

    public VisionElementRecipe(CraftingBookCategory category, ItemStack result, Ingredient ingredient) {
        super(category);
        this.result = result;
        this.ingredient = ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.of(ErModItems.UNOWNED_VISION));
        ingredients.add(this.ingredient);
        for (int i = 2; i < 9; i++) {
            ingredients.add(Ingredient.EMPTY);
        }
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registryAccess) {
        return this.result.copy();
    }

    public boolean matches(CraftingInput craftingInput, @NotNull Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < craftingInput.size(); ++k) {
            ItemStack itemstack = craftingInput.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(VISION_TAG)) {
                    ++i;
                } else {
                    if (!ingredient.test(itemstack)) {
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
        ItemStack vision = result.copy();

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemStack = input.getItem(i);
            if (itemStack.is(VISION_TAG)) {
                vision.set(DataComponentsRegister.VISION_FRAME, itemStack.getOrDefault(DataComponentsRegister.VISION_FRAME, Vision.Frame.MONDSTADT));
                break;
            }
        }
        return vision;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegister.VISION_ELEMENT.get();
    }

    public static class Serializer implements RecipeSerializer<VisionElementRecipe> {
        private static final MapCodec<VisionElementRecipe> codec = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(VisionElementRecipe::category),
                        ItemStack.CODEC.fieldOf("result").forGetter(VisionElementRecipe::getResult),
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(VisionElementRecipe::getIngredient)

                ).apply(instance, VisionElementRecipe::new)
        );
        private static final  StreamCodec<RegistryFriendlyByteBuf, VisionElementRecipe> streamCodec = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, VisionElementRecipe::category,
                ItemStack.STREAM_CODEC, VisionElementRecipe::getResult,
                Ingredient.CONTENTS_STREAM_CODEC,VisionElementRecipe::getIngredient,
                VisionElementRecipe::new
        );


        @Override
        public @NotNull MapCodec<VisionElementRecipe> codec() {
            return codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, VisionElementRecipe> streamCodec() {
            return streamCodec;
        }
    }
}
