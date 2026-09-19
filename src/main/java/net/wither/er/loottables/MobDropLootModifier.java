package net.wither.er.loottables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mcreator.er.EntityHurtEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;


public class MobDropLootModifier extends LootModifier implements IGlobalLootModifier{
    public static final Codec<MobDropLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            LootModifier.codecStart(inst).apply(inst, MobDropLootModifier::new)
    );

    private static final TagKey<Item> TIER_2 = ItemTags.create(new ResourceLocation("er:tier2"));
    private static final TagKey<Item> TIER_3 = ItemTags.create(new ResourceLocation("er:tier3"));
    private static final TagKey<Item> TIER_4 = ItemTags.create(new ResourceLocation("er:tier4"));

    protected MobDropLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
        if(lootContext.hasParam(LootContextParams.THIS_ENTITY)) {
            Entity entity = lootContext.getParam(LootContextParams.THIS_ENTITY);
            int level = EntityHurtEvent.getEntityLevel(entity);
            for (ItemStack itemStack : objectArrayList) {
                if(itemStack.isEmpty())
                    continue;
                if (itemStack.is(TIER_4))
                    itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 4));
                else if (itemStack.is(TIER_3))
                    itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 3));
                else if (itemStack.is(TIER_2))
                    itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 2));
                else
                    itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 1));
            }
        }
        return objectArrayList;
    }

    private int getCountMultiplied(int origin , int level , int type){
        double basicChance = 0.6;
        if (type == 4) {
            level -= 80;
            basicChance = 0;
        }
        if (type == 3) {
            level -= 60;
            basicChance = 0.2;
        }
        if (type == 2) {
            level -= 30;
            basicChance = 0.4;
        }
        if(level < 1)
            return  0 ;
        double count = (level / 30d + basicChance) * origin;
        int base = (int) count;
        if(Math.random() < count - base){
            base ++ ;
        }
        return base ;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
