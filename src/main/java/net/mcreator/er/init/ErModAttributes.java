/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EntityType;

import net.mcreator.er.ErMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ErModAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ErMod.MODID);
	public static final RegistryObject<Attribute> ELEMENTAL_MASTERY = REGISTRY.register("elemental_mastery", () -> new RangedAttribute("attribute.er.elemental_mastery", 0, 0, 32768).setSyncable(true));
	public static final RegistryObject<Attribute> ENERGY_RECHARGE = REGISTRY.register("energy_recharge", () -> new RangedAttribute("attribute.er.energy_recharge", 100, 0, 1024).setSyncable(true));
	public static final RegistryObject<Attribute> SHIELD_STRENGTH = REGISTRY.register("shield_strength", () -> new RangedAttribute("attribute.er.shield_strength", 0, -200, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> ANEMO_DMG_BONUS = REGISTRY.register("anemo_dmg_bonus", () -> new RangedAttribute("attribute.er.anemo_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> CRYO_DMG_BONUS = REGISTRY.register("cryo_dmg_bonus", () -> new RangedAttribute("attribute.er.cryo_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> DENDRO_DMG_BONUS = REGISTRY.register("dendro_dmg_bonus", () -> new RangedAttribute("attribute.er.dendro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> ELECTRO_DMG_BONUS = REGISTRY.register("electro_dmg_bonus", () -> new RangedAttribute("attribute.er.electro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> GEO_DMG_BONUS = REGISTRY.register("geo_dmg_bonus", () -> new RangedAttribute("attribute.er.geo_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> HYDRO_DMG_BONUS = REGISTRY.register("hydro_dmg_bonus", () -> new RangedAttribute("attribute.er.hydro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> PYRO_DMG_BONUS = REGISTRY.register("pyro_dmg_bonus", () -> new RangedAttribute("attribute.er.pyro_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> PHYSICAL_DMG_BONUS = REGISTRY.register("physical_dmg_bonus", () -> new RangedAttribute("attribute.er.physical_dmg_bonus", 1, 0, 65536).setSyncable(true));
	public static final RegistryObject<Attribute> ANEMO_RES = REGISTRY.register("anemo_res", () -> new RangedAttribute("attribute.er.anemo_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> CRYO_RES = REGISTRY.register("cryo_res", () -> new RangedAttribute("attribute.er.cryo_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> DENDRO_RES = REGISTRY.register("dendro_res", () -> new RangedAttribute("attribute.er.dendro_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> ELECTRO_RES = REGISTRY.register("electro_res", () -> new RangedAttribute("attribute.er.electro_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> GEO_RES = REGISTRY.register("geo_res", () -> new RangedAttribute("attribute.er.geo_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> HYDRO_RES = REGISTRY.register("hydro_res", () -> new RangedAttribute("attribute.er.hydro_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> PYRO_RES = REGISTRY.register("pyro_res", () -> new RangedAttribute("attribute.er.pyro_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> PHYSICAL_RES = REGISTRY.register("physical_res", () -> new RangedAttribute("attribute.er.physical_res", 0, -512, 100).setSyncable(true));
	public static final RegistryObject<Attribute> CRIT_DAMAGE = REGISTRY.register("crit_damage", () -> new RangedAttribute("attribute.er.crit_damage", 0.5, 0, 1024).setSyncable(true));
	public static final RegistryObject<Attribute> MAX_STAMINA = REGISTRY.register("max_stamina", () -> new RangedAttribute("attribute.er.max_stamina", 100, 20, 1024).setSyncable(true));
	public static final RegistryObject<Attribute> CRIT_RATE = REGISTRY.register("crit_rate", () -> new RangedAttribute("attribute.er.crit_rate", 0.05, 0, 1).setSyncable(true));
	public static final RegistryObject<Attribute> INCOMING_HEALING_BONUS = REGISTRY.register("incoming_healing_bonus", () -> new RangedAttribute("attribute.er.incoming_healing_bonus", 1, 0, 100).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.getTypes().forEach(entity -> event.add(entity, ELEMENTAL_MASTERY.get()));
		event.add(EntityType.PLAYER, ENERGY_RECHARGE.get());
		event.getTypes().forEach(entity -> event.add(entity, SHIELD_STRENGTH.get()));
		event.getTypes().forEach(entity -> event.add(entity, ANEMO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, CRYO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, DENDRO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, ELECTRO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, GEO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, HYDRO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, PYRO_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, PHYSICAL_DMG_BONUS.get()));
		event.getTypes().forEach(entity -> event.add(entity, ANEMO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, CRYO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, DENDRO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, ELECTRO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, GEO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, HYDRO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, PYRO_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, PHYSICAL_RES.get()));
		event.getTypes().forEach(entity -> event.add(entity, CRIT_DAMAGE.get()));
		event.add(EntityType.PLAYER, MAX_STAMINA.get());
		event.getTypes().forEach(entity -> event.add(entity, CRIT_RATE.get()));
		event.getTypes().forEach(entity -> event.add(entity, INCOMING_HEALING_BONUS.get()));
	}

	@Mod.EventBusSubscriber
	public static class PlayerAttributesSync {
		@SubscribeEvent
		public static void playerClone(PlayerEvent.Clone event) {
			Player oldPlayer = event.getOriginal();
			Player newPlayer = event.getEntity();
			newPlayer.getAttribute(ELEMENTAL_MASTERY.get()).setBaseValue(oldPlayer.getAttribute(ELEMENTAL_MASTERY.get()).getBaseValue());
			newPlayer.getAttribute(ENERGY_RECHARGE.get()).setBaseValue(oldPlayer.getAttribute(ENERGY_RECHARGE.get()).getBaseValue());
			newPlayer.getAttribute(SHIELD_STRENGTH.get()).setBaseValue(oldPlayer.getAttribute(SHIELD_STRENGTH.get()).getBaseValue());
			newPlayer.getAttribute(ANEMO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(ANEMO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(CRYO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(CRYO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(DENDRO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(DENDRO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(ELECTRO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(ELECTRO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(GEO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(GEO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(HYDRO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(HYDRO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(PYRO_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(PYRO_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(PHYSICAL_DMG_BONUS.get()).setBaseValue(oldPlayer.getAttribute(PHYSICAL_DMG_BONUS.get()).getBaseValue());
			newPlayer.getAttribute(ANEMO_RES.get()).setBaseValue(oldPlayer.getAttribute(ANEMO_RES.get()).getBaseValue());
			newPlayer.getAttribute(CRYO_RES.get()).setBaseValue(oldPlayer.getAttribute(CRYO_RES.get()).getBaseValue());
			newPlayer.getAttribute(DENDRO_RES.get()).setBaseValue(oldPlayer.getAttribute(DENDRO_RES.get()).getBaseValue());
			newPlayer.getAttribute(ELECTRO_RES.get()).setBaseValue(oldPlayer.getAttribute(ELECTRO_RES.get()).getBaseValue());
			newPlayer.getAttribute(GEO_RES.get()).setBaseValue(oldPlayer.getAttribute(GEO_RES.get()).getBaseValue());
			newPlayer.getAttribute(HYDRO_RES.get()).setBaseValue(oldPlayer.getAttribute(HYDRO_RES.get()).getBaseValue());
			newPlayer.getAttribute(PYRO_RES.get()).setBaseValue(oldPlayer.getAttribute(PYRO_RES.get()).getBaseValue());
			newPlayer.getAttribute(PHYSICAL_RES.get()).setBaseValue(oldPlayer.getAttribute(PHYSICAL_RES.get()).getBaseValue());
			newPlayer.getAttribute(CRIT_DAMAGE.get()).setBaseValue(oldPlayer.getAttribute(CRIT_DAMAGE.get()).getBaseValue());
			newPlayer.getAttribute(MAX_STAMINA.get()).setBaseValue(oldPlayer.getAttribute(MAX_STAMINA.get()).getBaseValue());
			newPlayer.getAttribute(CRIT_RATE.get()).setBaseValue(oldPlayer.getAttribute(CRIT_RATE.get()).getBaseValue());
			newPlayer.getAttribute(INCOMING_HEALING_BONUS.get()).setBaseValue(oldPlayer.getAttribute(INCOMING_HEALING_BONUS.get()).getBaseValue());
		}
	}
}