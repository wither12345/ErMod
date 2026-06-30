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
						event.addModifier(dataAttr.attribute(), new AttributeModifier(
								weaponSecondary, "secondary", dataAttr.baseAmount() * getSecondaryMultiply(level), dataAttr.type() ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION
						));
					}
				} else if (item.getItem() instanceof BowInterface bowInterface) {
					event.addModifier(Attributes.ATTACK_DAMAGE, new AttributeModifier(weaponLevel, "main", bowInterface.getDamage() * 0.1 * level + getAscensionAmount(ascension, star), AttributeModifier.Operation.ADDITION));
					WeaponAttributeData dataAttr = DataComponentsRegister.WEAPON_ATTR.getData(item);
					if (dataAttr != null && dataAttr.attribute() != null) {
						event.addModifier(dataAttr.attribute(), new AttributeModifier(
								weaponSecondary, "secondary", dataAttr.baseAmount() * getSecondaryMultiply(level), dataAttr.type() ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION
						));
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
				if(dataAttr != null &&  dataAttr.attribute() != null){
					event.addModifier(dataAttr.attribute(), new AttributeModifier(
							getArmorUUID(armor.getEquipmentSlot(), 2), "secondary", dataAttr.baseAmount() * getSecondaryMultiply(level), dataAttr.type() ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION)
					);
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

	private static double getSecondaryMultiply(int level){
		return 1 + (level / 5) * 0.2d ;
	}
	/*
	ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("enchantment.infusion");
	if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:anemo_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.ANEMO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:anemo_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	} else if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:cryo_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.CRYO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:cryo_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	} else if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:dendro_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.DENDRO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:dendro_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	} else if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:electro_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.ELECTRO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:electro_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	} else if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:geo_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.GEO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:geo_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	} else if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:hydro_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.HYDRO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:hydro_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	} else if (itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:pyro_infusion_enchantment")))) != 0) {
		event.addModifier(ErModAttributes.PYRO_DMG_BONUS.get(),
				new AttributeModifier(resourcelocation,
						((itemstack.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, new ResourceLocation("er:pyro_infusion_enchantment")))) - 1) * 0.2),
						AttributeModifier.Operation.MULTIPLY_BASE),
				EquipmentSlotGroup.bySlot(EquipmentSlot.MAINHAND));
	}
	if (EnchantmentHelper.getItemEnchantmentLevel(ErModEnchantments.ELEMENTAL_MASTER.get(), itemstack) != 0) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.MAINHAND) {
			_event.addModifier(ErModAttributes.ELEMENTAL_MASTERY.get(),
					(new AttributeModifier(UUID.fromString("d0ca7956-9b7b-4b7d-9477-db08d83d60ae"), "Elemental_Master", (itemstack.getEnchantmentLevel(ErModEnchantments.ELEMENTAL_MASTER.get()) * 25), AttributeModifier.Operation.ADDITION)));
		}
	}
	if (itemstack.getItem() instanceof SwordItem || itemstack.getItem() instanceof BowItem) {
		if (itemstack.is(ItemTags.create(new ResourceLocation("er:five_star_weapon")))) {
			if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.MAINHAND) {
				_event.addModifier(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
						(new AttributeModifier(UUID.fromString("61730CD6-BD2B-7D93-E517-275CDDB5F14F"), "Weapon_Level", (itemstack.getOrCreateTag().getDouble("level") * 0.6), AttributeModifier.Operation.ADDITION)));
			}
		} else {
			if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.MAINHAND) {
				_event.addModifier(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
						(new AttributeModifier(UUID.fromString("61730CD6-BD2B-7D93-E517-275CDDB5F14F"), "Weapon_Level", (itemstack.getOrCreateTag().getDouble("level") * 0.3), AttributeModifier.Operation.ADDITION)));
			}
		}
	}
	if (EnchantmentHelper.getItemEnchantmentLevel(ErModEnchantments.HARD.get(), itemstack) != 0) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.CHEST) {
			_event.addModifier(ErModAttributes.SHIELD_STRENGTH.get(),
					(new AttributeModifier(UUID.fromString("edd5e66a-30d0-47a8-a66d-b0859b363d4a"), "Hard", (itemstack.getEnchantmentLevel(ErModEnchantments.HARD.get()) * 30), AttributeModifier.Operation.ADDITION)));
		}
	}
	if (itemstack.getItem() == ErModItems.PYRO_ARMOR_HELMET.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.HEAD) {
			_event.addModifier(ErModAttributes.PYRO_RES.get(), (new AttributeModifier(UUID.fromString("05f5915f-9867-6ad2-a2ab-c1978f0e8051"), "hyro_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.PYRO_ARMOR_CHESTPLATE.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.CHEST) {
			_event.addModifier(ErModAttributes.PYRO_RES.get(), (new AttributeModifier(UUID.fromString("af424956-a55e-608a-2539-3450a9759ccc"), "hyro_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.PYRO_ARMOR_LEGGINGS.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.LEGS) {
			_event.addModifier(ErModAttributes.PYRO_RES.get(), (new AttributeModifier(UUID.fromString("f4905640-69d9-d047-b63c-fb596a5175d4"), "hyro_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.PYRO_ARMOR_BOOTS.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.FEET) {
			_event.addModifier(ErModAttributes.PYRO_RES.get(), (new AttributeModifier(UUID.fromString("8055f14c-79cb-10fa-8f96-b86b6bd1ba44"), "hyro_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.CRYO_ARMOR_HELMET.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.HEAD) {
			_event.addModifier(ErModAttributes.CRYO_RES.get(), (new AttributeModifier(UUID.fromString("05f5915f-1867-6cd2-a2ab-c1978f0e8051"), "cryo_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.CRYO_ARMOR_CHESTPLATE.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.CHEST) {
			_event.addModifier(ErModAttributes.CRYO_RES.get(), (new AttributeModifier(UUID.fromString("af420f56-a55e-608a-2539-3450a9759c0c"), "cryo_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.CRYO_ARMOR_LEGGINGS.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.LEGS) {
			_event.addModifier(ErModAttributes.CRYO_RES.get(), (new AttributeModifier(UUID.fromString("f4905640-69d9-d197-b63c-fb596a5175d4"), "cryo_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	} else if (itemstack.getItem() == ErModItems.CRYO_ARMOR_BOOTS.get()) {
		if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.FEET) {
			_event.addModifier(ErModAttributes.CRYO_RES.get(), (new AttributeModifier(UUID.fromString("8055f14c-79cb-10fa-8f96-be1b6bd1ba44"), "cryo_armor", 10, AttributeModifier.Operation.ADDITION)));
		}
	}
	if (itemstack.hasTag()) {
		if (itemstack.getTag().contains("Trim")) {
			if ((itemstack.getOrCreateTag().getCompound("Trim").getString("material")).equals("minecraft:gold")) {
				if (itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlot.HEAD) {
					if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.HEAD) {
						_event.addModifier(ErModAttributes.GEO_RES.get(), (new AttributeModifier(UUID.fromString("F0B193ED-5DEA-02C9-9F6C-4C97F919A539"), "geo_res", 1, AttributeModifier.Operation.ADDITION)));
					}
				} else if (itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlot.CHEST) {
					if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.CHEST) {
						_event.addModifier(ErModAttributes.GEO_RES.get(), (new AttributeModifier(UUID.fromString("8837BBFA-F183-CB18-72D6-BD29B7DCADEB"), "geo_res", 1, AttributeModifier.Operation.ADDITION)));
					}
				} else if (itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlot.LEGS) {
					if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.LEGS) {
						_event.addModifier(ErModAttributes.GEO_RES.get(), (new AttributeModifier(UUID.fromString("8837BBFA-F332-CB18-72D6-BD29B7DCADEB"), "geo_res", 1, AttributeModifier.Operation.ADDITION)));
					}
				} else if (itemstack.getItem() instanceof ArmorItem && ((ArmorItem) itemstack.getItem()).getEquipmentSlot() == EquipmentSlot.FEET) {
					if (event instanceof ItemAttributeModifierEvent _event && _event.getSlotType() == EquipmentSlot.FEET) {
						_event.addModifier(ErModAttributes.GEO_RES.get(), (new AttributeModifier(UUID.fromString("8837BBFA-F199-CB18-72D6-BD29B7DCADEB"), "geo_res", 1, AttributeModifier.Operation.ADDITION)));
					}
				}
			}
		}
	}
	*/
}