package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class AuraContainer {
    private final ArrayList<SingleElementalContainer> containersList ;
    private final Object owner ;

    public AuraContainer(Object owner){
        this.owner = owner;
        containersList = new ArrayList<>(Element.Category.values().length) ;
        for(Element.Category category : Element.Category.values()){
            containersList.add(new SingleElementalContainer(category));
        }
    }

    public void addAura(ElementSource auraToAdd){
        if(owner instanceof LivingEntity living)
            addAura(auraToAdd, living.level(),living.getX(),living.getY(),living.getZ(),0,0,null,null);
    }

    public void addAura(ElementSource auraToAdd, LevelAccessor accessor , double x , double y , double z, int level , double elemental_mastery , @Nullable EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier){
        if(auraToAdd.getGauge() == 0)
            return;
        if(auraToAdd.getElement().shouldReact(this, applier) && containersList.get(auraToAdd.getCategory().getId()).isAvailable(auraToAdd)) {
            for (SingleElementalContainer container : containersList) {
                if (auraToAdd.getGauge() > 0)
                    container.reactBy(this, auraToAdd, accessor, x, y, z, level, elemental_mastery, damageModifier, applier);
            }
            if (auraToAdd.isApplicable()) {
                auraToAdd.getElement().start(this);
                containersList.get(auraToAdd.getCategory().getId()).addAura(auraToAdd, applier);
            }
            if (damageModifier != null) {
                damageModifier.locked = false;
            }
            update();
        }
    }

    public boolean hasElementCategory(Element.Category category){
        return !this.containersList.get(category.getId()).isEmpty();
    }

    public void update(){
        if(owner instanceof AuraContainerInterface auraContainerInterface){
            auraContainerInterface.updateElements(this.toInt());
        }
    }

    public ArrayList<SingleElementalContainer> getAura() {
        return containersList;
    }

    public void tick(LevelAccessor accessor , double x , double y , double z, int level){
        for(SingleElementalContainer container : this.containersList){
            container.tick(this,accessor,x,y,z,level);
        }
    }

    public Object getOwner() {
        return owner;
    }

    public int toInt(){
        int elements = 0 ;
        for(SingleElementalContainer container : this.containersList){
            elements = container.update(elements) ;
        }
        return elements;
    }

    public static class AuraCooldown{
        public int time ;
        public int count ;

        public AuraCooldown(){
            this.time = 50 ;
            this.count = 2 ;
        }

        public AuraCooldown(int count , int time){
            this.count = count ;
            this.time = time ;
        }
    }
}
