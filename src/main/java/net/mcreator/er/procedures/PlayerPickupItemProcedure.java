package net.mcreator.er.procedures;

import net.minecraft.world.entity.player.Player;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.artifact_effect.ArtifactEffectRegistry;

import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.init.ErModItems;

@EventBusSubscriber
public class PlayerPickupItemProcedure {
	@SubscribeEvent
	public static void onPickup(ItemEntityPickupEvent.Post event) {
		ItemStack itemStack = event.getOriginalStack();
		Player entity = event.getPlayer();
        ItemStack item;
		ArtifactData data = itemStack.getComponents().get(DataComponentsRegister.ARTIFACT.get());
		if (itemStack.getItem() == ErModItems.MORA.get()) {
			if (entity instanceof ErEntityInterface entityInterface && entityInterface.er$getArtifactEffectLevel(ArtifactEffectRegistry.LUCKY_DOG) > 3) {
				entity.heal(6);
			}
			if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler) {
				for (int _idx = 0; _idx < _modHandler.getSlots(); _idx++) {
                    item = _modHandler.getStackInSlot(_idx);
					if (item.getItem() == ErModItems.MORA_BAG.get()) {
						{
							final String _tagName = "moras";
							final double _tagValue = (itemStack.getCount() + item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras"));
							CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putDouble(_tagName, _tagValue));
						}
						itemStack.setCount(0);
					}

				}
			}
		} else if (itemStack.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
			if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
				for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
					item = _modHandlerIter.getStackInSlot(_idx);
					if (item.getItem() == ErModItems.MORA_BAG.get()) {
						{
							final String _tagName = "moras";
							final double _tagValue = (itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras") + item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("moras"));
							CustomData.update(DataComponents.CUSTOM_DATA, item, tag -> tag.putDouble(_tagName, _tagValue));
						}
						itemStack.setCount(0);
					}
				}
			}
		}
		if (data != null && data.main().amount() == 0) {
			itemStack.update(DataComponentsRegister.ARTIFACT.get(), data, ArtifactData::rolling);
		}
	}
}