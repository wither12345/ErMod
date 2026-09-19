package net.wither.er.loottables;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public class MobDropLootCondition implements LootItemCondition {
    @Override
    public @NotNull LootItemConditionType getType() {
        return LootConditionRegister.MOB_DROP.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getQueriedLootTableId().getPath().split("/")[0].equals("entities");
    }

    public static class Serialize implements Serializer<MobDropLootCondition>{
        @Override
        public void serialize(@NotNull JsonObject jsonObject, @NotNull MobDropLootCondition mobDropLootCondition, @NotNull JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public @NotNull MobDropLootCondition deserialize(@NotNull JsonObject jsonObject, @NotNull JsonDeserializationContext jsonDeserializationContext) {
            return new MobDropLootCondition();
        }
    }
}
