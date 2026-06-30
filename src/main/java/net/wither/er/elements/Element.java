package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.init.ElementRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;

import static net.minecraft.core.registries.Registries.DAMAGE_TYPE;
import static net.minecraft.core.registries.Registries.ENTITY_TYPE;

public abstract class Element {
    public static final ResourceKey<DamageType> electroCharged = ResourceKey.create(DAMAGE_TYPE, new ResourceLocation("er:electro_charged"));
    public static final ResourceKey<DamageType> burning = ResourceKey.create(DAMAGE_TYPE, new ResourceLocation("er:burning"));
    public static final ResourceKey<DamageType> overload = ResourceKey.create(DAMAGE_TYPE, new ResourceLocation("er:overloaded"));
    public static final TagKey<EntityType<?>> inert = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:inert"));

    public abstract Category getCategory() ;
    public static final ArrayList<Element> types = new ArrayList<>();
    private static int count = 0;
    private static int rendererCount = 0;

    public abstract float reactWith(AuraContainer container , SingleElementalContainer singleElementalContainer , float strength, LevelAccessor accessor , double x , double y , double z, int level , double elemental_mastery , EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) ;

    public TagKey<EntityType<?>> getImmuneTag(){
        return null;
    }

    public boolean shouldReact(AuraContainer container, @Nullable Entity applier){
        if(container.getOwner() instanceof Entity entity)
            return !entity.getType().is(inert);
        return true;
    }

    public void tick(AuraContainer container ,ElementalAura aura, LevelAccessor accessor , double x , double y , double z, int level , boolean naturalReduction){
        if(naturalReduction && aura.reduce())
            container.update();
        aura.tick ++ ;
    }

    public void start(AuraContainer container){
    }

    public void end(AuraContainer container){
        if(container.getOwner() instanceof AuraContainerInterface auraContainerInterface){
            auraContainerInterface.updateElements(container.toInt());
        }
    }

    public boolean independent(){
        return false ;
    }

    public RenderId getRenderId(){
        return null ;
    }

    public boolean isApplicable(){
        return true;
    }

    public boolean overrideReduceRate(){
        return false;
    }

    public float getReduceRate(float gauge){
        return 1/(gauge * 2.5f + 7) ;
    }

    public static float reacting(float gauge , SingleElementalContainer container , float coefficient){
        float gauge_reduction = 0 ;
        if (gauge * coefficient >= container.getGauge()) {
            gauge_reduction = container.getGauge() / coefficient ;
            container.reduceAll(container.getGauge());
        }
        else {
            gauge_reduction = gauge;
            container.reduceAll(gauge * coefficient);
        }
        return gauge_reduction ;
    }

    public static float reacting(float gauge , SingleElementalContainer container){
        return reacting(gauge,container,1) ;
    }

    public static float reactingExcept(float gauge , SingleElementalContainer container , Element element){
        float gauge_reduction = 0 ;
        if (gauge >= container.getGaugeExcept(element)) {
            gauge_reduction = container.getGaugeExcept(element) ;
            container.reduceExcept(container.getGauge(),element);
        }
        else {
            gauge_reduction = gauge;
            container.reduceExcept(gauge,element);
        }
        return gauge_reduction ;
    }

    public Attribute getDamageAttr() {
        return this.getCategory().getDamageAttr();
    }

    public Attribute getResAttr() {
        return this.getCategory().getResAttr();
    }

