package net.wither.er.item.data.weapon;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.wither.er.elements.*;
import net.wither.er.init.EffectRegister;
import net.wither.er.init.ElementRegistry;

public class FunctionalAbilities {
    private static final ResourceKey<DamageType> PHYSICAL = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ErMod.MODID,"physical"));

    public static ElementSource anemoInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.ANEMO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, false);
    }

    public static ElementSource pyroInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.PYRO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
    }

    public static ElementSource hydroInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.HYDRO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
    }

    public static ElementSource cryoInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.CRYO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
    }

    public static ElementSource electroInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.ELECTRO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
    }

    public static ElementSource dendroInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.DENDRO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
    }

    public static ElementSource geoInfusion(ItemStack itemstack, Entity entity, int level){
        return new ElementSource(ElementRegistry.GEO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, false);
    }

    public static ElementSource woodenClub(ItemStack itemstack, Entity entity, int level){
        int restPyro = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("Pyro");
        if(restPyro > 0) {
            CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putInt("Pyro", restPyro - 1));
            return new ElementSource(ElementRegistry.PYRO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
        }
        return null;
    }

    public static void coolSteel(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier, int level){
        if(entity instanceof AuraContainerInterface auraContainerInterface &&
                (auraContainerInterface.er$getAuraContainer().hasElementCategory(Element.Category.CRYO) ||
                auraContainerInterface.er$getAuraContainer().hasElementCategory(Element.Category.HYDRO))){
            modifier.common_multiply += 0.09f + 0.03f * level;
        }
    }

    public static void darkIronSword(AuraContainer container, ElementSource elementToAdd, Element.Category elementReacted, EntityHurtEvent.DamageModifier damageModifier, Entity applier, int level){
        if(applier instanceof LivingEntity livingEntity && (elementReacted == Element.Category.ELECTRO || elementToAdd.getCategory() == Element.Category.ELECTRO)){
            livingEntity.addEffect(new MobEffectInstance(EffectRegister.OVERLOADED, 240, level - 1));
        }
    }

    public static void travelersHandySword(Entity orb, LivingEntity picker, int level){
        picker.heal((0.0075f + 0.0025f * level) * picker.getMaxHealth());
    }

    public static void skyriderSword(LivingEntity entity, int level){
        entity.addEffect(new MobEffectInstance(EffectRegister.DETERMINATION, 300, level - 1));
    }

    public static void filletBlade(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier, int level){
        if(source.getEntity() instanceof LivingEntity living && !living.hasEffect(EffectRegister.GASH) && living.getRandom().nextBoolean()){
            living.addEffect(new MobEffectInstance(EffectRegister.GASH, 320 - 20 * level, 0));
            AttributeInstance instance = living.getAttribute(Attributes.ATTACK_DAMAGE);
            if(instance != null)
                entity.hurt(living.damageSources().source(PHYSICAL, living), (float) instance.getValue() * (2 + 0.4f * level));
        }
    }

    public static class WoodenClub implements InfusionAbility, BeAttackedAbility{
        @Override
        public void beAttacked(LivingEntity self, DamageSource source, EntityHurtEvent.DamageModifier modifier, float dmgAmount, int level) {
            if(source instanceof ElementSourceInterface sourceInterface && sourceInterface.er$getSource() != null && sourceInterface.er$getSource().getCategory() == Element.Category.PYRO){
                if(self.getMainHandItem().is(ErModItems.WOODEN_CLUB.get()))CustomData.update(DataComponents.CUSTOM_DATA, self.getMainHandItem(), tag -> tag.putInt("Pyro", 7));
            }
        }

        @Override
        public ElementSource getInfusion(ItemStack itemstack, Entity entity, int level) {
            int restPyro = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("Pyro");
            if(restPyro > 0) {
                CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag -> tag.putInt("Pyro", restPyro - 1));
                return new ElementSource(ElementRegistry.PYRO.get(), ResourceLocation.parse("er:infusion"), 1 + level * 0.2f, true);
            }
            return null;
        }
    }
}
