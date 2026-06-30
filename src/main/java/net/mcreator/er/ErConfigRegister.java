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

import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber()
public class ErConfigRegister {
	/*
		@SubscribeEvent
		public static void register(FMLConstructModEvent event) {
			event.enqueueWork(() -> {
				ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, ERConfig.SPEC, "er-common.toml");
				ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
			});
		}
		*/
	@SubscribeEvent
	public static void commonSetup(FMLConstructModEvent event) {
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, ERConfig.SPEC, "er-common.toml");
		ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, ERClientConfig.SPEC, "er-client.toml");
	}

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}
}
