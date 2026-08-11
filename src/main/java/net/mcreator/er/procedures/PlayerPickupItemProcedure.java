package net.mcreator.er.procedures;

import net.mcreator.er.init.ErModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;

import static net.wither.er.init.DataComponentsRegister.ARTIFACT;

@Mod.EventBusSubscriber
public class PlayerPickupItemProcedure {
	@SubscribeEvent
	public static void onPickup(EntityItemPickupEvent event) {
		ItemStack itemStack = event.getItem().getItem();
		Player entity = event.getEntity();
        ItemStack item;
		ArtifactData data = DataComponentsRegister.ARTIFACT.getData(itemStack);
		if (itemStack.getItem() == ErModItems.MORA.get()) {
			if (entity instanceof ErEntityInterface entityInterface && entityInterface.er$getArtifactEffectLevel(ArtifactEffectRegistry.LUCKY_DOG.get()) > 3) {
				entity.heal(6);
			}
            LazyOptional<IItemHandler> LazyOpt = entity.getCapability(ForgeCapabilities.ITEM_HANDLER);
			if (LazyOpt.isPresent() && LazyOpt.resolve().isPresent()) {
                IItemHandler _modHandler = LazyOpt.resolve().get();
				for (int _idx = 0; _idx < _modHandler.getSlots(); _idx++) {
                    item = _modHandler.getStackInSlot(_idx);
					if (item.getItem() == ErModItems.MORA_BAG.get()) {
						item.getOrCreateTag().putInt("moras", itemStack.getCount() + item.getOrCreateTag().getInt("moras"));
						itemStack.setCount(0);
                        break;
					}

				}
			}
		} else if (itemStack.getItem() == ErModItems.A_BAG_OF_MORA.get()) {
            LazyOptional<IItemHandler> LazyOpt = entity.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if (LazyOpt.isPresent() && LazyOpt.resolve().isPresent()) {
                IItemHandler _modHandler = LazyOpt.resolve().get();
				for (int _idx = 0; _idx < _modHandler.getSlots(); _idx++) {
					item = _modHandler.getStackInSlot(_idx);
					if (item.getItem() == ErModItems.MORA_BAG.get()) {
						item.getOrCreateTag().putInt("moras", itemStack.getOrCreateTag().getInt("moras") + item.getOrCreateTag().getInt("moras"));
						itemStack.setCount(0);
                        break;
					}
				}
			}
		}
		if (data != null && data.main().amount() == 0) {
            ARTIFACT.update(itemStack, ArtifactData::rolling);
        }
	}
}