package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.recipe.VisionFrameRecipe;

import java.util.function.Supplier;

public class RecipeSerializerRegister {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ErMod.MODID);

    public static final Supplier<RecipeSerializer<VisionFrameRecipe>> VISION_FRAME = RECIPE_SERIALIZERS.register("vision_frame_recipe", VisionFrameRecipe.Serializer::new);


    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ErMod.MODID);

    public static final Supplier<RecipeType<VisionFrameRecipe>> VISION_FRAME_TYPE = RECIPE_TYPES.register("vision_frame_recipe", () -> VisionFrameRecipe.TYPE);

}
