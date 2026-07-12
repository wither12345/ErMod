package net.wither.er.recipe;

import com.google.gson.JsonObject;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.wither.er.init.RecipeSerializerRegister;
import net.wither.er.item.Vision;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VisionFrameRecipe extends CustomRecipe {
    private final Vision.Frame frame;
    private final TagKey<Item> tag ;
    private static final TagKey<Item> VISION_TAG = TagKey.create(Registries.ITEM, new ResourceLocation(ErMod.MODID, "vision"));

    public VisionFrameRecipe(ResourceLocation location, Vision.Frame frame) {
        super(location, CraftingBookCategory.MISC);
        this.frame = frame;
        this.tag = TagKey.create(Registries.ITEM, new ResourceLocation(ErMod.MODID, frame.name().toLowerCase()));
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
        ingredients.add(Ingredient.of(ErModItems.UNOWNED_VISION.get()));
        ingredients.add(Ingredient.of(tag));
        for (int i = 2; i < 9; i++) {
            ingredients.add(Ingredient.EMPTY);
        }
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        ItemStack result = new ItemStack(ErModItems.UNOWNED_VISION.get());
        result.getOrCreateTag().putInt("frame", frame.ordinal());
        return result;
    }
    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        int i = 0;
        int j = 0;

        for(int k = 0; k < container.getContainerSize(); ++k) {
            ItemStack itemstack = container.getItem(k);
            if (!itemstack.isEmpty()) {
                if (itemstack.is(VISION_TAG)) {
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
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess access) {
        ItemStack vision = ItemStack.EMPTY;

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.is(VISION_TAG)) {
                vision = itemStack.copy();
                break;
            }
        }
        vision.getOrCreateTag().putInt("frame", frame.ordinal());
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
        @Override
        public @NotNull VisionFrameRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            return new VisionFrameRecipe(resourceLocation, Vision.Frame.fromString(jsonObject.get("frame").getAsString()));
        }

        @Override
        public @Nullable VisionFrameRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            return new VisionFrameRecipe(resourceLocation, Vision.Frame.fromId(friendlyByteBuf.readInt()));
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull VisionFrameRecipe visionFrameRecipe) {
            friendlyByteBuf.writeInt(visionFrameRecipe.frame.ordinal());
        }
    }
}
