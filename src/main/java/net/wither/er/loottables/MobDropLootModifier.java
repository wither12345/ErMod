package net.wither.er.loottables;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.entity.TrounceBlossomEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;


public class MobDropLootModifier extends LootModifier {
    public static final MapCodec<MobDropLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            // LootModifier#codecStart adds the conditions field.
            LootModifier.codecStart(inst).apply(inst, MobDropLootModifier::new)
    );

    private static final ResourceLocation tier2 = ResourceLocation.parse("er:tier2") ;
    private static final ResourceLocation tier3 = ResourceLocation.parse("er:tier3") ;

    protected MobDropLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
        if(lootContext.hasParam(LootContextParams.THIS_ENTITY)) {
            Entity entity = lootContext.getParam(LootContextParams.THIS_ENTITY);
            if(!(entity instanceof TrounceBlossomEntity)) {
                int level = EntityHurtEvent.getEntityLevel(entity);
                for (ItemStack itemStack : objectArrayList) {
                    if(itemStack.isEmpty())
                        continue;
                    if (itemStack.is(ItemTags.create(tier3)))
                        itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 3));
                    else if (itemStack.is(ItemTags.create(tier2)))
                        itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 2));
                    else
                        itemStack.setCount(getCountMultiplied(itemStack.getCount(), level, 1));
                }
            }
        }
        return objectArrayList;
    }

    private int getCountMultiplied(int origin , int level , int type){
        if (type == 3) level -= 70 ;
        if (type == 2) level -= 30 ;
        if(level < 1)
            return  0 ;
        double count = (level / 30d + 0.7) * origin;
        int base = (int) count;
        if(Math.random() < count - base){
            base ++ ;
        }
        return base ;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
