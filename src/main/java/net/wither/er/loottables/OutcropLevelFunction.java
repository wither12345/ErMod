package net.wither.er.loottables;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.entity.TrounceBlossomEntity;
import net.mcreator.er.init.ErModItems;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.util.Mth.randomBetween;

public class OutcropLevelFunction extends LootItemConditionalFunction {
    private final int min_level;
    private final int max_level;
    private final int min_base_count;
    private final int max_base_count;
    private final boolean scaling;


    public OutcropLevelFunction(LootItemCondition[] conditions, int min_level , int max_level , int min_base_count , int max_base_count , boolean scaling) {
        super(conditions);
        this.min_level = min_level;
        this.max_level = max_level;
        this.min_base_count = min_base_count ;
        this.max_base_count = max_base_count ;
        this.scaling = scaling;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return RegisterLootFunction.OUTCROP_LEVEL_FUNCTION.get();
    }
    
    @Override
    public @NotNull ItemStack run(@NotNull ItemStack stack, LootContext context) {
        RandomSource random = context.getRandom();
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
                    if (scaling)
                        stack.getOrCreateTag().putInt("moras", (int) (randomBetween(random, min_base_count, max_base_count) * f));
                    else
                        stack.getOrCreateTag().putInt("moras", (int) (randomBetween(random, min_base_count, max_base_count)));
                } else {
                    float f = ((level - min_level) / 25f + 1) * (1 + omenLevel * 0.5f) ;
                    if(!scaling)
                        f = 1 ;
                    stack.setCount((int)(Mth.randomBetween(random , min_base_count , max_base_count) * f));
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
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<OutcropLevelFunction> {
        public void serialize(@NotNull JsonObject object, @NotNull OutcropLevelFunction function, @NotNull JsonSerializationContext context) {
            super.serialize(object, function, context);
            object.add("min_level", context.serialize(function.min_level));
            object.add("max_level", context.serialize(function.max_level));
            object.add("min_count", context.serialize(function.min_base_count));
            object.add("max_count", context.serialize(function.max_base_count));
            object.add("scaling", context.serialize(function.scaling));
        }

        public @NotNull OutcropLevelFunction deserialize(@NotNull JsonObject obj, @NotNull JsonDeserializationContext context, LootItemCondition @NotNull [] conditions) {
            int min_level = GsonHelper.getAsInt(obj, "min_level", 0);
            int max_level = GsonHelper.getAsInt(obj, "max_level", 0);
            int min_base_count = GsonHelper.getAsInt(obj, "min_count", 0);
            int max_base_count = GsonHelper.getAsInt(obj, "max_count", 0);
            boolean scaling = GsonHelper.getAsBoolean(obj, "scaling", false);
            return new OutcropLevelFunction(conditions, min_level, max_level, min_base_count, max_base_count, scaling);
        }
    }
}
