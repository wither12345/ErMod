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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.client.renderer.damage.RenderDamageAmount;
import net.wither.er.combat.DamageModifierInterface;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.ElementSourceInterface;
import net.wither.er.entity.IErEntity;
import net.wither.er.entity.slimes.DendroSlime;
import net.wither.er.init.AdvancementTriggerRegister;
import net.wither.er.init.ElementRegistry;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.item.Vision;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.BeAttackedAbility;
import net.wither.er.item.data.weapon.DamageAbility;
import net.wither.er.item.data.weapon.InfusionAbility;
import net.wither.er.item.data.weapon.WeaponAbilityData;
import net.wither.er.network.DamageDisplayMessage;
import net.wither.er.network.ErItemVariables;
import net.wither.er.shield.ShieldStack;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.core.registries.Registries.DAMAGE_TYPE;

@Mod.EventBusSubscriber
public class EntityHurtEvent {
    public static final TagKey<DamageType> ER$NO_KB = TagKey.create(DAMAGE_TYPE, new ResourceLocation("er:no_knockback")) ;
    private static final TagKey<DamageType> NO_CRITICAL = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:no_critical")) ;
    private static final TagKey<DamageType> CATALYZE = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:reaction_multiply/catalyze")) ;
    private static final TagKey<DamageType> TRANSFORMATIVE = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:reaction_multiply/transformative")) ;
    private static final TagKey<DamageType> LUNAR = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:lunar")) ;
    private static final TagKey<DamageType> NORMAL_ATTACK = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("er:normal_attack")) ;

	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
        if (event == null)
            return;
        DamageSource damagesource = event.getSource();
        LivingEntity entity = event.getEntity();
        Entity sourceentity = event.getSource().getEntity();
        LevelAccessor world = entity.level();
        if(world.isClientSide()) return;
        float amount = event.getAmount();

        modifyDamageSource(damagesource, entity) ;
        modifyReaction(damagesource);
        if(damagesource instanceof DamageModifierInterface modifierInterface && damagesource instanceof ElementSourceInterface elementSourceInterface && entity instanceof AuraContainerInterface auraContainerInterface) {
            DamageModifier modifier = modifierInterface.er$getModifier();
            if(sourceentity instanceof IErEntity erEntity){
                Object2IntMap<ArtifactEffect> map = erEntity.er$getEffectMap();
                for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                    if(effect.getKey() instanceof DamageAbility damageAbility){
                        damageAbility.onHurt(damagesource, entity, modifier, effect.getIntValue());
                    }
                }
            }
            if(sourceentity instanceof LivingEntity living) {
                ItemStack itemStack = living.getMainHandItem();
                itemStack.getCapability(WeaponAbilityData.WEAPON_ABILITY).ifPresent(weaponAbility -> {
                    if(weaponAbility.ability().get() instanceof DamageAbility damageAbility) {
                        CompoundTag tag = itemStack.getOrCreateTag();
                        int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
                        damageAbility.onHurt(damagesource, entity, modifier, refinement);
                    }
                });
            }
            double elemental_mastery = 0 ;
            if (sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_MASTERY.get()) != null)
                elemental_mastery = living.getAttributeValue(ErModAttributes.ELEMENTAL_MASTERY.get());
            ElementSource source = elementSourceInterface.er$getSource();
            if(source != null && source.getElement() != null) {
                auraContainerInterface.er$getAuraContainer().addAura(source, modifier, sourceentity);
                ApplyElementMultiply(source.getElement(), entity, sourceentity, modifier);
                if(sourceentity instanceof LivingEntity living){
                    Vision.getOptDelusion(living).ifPresent(
                            data -> {
                                modifier.crit_multiply += data.critMulti;
                                modifier.common_multiply += data.dmgMulti;
                                float dmg = amount * data.healthConsume;
                                if(living instanceof ServerPlayer player)
                                    AdvancementTriggerRegister.DELUSION.trigger(player, dmg);
                                if(living.getHealth() >= dmg)
                                    living.setHealth(living.getHealth() - dmg);
                                else
                                    living.hurt(new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.WITHER)), living.getMaxHealth() * 20);

                            }
                    );
                }
            }
            else {
                if (entity.getAttribute(ErAttributeRegister.PHYSICAL_RES.get()) != null)
                    modifier.res_multiply *= (100f - (float) entity.getAttributeValue(ErAttributeRegister.PHYSICAL_RES.get())) / 100f;
                if (sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.PHYSICAL_DMG_BONUS.get()) != null)
                    modifier.common_multiply += (float) living.getAttributeValue(ErModAttributes.PHYSICAL_DMG_BONUS.get()) - 1;
            }
            if(damagesource.is(StellaFortunas.SKILL) && sourceentity instanceof LivingEntity living && living.getAttribute(ErModAttributes.ELEMENTAL_SKILL_DMG.get()) != null)
                modifierInterface.er$getModifier().common_multiply += (float) living.getAttributeValue(ErModAttributes.ELEMENTAL_SKILL_DMG.get()) - 1f;

            if (!damagesource.is(NO_CRITICAL) && sourceentity instanceof LivingEntity living && living.getAttributeValue(ErModAttributes.CRIT_RATE.get()) > Math.random()) {
                modifier.crit_multiply += (float) living.getAttributeValue(ErModAttributes.CRIT_DAMAGE.get());
                modifier.critical = true;
            }

            if(entity instanceof DendroSlime slime && slime.onGround() && slime.isHiding() && damagesource.getDirectEntity() != null)
                modifier.reaction_multiply = 0 ;


            float final_amount = modifier.calculate(amount, elemental_mastery);
            if (entity instanceof IErEntity anInterface) {
                List<ShieldStack> shields = anInterface.er$getShieldStacks();
                float shield_absorb = 0f;
                for (ShieldStack shield : shields) {
                    if(elementSourceInterface.er$getSource() != null)
                        shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, elementSourceInterface.er$getSource().getCategory().getId()));
                    else
                        shield_absorb = Math.max(shield_absorb, shield.getShield().onHurt(shield, entity, damagesource, final_amount, 0));
                }
                final_amount -= shield_absorb;
                event.setAmount(final_amount);

                Object2IntMap<ArtifactEffect> map = anInterface.er$getEffectMap();
                for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                    if(effect.getKey() instanceof BeAttackedAbility ability){
                        ability.beAttacked(entity, damagesource, modifier, final_amount, effect.getIntValue());
                    }
                }
                ItemStack itemStack = entity.getMainHandItem();
                float final_amount1 = final_amount;
                itemStack.getCapability(WeaponAbilityData.WEAPON_ABILITY).ifPresent(weaponAbility -> {
                    if(weaponAbility.ability().get() instanceof BeAttackedAbility ability) {
                        CompoundTag tag = itemStack.getOrCreateTag();
                        int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
                        ability.beAttacked(entity, damagesource, modifier, final_amount1, refinement);
                    }
                });
            }
        }
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void afterDamage(LivingDamageEvent event){
		int dmg = Mth.ceil(event.getAmount()) ;
        DamageSource source = event.getSource();
		if(source instanceof DamageModifierInterface modifierInterface && dmg > 0) {
            DamageModifier modifier = modifierInterface.er$getModifier();
            if(modifier.critical && source.getEntity() instanceof ServerPlayer player){
                AdvancementTriggerRegister.CRIT_DAMAGE.trigger(player, event.getAmount());
            }
			ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new DamageDisplayMessage(dmg, event.getEntity().getId(), getARGB(source), modifier.critical, modifier.type));
		}
        ((ElementSourceInterface)source).er$setElement(null);
	}


    private static void modifyDamageSource(DamageSource source, Entity entity){
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
                elementSourceInterface.er$setElement(new ElementSource(element, new ResourceLocation("er:default"), gauge, element.isApplicable()));
                return;
            }
        }

        if(source.getDirectEntity() instanceof ElementSourceInterface elementSourceInterface1){
            ElementSource source1 = elementSourceInterface1.er$getSource();
            if(source1 == null) return;
            elementSourceInterface.er$setElement(source1) ;
        }

        if(source.getEntity() != null &&
                elementSourceInterface.er$getSource() == null &&
                source.is(NORMAL_ATTACK) &&
                source.getEntity() == source.getDirectEntity())
            elementSourceInterface.er$setElement(getElementSource(source.getEntity().level(), source.getEntity(), source.getDirectEntity()));
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

	private static void ApplyElementMultiply(Element element, LivingEntity entity, Entity sourceentity , DamageModifier modifier){
		element.getDamageAttr();
		if(sourceentity instanceof LivingEntity living && element.getDamageAttr() != null && living.getAttribute(element.getDamageAttr()) != null)
			modifier.common_multiply += (float) living.getAttributeValue(element.getDamageAttr()) - 1;
		if(element.getImmuneTag() != null && entity.getType().is(element.getImmuneTag()))
			modifier.reaction_multiply = 0;
		else if(element.getResAttr() != null && entity.getAttribute(element.getResAttr()) != null)
			modifier.res_multiply *= (100f - (float) entity.getAttributeValue(element.getResAttr())) / 100f ;
	}

    @Nullable
    public static ElementSource getElementSource(LevelAccessor world, Entity entity, Entity immediatesourceentity){
        if (entity == null) return null;
        Element element = null;
        if (immediatesourceentity == entity) {
            if(entity instanceof LivingEntity living){
                ItemStack itemStack = living.getMainHandItem();
                WeaponAbilityData data = itemStack.getCapability(WeaponAbilityData.WEAPON_ABILITY).resolve().orElse(null);
                if (data != null && data.ability().get() instanceof InfusionAbility ability) {
                    CompoundTag tag = itemStack.getOrCreateTag();
                    int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1;
                    return ability.getInfusion(itemStack, entity, refinement);
                }
            }
            if (IsAnemoInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.ANEMO.get();
            } else if (IsCryoInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.CRYO.get();
            } else if (IsDendroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.DENDRO.get();
            } else if (IsElectroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.ELECTRO.get();
            } else if (IsGeoInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.GEO.get();
            } else if (IsHydroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.HYDRO.get();
            } else if (IsPyroInfusionProcedure.execute(world, entity)) {
                element = ElementRegistry.PYRO.get();
            }
        }
        if(element == null) return null;
        return new ElementSource(element, new ResourceLocation("er:default"), 1, element.isApplicable());
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
		return 1 ;
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
        public float crit_multiply = 1;
        public float basic = 1;
        public float res_multiply = 1;
        public float additional_amount = 0;
        public ReactionMultiply multiply = null;
        private RenderDamageAmount.DamageDisplayType type = RenderDamageAmount.DamageDisplayType.NORMAL;

        public float calculate(float dmg, double elementalMastery){
            return (dmg + additional_amount) * basic * (reaction_multiply + (multiply == null ? common_multiply : multiply.getMulti(elementalMastery)) - 1) * res_multiply * (critical ? crit_multiply : 1);
        }

        @Override
        public String toString() {
            return "DamageModifier{" +
                    "locked=" + locked +
                    ", critical=" + critical +
                    ", reaction_multiply=" + reaction_multiply +
                    ", common_multiply=" + common_multiply +
                    ", crit_multiply=" + crit_multiply +
                    ", basic=" + basic +
                    ", res_multiply=" + res_multiply +
                    ", additional_amount=" + additional_amount +
                    ", multiply=" + multiply +
                    ", type=" + type +
                    '}';
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