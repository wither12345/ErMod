package net.mcreator.er.procedures;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.wither.er.entity.IErEntity;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.morabag.MoraBagItem;

@EventBusSubscriber
public class PlayerPickupItemProcedure {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPickup(ItemEntityPickupEvent.Pre event) {
		ItemStack itemStack = event.getItemEntity().getItem();
        if(event.getItemEntity().hasPickUpDelay() && !event.canPickup().isTrue())
            return;
		Player entity = event.getPlayer();
		ArtifactData data = itemStack.getComponents().get(DataComponentsRegister.ARTIFACT.get());
		if (itemStack.getItem() == ErModItems.MORA.get()) {
			if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandler) {
				for (int _idx = 0; _idx < _modHandler.getSlots(); _idx++) {
                    ItemStack item = _modHandler.getStackInSlot(_idx);
					if (item.has(DataComponentsRegister.MORA_BAG)) {
                        if(((IErEntity)entity).er$getArtifactEffectLevel(ArtifactEffectRegistry.LUCKY_DOG) > 3)
                            entity.heal(6);
                        MoraBagItem.putMora(item, itemStack.getCount());
                        itemStack.setCount(0);
                        break;
					}

				}
			}
		} else if (itemStack.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
			if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
				for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
                    ItemStack item = _modHandlerIter.getStackInSlot(_idx);
                    if (item.has(DataComponentsRegister.MORA_BAG)) {
                        MoraBagItem.putMora(item, itemStack.getCount());
                        itemStack.setCount(0);
                        break;
					}
				}
			}
		}
		if (data != null && data.main().amount() == 0) {
			itemStack.update(DataComponentsRegister.ARTIFACT.get(), data, ArtifactData::rolling);
		}
	}

    @SubscribeEvent
    public static void afterPickUp(ItemEntityPickupEvent.Post event) {
        if(event.getOriginalStack().is(ErModItems.MORA) && ((IErEntity)event.getPlayer()).er$getArtifactEffectLevel(ArtifactEffectRegistry.LUCKY_DOG) > 3)
            event.getPlayer().heal(6);
    }
}