package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.entity.BloomEntityEntity;
import net.wither.er.entity.LunarChargedCloud;
import net.wither.er.init.ElementRegistry;
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
    private final Map<Category, ReactionBehavior> map;

    protected Element(Map<Category, ReactionBehavior> map) {
        this.map = map;
    }

    public float reactWith(AuraContainer auraContainer, SingleElementalContainer singleElementalContainer, float gauge, LevelAccessor accessor , double x , double y , double z, EntityHurtEvent.DamageModifier modifier, @Nullable Entity applier){
        return this.map.get(singleElementalContainer.getCategory()).reactWith(auraContainer, singleElementalContainer, gauge, accessor, x, y, z, modifier, applier);
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

    protected static float amplifying2(AuraContainer container ,
                                    SingleElementalContainer singleElementalContainer ,
                                    float gauge,
                                    LevelAccessor accessor ,
                                    double x ,
                                    double y ,
                                    double z,
                                    EntityHurtEvent.DamageModifier damageModifier,
                                    @Nullable Entity applier){
        if (damageModifier != null && !damageModifier.locked) {
            damageModifier.basic = 2f ;
            damageModifier.locked = true;
            damageModifier.multiply = EntityHurtEvent.ReactionMultiply.AMPLIFYING;
        }
        return reacting(gauge , singleElementalContainer , 2) ;
    }

    protected static float amplifying15(AuraContainer container ,
                                     SingleElementalContainer singleElementalContainer ,
                                     float gauge,
                                     LevelAccessor accessor ,
                                     double x ,
                                     double y ,
                                     double z,
                                     EntityHurtEvent.DamageModifier damageModifier,
                                     @Nullable Entity applier){
        if (damageModifier != null && !damageModifier.locked) {
            damageModifier.basic = 1.5f ;
            damageModifier.locked = true;
            damageModifier.multiply = EntityHurtEvent.ReactionMultiply.AMPLIFYING;
        }
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    protected static float electroCharged(AuraContainer container ,
                                          SingleElementalContainer singleElementalContainer ,
                                          float gauge,
                                          LevelAccessor accessor ,
                                          double x ,
                                          double y ,
                                          double z,
                                          EntityHurtEvent.DamageModifier damageModifier,
                                          @Nullable Entity applier){
        if(container.getOwner() instanceof LivingEntity entity && accessor instanceof ServerLevel level){
            if(isLunar(applier)){
                final Vec3 _center = new Vec3(x, y, z);
                List<LunarChargedCloud> clouds = accessor.getEntitiesOfClass(LunarChargedCloud.class, new AABB(_center, _center).inflate(20 / 2d), e -> true);
                if(clouds.isEmpty()){
                    ErModEntities.LUNAR_CLOUD.get().spawn(level, entity.getOnPos().above(5), MobSpawnType.MOB_SUMMONED);
                }
                else {
                    clouds.forEach(LunarChargedCloud::refresh);
                }
            }
        }
        return 0;
    }

    protected static float burning(AuraContainer container ,
                                   SingleElementalContainer singleElementalContainer ,
                                   float gauge,
                                   LevelAccessor accessor ,
                                   double x ,
                                   double y ,
                                   double z,
                                   EntityHurtEvent.DamageModifier damageModifier,
                                   @Nullable Entity applier){
        container.addAura(new ElementSource(ElementRegistry.BURNING.get(), null , 1.6f, true) , accessor,x,y,z,null,applier);
        return 0;
    }

    protected static float overLoad(AuraContainer container ,
                                    SingleElementalContainer singleElementalContainer ,
                                    float gauge,
                                    LevelAccessor accessor ,
                                    double x ,
                                    double y ,
                                    double z,
                                    EntityHurtEvent.DamageModifier damageModifier,
                                    @Nullable Entity applier){
        if (damageModifier != null && !damageModifier.locked) {
            if (accessor instanceof ServerLevel _level) {
                _level.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 1, 0, 0,
                        0, 0);
                _level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation("entity.generic.explode")), SoundSource.NEUTRAL, 1, 1);
            }
            final Vec3 _center = new Vec3(x, y, z);
            List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
            for (LivingEntity entityiterator : _entfound) {
                if (EntityHurtEvent.shouldHurt(applier, entityiterator)) {
                    entityiterator.hurt(
                            ElementSource.createDamageSource(
                                    accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(OVERLOAD),
                                    applier,
                                    new ElementSource(ElementRegistry.PYRO.get(), new ResourceLocation("er.overloaded.reaction"), 0, true)
                            ), 11  * EntityHurtEvent.getLevelMultiply(applier));
                    entityiterator.setDeltaMovement(new Vec3((entityiterator.getDeltaMovement().x() * 3), (entityiterator.getDeltaMovement().y() * 2), (entityiterator.getDeltaMovement().z() * 3)));
                }
            }
            damageModifier.locked = true;
        }
        return reacting(gauge , singleElementalContainer) ;
    }

    protected static float bloom(AuraContainer container,
                                SingleElementalContainer singleElementalContainer,
                                float gauge,
                                LevelAccessor accessor,
                                double x,
                                double y,
                                double z,
                                EntityHurtEvent.DamageModifier damageModifier,
                                @Nullable Entity applier){
        if (accessor instanceof ServerLevel _level) {
            BloomEntityEntity entityToSpawn = ErModEntities.BLOOM_ENTITY.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
            if(entityToSpawn != null) {
                entityToSpawn.setOwner(applier);
                entityToSpawn.moveTo(x, y, z, accessor.getRandom().nextFloat() * 360F, 0);
                entityToSpawn.setYRot(accessor.getRandom().nextFloat() * 360F);
            }
        }
        return reacting(gauge , singleElementalContainer , 0.5f) ;
    }

    protected static float superconduct(AuraContainer container ,
                                        SingleElementalContainer singleElementalContainer ,
                                        float gauge,
                                        LevelAccessor accessor ,
                                        double x ,
                                        double y ,
                                        double z,
                                        EntityHurtEvent.DamageModifier damageModifier,
                                        @Nullable Entity applier){
        final Vec3 _center = new Vec3(x, y, z);
        List<LivingEntity> _entfound = accessor.getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (LivingEntity entity_iterator : _entfound) {
            if (entity_iterator != applier) {
                entity_iterator.hurt(
                        ElementSource.createDamageSource(
                                accessor.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SUPERCONDUCT) ,
                                applier ,
                                new ElementSource(ElementRegistry.CRYO.get() , new ResourceLocation("er.superconduct.reaction") , 0, true)
                        ),
                        2.4f * EntityHurtEvent.getLevelMultiply(applier));
                if (!entity_iterator.level().isClientSide())
                    entity_iterator.addEffect(new MobEffectInstance(ErModMobEffects.SUPERCONDUCT.get(), 144, 0, false, false));
            }
        }
        return reacting(gauge , singleElementalContainer) ;
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

    public boolean canReact(Category category) {
        return this.map.containsKey(category);
    }

    public enum Category implements StringRepresentable {
        ANEMO(0xFF44FF99, ErModAttributes.ANEMO_DMG_BONUS, ErModAttributes.ANEMO_RES, true, ErModItems.DANDELION_SEED, ElementRegistry.ANEMO),
        PYRO(0xFFFF4444, ErModAttributes.PYRO_DMG_BONUS, ErModAttributes.PYRO_RES, false, ErModItems.FLAMING_FLOWER_STAMEN, ElementRegistry.PYRO),
        CRYO(0xFF33FFFF, ErModAttributes.CRYO_DMG_BONUS, ErModAttributes.CRYO_RES, true, ErModItems.MIST_FLOWER_COROLLA, ElementRegistry.CRYO),
        DENDRO(0xFF44FF44, ErModAttributes.DENDRO_DMG_BONUS, ErModAttributes.DENDRO_RES, true, ErModItems.SUMERU_ROSE, ElementRegistry.DENDRO),
        ELECTRO(0xFF9944FF, ErModAttributes.ELECTRO_DMG_BONUS, ErModAttributes.ELECTRO_RES, false, ErModItems.ELECTRO_CRYSTAL, ElementRegistry.ELECTRO),
        GEO(0xFFC87644, ErModAttributes.GEO_DMG_BONUS, ErModAttributes.GEO_RES, false, ErModItems.COR_LAPIS, ElementRegistry.GEO),
        HYDRO(0xFF114ACB, ErModAttributes.HYDRO_DMG_BONUS, ErModAttributes.HYDRO_RES, false, ErModItems.LOTUS_HEAD, ElementRegistry.HYDRO);

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
