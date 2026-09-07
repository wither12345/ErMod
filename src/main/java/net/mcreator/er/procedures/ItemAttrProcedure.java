package net.mcreator.er.procedures;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.BowInterface;
import net.wither.er.item.data.weapon.WeaponAttributeData;
import net.wither.er.item.data.weapon.WeaponLevelData;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ItemAttrProcedure {
	private static final UUID weaponLevel = UUID.fromString("B0FFBAE0-9CC9-7C55-94F7-47C1CC02C402");
	private static final UUID weaponSecondary = UUID.fromString("EC9FB524-0C21-DFA3-64C1-9590293A0473");

	@SubscribeEvent
	public static void addAttributeModifier(ItemAttributeModifierEvent event) {
		ItemStack item = event.getItemStack();
		WeaponLevelData levelData = DataComponentsRegister.WEAPON_LEVEL.getData(item);
 		if(levelData != null){
			int level = levelData.level() ;
			int ascension = levelData.ascension() ;
			int star = WeaponLevelData.getItemWeaponStar(item) ;
			if (event.getSlotType() == EquipmentSlot.MAINHAND) {
				if (item.getItem() instanceof TieredItem tieredItem) {
					Tier tier = tieredItem.getTier();
					event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(weaponLevel, "main", level * (tier.getAttackDamageBonus() + 3) * 0.1 + getAscensionAmount(ascension, star), AttributeModifier.Operation.ADDITION));
					WeaponAttributeData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
					if (dataAttr != null && dataAttr.attribute() != null) {
						event.addModifier(dataAttr.attribute(), dataAttr.getModifier(weaponSecondary, level));
					}
				} else if (item.getItem() instanceof BowInterface bowInterface) {
					event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(weaponLevel, "main", bowInterface.getDamage() * 0.1 * level + getAscensionAmount(ascension, star), AttributeModifier.Operation.ADDITION));
					WeaponAttributeData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
					if (dataAttr != null && dataAttr.attribute() != null) {
						event.addModifier(dataAttr.attribute(), dataAttr.getModifier(weaponSecondary, level));
					}
				}
			}
			if (item.getItem() instanceof ArmorItem armor) {
				if(armor.getEquipmentSlot() != event.getSlotType())
					return;
				event.addModifier(Attributes.ARMOR, new AttributeModifier(getArmorUUID(armor.getEquipmentSlot(), 0), "main", level * armor.getDefense() * 0.1, AttributeModifier.Operation.ADDITION));
				if(ascension > 0)
					event.addModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(getArmorUUID(armor.getEquipmentSlot(), 1), "ascension", ascension * (armor.getToughness() + 1) * 0.1, AttributeModifier.Operation.ADDITION));

				WeaponAttributeData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
				if(dataAttr != null && dataAttr.attribute() != null){
					event.addModifier(dataAttr.attribute(),
                            dataAttr.getModifier(getArmorUUID(armor.getEquipmentSlot(), 2), level));
				}
			}
		}
	}

	private static UUID getArmorUUID(EquipmentSlot slot, int id){
		return UUID.fromString(switch (slot){
            case FEET -> "992E1CB7-96DD-FED0-5552-CC41C1DA51D";
			case HEAD -> "C8425B2D-BBAA-E982-F5B7-E30FD3B6116";
			case LEGS -> "48541D01-5DCB-6DC8-EBDB-ED6FF1AEEC4";
			case CHEST -> "F2772E3A-E4EB-D1F0-9ECA-4BD40F6F74E";
            default -> "57022802-93A3-FE0A-A2D1-750B7296980";
        } + id);
	}

	private static double getAscensionAmount(int ascension, int star){
		if(star == 0)
			return  ascension ;
		return star * 0.5 * ascension ;
	}
}