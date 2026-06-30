package net.wither.er.network;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = {Dist.CLIENT})
public class ErKeyMappings {
	public static final KeyMapping SkillKey = new KeyMapping("key.er.key_r", GLFW.GLFW_KEY_R, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new SkillMessage(0, false));
				SkillMessage.pressAction(Minecraft.getInstance().player, 0, false);
			} else if (isDownOld != isDown && !isDown) {
				PacketDistributor.sendToServer(new SkillMessage(1, false));
				SkillMessage.pressAction(Minecraft.getInstance().player, 1, false);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping BurstKey = new KeyMapping("key.er.key_c", GLFW.GLFW_KEY_C, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new SkillMessage(0, true));
				SkillMessage.pressAction(Minecraft.getInstance().player, 0, true);
			} else if (isDownOld != isDown && !isDown) {
				PacketDistributor.sendToServer(new SkillMessage(1, true));
				SkillMessage.pressAction(Minecraft.getInstance().player, 1, true);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SkillKey);
	}

	@EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				SkillKey.consumeClick();
			}
		}
	}
}