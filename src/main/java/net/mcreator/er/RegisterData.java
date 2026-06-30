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

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber
public class RegisterData {
	@SubscribeEvent
	public static void Register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1"); // All subsequent payloads will register on the network thread
		registrar.playBidirectional(ErData.TYPE, ErData.STREAM_CODEC, new DirectionalPayloadHandler<>(ErDataHandler::handleDataOnMain, ErDataHandler::handleDataOnMain));
	}
}
