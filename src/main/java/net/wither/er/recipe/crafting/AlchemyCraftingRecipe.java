package net.wither.er.recipe.crafting;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.NotNull;

public class AlchemyCraftingRecipe implements Recipe<RecipeInput> {
	private final ItemStack output;
	private final NonNullList<ItemStack> recipeItems;
	private final int mora;

	public AlchemyCraftingRecipe(ItemStack output, NonNullList<ItemStack> recipeItems, int mora) {
		this.output = output;
		this.recipeItems = recipeItems;
		this.mora = mora;
	}

	/*
		public AlchemyCraftingRecipe(ItemStack output, NonNullList<Ingredient> recipeItems) {
			this.output = output;
			this.recipeItems = recipeItems;
		}
	*/
	@Override
	public boolean matches(RecipeInput pContainer, Level pLevel) {
		if (pLevel.isClientSide()) {
			return false;
		}
		return false;
	}


	public NonNullList<ItemStack> getIngredient() {
		return recipeItems;
	}

	public int getMora() {
		return mora;
	}

	@Override
	public ItemStack assemble(RecipeInput input, HolderLookup.Provider holder) {
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight) {
		return true;
	}

	@Override
	public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
		return getResultItem();
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
				.mapCodec(builder -> builder.group(ItemStack.STRICT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output), ItemStack.STRICT_CODEC.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
					ItemStack[] aingredient = ingredients.toArray(ItemStack[]::new); // Skip the empty check and create the array.
					if (aingredient.length == 0) {
						return DataResult.error(() -> "No ingredients found in custom recipe");
					} else {
						return DataResult.success(NonNullList.of(ItemStack.EMPTY, aingredient));
					}
				}, DataResult::success).forGetter(recipe -> recipe.recipeItems), Codec.INT.fieldOf("mora").forGetter(recipe -> recipe.mora)).apply(builder, AlchemyCraftingRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyCraftingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<AlchemyCraftingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, AlchemyCraftingRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static AlchemyCraftingRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			NonNullList<ItemStack> inputs = NonNullList.withSize(buf.readVarInt(), ItemStack.EMPTY);
			inputs.replaceAll(ingredients -> ItemStack.STREAM_CODEC.decode(buf));
			//NonNullList<Integer> count = NonNullList.withSize(buf.readVarInt(), Integer.valueOf(0));
			//count.replaceAll(ingredients -> ByteBufCodecs.INT.decode(buf));
			return new AlchemyCraftingRecipe(ItemStack.STREAM_CODEC.decode(buf), inputs, ByteBufCodecs.INT.decode(buf));
		}

		private static void toNetwork(RegistryFriendlyByteBuf buf, AlchemyCraftingRecipe recipe) {
			buf.writeVarInt(recipe.getIngredient().size());
			for (ItemStack ing : recipe.getIngredient()) {
				ItemStack.STREAM_CODEC.encode(buf, ing);
			}
			ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(null));
			ByteBufCodecs.INT.encode(buf, recipe.getMora());
		}
	}
}