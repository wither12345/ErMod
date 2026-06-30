package net.mcreator.er.procedures;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.weapon.WeaponLevelData;

public class ApplyErlevelProcedure {
	private static final ResourceLocation locationHealth = ResourceLocation.parse("er:entity_level.health");
	private static final ResourceLocation locationDamage = ResourceLocation.parse("er:.entity_level.damage");
	private static final ResourceLocation locationArmor = ResourceLocation.parse("er:entity_level.armor");

	public static void execute(Entity entity, int level) {
		if (entity == null)
			return;
		entity.getPersistentData().putInt("erLevel", level);
		if (entity instanceof LivingEntity living) {
			float scalingHealth = 0.0064f * level * level + 0.1436f * level - 0.80418f;
			float scalingDamage = level * 0.2f ;
			living.getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(locationHealth, scalingHealth * living.getAttributeBaseValue(Attributes.MAX_HEALTH),  AttributeModifier.Operation.ADD_VALUE));
			living.getAttribute(Attributes.ARMOR).addOrReplacePermanentModifier(new AttributeModifier(locationArmor, level * 2 + 300, AttributeModifier.Operation.ADD_VALUE));
			if (living.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE))
				living.getAttribute(Attributes.ATTACK_DAMAGE).addOrReplacePermanentModifier(new AttributeModifier(locationDamage, scalingDamage * living.getAttributeBaseValue(Attributes.ATTACK_DAMAGE), AttributeModifier.Operation.ADD_VALUE));
			for(EquipmentSlot slot : EquipmentSlot.values()){
				ItemStack slotItem = living.getItemBySlot(slot) ;
				WeaponLevelData levelData = slotItem.getComponents().get(DataComponentsRegister.WEAPON_LEVEL.get()) ;
				if(levelData != null)
					slotItem.update(DataComponentsRegister.WEAPON_LEVEL.get(),levelData,d-> WeaponLevelData.create(Math.min(90,level))) ;
			}
			living.setHealth(living.getMaxHealth());
		}
	}
}