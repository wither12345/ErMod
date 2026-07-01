package net.mcreator.er.jei_recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlchemyCraftingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id ;
	private final ItemStack output;
	private final NonNullList<ItemStack> recipeItems;
	private final int mora;

	public AlchemyCraftingRecipe(ResourceLocation id, ItemStack output, NonNullList<ItemStack> recipeItems, int mora) {
        this.id = id;
		this.output = output;
		this.recipeItems = recipeItems;
		this.mora = mora;
	}


	public NonNullList<ItemStack> getIngredient() {
		return recipeItems;
	}

	public int getMora() {
		return mora;
	}


	@Override
	public boolean matches(SimpleContainer simpleContainer, Level level) {
		return false;
	}

	@Override
	public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return output.copy();
	}


    @Override
    public ResourceLocation getId() {
        return this.id;
    }


	public @NotNull ItemStack getResultItem() {
		return output.copy();
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return Serializer.INSTANCE;
	}

	public static class Type implements RecipeType<AlchemyCraftingRecipe> {
		private Type() {
		}

		public static final RecipeType<AlchemyCraftingRecipe> INSTANCE = new Type();
	}

	public static class Serializer implements RecipeSerializer<AlchemyCraftingRecipe> {
		public static final Serializer INSTANCE = new Serializer();
		private static final MapCodec<AlchemyCraftingRecipe> CODEC = RecordCodecBuilder
				.mapCodec(builder -> builder.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(recipe -> recipe.id),
                    ItemStack.CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
                    ItemStack.CODEC.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
					ItemStack[] aingredient = ingredients.toArray(ItemStack[]::new); // Skip the empty check and create the array.
					if (aingredient.length == 0) {
						return DataResult.error(() -> "No ingredients found in custom recipe");
					} else {
						return DataResult.success(NonNullList.of(ItemStack.EMPTY, aingredient));
					}
				}, DataResult::success).forGetter(recipe -> recipe.recipeItems), Codec.INT.fieldOf("mora").forGetter(recipe -> recipe.mora)).apply(builder, AlchemyCraftingRecipe::new));

		public MapCodec<AlchemyCraftingRecipe> getCodec() {
			return CODEC;
		}

		@Override
		public @NotNull AlchemyCraftingRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "output"));

			// 解析材料列表
			NonNullList<ItemStack> ingredients = NonNullList.create();
			JsonArray ingredientsArray = jsonObject.getAsJsonArray("ingredients");
			for (JsonElement element : ingredientsArray) {
				JsonObject ingredientObj = element.getAsJsonObject();
				ItemStack stack = ShapedRecipe.itemStackFromJson(ingredientObj);
				ingredients.add(stack);
			}

			// 解析 mora
			int mora = jsonObject.get("mora").getAsInt();
			return new AlchemyCraftingRecipe(resourceLocation, output, ingredients, mora);
		}

		@Override
		public @Nullable AlchemyCraftingRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
			NonNullList<ItemStack> inputs = NonNullList.withSize(friendlyByteBuf.readVarInt(), ItemStack.EMPTY);
			inputs.replaceAll(ingredients -> friendlyByteBuf.readItem());
			return new AlchemyCraftingRecipe(resourceLocation, friendlyByteBuf.readItem(), inputs, friendlyByteBuf.readInt());
		}

		@Override
		public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull AlchemyCraftingRecipe alchemyCraftingRecipe) {
			friendlyByteBuf.writeVarInt(alchemyCraftingRecipe.getIngredient().size());
			for (ItemStack ing : alchemyCraftingRecipe.getIngredient()) {
				friendlyByteBuf.writeItem(ing);
			}
			friendlyByteBuf.writeItem(alchemyCraftingRecipe.getResultItem());
			friendlyByteBuf.writeInt(alchemyCraftingRecipe.getMora());
		}
	}
}