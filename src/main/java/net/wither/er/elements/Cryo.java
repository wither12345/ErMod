package net.wither.er.elements;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class Cryo extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:cryo_immune"));

    public Cryo() {
        super(Map.of(
                Category.PYRO, Element::amplifying2,
                Category.HYDRO, Cryo::frozen,
                Category.ELECTRO, Element::superconduct,
                Category.GEO, Geo::cryo,
                Category.ANEMO, Anemo::swirl
        ));
    }

    protected Cryo(Map<Category, ReactionBehavior> map){
        super(map);
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
