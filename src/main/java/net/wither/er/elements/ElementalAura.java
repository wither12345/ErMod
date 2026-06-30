package net.wither.er.elements;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class ElementalAura {
    private float reduce_rate ;
    private float gauge;
    public int tick ;
    private Entity applier ;

    ElementalAura(ElementSource source , @Nullable Entity applier) {
        this.gauge = source.getGauge() ;
        this.reduce_rate = source.isPermanent() ? 0 : source.getElement().getReduceRate(gauge) ;
        this.applier = applier ;
    }

    public boolean reduce(){
        return this.reduce(reduce_rate * 0.05f);
    }

    public float getGauge() {
        return gauge;
    }

    public void setGauge(float gauge){
        this.gauge = gauge ;
    }

    public boolean reduce(float gauge){
        boolean flag = this.gauge > 0.25 ;
        this.gauge -= gauge ;
        return this.gauge < 0.25 && flag ;
    }

    public void tryToSetGauge(ElementSource source){
        if(this.gauge <= 0){
            this.gauge = source.getGauge() ;
            this.reduce_rate = source.isPermanent() ? 0 : source.getElement().getReduceRate(gauge) ;
        }
        else if(this.gauge < source.getGauge()){
            this.gauge = source.getGauge() ;
            if(source.getElement().overrideReduceRate())
                this.reduce_rate = source.isPermanent() ? 0 : source.getElement().getReduceRate(gauge) ;
        }
    }

    public void setReduceRate(float f){
        this.reduce_rate = f;
    }

    public void addReduceRate(float f){
        this.reduce_rate += f;
    }

    public void setApplier(Entity applier) {
        this.applier = applier ;
    }

    public Entity getApplier() {
        return applier;
    }
}
