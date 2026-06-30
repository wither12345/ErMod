package net.mcreator.er.procedures;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.item.Artifact;

public class ArtifactAttrAddProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		ItemStack item = ItemStack.EMPTY;
		double count = 0;
		String type = "";
		String uuid = "";
		if (entity instanceof Player && itemstack.getItem() instanceof Artifact) {
			type = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("main_attribute_name");
			Attribute attr = BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse(type));
			uuid = Artifact.getUuid(itemstack, 4);
			count = Artifact.final_attribute(itemstack, 0);
			ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace(uuid);
			if (BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse(type)) != null) {
				((LivingEntity) entity).getAttribute(BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(type)).get())
						.addOrReplacePermanentModifier(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("main_attribute_multiplied")
								? new AttributeModifier(resourcelocation, count, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
								: new AttributeModifier(resourcelocation, count, AttributeModifier.Operation.ADD_VALUE));
			}
			for (int index0 = 0; index0 < 4; index0++) {
				type = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(("minor_attribute_name_" + new java.text.DecimalFormat("#").format(index0)));
				uuid = Artifact.getUuid(itemstack, index0);
				resourcelocation = ResourceLocation.withDefaultNamespace(uuid);
				count = Artifact.final_attribute(itemstack, index0 + 1);
				if (BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.parse(type)) != null) {
					((LivingEntity) entity).getAttribute(BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(type)).get())
							.addOrReplacePermanentModifier(itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("minor_attribute_multiplied_" + new java.text.DecimalFormat("#").format(index0))
									? new AttributeModifier(resourcelocation, count, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
									: new AttributeModifier(resourcelocation, count, AttributeModifier.Operation.ADD_VALUE));
				}
			}
		}
	}
}