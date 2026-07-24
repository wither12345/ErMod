package net.wither.er.elements;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ElementRegistry;
import net.wither.er.item.data.weapon.ReactionAbility;
import net.wither.er.item.data.weapon.WeaponRefinement;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SingleElementalContainer {
    private final Element.Category category ;
    private final Map<Element,ElementalAura> auras ;
    private final Map<ResourceLocation, AuraContainer.AuraCooldown> cooldownMap = new HashMap<>() ;
    private boolean naturalReduction = true ;

    public SingleElementalContainer(Element.Category category) {
        this.category = category;
        this.auras = new HashMap<>() ;
    }

    public Element.Category getCategory() {
        return category;
    }

    public boolean hasElement(Element element){
        return auras.containsKey(element) ;
    }

    public void reactBy(AuraContainer container ,ElementSource source, LevelAccessor accessor , double x , double y , double z,@Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        float gauge_reducing = source.getElement().reactWith(container, this, source.getGauge(), accessor, x, y, z, damageModifier, applier);//source.getElement().reactWith(container, this, source.getGauge(), accessor, x, y, z, elemental_mastery, damageModifier, applier);
        source.reduce(gauge_reducing);
        if(applier instanceof ErEntityInterface erEntityInterface){
            Object2IntMap<Holder<ArtifactEffect>> map = erEntityInterface.er$getEffectMap();
            for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                if(effect.getKey().value() instanceof ReactionAbility reactionAbility){
                    reactionAbility.onReaction(container, source, this.category, damageModifier, applier, effect.getIntValue());
                }
            }
        }
        if(applier instanceof LivingEntity living){
            WeaponRefinement refinement = living.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
            if(refinement != null && refinement.getAbility() instanceof ReactionAbility reactionAbility)
                reactionAbility.onReaction(container, source, this.category, damageModifier, applier, refinement.refineLevel());
        }
    }


    public void tick(AuraContainer container, LevelAccessor accessor , double x , double y , double z) {
        cooldownMap.values().removeIf(cooldown -> cooldown.time-- <= 0);
        Iterator<Map.Entry<Element, ElementalAura>> iterator = auras.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Element, ElementalAura> ele = iterator.next();
            ElementalAura aura = ele.getValue();
            if (aura.getGauge() > 0) {
                ele.getKey().tick(container, aura, accessor, x, y, z ,this.naturalReduction);
            }
            if (aura.getGauge() <= 0) {
                iterator.remove();
                ele.getKey().end(container);
                container.update();
            }
        }
    }

    public void addAura(ElementSource source, @Nullable Entity applier){
        Element ele = source.getElement() ;
        if(auras.containsKey(ele)){
            auras.get(ele).tryToSetGauge(source);
            auras.get(ele).setApplier(applier);
        }
        else {
            auras.put(ele , new ElementalAura(source, applier)) ;
        }
    }

    public boolean isAvailable(ElementSource source){
        ResourceLocation location = source.getResourceLocation() ;
        if(location != null){
            if(cooldownMap.containsKey(location)) {
                if (cooldownMap.get(location).count-- <= 0) {
                    cooldownMap.get(location).count = source.getCount() ;
                    return true ;
                }
                return false;
            }
            cooldownMap.put(location,new AuraContainer.AuraCooldown(source.getCount() ,source.getTime())) ;
        }
        return true;
    }

    public boolean isEmpty(){
        return auras.isEmpty() ;
    }

    public float getGauge(){
        float gauge = 0 ;
        float independent_gauge = 0 ;
        Set<Map.Entry<Element,ElementalAura>> entrySet = auras.entrySet() ;
        for(Map.Entry<Element,ElementalAura> entry : entrySet){
            if(entry.getKey().independent()){
                independent_gauge += entry.getValue().getGauge() ;
            }
            else {
                gauge = Math.max(gauge, entry.getValue().getGauge());
            }
        }
        return gauge + independent_gauge;
    }

    public float getGaugeExcept(Element element){
        float gauge = 0 ;
        float independent_gauge = 0 ;
        Set<Map.Entry<Element,ElementalAura>> entrySet = auras.entrySet() ;
        for(Map.Entry<Element,ElementalAura> entry : entrySet){
            if(entry.getKey() == element)
                continue;
            if(entry.getKey().independent()){
                independent_gauge += entry.getValue().getGauge() ;
            }
            else {
                gauge = Math.max(gauge, entry.getValue().getGauge());
            }
        }
        return gauge + independent_gauge;
    }

    public boolean reduceAll(float guage){
        boolean flag = false ;
        Set<Map.Entry<Element,ElementalAura>> entrySet = auras.entrySet() ;
        for(Map.Entry<Element,ElementalAura> entry : entrySet){
            if(guage <= 0){
                break;
            }
            if(entry.getKey().independent()){
                ElementalAura aura = entry.getValue() ;
                if(aura.getGauge() > guage){
                    flag = aura.reduce(guage);
                    guage = 0 ;
                }
                else {
                    guage -= aura.getGauge() ;
                    aura.setGauge(0);
                }
            }
        }

        for(Map.Entry<Element,ElementalAura> entry : entrySet){
            if(!entry.getKey().independent()){
                ElementalAura aura = entry.getValue() ;
                if(aura.getGauge() > guage){
                    flag |= aura.reduce(guage) ;
                }
                else {
                    aura.setGauge(0);
                }
            }
        }
        return flag ;
    }

    public void reduceExcept(float guage , Element element){
        Set<Map.Entry<Element,ElementalAura>> entrySet = auras.entrySet() ;
        for(Map.Entry<Element,ElementalAura> entry : entrySet){
            if(guage <= 0){
                break;
            }
            if(entry.getKey() == element) continue;
            if(entry.getKey().independent()){
                ElementalAura aura = entry.getValue() ;
                if(aura.getGauge() > guage){
                    aura.reduce(guage);
                    guage = 0 ;
                }
                else {
                    guage -= aura.getGauge() ;
                    aura.setGauge(0);
                }
            }
        }

        for(Map.Entry<Element,ElementalAura> entry : entrySet){
            if(entry.getKey() == element) continue;
            if(!entry.getKey().independent()){
                ElementalAura aura = entry.getValue() ;
                if(aura.getGauge() > guage){
                    aura.reduce(guage);
                }
                else {
                    aura.setGauge(0);
                }
            }
        }
    }

    public void disableNaturalReduction(){
        this.naturalReduction = false;
    }

    public void enableNaturalReduction(){
        this.naturalReduction = true;
    }

    public int update(int elements){
        for(Map.Entry<Element,ElementalAura> entry : auras.entrySet()){
            if(entry.getKey().getRenderId() != null){
                int index = entry.getKey().getRenderId().getId();
                int value = (elements >> (index << 1)) & 3 ;
                int pos = 3 << (index << 1) ;

                if(value != 3){
                    elements &= ~pos ;
                    if(entry.getValue().getGauge() >= 0.25)
                        elements |= 2 << (index << 1) ;
                    else if(value != 2)
                        elements |= 1 << (index << 1) ;
                }

                if(entry.getKey() == ElementRegistry.FROZEN.get()){
                    elements |= (3 << (Element.RenderId.CRYO.getId() << 1)) ;
                }
                if(entry.getKey() == ElementRegistry.QUICKEN.get()){
                    elements |= (3 << (Element.RenderId.DENDRO.getId() << 1)) ;
                }
                if(entry.getKey() == ElementRegistry.BURNING.get()){
                    elements |= (3 << (Element.RenderId.DENDRO.getId() << 1)) ;
                    elements |= (3 << (Element.RenderId.PYRO.getId() << 1)) ;
                }
            }
        }
        return elements;
    }

    public void remove(Element element) {
        this.auras.remove(element);
    }
}
