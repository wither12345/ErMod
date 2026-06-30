package net.wither.er.loottables;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.ErMod;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;

import java.util.List;

public class ArtifactInitFunction extends LootItemConditionalFunction {
    private final int star ;

    public static final MapCodec<ArtifactInitFunction> CODEC =
            RecordCodecBuilder.mapCodec(inst -> commonFields(inst).and(
                    Codec.INT.fieldOf("star").forGetter(e -> e.star)
            ).apply(inst, ArtifactInitFunction::new));


    public ArtifactInitFunction(List<LootItemCondition> conditions, int star) {
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

        ArtifactData data = stack.getComponents().get(DataComponentsRegister.ARTIFACT.get()) ;
        if(data != null) {
            stack.update(DataComponentsRegister.ARTIFACT.get(), data, d -> d.rolling(this.star)) ;
        }
        else {
            ErMod.LOGGER.warn("Item to Apply is not an artifact {}", stack);
        }
        return stack;
    }
}
