/*
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.er as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.er;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

//@EventBusSubscriber()
public class ErDataHandler {
	public static void handleDataOnMain(final ErData data, final IPayloadContext context) {
		// Do something with the data, on the main thread
		Entity entity = Minecraft.getInstance().level.getEntity(data.entityID());
		if (!Minecraft.getInstance().level.isClientSide())
			return;
		context.enqueueWork(() -> {
			if (entity != null) {
				entity.getPersistentData().putInt("Frozen", data.time());
			}
		});
	}
}
