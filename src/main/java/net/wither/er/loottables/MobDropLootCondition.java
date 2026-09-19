package net.wither.er.loottables;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public class MobDropLootCondition implements LootItemCondition {
    public static final MapCodec<MobDropLootCondition> CODEC = MapCodec.unit(MobDropLootCondition::new);
    @Override
    public @NotNull LootItemConditionType getType() {
        return LootConditionRegister.MOB_DROP.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getQueriedLootTableId().getPath().split("/")[0].equals("entities");
    }
}
