/*
 * Type :
 * 1 Anemo
 * 2 Cryo
 * 3 Dendro
 * 4 Electro
 * 5 Geo
 * 6 Hydro
 * 7 Pyro
*/
package net.mcreator.er;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.procedures.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.client.renderer.damage.RenderDamageAmount;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.entity.slimes.DendroSlime;
import net.wither.er.init.ElementRegistry;
import net.wither.er.item.data.weapon.DamageAbility;
import net.wither.er.item.weapons.AbilityWeapon;
import net.wither.er.network.DamageDisplayMessage;
import net.wither.er.network.ErItemVariables;
import net.wither.er.shield.ShieldStack;

import java.util.List;

import static net.minecraft.core.registries.Registries.DAMAGE_TYPE;

@Mod.EventBusSubscriber
public class EntityHurtEvent {
    public static final TagKey<DamageType> ER$NO_KB = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er:no_knockback")) ;
    private static final TagKey<DamageType> NO_CRITICAL = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:no_critical")) ;
    private static final TagKey<DamageType> CATALYZE = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:reaction_multiply/catalyze")) ;
    private static final TagKey<DamageType> TRANSFORMATIVE = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:reaction_multiply/transformative")) ;
    private static final TagKey<DamageType> LUNAR = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:lunar")) ;

	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
        if (event == null)
            return;
        DamageSource damagesource = event.getSource();
        LivingEntity entity = event.getEntity();
        Entity sourceentity = event.getSource().getEntity();
        LevelAccessor world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        float crit_mult = 1;
        modifyDamageSource(damagesource) ;
        if(damagesource instanceof DamageModifierInterface modifierInterface && damagesource instanceof ElementSourceInterface elementSourceInterface && entity instanceof AuraContainerInterface auraContainerInterface) {
            if(sourceentity instanceof ErEntityInterface erEntityInterface){
                Object2IntMap<ArtifactEffect> map = erEntityInterface.er$getEffectMap();
                for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                    if(effect.getKey() instanceof DamageAbility damageAbility){
                        damageAbility.onHurt(damagesource, entity, modifierInterface.getModifier(), effect.getIntValue());
                    }
                }
            }
            if(sourceentity instanceof LivingEntity living && living.getMainHandItem().getItem() instanceof AbilityWeapon abilityWeapon && abilityWeapon.getAbility() instanceof DamageAbility ability) {
                CompoundTag tag = living.getMainHandItem().getOrCreateTag();
                int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
                ability.onHurt(damagesource, entity, modifierInterface.getModifier(), refinement);
            }
            double elemental_mastery = 0 ;
            if (sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY.get()) != null)
                elemental_mastery = living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY.get());
            if(elementSourceInterface.er$getSource() != null && elementSourceInterface.er$getSource().getElement() != null) {
                auraContainerInterface.er$getAuraContainer().addAura(elementSourceInterface.er$getSource(), world, x, y, z, modifierInterface.getModifier(), sourceentity);
                ApplyElementMultiply(elementSourceInterface.er$getSource().getElement(), entity, sourceentity, modifierInterface.getModifier());
            }

            if (!damagesource.is(NO_CRITICAL) && sourceentity instanceof LivingEntity living && living.getAttributeValue(ErModAttributes.CRIT_RATE.get()) > Math.random()) {
                crit_mult += (float) living.getAttributeValue(ErModAttributes.CRIT_DAMAGE.get());
                modifierInterface.getModifier().critical = true;
            }

            if(entity instanceof DendroSlime slime && slime.onGround() && slime.isHiding() && damagesource.getDirectEntity() != null)
                modifierInterface.getModifier().reaction_multiply = 0 ;

            float final_amount = modifierInterface.getModifier().calculate(event.getAmount(), elemental_mastery) * crit_mult;
            if (entity instanceof ErEntityInterface enti) {
                List<ShieldStack> shields = enti.er$getShieldStacks();
                float shield_absorb = 0f;
                for (ShieldStack shield : shields) {
                    if(elementSourceInterface.er$getSource() != null)
                        shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, elementSourceInterface.er$getSource().getCategory().getId()));
                    else
                        shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, 0));
                }
                event.setAmount(final_amount - shield_absorb);
            }
        }
	}

	@SubscribeEvent
	public static void afterDamage(LivingDamageEvent event){
		int dmg = Mth.ceil(event.getAmount()) ;
		if(event.getSource() instanceof DamageModifierInterface modifierInterface && dmg > 0) {
			ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new DamageDisplayMessage(dmg, event.getEntity().getId(), getARGB(event.getSource()), modifierInterface.getModifier().critical, modifierInterface.getModifier().type));
		}
	}

	private static void modifyDamageSource(DamageSource source){
		int elemental_type = 0;
        float gauge ;

		if(source instanceof ElementSourceInterface elementSourceInterface){
			if(elementSourceInterface.er$getSource() != null)
				return;
            for(Element.Category category : Element.Category.values()){
                if(category.match(source)){
                    gauge = category.getAura(source);
                    Element element = category.getDefault();
                    elementSourceInterface.er$setElement(new ElementSource(element, new ResourceLocation("er:default"), gauge, element.isApplicable()));
                    break;
                }
            }
            if(source.getDirectEntity() instanceof ElementSourceInterface elementSourceInterface1){
                ElementSource source1 = elementSourceInterface1.er$getSource();
                if(source1 == null) return;
                elementSourceInterface.er$setElement(source1) ;
            }
			if(source.getEntity() != null)
				elemental_type = getInfusionType(source.getEntity().level(), source.getEntity(), source.getDirectEntity());
			if(elemental_type != 0)
				elementSourceInterface.er$setElement(new ElementSource(getEle(elemental_type), new ResourceLocation("er:default"), 1, getEle(elemental_type).isApplicable())) ;

            if(source instanceof DamageModifierInterface modifierInterface){
                if(source.is(CATALYZE))
                    modifierInterface.getModifier().multiply = ReactionMultiply.CATALYZE;
                if(source.is(TRANSFORMATIVE))
                    modifierInterface.getModifier().multiply = ReactionMultiply.TRANSFORMATIVE;
                if(source.is(LUNAR)){
                    modifierInterface.getModifier().multiply = ReactionMultiply.VARIANT;
                    modifierInterface.getModifier().type = RenderDamageAmount.DamageDisplayType.LUNAR;
                }
            }
		}
	}

	private static Element getEle(int i){
        return switch (i){
            case 2 -> ElementRegistry.CRYO.get();
            case 3 -> ElementRegistry.DENDRO.get();
            case 4 -> ElementRegistry.ELECTRO.get();
            case 5 -> ElementRegistry.GEO.get();
            case 6 -> ElementRegistry.HYDRO.get();
            case 7 -> ElementRegistry.PYRO.get();
            default -> ElementRegistry.ANEMO.get();
        };
	}

	private static int getARGB(DamageSource source){
		if(source instanceof ElementSourceInterface elementSourceInterface && elementSourceInterface.er$getSource() != null){
			return elementSourceInterface.er$getSource().getCategory().getColor() ;
		}
		return 0xffffffff ;
	}

	private static void ApplyElementMultiply(Element element, LivingEntity entity, Entity sourceentity , DamageModifier modifier){
		element.getDamageAttr();
		if(sourceentity instanceof LivingEntity living && element.getDamageAttr() != null && living.getAttribute(element.getDamageAttr()) != null){
			modifier.reaction_multiply *= (float) living.getAttributeValue(element.getDamageAttr()) ;
		}
		if(element.getImmuneTag() != null && entity.getType().is(element.getImmuneTag())){
			modifier.reaction_multiply = 0;
		}
		else if(element.getResAttr() != null && entity.getAttribute(element.getResAttr()) != null){
			modifier.reaction_multiply *= (100f - (float) entity.getAttributeValue(element.getResAttr())) / 100f ;
		}
	}

	public static int getInfusionType(LevelAccessor world, Entity entity, Entity immediatesourceentity) {
		if (entity == null)
			return 0;
		if (immediatesourceentity == entity) {
			if (entity instanceof LivingEntity && ((LivingEntity) entity).getMainHandItem().getItem() instanceof MultipleInfusion) {
				Item item = ((LivingEntity) entity).getMainHandItem().getItem();
				return ((MultipleInfusion) item).getInfusion(((LivingEntity) entity).getMainHandItem(), entity);
			}
			if (IsAnemoInfusionProcedure.execute(world, entity)) {
				return 1;
			} else if (IsCryoInfusionProcedure.execute(world, entity)) {
				return 2;
			} else if (IsDendroInfusionProcedure.execute(world, entity)) {
				return 3;
			} else if (IsElectroInfusionProcedure.execute(world, entity)) {
				return 4;
			} else if (IsGeoInfusionProcedure.execute(world, entity)) {
				return 5;
			} else if (IsHydroInfusionProcedure.execute(world, entity)) {
				return 6;
			} else if (IsPyroInfusionProcedure.execute(world, entity)) {
				return 7;
			}
		}
		return 0;
	}

	public static float getElementalMasteryMultiply(int type, double elemental_mastery) {
		if (type == 0) //Melt Vaporize
			return 2.78f * (float) (elemental_mastery / (elemental_mastery + 1400));
		if (type == 1) //Overloaded, Superconduct, Electro-Charged, Burning, Shattered, Swirl, Bloom, Hyperbloom,Burgeon
			return 16f * (float) (elemental_mastery / (elemental_mastery + 2000)) + 1f;
		if (type == 2) //Spread Aggravate
			return 5f * (float) (elemental_mastery / (elemental_mastery + 1200));
		if (type == 3) //Crystallize
			return 4.44f * (float) (elemental_mastery / (elemental_mastery + 1400)) + 1;
		return 1f;
	}

	public static boolean shouldHurt(Entity entity1, Entity entity2) {
		if (entity1 == null || entity2 == null)
			return true;
		PlayerTeam team1 = entity1.level().getScoreboard().getPlayersTeam(entity1 instanceof Player _pl ? _pl.getGameProfile().getName() : entity1.getStringUUID());
		PlayerTeam team2 = entity2.level().getScoreboard().getPlayersTeam(entity2 instanceof Player _pl ? _pl.getGameProfile().getName() : entity2.getStringUUID());
		return entity1 != entity2 && (team1 != team2 || team1 == null) && (entity1 instanceof OwnableEntity owned ? owned.getOwner() : entity1) != (entity2 instanceof OwnableEntity owned ? owned.getOwner() : entity2);
	}

	public static float getLevelMultiply(int level) {
		return (0.001642f * level * level * level + 0.015823f * level * level + 16.980456f * level + 0.832476f)/17;
	}

	public static float getLevelMultiply(Entity entity) {
		return getLevelMultiply(getEntityLevel(entity)) ;
	}

	public static int getEntityLevel(Entity entity) {
		if (entity instanceof Player) {
			ErItemVariables.PlayerVariables _vars = entity.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables());
			if (_vars.Stella_Fortuna.getItem() instanceof StellaFortunas) {
				return _vars.Stella_Fortuna.getOrCreateTag().getInt("level") + 1;
			}
			return 1 ;
		}
		else if(entity instanceof LivingEntity)
			return entity.getPersistentData().getInt("erLevel");
		else if(entity instanceof OwnableEntity ownable)
			return getEntityLevel(ownable.getOwner()) ;
		return 0 ;
	}

	public static double getElementalMastery(Entity entity){
		if(entity instanceof LivingEntity living){
			return living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY.get());
		}
		return 0 ;
	}

    public static class DamageModifier {
        public boolean locked = false ;
        public boolean critical = false ;
        public float reaction_multiply = 1;
        public float common_multiply = 1;
        public float basic = 1;
        public float res_multiply = 1;
        public float additional_amount = 0;
        public ReactionMultiply multiply = null;
        private RenderDamageAmount.DamageDisplayType type = RenderDamageAmount.DamageDisplayType.NORMAL;

        public float calculate(float dmg, double elementalMastery){
            return (dmg + additional_amount) * basic * (reaction_multiply + (multiply == null ? 1 : multiply.getMulti(elementalMastery)) - 1) * common_multiply * res_multiply;
        }
    }

    public enum ReactionMultiply {
        AMPLIFYING(2.78f, 1400),//Melt Vaporize
        CATALYZE(5, 1200),//Spread Aggravate
        TRANSFORMATIVE(16, 2000),//Overloaded, Superconduct, Electro-Charged, Burning, Shattered, Swirl, Bloom, Hyperbloom, Burgeon
        VARIANT(6, 2000),
        CRYSTALLIZE(4.44f, 1400);

        private final float maxMultiply;
        private final int halfMultiAmount;

        ReactionMultiply(float maxMultiply, int halfMultiAmount) {
            this.maxMultiply = maxMultiply;
            this.halfMultiAmount = halfMultiAmount;
        }

        public float getMulti(double elementalMastery) {
            return (float) (maxMultiply * (elementalMastery) / (elementalMastery + halfMultiAmount)) + 1;
        }

        public float getMulti(Entity entity){
            if(entity instanceof LivingEntity living){
                AttributeInstance instance = living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY.get());
                if(instance != null)
                    return this.getMulti(instance.getValue());
            }
            return 1;
        }
    }
}