package net.wither.er.loottables;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.mcreator.er.ErMod;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.jetbrains.annotations.NotNull;

import static net.wither.er.init.DataComponentsRegister.ARTIFACT;

public class ArtifactInitFunction extends LootItemConditionalFunction {
    private final int star ;


    public ArtifactInitFunction(LootItemCondition[] conditions,int star) {
        super(conditions);
        this.star = star;
    }

    @Override
    public LootItemFunctionType getType() {
        return RegisterLootFunction.ARTIFACT_INIT_FUNCTION.get();
    }

    // Run our enchantment application logic. Most of this is copied from EnchantRandomlyFunction#run.
    @Override
    public ItemStack run(ItemStack stack, LootContext context) {
        RandomSource random = context.getRandom();

        ArtifactData data = DataComponentsRegister.ARTIFACT.getData(stack);
        if(data != null) {
            ARTIFACT.update(stack, d -> d.rolling(this.star));
        }
        else {
            ErMod.LOGGER.warn("Item to Apply is not an artifact {}", stack);
        }
        return stack;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<ArtifactInitFunction> {
        public void serialize(@NotNull JsonObject object, @NotNull ArtifactInitFunction function, @NotNull JsonSerializationContext context) {
            super.serialize(object, function, context);
            object.add("star", context.serialize(function.star));
        }

        public @NotNull ArtifactInitFunction deserialize(@NotNull JsonObject obj, @NotNull JsonDeserializationContext context, LootItemCondition @NotNull [] conditions) {
            int star = GsonHelper.getAsInt(obj, "star", 0);
            return new ArtifactInitFunction(conditions, star);
        }
    }
}
