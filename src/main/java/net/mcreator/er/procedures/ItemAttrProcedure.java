package net.mcreator.er.procedures;

import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.init.ErModEnchantments;
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
import net.wither.er.item.IProjectileWeapon;
import net.wither.er.item.data.weapon.WeaponAbilityData;
import net.wither.er.item.data.weapon.WeaponLevelData;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ItemAttrProcedure {
	private static final UUID WEAPON_LEVEL = UUID.fromString("B0FFBAE0-9CC9-7C55-94F7-47C1CC02C402");
	private static final UUID WEAPON_SECONDARY = UUID.fromString("EC9FB524-0C21-DFA3-64C1-9590293A0473");
    private static final UUID BASE_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ENCHANT_UUID = UUID.fromString("B0FFBAE0-9DD9-7C55-93F1-47C1CC02C402");

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
					event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(WEAPON_LEVEL, "main", level * (tier.getAttackDamageBonus() + 3) * 0.1 + getAscensionAmount(ascension, star), AttributeModifier.Operation.ADDITION));
					WeaponAbilityData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
					if (dataAttr != null && dataAttr.attribute() != null) {
						event.addModifier(dataAttr.attribute(), dataAttr.getModifier(WEAPON_SECONDARY, level));
					}
				} else if (item.getItem() instanceof IProjectileWeapon projectileWeapon) {
					event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_DAMAGE_UUID, "main", projectileWeapon.er$getDamage() * (0.1 * level + 1) + getAscensionAmount(ascension, star), AttributeModifier.Operation.ADDITION));
					WeaponAbilityData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
					if (dataAttr != null && dataAttr.attribute() != null) {
						event.addModifier(dataAttr.attribute(), dataAttr.getModifier(WEAPON_SECONDARY, level));
					}
				}
                applyInfusionEnchantment(item, event);

			}
			if (item.getItem() instanceof ArmorItem armor) {
				if(armor.getEquipmentSlot() != event.getSlotType())
					return;
				event.addModifier(Attributes.ARMOR, new AttributeModifier(getArmorUUID(armor.getEquipmentSlot(), 0), "main", level * armor.getDefense() * 0.1, AttributeModifier.Operation.ADDITION));
				if(ascension > 0)
					event.addModifier(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(getArmorUUID(armor.getEquipmentSlot(), 1), "ascension", ascension * (armor.getToughness() + 1) * 0.1, AttributeModifier.Operation.ADDITION));

				WeaponAbilityData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
				if(dataAttr != null && dataAttr.attribute() != null){
					event.addModifier(dataAttr.attribute(),
                            dataAttr.getModifier(getArmorUUID(armor.getEquipmentSlot(), 2), level));
				}

                int hardLevel = item.getEnchantmentLevel(ErModEnchantments.HARD.get());
                if(hardLevel > 0)
                    event.addModifier(ErModAttributes.SHIELD_STRENGTH.get(), new AttributeModifier(getArmorUUID(armor.getEquipmentSlot(), 3), "hard", hardLevel * 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
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


    private static void applyInfusionEnchantment(ItemStack itemStack, ItemAttributeModifierEvent event){
        int enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.PYRO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.PYRO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.ELECTRO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.ELECTRO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.ANEMO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.ANEMO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.CRYO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.CRYO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.DENDRO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.DENDRO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.HYDRO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.HYDRO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.GEO_INFUSION_ENCHANTMENT.get()) - 1;
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.GEO_DMG_BONUS.get(), new AttributeModifier(ENCHANT_UUID, "infusion", enchantLevel * 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        enchantLevel = itemStack.getEnchantmentLevel(ErModEnchantments.ELEMENTAL_MASTER.get());
        if(enchantLevel > 0)
            event.addModifier(ErModAttributes.ELEMENTAL_MASTERY.get(), new AttributeModifier(ENCHANT_UUID, "elemental_master", enchantLevel * 25, AttributeModifier.Operation.ADDITION));
    }
}