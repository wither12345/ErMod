package net.wither.er.elements;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class Hydro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:hydro_immune"));

    public Hydro() {
        super(Map.of(
                Category.PYRO, Element::amplifying15,
                Category.CRYO, Element::frozen,
                Category.DENDRO, Element::bloom,
                Category.ELECTRO, Element::electroCharged,
                Category.GEO, Geo::hydro,
                Category.ANEMO, Anemo::swirl
        ));
    }

    @Override
    public Category getCategory() {
        return Category.HYDRO;
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
