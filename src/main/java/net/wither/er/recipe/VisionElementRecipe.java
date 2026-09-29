package net.wither.er.recipe;

import com.google.gson.JsonObject;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.wither.er.init.RecipeSerializerRegister;
import net.wither.er.item.Vision;
import net.wither.er.item.data.DelusionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VisionElementRecipe extends CustomRecipe {
    private final ItemStack result ;
    private final Ingredient ingredient;
    private final boolean delusion;

    public VisionElementRecipe(ResourceLocation location, ItemStack result, Ingredient ingredient, boolean delusion) {
        super(location, CraftingBookCategory.MISC);
        this.result = result;
        this.ingredient = ingredient;
        this.delusion = delusion;
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
        ingredients.add(Ingredient.of(delusion ? ErModItems.UNOWNED_DELUSION.get() : ErModItems.UNOWNED_VISION.get()));
        ingredients.add(this.ingredient);
        for (int i = 2; i < 9; i++) {
            ingredients.add(Ingredient.EMPTY);
        }
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
        return this.result.copy();
    }

    public boolean matches(CraftingContainer container, @NotNull Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < container.getContainerSize(); ++k) {
            ItemStack itemstack = container.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(Vision.VISION_TAG) && this.delusion == Vision.isDelusion(itemstack)) {
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
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess access) {
        ItemStack vision = result.copy();

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.is(Vision.VISION_TAG)) {
                vision.getOrCreateTag().putInt("frame", itemStack.getOrCreateTag().getInt("frame"));
                itemStack.getCapability(DelusionData.DELUSION).ifPresent(data ->
                        vision.getCapability(DelusionData.DELUSION).ifPresent(
                                dataNew -> {
                                    dataNew.dmgMulti = data.dmgMulti;
                                    dataNew.critMulti = data.critMulti;
                                    dataNew.healthConsume = data.healthConsume;
                                })
                );
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
        @Override
        public @NotNull VisionElementRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            boolean delusion = jsonObject.has("delusion") && jsonObject.get("delusion").getAsBoolean();
            return new VisionElementRecipe(resourceLocation,
                    ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("result")),
                    Ingredient.fromJson(jsonObject.get("ingredient"), false), delusion);
        }

        @Override
        public @Nullable VisionElementRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            return new VisionElementRecipe(resourceLocation,
                    friendlyByteBuf.readItem(),
                    Ingredient.fromNetwork(friendlyByteBuf),
                    friendlyByteBuf.readBoolean());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull VisionElementRecipe visionElementRecipe) {
            friendlyByteBuf.writeItem(visionElementRecipe.result);
            visionElementRecipe.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeBoolean(visionElementRecipe.delusion);
        }
    }
}
