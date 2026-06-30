package net.mcreator.er.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ApplyErlevelProcedure {
	private static final UUID locationHealth = UUID.fromString("026C0FCE-43CF-4CE1-50C9-D0011F9AA0DE");
	private static final UUID locationDamage = UUID.fromString("B5B29AE8-4262-1780-D24B-EE410C01385A");
	private static final UUID locationArmor = UUID.fromString("F6DF7613-3F4E-A6DA-5989-B73B627784AC");

	public static void execute(Entity entity, int level) {
		if (entity == null)
			return;
		entity.getPersistentData().putInt("erLevel", level);
		if (entity instanceof LivingEntity living) {
			float scalingHealth = 0.0064f * level * level + 0.1436f * level - 0.80418f;
			float scalingDamage = level * 0.2f ;
            living.getAttribute(Attributes.MAX_HEALTH).removeModifier(locationHealth);
            living.getAttribute(Attributes.ARMOR).removeModifier(locationArmor);
			living.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(locationHealth, "level_health", scalingHealth * living.getAttributeBaseValue(Attributes.MAX_HEALTH),  AttributeModifier.Operation.ADDITION));
			living.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(locationArmor, "level_armor", level * 2 + 300, AttributeModifier.Operation.ADDITION));
			if (living.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
                living.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(locationDamage);
                living.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(locationDamage, "level_damage", scalingDamage * living.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), AttributeModifier.Operation.ADDITION));
            }
			for(EquipmentSlot slot : EquipmentSlot.values()){
				ItemStack slotItem = living.getItemBySlot(slot) ;
				slotItem.getOrCreateTag().putInt("erLevel", Math.min(90,level));
			}
			living.setHealth(living.getMaxHealth());
		}
	}
}