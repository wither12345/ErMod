package net.wither.er.loottables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class AddLootModifier extends LootModifier implements IGlobalLootModifier {
    private final ResourceLocation tableToAdd;
    public static final Codec<AddLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter((AddLootModifier::conditions)),
                    ResourceLocation.CODEC.fieldOf("table").forGetter(AddLootModifier::tableToAdd)
            ).apply(inst, AddLootModifier::new)
    );

    private LootItemCondition[] conditions(){
        return this.conditions;
    }

    private ResourceLocation tableToAdd(){
        return tableToAdd;
    }

    protected AddLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootId) {
        super(conditionsIn);
        this.tableToAdd = lootId;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
        LootTable table = lootContext.getLevel().getServer().getLootData().getLootTable(tableToAdd);
        LootContext context = new LootContext.Builder(lootContext).withQueriedLootTableId(tableToAdd).create(null);
        table.getRandomItems(context, objectArrayList::add);
        return objectArrayList;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return null;
    }
}
