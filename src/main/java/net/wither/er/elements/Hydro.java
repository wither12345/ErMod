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

public class Hydro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:hydro_immune"));

    public Hydro() {
        super(Map.of(
                Category.PYRO, Element::amplifying2,
                Category.CRYO, Hydro::cryo,
                Category.DENDRO, Element::bloom,
                Category.ELECTRO, Element::electroCharged
        ));
    }
    @Override
    public Category getCategory() {
        return Category.HYDRO;
    }

    private static float cryo(AuraContainer container, SingleElementalContainer singleElementalContainer, float gauge, LevelAccessor accessor, double x, double y, double z, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        float gauge_reduction = reactingExcept(gauge, singleElementalContainer, ElementRegistry.FROZEN.get()) ;
        container.addAura(new ElementSource(ElementRegistry.FROZEN.get(), null , 2 * gauge_reduction, true) , accessor,x,y,z,null,applier);
        return gauge_reduction;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.HYDRO ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
