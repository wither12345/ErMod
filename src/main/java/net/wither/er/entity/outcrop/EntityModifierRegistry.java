package net.wither.er.entity.outcrop;

import net.mcreator.er.ErMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.init.AdditionalRegistries;

import java.util.function.Supplier;

public class EntityModifierRegistry {

	public static final DeferredRegister<EntityModifier> MODIFIERS = DeferredRegister.create(AdditionalRegistries.ENTITY_MODIFIER_REGISTRY, ErMod.MODID);
	public static final Supplier<EntityModifier> HELMET = MODIFIERS.register("helmet", () -> new ItemGiver(EquipmentSlot.HEAD));
	public static final Supplier<EntityModifier> CHESTPLATE = MODIFIERS.register("chestplate", () -> new ItemGiver(EquipmentSlot.CHEST));
	public static final Supplier<EntityModifier> LEGGINGS = MODIFIERS.register("leggings", () -> new ItemGiver(EquipmentSlot.LEGS));
	public static final Supplier<EntityModifier> BOOTS = MODIFIERS.register("boots", () -> new ItemGiver(EquipmentSlot.FEET));
	public static final Supplier<EntityModifier> MAIN_HAND = MODIFIERS.register("main_hand", () -> new ItemGiver(EquipmentSlot.MAINHAND));

}
