package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.Map;

public class Quicken extends Dendro{
    public Quicken(){
        super(Map.of(
                Category.HYDRO, Element::bloom,
                Category.PYRO, Element::burning,
                Category.ELECTRO, Quicken::aggravate,
                Category.DENDRO, Quicken::spread
        ));
    }

    private static float aggravate(AuraContainer auraContainer,
                                   Element self,
                                   ElementalAura boundAura,
                                   ElementSource source,
                                   EntityHurtEvent.DamageModifier modifier,
                                   @Nullable Entity applier){
        float multiply = 1.15f + EntityHurtEvent.ReactionMultiply.CATALYZE.getMulti(applier);
        if (modifier != null && !modifier.locked) {
            modifier.additional_amount += 3 * multiply * EntityHurtEvent.getLevelMultiply(applier);
        }
        return 0 ;
    }

    private static float spread(AuraContainer auraContainer,
                                Element self,
                                ElementalAura boundAura,
                                ElementSource source,
                                EntityHurtEvent.DamageModifier modifier,
                                @Nullable Entity applier){
        float multiply = 1.25f + EntityHurtEvent.ReactionMultiply.CATALYZE.getMulti(applier);
        if (modifier != null && !modifier.locked) {
            modifier.additional_amount += 3 * multiply * EntityHurtEvent.getLevelMultiply(applier);
        }
        return 0 ;
    }

    @Override
    public Category getCategory() {
        return Category.DENDRO;
    }


    @Override
    public boolean overrideReduceRate() {
        return true;
    }

    @Override
    public float getReduceRate(float gauge) {
        return 1/(gauge * 5 + 6) ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.QUICKEN;
    }
}
