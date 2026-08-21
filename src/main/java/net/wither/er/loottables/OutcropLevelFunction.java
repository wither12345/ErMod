package net.wither.er.loottables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.entity.TrounceBlossomEntity;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OutcropLevelFunction extends LootItemConditionalFunction {
    private final int min_level;
    private final int max_level;
    private final int min_base_count;
    private final int max_base_count;
    private final boolean scaling;

    public static final MapCodec<OutcropLevelFunction> CODEC =
            RecordCodecBuilder.mapCodec(inst -> commonFields(inst).and(inst.group(
                    Codec.INT.fieldOf("min_level").forGetter(e -> e.min_level),
                    Codec.INT.fieldOf("max_level").forGetter(e -> e.max_level),
                    Codec.INT.fieldOf("min_count").forGetter(e -> e.min_base_count),
                    Codec.INT.fieldOf("max_count").forGetter(e -> e.max_base_count),
                    Codec.BOOL.fieldOf("scaling").forGetter(e -> e.scaling)
            )).apply(inst, OutcropLevelFunction::new));


    public OutcropLevelFunction(List<LootItemCondition> conditions, int min_level , int max_level , int min_base_count , int max_base_count , boolean scaling) {
        super(conditions);
        this.min_level = min_level;
        this.max_level = max_level;
        this.min_base_count = min_base_count ;
        this.max_base_count = max_base_count ;
        this.scaling = scaling;
    }

    @Override
    public @NotNull LootItemFunctionType<OutcropLevelFunction> getType() {
        return RegisterLootFunction.OUTCROP_LEVEL_FUNCTION.get();
    }

    // Run our enchantment application logic. Most of this is copied from EnchantRandomlyFunction#run.
    @Override
    public @NotNull ItemStack run(@NotNull ItemStack stack, LootContext context) {
        RandomSource random = context.getRandom();
        double multi = context.hasParam(TrounceBlossomEntity.BLOSSOM_MULTI) ? context.getParam(TrounceBlossomEntity.BLOSSOM_MULTI) : 1;
        if(context.hasParam(LootContextParams.THIS_ENTITY)) {
            Entity entity = context.getParam(LootContextParams.THIS_ENTITY);
            int omenLevel = 0 ;
            if(entity instanceof TrounceBlossomEntity trounceBlossom){
                omenLevel = trounceBlossom.getOmenLevel() ;
            }
            int level = EntityHurtEvent.getEntityLevel(entity);
            if(level <= this.max_level && level >= this.min_level) {
                if (stack.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
                    int f = (level - min_level)  + (1 + omenLevel) ;
                    if(scaling)
                        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt("moras", (int)(Mth.randomBetween(random , min_base_count , max_base_count) * f * multi)));
                    else
                        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt("moras", (int)(Mth.randomBetween(random , min_base_count , max_base_count) * multi)));
                } else {
                    float f = ((level - min_level) / 25f + 1) * (1 + omenLevel * 0.5f) ;
                    if(!scaling)
                        f = 1 ;
                    stack.setCount((int)(Mth.randomBetween(random , min_base_count , max_base_count) * f * multi));
                }
            }
            else {
                stack.setCount(0);
            }
        }
        else {
            ErMod.LOGGER.warn("Couldn't find a entity {}", stack);
        }
        return stack;
    }
}
