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
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.client.renderer.damge.RenderDamageAmount;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.entity.slimes.DendroSlime;
import net.wither.er.init.AdvancementTriggerRegister;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ElementRegistry;
import net.wither.er.item.data.weapon.DamageAbility;
import net.wither.er.item.data.weapon.WeaponRefinement;
import net.wither.er.network.DamageDisplayMessage;
import net.wither.er.network.ErItemVariables;
import net.wither.er.shield.ShieldStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber
public class EntityHurtEvent {
	private static final TagKey<DamageType> NO_CRITICAL = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:no_critical")) ;
    private static final TagKey<DamageType> CATALYZE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:reaction_multiply/catalyze")) ;
    private static final TagKey<DamageType> TRANSFORMATIVE = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:reaction_multiply/transformative")) ;
    private static final TagKey<DamageType> LUNAR = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:lunar")) ;
	@SubscribeEvent
	public static void onEntityAttacked(LivingIncomingDamageEvent event) {
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
		modifyDamageSource(damagesource, entity) ;
        modifyReaction(damagesource);

		if(damagesource instanceof DamageModifierInterface modifierInterface && damagesource instanceof ElementSourceInterface elementSourceInterface && entity instanceof AuraContainerInterface auraContainerInterface) {
            DamageModifier modifier = modifierInterface.er$getModifier();
            if(sourceentity instanceof ErEntityInterface erEntityInterface){
                Object2IntMap<Holder<ArtifactEffect>> map = erEntityInterface.er$getEffectMap();
                for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                    if(effect.getKey().value() instanceof DamageAbility damageAbility){
                        damageAbility.onHurt(damagesource, entity, modifier, effect.getIntValue());
                    }
                }
            }
            if(sourceentity instanceof LivingEntity living) {
                WeaponRefinement refinement = living.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
                if (refinement != null && refinement.getAbility() instanceof DamageAbility damageAbility)
                    damageAbility.onHurt(damagesource, entity, modifier, refinement.refineLevel());
            }

            double elemental_mastery = 0 ;
			if (sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY) != null)
				elemental_mastery = living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY);
            ElementSource source = elementSourceInterface.er$getSource();
			if(source != null) {
                auraContainerInterface.er$getAuraContainer().addAura(source, modifier, sourceentity);
                ApplyElementMultiply(source.getElement(), entity, sourceentity, modifier);
            }
            else if(entity.getAttribute(ErAttributeRegister.PHYSICAL_RES) != null){
                modifier.res_multiply *= (100f - (float) entity.getAttributeValue(ErAttributeRegister.PHYSICAL_RES)) / 100f ;
            }

			if (!damagesource.is(NO_CRITICAL) && sourceentity instanceof LivingEntity living && living.getAttributeValue(ErModAttributes.CRIT_RATE) > Math.random()) {
				crit_mult += (float) living.getAttributeValue(ErModAttributes.CRIT_DAMAGE);
				modifier.critical = true;
			}

			if(entity instanceof DendroSlime slime && slime.onGround() && slime.isHiding() && damagesource.getDirectEntity() != null)
				modifier.reaction_multiply = 0 ;

			float final_amount = modifier.calculate(event.getAmount(), elemental_mastery) * crit_mult;
            
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
		//PacketDistributor.sendToAllPlayers(new ErData(entity.getId(), entity.getPersistentData().getInt("Frozen")));
	}

	@SubscribeEvent
	public static void afterDamage(LivingDamageEvent.Post event){
		int dmg = Mth.ceil(event.getNewDamage()) ;
        DamageSource source = event.getSource();
		if(source instanceof DamageModifierInterface modifierInterface && dmg > 0) {
            DamageModifier modifier = modifierInterface.er$getModifier();
            if(modifier.critical && source.getEntity() instanceof ServerPlayer player){
                AdvancementTriggerRegister.CRITICAL_DAMAGE.get().trigger(player, event.getNewDamage());
            }
			PacketDistributor.sendToAllPlayers(new DamageDisplayMessage(dmg, event.getEntity().getId(), getARGB(source), modifier.critical, modifier.type));
		}
        ((ElementSourceInterface)source).er$setElement(null);
	}

    private static void modifyDamageSource(DamageSource source, Entity entity){
        int elemental_type = 0;
        float gauge ;
        DamageModifierInterface damageModifierInterface = (DamageModifierInterface) source;

        if(damageModifierInterface.er$getTarget() != entity){
            damageModifierInterface.er$setTarget(entity);
            damageModifierInterface.er$reset();
        }
        ElementSourceInterface elementSourceInterface = (ElementSourceInterface) source;

        if (!damageModifierInterface.er$oriEmpty())
            return;

        for(Element.Category category : Element.Category.values()){
            if(category.match(source)){
                gauge = category.getAura(source);
                Element element = category.getDefault();
                elementSourceInterface.er$setElement(new ElementSource(element, ResourceLocation.parse("er:default"), gauge, element.isApplicable()));
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
            elementSourceInterface.er$setElement(new ElementSource(getEle(elemental_type), ResourceLocation.parse("er:default"), 1, getEle(elemental_type).isApplicable())) ;
    }

    private static void modifyReaction(DamageSource source){
        DamageModifierInterface damageModifierInterface = (DamageModifierInterface) source;
        if(source.is(CATALYZE))
            damageModifierInterface.er$getModifier().multiply = ReactionMultiply.CATALYZE;
        if(source.is(TRANSFORMATIVE))
            damageModifierInterface.er$getModifier().multiply = ReactionMultiply.TRANSFORMATIVE;
        if(source.is(LUNAR)) {
            damageModifierInterface.er$getModifier().multiply = ReactionMultiply.VARIANT;
            damageModifierInterface.er$getModifier().type = RenderDamageAmount.DamageDisplayType.LUNAR;
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

	private static void ApplyElementMultiply(@NotNull Element element, LivingEntity entity, Entity sourceentity , DamageModifier modifier){
		element.getDamageAttr();
		if(sourceentity instanceof LivingEntity living && element.getDamageAttr() != null && living.getAttribute(element.getDamageAttr()) != null){
			modifier.common_multiply *= (float) living.getAttributeValue(element.getDamageAttr()) ;
		}
		if(element.getImmuneTag() != null && entity.getType().is(element.getImmuneTag())){
			modifier.reaction_multiply = 0;
		}
		else if(element.getResAttr() != null && entity.getAttribute(element.getResAttr()) != null){
			modifier.res_multiply *= (100f - (float) entity.getAttributeValue(element.getResAttr())) / 100f ;
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
        return switch (type){
            case 0 -> 2.78f * (float) (elemental_mastery / (elemental_mastery + 1400));//Melt Vaporize
            case 1 -> 16f * (float) (elemental_mastery / (elemental_mastery + 2000)) + 1f;//Overloaded, Superconduct, Electro-Charged, Burning, Shattered, Swirl, Bloom, Hyperbloom,Burgeon
            case 2 -> 5f * (float) (elemental_mastery / (elemental_mastery + 1200));//Spread Aggravate
            case 3 -> 4.44f * (float) (elemental_mastery / (elemental_mastery + 1400)) + 1;//Crystallize
            default -> 1f;
        };
	}

	public static boolean shouldHurt(Entity entity1, Entity entity2) {
		if (entity1 == null || entity2 == null)
			return true;
        if(entity1 instanceof OwnableEntity ownable && ownable.getOwner() != null)
            entity1 = ownable.getOwner();
        if(entity2 instanceof OwnableEntity ownable && ownable.getOwner() != null)
            entity2 = ownable.getOwner();
		PlayerTeam team1 = entity1.level().getScoreboard().getPlayersTeam(entity1 instanceof Player _pl ? _pl.getGameProfile().getName() : entity1.getStringUUID());
		PlayerTeam team2 = entity2.level().getScoreboard().getPlayersTeam(entity2 instanceof Player _pl ? _pl.getGameProfile().getName() : entity2.getStringUUID());
        if(team1 != null || team2 != null) return team1 != team2;
        if(entity1 == entity2) return false;
        if(entity1 instanceof Targeting targeting && targeting.getTarget() == entity2) return true;
        if(entity2 instanceof Targeting targeting && targeting.getTarget() == entity1) return true;
        return (entity1 instanceof Enemy) ^ (entity2 instanceof Enemy);
    }

	public static float getLevelMultiply(int level) {
		return (0.001642f * level * level * level + 0.015823f * level * level + 16.980456f * level + 0.832476f)/17;
	}

	public static float getLevelMultiply(Entity entity) {
		return entity == null ? 1 : getLevelMultiply(getEntityLevel(entity)) ;
	}

	public static int getEntityLevel(Entity entity) {
		if (entity instanceof Player) {
			ErItemVariables.PlayerVariables _vars = entity.getData(ErItemVariables.PLAYER_VARIABLES);
			if (_vars.Stella_Fortuna.getItem() instanceof StellaFortunas) {
				return _vars.Stella_Fortuna.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("level") + 1;
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
			return living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY) ;
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
        public RenderDamageAmount.DamageDisplayType type = RenderDamageAmount.DamageDisplayType.NORMAL;

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
                AttributeInstance instance = living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY);
                if(instance != null)
                    return this.getMulti(instance.getValue());
            }
            return 1;
        }
    }
}