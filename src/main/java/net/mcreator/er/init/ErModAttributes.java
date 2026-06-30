/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.er.ErMod;

@EventBusSubscriber
public class ErModAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, ErMod.MODID);
	public static final DeferredHolder<Attribute, Attribute> ELEMENTAL_MASTERY = REGISTRY.register("elemental_mastery", () -> new RangedAttribute("attribute.er.elemental_mastery", 0, 0, 32768).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ENERGY_RECHARGE = REGISTRY.register("energy_recharge", () -> new RangedAttribute("attribute.er.energy_recharge", 100, 0, 1024).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> SHIELD_STRENGTH = REGISTRY.register("shield_strength", () -> new RangedAttribute("attribute.er.shield_strength", 0, -200, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ANEMO_DMG_BONUS = REGISTRY.register("anemo_dmg_bonus", () -> new RangedAttribute("attribute.er.anemo_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> CRYO_DMG_BONUS = REGISTRY.register("cryo_dmg_bonus", () -> new RangedAttribute("attribute.er.cryo_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> DENDRO_DMG_BONUS = REGISTRY.register("dendro_dmg_bonus", () -> new RangedAttribute("attribute.er.dendro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ELECTRO_DMG_BONUS = REGISTRY.register("electro_dmg_bonus", () -> new RangedAttribute("attribute.er.electro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> GEO_DMG_BONUS = REGISTRY.register("geo_dmg_bonus", () -> new RangedAttribute("attribute.er.geo_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> HYDRO_DMG_BONUS = REGISTRY.register("hydro_dmg_bonus", () -> new RangedAttribute("attribute.er.hydro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> PYRO_DMG_BONUS = REGISTRY.register("pyro_dmg_bonus", () -> new RangedAttribute("attribute.er.pyro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> PHYSICAL_DMG_BONUS = REGISTRY.register("physical_dmg_bonus", () -> new RangedAttribute("attribute.er.physical_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ANEMO_RES = REGISTRY.register("anemo_res", () -> new RangedAttribute("attribute.er.anemo_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> CRYO_RES = REGISTRY.register("cryo_res", () -> new RangedAttribute("attribute.er.cryo_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> DENDRO_RES = REGISTRY.register("dendro_res", () -> new RangedAttribute("attribute.er.dendro_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ELECTRO_RES = REGISTRY.register("electro_res", () -> new RangedAttribute("attribute.er.electro_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> GEO_RES = REGISTRY.register("geo_res", () -> new RangedAttribute("attribute.er.geo_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> HYDRO_RES = REGISTRY.register("hydro_res", () -> new RangedAttribute("attribute.er.hydro_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> PYRO_RES = REGISTRY.register("pyro_res", () -> new RangedAttribute("attribute.er.pyro_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> PHYSICAL_RES = REGISTRY.register("physical_res", () -> new RangedAttribute("attribute.er.physical_res", 0, -512, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> CRIT_DAMAGE = REGISTRY.register("crit_damage", () -> new RangedAttribute("attribute.er.crit_damage", 0.5, 0, 1024).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> MAX_STAMINA = REGISTRY.register("max_stamina", () -> new RangedAttribute("attribute.er.max_stamina", 100, 20, 1024).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> CRIT_RATE = REGISTRY.register("crit_rate", () -> new RangedAttribute("attribute.er.crit_rate", 0.05, 0, 1).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> INCOMING_HEALING_BONUS = REGISTRY.register("incoming_healing_bonus", () -> new RangedAttribute("attribute.er.incoming_healing_bonus", 1, 0, 100).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.getTypes().forEach(entity -> event.add(entity, ELEMENTAL_MASTERY));
		event.add(EntityType.PLAYER, ENERGY_RECHARGE);
		event.getTypes().forEach(entity -> event.add(entity, SHIELD_STRENGTH));
		event.getTypes().forEach(entity -> event.add(entity, ANEMO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, CRYO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, DENDRO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, ELECTRO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, GEO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, HYDRO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, PYRO_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, PHYSICAL_DMG_BONUS));
		event.getTypes().forEach(entity -> event.add(entity, ANEMO_RES));
		event.getTypes().forEach(entity -> event.add(entity, CRYO_RES));
		event.getTypes().forEach(entity -> event.add(entity, DENDRO_RES));
		event.getTypes().forEach(entity -> event.add(entity, ELECTRO_RES));
		event.getTypes().forEach(entity -> event.add(entity, GEO_RES));
		event.getTypes().forEach(entity -> event.add(entity, HYDRO_RES));
		event.getTypes().forEach(entity -> event.add(entity, PYRO_RES));
		event.getTypes().forEach(entity -> event.add(entity, PHYSICAL_RES));
		event.getTypes().forEach(entity -> event.add(entity, CRIT_DAMAGE));
		event.add(EntityType.PLAYER, MAX_STAMINA);
		event.getTypes().forEach(entity -> event.add(entity, CRIT_RATE));
		event.getTypes().forEach(entity -> event.add(entity, INCOMING_HEALING_BONUS));
	}
}