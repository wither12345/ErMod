package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelAccessor;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.Map;

public class Cryo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:cryo_immune"));

    public Cryo() {
        super(Map.of(
                Category.PYRO, Element::amplifying15,
                Category.HYDRO, Cryo::hydro,
                Category.ELECTRO, Element::superconduct
        ));
    }

    private static float hydro(AuraContainer container,
                               SingleElementalContainer singleElementalContainer,
                               float gauge,
                               LevelAccessor accessor,
                               double x,
                               double y,
                               double z,
                               EntityHurtEvent.DamageModifier damageModifier,
                               @Nullable Entity applier){
        float gauge_reduction = reacting(gauge , singleElementalContainer) ;
        container.addAura(new ElementSource(ElementRegistry.FROZEN.get(), null , gauge_reduction * 2, true) , accessor,x,y,z,null,applier);
        return gauge_reduction;
    }

    @Override
    public Category getCategory() {
        return Category.CRYO;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.CRYO ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
