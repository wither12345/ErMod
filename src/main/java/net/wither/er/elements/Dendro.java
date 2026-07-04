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

public class Dendro extends Element{
    public static final TagKey<EntityType<?>> immune = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:dendro_immune"));

    public Dendro(){
        super(Map.of(
                Category.DENDRO, Dendro::quicken,
                Category.HYDRO, Element::bloom,
                Category.PYRO, Element::burning,
                Category.ELECTRO, Dendro::electro
        ));
    }

    @Override
    public Category getCategory() {
        return Category.DENDRO;
    }

    private static float electro(AuraContainer container ,
                                 SingleElementalContainer singleElementalContainer ,
                                 float gauge,
                                 LevelAccessor accessor ,
                                 double x ,
                                 double y ,
                                 double z,
                                 EntityHurtEvent.DamageModifier damageModifier,
                                 @Nullable Entity applier){

        float gauge_reduction = reacting(gauge, singleElementalContainer) ;
        container.addAura(new ElementSource(ElementRegistry.QUICKEN.get(), null , gauge_reduction, true) , accessor,x,y,z,null,applier);
        return gauge_reduction;
    }

    private static float quicken(AuraContainer container ,
                                   SingleElementalContainer singleElementalContainer ,
                                   float gauge,
                                   LevelAccessor accessor ,
                                   double x ,
                                   double y ,
                                   double z,
                                   EntityHurtEvent.DamageModifier damageModifier,
                                   @Nullable Entity applier){
        if(singleElementalContainer.hasElement(ElementRegistry.QUICKEN.get())) {
            float multiply = 1.25f + EntityHurtEvent.ReactionMultiply.CATALYZE.getMulti(applier);
            if (damageModifier != null && !damageModifier.locked) {
                damageModifier.additional_amount += 3 * multiply * EntityHurtEvent.getLevelMultiply(applier);
            }
        }
        return 0 ;
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
