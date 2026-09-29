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
import net.wither.er.item.data.DelusionData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VisionFrameRecipe extends CustomRecipe {
    private final Vision.Frame frame;
    private final TagKey<Item> tag ;
    private final boolean enableDelusion;

    public VisionFrameRecipe(ResourceLocation location, Vision.Frame frame, boolean enableDelusion) {
        super(location, CraftingBookCategory.MISC);
        this.frame = frame;
        this.tag = TagKey.create(Registries.ITEM, new ResourceLocation(ErMod.MODID, frame.name().toLowerCase()));
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
                if (itemstack.is(Vision.VISION_TAG) && (this.enableDelusion || !Vision.isDelusion(itemstack))) {
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

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.is(Vision.VISION_TAG)) {
                ItemStack vision = itemStack.copy();

                vision.getCapability(DelusionData.DELUSION).ifPresent(dataNew ->
                        itemStack.getCapability(DelusionData.DELUSION).ifPresent(
                                data -> {
                                    dataNew.dmgMulti = data.dmgMulti;
                                    dataNew.critMulti = data.critMulti;
                                    dataNew.healthConsume = data.healthConsume;
                                })
                );
                vision.getOrCreateTag().putInt("frame", frame.ordinal());
                return vision;
            }
        }
        return ItemStack.EMPTY;
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
            boolean delusion = !jsonObject.has("enable_delusion") || jsonObject.get("enable_delusion").getAsBoolean();
            return new VisionFrameRecipe(resourceLocation, Vision.Frame.fromString(jsonObject.get("frame").getAsString()), delusion);
        }

        @Override
        public @Nullable VisionFrameRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, @NotNull FriendlyByteBuf friendlyByteBuf) {
            return new VisionFrameRecipe(resourceLocation, Vision.Frame.fromId(friendlyByteBuf.readInt()), friendlyByteBuf.readBoolean());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull VisionFrameRecipe visionFrameRecipe) {
            friendlyByteBuf.writeInt(visionFrameRecipe.frame.ordinal());
            friendlyByteBuf.writeBoolean(visionFrameRecipe.enableDelusion);
        }
    }
}
