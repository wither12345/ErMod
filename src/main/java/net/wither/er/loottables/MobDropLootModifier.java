package net.wither.er.loottables;

import com.mojang.serialization.Codec;
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
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;


public class MobDropLootModifier extends LootModifier implements IGlobalLootModifier{
    public static final Codec<MobDropLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            LootModifier.codecStart(inst).apply(inst, MobDropLootModifier::new)
    );

    private static final ResourceLocation tier2 = new ResourceLocation("er:tier2");
    private static final ResourceLocation tier3 = new ResourceLocation("er:tier3");

    protected MobDropLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> objectArrayList, LootContext lootContext) {
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
        if (level <= 15)
            return 1 ;
        double count = (level / 30d) * origin ;
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
