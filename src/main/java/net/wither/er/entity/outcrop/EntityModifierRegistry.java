package net.wither.er.entity.outcrop;

import net.minecraft.world.entity.EquipmentSlot;
import net.wither.er.init.AdditionalRegistries;

import java.util.function.Supplier;

public class EntityModifierRegistry {
	public static final Supplier<EntityModifier> HELMET = AdditionalRegistries.MODIFIERS.register("helmet", () -> new ItemGiver(EquipmentSlot.HEAD));
	public static final Supplier<EntityModifier> CHESTPLATE = AdditionalRegistries.MODIFIERS.register("chestplate", () -> new ItemGiver(EquipmentSlot.CHEST));
	public static final Supplier<EntityModifier> LEGGINGS = AdditionalRegistries.MODIFIERS.register("leggings", () -> new ItemGiver(EquipmentSlot.LEGS));
	public static final Supplier<EntityModifier> BOOTS = AdditionalRegistries.MODIFIERS.register("boots", () -> new ItemGiver(EquipmentSlot.FEET));
	public static final Supplier<EntityModifier> MAIN_HAND = AdditionalRegistries.MODIFIERS.register("main_hand", () -> new ItemGiver(EquipmentSlot.MAINHAND));

}
