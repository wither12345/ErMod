package net.wither.er.elements;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class Dendro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:dendro_immune"));

    public Dendro(){
        super(Map.of(
                Category.HYDRO, Element::bloom,
                Category.PYRO, Element::burning,
                Category.ELECTRO, Element::quicken
        ));
    }

    protected Dendro(Map<Category, ReactionBehavior> map){
        super(map);
    }

    @Override
    public Category getCategory() {
        return Category.DENDRO;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.DENDRO ;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
