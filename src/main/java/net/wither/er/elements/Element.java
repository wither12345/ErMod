package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.entity.BloomEntityEntity;
import net.wither.er.entity.LunarChargedCloud;
import net.wither.er.init.ElementRegistry;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.Vision;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.core.registries.Registries.DAMAGE_TYPE;

public abstract class Element {
    public static final ResourceKey<DamageType> ELECTRO_CHARGED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:electro_charged"));
    public static final ResourceKey<DamageType> LUNAR_CHARGED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:lunar_charged"));
    public static final ResourceKey<DamageType> BURNING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:burning"));
    public static final ResourceKey<DamageType> OVERLOAD = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:overloaded"));
    public static final ResourceKey<DamageType> SWIRL = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:swirl"));
    public static final ResourceKey<DamageType> BLOOM = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:bloom"));
    public static final ResourceKey<DamageType> SUPERCONDUCT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:superconduct"));
    public static final ResourceKey<DamageType> LUNAR_CRYSTALLIZE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:lunar_crystallize"));
    public static final TagKey<EntityType<?>> INERT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("er:inert"));

    public abstract Category getCategory() ;
    public static final ArrayList<Element> types = new ArrayList<>();
    private static int count = 0;
    private static int rendererCount = 0;
    protected final Map<Category, ReactionBehavior> map;

    protected Element(Map<Category, ReactionBehavior> map) {
        this.map = map;
    }

    public boolean canReact(ElementSource source){
        return this.map.containsKey(source.getCategory());
    }

    public float reactWith(AuraContainer auraContainer, ElementalAura boundAura, ElementSource source, EntityHurtEvent.DamageModifier modifier, @Nullable Entity applier){
        return this.map.get(source.getCategory()).reactWith(auraContainer, this, boundAura, source, modifier, applier);
    }

    public TagKey<EntityType<?>> getImmuneTag(){
        return null;
    }

    public boolean shouldReact(AuraContainer container, @Nullable Entity applier){
        if(container.getOwner() instanceof Entity entity)
            return !entity.getType().is(INERT);
        return true;
    }

    public void tick(AuraContainer container ,ElementalAura aura, LevelAccessor accessor , double x , double y , double z , boolean naturalReduction){
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

    public static boolean isLunar(Entity entity){
        return entity instanceof Player player && player.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables()).Vision.getOrCreateTag().getInt("frame") == Vision.Frame.MOON_WHEEL.ordinal();
    }

    public static float reacting(ElementSource source, ElementalAura aura , float coefficient){
        float gauge_reduction = 0 ;
        float gauge = source.getGauge();
        if (gauge * coefficient >= aura.getGauge()) {
            gauge_reduction = aura.getGauge() / coefficient ;
            aura.reduce(aura.getGauge());
        }
        else {
            gauge_reduction = gauge;
            aura.reduce(gauge * coefficient);
        }
        return gauge_reduction ;
    }

    public static float reacting(ElementSource source , ElementalAura aura){
        return reacting(source, aura,1) ;
    }

    protected static float amplifying2(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if (modifier != null && !modifier.locked) {
            modifier.basic = 2f ;
            modifier.locked = true;
            modifier.multiply = EntityHurtEvent.ReactionMultiply.AMPLIFYING;
        }
        return reacting(source , boundAura , 2) ;
    }

    protected static float amplifying15(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if (modifier != null && !modifier.locked) {
            modifier.basic = 1.5f ;
            modifier.locked = true;
            modifier.multiply = EntityHurtEvent.ReactionMultiply.AMPLIFYING;
        }
        return reacting(source , boundAura , 0.5f) ;
    }

    protected static float electroCharged(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if(auraContainer.getOwner() instanceof LivingEntity entity && entity.level() instanceof ServerLevel level){
            if(isLunar(applier)){
                final Vec3 _center = entity.position();
                List<LunarChargedCloud> clouds = level.getEntitiesOfClass(LunarChargedCloud.class, new AABB(_center, _center).inflate(20 / 2d), e -> true);
                if(clouds.isEmpty())
                    ErModEntities.LUNAR_CLOUD.get().spawn(level, entity.getOnPos().above(5), MobSpawnType.MOB_SUMMONED);
                else
                    clouds.forEach(LunarChargedCloud::refresh);
            }
        }
        return 0;
    }

    protected static float frozen(AuraContainer auraContainer,
                                Element self,
                                ElementalAura boundAura,
                                ElementSource source,
                                EntityHurtEvent.DamageModifier modifier,
                                @Nullable Entity applier){
        float gauge_reduction = reacting(source , boundAura) ;
        if(source.getElement() != ElementRegistry.FROZEN.get())
            auraContainer.addAura(new ElementSource(ElementRegistry.FROZEN.get(), null , gauge_reduction * 2, true) , null,applier);
        return gauge_reduction;
    }

    protected static float burning(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if(source.getElement() != ElementRegistry.BURNING.get())
            auraContainer.addAura(new ElementSource(ElementRegistry.BURNING.get(), null , 1.6f, true) , null,applier);
        return 0;
    }

    protected static float quicken(AuraContainer auraContainer,
                                  Element self,
                                  ElementalAura boundAura,
                                  ElementSource source,
                                  EntityHurtEvent.DamageModifier modifier,
                                  @Nullable Entity applier){
        float gauge_reduction = reacting(source, boundAura) ;
        auraContainer.addAura(new ElementSource(ElementRegistry.QUICKEN.get(), null , gauge_reduction, true) , null,applier);
        return gauge_reduction;
    }

    protected static float overLoad(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if (modifier != null && !modifier.locked && auraContainer.getOwner() instanceof Entity entity) {
            Level level = entity.level();
            Vec3 pos = entity.position();

            if (level instanceof ServerLevel _level) {
                _level.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z, 1, 0, 0,
                        0, 0);
                _level.playSound(null, entity.getOnPos(), BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.explode")), SoundSource.NEUTRAL, 1, 1);
            }
            level.getEntitiesOfClass(LivingEntity.class, new AABB(pos, pos).inflate(3), e -> true).stream()
                    .filter(e -> EntityHurtEvent.shouldHurt(e, applier))
                    .forEach(e -> e.hurt(
                            ElementSource.createDamageSource(
                                    level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(OVERLOAD),
                                    applier,
                                    new ElementSource(ElementRegistry.PYRO.get(), new ResourceLocation("er.overloaded.reaction"), 0, true)
                            ), 11 * EntityHurtEvent.getLevelMultiply(applier)));
            modifier.locked = true;
        }
        return reacting(source , boundAura) ;
    }

    protected static float bloom(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if (auraContainer.getOwner() instanceof Entity entity && entity.level() instanceof ServerLevel serverLevel) {
            BloomEntityEntity entityToSpawn = ErModEntities.BLOOM_ENTITY.get().spawn(serverLevel, entity.getOnPos(), MobSpawnType.MOB_SUMMONED);
            if(entityToSpawn != null) {
                entityToSpawn.setOwner(applier);
                entityToSpawn.moveTo(entity.position().offsetRandom(RandomSource.create(), 1));
            }
        }
        return reacting(source , boundAura , 0.5f) ;
    }

    protected static float superconduct(AuraContainer auraContainer,
                                       Element self,
                                       ElementalAura boundAura,
                                       ElementSource source,
                                       EntityHurtEvent.DamageModifier modifier,
                                       @Nullable Entity applier){
        if(auraContainer.getOwner() instanceof Entity entity) {
            final Vec3 _center = entity.position();
            Level level = entity.level();
            List<LivingEntity> _entfound = level.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(2), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entity_iterator : _entfound) {
                if (entity_iterator != applier) {
                    entity_iterator.hurt(
                            ElementSource.createDamageSource(
                                    level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SUPERCONDUCT),
                                    applier,
                                    new ElementSource(ElementRegistry.CRYO.get(), new ResourceLocation("er.superconduct.reaction"), 0, true)
                            ),
                            2.4f * EntityHurtEvent.getLevelMultiply(applier));
                    if (!entity_iterator.level().isClientSide())
                        entity_iterator.addEffect(new MobEffectInstance(ErModMobEffects.SUPERCONDUCT.get(), 144, 0, false, false));
                }
            }
        }
        return reacting(source , boundAura) ;
    }

    public static SimpleParticleType getParticle(Category category){
        return switch (category){
            case GEO -> ErModParticleTypes.SMALL_GEO_PARTICLE.get();
            case CRYO -> ErModParticleTypes.SMALL_CRYO_PARTICLE.get();
            case PYRO -> ErModParticleTypes.SMALL_PYRO_PARTICLE.get();
            case ANEMO -> ErModParticleTypes.SMALL_ANEMO_PARTICLE.get();
            case HYDRO -> ErModParticleTypes.SMALL_HYDRO_PARTICLE.get();
            case DENDRO -> ErModParticleTypes.SMALL_DENDRO_PARTICLE.get();
            case ELECTRO -> ErModParticleTypes.SMALL_ELECTRO_PARTICLE.get();
        };
    }

    public Attribute getDamageAttr() {
        return this.getCategory().getDamageAttr();
    }

    public Attribute getResAttr() {
        return this.getCategory().getResAttr();
    }

    public enum Category implements StringRepresentable {
        ANEMO(0xFF44FF99, ErModAttributes.ANEMO_DMG_BONUS, ErAttributeRegister.ANEMO_RES, true, ErModItems.DANDELION_SEED, ElementRegistry.ANEMO),
        PYRO(0xFFFF4444, ErModAttributes.PYRO_DMG_BONUS, ErAttributeRegister.PYRO_RES, false, ErModItems.FLAMING_FLOWER_STAMEN, ElementRegistry.PYRO),
        CRYO(0xFF33FFFF, ErModAttributes.CRYO_DMG_BONUS, ErAttributeRegister.CRYO_RES, true, ErModItems.MIST_FLOWER_COROLLA, ElementRegistry.CRYO),
        DENDRO(0xFF44FF44, ErModAttributes.DENDRO_DMG_BONUS, ErAttributeRegister.DENDRO_RES, true, ErModItems.SUMERU_ROSE, ElementRegistry.DENDRO),
        ELECTRO(0xFF9944FF, ErModAttributes.ELECTRO_DMG_BONUS, ErAttributeRegister.ELECTRO_RES, false, ErModItems.ELECTRO_CRYSTAL, ElementRegistry.ELECTRO),
        GEO(0xFFC87644, ErModAttributes.GEO_DMG_BONUS, ErAttributeRegister.GEO_RES, false, ErModItems.COR_LAPIS, ElementRegistry.GEO),
        HYDRO(0xFF114ACB, ErModAttributes.HYDRO_DMG_BONUS, ErAttributeRegister.HYDRO_RES, false, ErModItems.LOTUS_HEAD, ElementRegistry.HYDRO);

        private final int id ;
        private final int color ;
        private final boolean dmgPotType;
        private final RegistryObject<Item> brewIngredient ;
        private final RegistryObject<Attribute> damageAttr ;
        private final RegistryObject<Attribute> resAttr ;
        private final Supplier<Element> defaultElement;
        private final TagKey<DamageType> tag;
        private final TagKey<DamageType> tagWeak;//1
        private final TagKey<DamageType> tagMedium;//1.5
        private final TagKey<DamageType> tagStrong;//2
        private final String nameLowerCase ;

        Category(int color , @Nullable RegistryObject<Attribute> damageAttr , @Nullable RegistryObject<Attribute> resAttr, boolean dmgPotType, RegistryObject<Item> brewIngredient, Supplier<Element> defaultElement){
            this.defaultElement = defaultElement;
            id = count ++;
            this.color = color ;
            this.damageAttr = damageAttr;
            this.resAttr = resAttr;
            this.dmgPotType = dmgPotType ;
            this.brewIngredient = brewIngredient;
            nameLowerCase = this.name().toLowerCase();
            tag = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", nameLowerCase));
            tagWeak = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", nameLowerCase + "/weak"));
            tagMedium = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", nameLowerCase + "/medium"));
            tagStrong = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er", nameLowerCase + "/strong"));
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

        public RegistryObject<Attribute> getResHolder(){
            return resAttr;
        }

        public Item getBrewIngredient() {
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

        @Override
        public @NotNull String getSerializedName() {
            return this.nameLowerCase;
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
