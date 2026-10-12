package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.HealthFloatingAbility;
import net.wither.er.item.data.weapon.TickAbility;
import net.wither.er.item.data.weapon.WeaponRefinement;

@EventBusSubscriber
public class OnEvents {
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event){
        LivingEntity entity = event.getEntity();
        if(entity.getAttributes().hasAttribute(ErModAttributes.INCOMING_HEALING_BONUS))
            event.setAmount(event.getAmount() * (float)entity.getAttributeValue(ErModAttributes.INCOMING_HEALING_BONUS));
    }

    @SubscribeEvent
    public static void onTick(EntityTickEvent.Pre event){
        Entity entity = event.getEntity();
        if(entity instanceof IErEntity erEntity && entity instanceof LivingEntity living){
            Object2IntMap<Holder<ArtifactEffect>> map = erEntity.er$getEffectMap();
            for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                if(effect.getKey().value() instanceof TickAbility ability){
                    ability.tick(living, effect.getIntValue());
                }
            }
            WeaponRefinement refinement = living.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
            if(refinement != null && refinement.getAbility() instanceof TickAbility ability){
                ability.tick(living, refinement.refineLevel());
            }
        }
    }

    @SubscribeEvent
    public static void onDrop(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Frog){
            event.getDrops().add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ErModItems.FROG.get())));
        }
    }

    public static void onFloating(LivingEntity entity, float d){
        if(entity instanceof IErEntity erEntity){
            Object2IntMap<Holder<ArtifactEffect>> map = erEntity.er$getEffectMap();
            for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                if(effect.getKey().value() instanceof HealthFloatingAbility ability){
                    ability.onFloat(entity, d, effect.getIntValue());
                }
            }
        }
        WeaponRefinement refinement = entity.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
        if(refinement != null && refinement.getAbility() instanceof HealthFloatingAbility ability){
            ability.onFloat(entity, d, refinement.refineLevel());
        }
    }
}
