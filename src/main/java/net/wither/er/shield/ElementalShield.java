package net.wither.er.shield;

import net.minecraft.world.entity.Entity;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;

public abstract class ElementalShield extends ErShield{
    abstract Element getElement() ;

    abstract float getGauge() ;
    
    protected boolean permanent(){
        return true;
    }

    @Override
    public void start(Entity owner) {
        super.start(owner);
        if(owner instanceof AuraContainerInterface auraContainerInterface){
            auraContainerInterface.er$getAuraContainer().addAura(new ElementSource(getElement(),null,getGauge(),true, permanent()));
        }
    }

    @Override
    public void end(Entity owner) {
        super.end(owner);
        if(owner instanceof AuraContainerInterface auraContainerInterface)
            auraContainerInterface.er$getAuraContainer().remove(this.getElement());
    }

    @Override
    public boolean tick(ShieldStack stack, Entity owner) {
        return owner instanceof AuraContainerInterface containerInterface && containerInterface.er$getAuraContainer().getAura().get(getElement().getCategory().getId()).hasElement(getElement()) ;
    }
}
