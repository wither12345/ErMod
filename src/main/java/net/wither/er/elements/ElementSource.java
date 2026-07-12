package net.wither.er.elements;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ElementSource {
    private final Element element ;
    private final ResourceLocation resourceLocation ;
    private float gauge ;
    private boolean applicable;
    private final int time ;
    private final int count ;
    private final boolean permanent;

    public static final ResourceKey<DamageType> ReactionKey = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:reaction")) ;

    public ElementSource(Element element, @Nullable ResourceLocation resourceLocation , float gauge , boolean applicable) {
        this(element,resourceLocation,gauge,applicable,50,2, false);
    }

    public ElementSource(Element element, @Nullable ResourceLocation resourceLocation , float gauge , boolean applicable, boolean permanent) {
        this(element,resourceLocation,gauge,applicable,50,2, permanent);
    }

    public ElementSource(Element element, @Nullable ResourceLocation resourceLocation , float gauge , boolean applicable , int time , int count, boolean permanent) {
        this.element = element;
        this.resourceLocation = resourceLocation;
        this.gauge = gauge ;
        this.applicable = applicable ;
        this.time = time;
        this.count = count;
        this.permanent = permanent;
    }

    public void reduce(float gauge){
        if(gauge != 0) {
            this.gauge -= gauge;
            this.applicable = false ;
        }
    }

    public boolean isApplicable(){
        return applicable ;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public Element getElement() {
        return element;
    }

    public float getGauge() {
        return gauge;
    }

    public Element.Category getCategory() {
        return this.element.getCategory() ;
    }

    public static DamageSource createDamageSource(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 damageSourcePosition, @Nullable ElementSource source){
        return (DamageSource) ((ElementSourceInterface)(new DamageSource(type,directEntity,causingEntity,damageSourcePosition))).er$setElement(source);
    }

    public static DamageSource createDamageSource(Holder<DamageType> type, @Nullable Entity directEntity, @Nullable ElementSource source){
        return createDamageSource(type,directEntity,directEntity,null,source);
    }

    public static DamageSource createDamageSource(DamageSource damageSource, @Nullable ElementSource elementSource){
        return (DamageSource) ((ElementSourceInterface)(damageSource)).er$setElement(elementSource);
    }

    public int getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public boolean canReact(SingleElementalContainer container) {
        return this.getElement().canReact(container.getCategory());
    }

    public ElementSource copy() {
        return new ElementSource(element,resourceLocation,gauge,applicable,time,count, permanent);
    }
}
