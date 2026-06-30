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

import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.procedures.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.entity.slimes.DendroSlime;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ElementRegistry;
import net.wither.er.item.data.weapon.WeaponRefinement;
import net.wither.er.network.DamageDisplayMessage;
import net.wither.er.network.ErItemVariables;
import net.wither.er.shield.ShieldStack;

import java.util.List;

@EventBusSubscriber
public class EntityHurtEvent {
	private static final TagKey<DamageType> noCritical = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("er:no_critical")) ;
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
		applyElementSource(damagesource) ;
		if(damagesource instanceof DamageModifierInterface modifierInterface && damagesource instanceof ElementSourceInterface elementSourceInterface && entity instanceof AuraContainerInterface auraContainerInterface) {
			if(sourceentity instanceof LivingEntity living) {
                WeaponRefinement refinement = living.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
                if (refinement != null)
                    refinement.modify(damagesource, entity, modifierInterface.getModifier());
            }

            double elemental_mastery = 0 ;
			if (sourceentity instanceof LivingEntity && ((LivingEntity) sourceentity).getAttribute(ErModAttributes.ELEMENTAL_MASTERY) != null)
				elemental_mastery = ((LivingEntity) sourceentity).getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY);
			if(elementSourceInterface.getSource() != null && elementSourceInterface.getSource().getElement() != null) {
				auraContainerInterface.er$getAuraContainer().addAura(elementSourceInterface.getSource(), world, x, y, z, getEntityLevel(sourceentity), elemental_mastery, modifierInterface.getModifier(), sourceentity);
				ApplyElementMultiply(elementSourceInterface.getSource().getElement(), entity, sourceentity, modifierInterface.getModifier());
			}

			if (!damagesource.is(noCritical) && sourceentity instanceof LivingEntity living && living.getAttributeValue(ErModAttributes.CRIT_RATE) > Math.random()) {
				crit_mult += (float) living.getAttributeValue(ErModAttributes.CRIT_DAMAGE);
				modifierInterface.getModifier().critical = true;
			}

			if(entity instanceof DendroSlime slime && slime.onGround() && slime.isHiding() && damagesource.getDirectEntity() != null)
				modifierInterface.getModifier().reaction_multiply = 0 ;

			float final_amount = modifierInterface.getModifier().calculate(event.getAmount()) * crit_mult;
			if (entity instanceof ErEntityInterface enti) {
				List<ShieldStack> shields = enti.er$getShieldStacks();
				float shield_absorb = 0f;
				for (ShieldStack shield : shields) {
					if(elementSourceInterface.getSource() != null)
						shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, elementSourceInterface.getSource().getCategory().getId()));
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
		if(event.getSource() instanceof DamageModifierInterface modifierInterface && dmg > 0) {
			PacketDistributor.sendToAllPlayers(new DamageDisplayMessage(dmg, event.getEntity().getId(), getARGB(event.getSource()), modifierInterface.getModifier().critical));
		}
	}

    private static void applyElementSource(DamageSource source){
        int elemental_type = 0;
        float gauge ;
        if(source instanceof ElementSourceInterface elementSourceInterface){
            if(elementSourceInterface.getSource() != null)
                return;
            ResourceLocation resourceLocation = ResourceLocation.parse("er:default");
            for(Element.Category category : Element.Category.values()){
                if(category.match(source)){
                    gauge = category.getAura(source);
                    Element element = category.getDefault();
                    elementSourceInterface.setElement(new ElementSource(element, ResourceLocation.parse("er:default"), gauge, element.isApplicable()));
                    return;
                }
            }
            if(source.getEntity() != null)
                elemental_type = getInfusion_Type(source.getEntity().level(), source.getEntity(), source.getDirectEntity());
            if(elemental_type != 0)
                elementSourceInterface.setElement(new ElementSource(getEle(elemental_type), ResourceLocation.parse("er:default"), 1, getEle(elemental_type).isApplicable())) ;
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
		if(source instanceof ElementSourceInterface elementSourceInterface && elementSourceInterface.getSource() != null){
			return elementSourceInterface.getSource().getCategory().getColor() ;
		}
		return 0xffffffff ;
	}

	private static void ApplyElementMultiply(Element element, LivingEntity entity, Entity sourceentity , DamageModifier modifier){
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

	public static class DamageModifier {
		public boolean locked = false ;
		public boolean critical = false ;
		public float reaction_multiply = 1;
        public float common_multiply = 1;
        public float res_multiply = 1;
		public float additional_amount = 0;

        public float calculate(float dmg){
            return (dmg + additional_amount) * reaction_multiply * common_multiply * res_multiply;
        }
	}

	public static int getInfusion_Type(LevelAccessor world, Entity entity, Entity immediatesourceentity) {
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
		} else {
			if (immediatesourceentity instanceof LargeFireball || immediatesourceentity instanceof SmallFireball) {
				return 7;
			} else {
				return immediatesourceentity.getPersistentData().getInt("Element");
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
}