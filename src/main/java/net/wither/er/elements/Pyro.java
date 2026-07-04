package net.wither.er.elements;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.wither.er.entity.BloomEntityEntity;

import javax.annotation.Nullable;
import java.util.Map;

public class Pyro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:pyro_immune"));

    public Pyro() {
        super(Map.of(
                Category.HYDRO, Element::amplifying15,
                Category.CRYO, Element::amplifying2,
                Category.DENDRO, Element::burning,
                Category.ELECTRO, Element::overLoad
        ));
    }

    @Override
    public Category getCategory() {
        return Category.PYRO ;
    }

    @Override
    public boolean shouldReact(AuraContainer container, @Nullable Entity applier) {
        if(container.getOwner() instanceof BloomEntityEntity bloom){
            bloom.explode(9f, applier);
            return false ;
        }
        return super.shouldReact(container,applier) ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.PYRO ;
    }

    @Override
    public boolean overrideReduceRate() {
        return true;
    }

    @Override
    public TagKey<EntityType<?>> getImmuneTag() {
        return immune;
    }
}