    public enum Category{
        ANEMO(0xFF00FF99, ErModAttributes.ANEMO_DMG_BONUS, ErModAttributes.ANEMO_RES, true, ErModItems.DANDELION_SEED, ElementRegistry.ANEMO),
        PYRO(0xFFFF0000, ErModAttributes.PYRO_DMG_BONUS, ErModAttributes.PYRO_RES, false, ErModItems.FLAMING_FLOWER_STAMEN, ElementRegistry.PYRO),
        CRYO(0xFF33FFFF, ErModAttributes.CRYO_DMG_BONUS, ErModAttributes.CRYO_RES, true, ErModItems.MIST_FLOWER_COROLLA, ElementRegistry.CRYO),
        DENDRO(0xFF00FF00, ErModAttributes.DENDRO_DMG_BONUS, ErModAttributes.DENDRO_RES, true, ErModItems.SUMERU_ROSE, ElementRegistry.DENDRO),
        ELECTRO(0xFF9900FF, ErModAttributes.ELECTRO_DMG_BONUS, ErModAttributes.ELECTRO_RES, false, ErModItems.ELECTRO_CRYSTAL, ElementRegistry.ELECTRO),
        GEO(0xFFC87600, ErModAttributes.GEO_DMG_BONUS, ErModAttributes.GEO_RES, false, ErModItems.COR_LAPIS, ElementRegistry.GEO),
        HYDRO(0xFF114ACB, ErModAttributes.HYDRO_DMG_BONUS, ErModAttributes.HYDRO_RES, false, ErModItems.LOTUS_HEAD, ElementRegistry.HYDRO);

        private final int id ;
        private final int color ;
        private final boolean dmgPotType;
        private final RegistryObject<Item> brewIngredient ;
        private final RegistryObject<Attribute> damageAttr ;
        private final RegistryObject<Attribute> resAttr ;
        private final RegistryObject<Element> defaultElement;
        private final TagKey<DamageType> tag;
        private final TagKey<DamageType> tagWeak;//1
        private final TagKey<DamageType> tagMedium;//1.5
        private final TagKey<DamageType> tagStrong;//2

        Category(int color ,
                 @Nullable RegistryObject<Attribute> damageAttr ,
                 @Nullable RegistryObject<Attribute> resAttr,
                 boolean dmgPotType,
                 RegistryObject<Item> brewIngredient,
                 RegistryObject<Element> defaultElement){
            this.defaultElement = defaultElement;
            id = count ++;
            this.color = color ;
            this.damageAttr = damageAttr;
            this.resAttr = resAttr;
            this.dmgPotType = dmgPotType ;
            this.brewIngredient = brewIngredient;
            String name = this.name().toLowerCase();
            tag = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", name));
            tagWeak = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", name + "/weak"));
            tagMedium = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", name + "/medium"));
            tagStrong = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", name + "/strong"));
        }

        public int getId(){
            return this.id ;
        }

        public int getColor(){
            return this.color ;
        }

        public Attribute getDamageAttr() {
            return damageAttr.get();
        }

        public Attribute getResAttr() {
            return resAttr.get();
        }

        public RegistryObject<Item> getBrewIngredient() {
            return brewIngredient;
        }

        public Item getBrewItem() {
            return brewIngredient.get();
        }

        public boolean getDmgPotType() {
            return dmgPotType;
        }

        public boolean match(DamageSource source){return source.is(tag);}

        public float getAura(DamageSource source){
            if(source.is(tagStrong)) return 2;
            if(source.is(tagMedium)) return 1.5f;
            if(source.is(tagWeak)) return 1;
            return 0;
        }

        public Element getDefault(){
            return this.defaultElement.get();
        }
    }

    public enum RenderId{
        PYRO("er:textures/mob_effect/pyro.png") ,
        ANEMO("er:textures/mob_effect/anemo.png") ,
        CRYO("er:textures/mob_effect/cryo.png") ,
        ELECTRO("er:textures/mob_effect/electro.png") ,
        DENDRO("er:textures/mob_effect/dendro.png") ,
        HYDRO("er:textures/mob_effect/hydro.png") ,
        GEO("er:textures/mob_effect/geo.png") ,
        FROZEN("er:textures/mob_effect/frozen.png") ,
        QUICKEN("er:textures/mob_effect/quicken.png") ,
        BURNING("er:textures/mob_effect/burning.png") ;

        private final int id ;
        private final ResourceLocation location ;

        RenderId(String location){
            this.id = rendererCount ++ ;
            this.location = new ResourceLocation(location) ;
        }

        public int getId() {
            return id;
        }

        public ResourceLocation getLocation() {
            return location;
        }
    }
}
