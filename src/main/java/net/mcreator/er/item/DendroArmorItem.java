
package net.mcreator.er.item;

import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.Util;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModAttributes;

import java.util.List;
import java.util.EnumMap;

@EventBusSubscriber
public abstract class DendroArmorItem extends ArmorItem {
	public static Holder<ArmorMaterial> ARMOR_MATERIAL = null;

	@SubscribeEvent
	public static void registerArmorMaterial(RegisterEvent event) {
		event.register(Registries.ARMOR_MATERIAL, registerHelper -> {
			ArmorMaterial armorMaterial = new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 2);
				map.put(ArmorItem.Type.LEGGINGS, 5);
				map.put(ArmorItem.Type.CHESTPLATE, 6);
				map.put(ArmorItem.Type.HELMET, 2);
				map.put(ArmorItem.Type.BODY, 6);
			}), 9, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.EMPTY), () -> Ingredient.of(new ItemStack(ErModItems.CONDENSED_DENDRO.get())), List.of(new ArmorMaterial.Layer(ResourceLocation.parse("er:dendro_"))), 0.1f, 0f);
			registerHelper.register(ResourceLocation.parse("er:dendro_armor"), armorMaterial);
			ARMOR_MATERIAL = BuiltInRegistries.ARMOR_MATERIAL.wrapAsHolder(armorMaterial);
		});
	}

	public DendroArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(ARMOR_MATERIAL, type, properties);
	}

	public static class Helmet extends DendroArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(16)).fireResistant());
		}

		@Override
		public ItemAttributeModifiers getDefaultAttributeModifiers() {
			ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("er.dendro_res.head");
			return super.getDefaultAttributeModifiers().withModifierAdded(ErModAttributes.DENDRO_RES, new AttributeModifier(resourcelocation, 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.HEAD);
		}
	}

	public static class Chestplate extends DendroArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(16)).fireResistant());
		}

		@Override
		public ItemAttributeModifiers getDefaultAttributeModifiers() {
			ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("er.dendro_res.chest");
			return super.getDefaultAttributeModifiers().withModifierAdded(ErModAttributes.DENDRO_RES, new AttributeModifier(resourcelocation, 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.CHEST);
		}
	}

	public static class Leggings extends DendroArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(16)).fireResistant());
		}

		@Override
		public ItemAttributeModifiers getDefaultAttributeModifiers() {
			ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("er.dendro_res.legs");
			return super.getDefaultAttributeModifiers().withModifierAdded(ErModAttributes.DENDRO_RES, new AttributeModifier(resourcelocation, 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.LEGS);
		}
	}

	public static class Boots extends DendroArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(16)).fireResistant());
		}

		@Override
		public ItemAttributeModifiers getDefaultAttributeModifiers() {
			ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("er.dendro_res.feet");
			return super.getDefaultAttributeModifiers().withModifierAdded(ErModAttributes.DENDRO_RES, new AttributeModifier(resourcelocation, 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.FEET);
		}
	}
}
