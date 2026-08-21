package net.wither.er.entity.outcrop;

import net.mcreator.er.ErMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.init.AdditionalRegistries;

import java.util.function.Supplier;

public class EntityModifierRegistry {

	public static final DeferredRegister<EntityModifier.Builder> MODIFIERS = DeferredRegister.create(AdditionalRegistries.ENTITY_MODIFIER_REGISTRY, ErMod.MODID);
	public static final Supplier<EntityModifier.Builder> HELMET = MODIFIERS.register("helmet", () -> (j -> ItemGiver.read(j, EquipmentSlot.HEAD)));
	public static final Supplier<EntityModifier.Builder> CHESTPLATE = MODIFIERS.register("chestplate", () -> (j -> ItemGiver.read(j, EquipmentSlot.CHEST)));
	public static final Supplier<EntityModifier.Builder> LEGGINGS = MODIFIERS.register("leggings", () -> (j -> ItemGiver.read(j, EquipmentSlot.LEGS)));
	public static final Supplier<EntityModifier.Builder> BOOTS = MODIFIERS.register("boots", () -> (j -> ItemGiver.read(j, EquipmentSlot.FEET)));
	public static final Supplier<EntityModifier.Builder> MAIN_HAND = MODIFIERS.register("main_hand", () -> (j -> ItemGiver.read(j, EquipmentSlot.MAINHAND)));
    public static final Supplier<EntityModifier.Builder> ATTRIBUTE = MODIFIERS.register("attribute", () -> AttributeGiver::read);

}
