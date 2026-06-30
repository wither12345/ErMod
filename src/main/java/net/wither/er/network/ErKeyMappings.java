package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = {Dist.CLIENT})
public class ErKeyMappings {
	public static final KeyMapping SkillKey = new KeyMapping("key.er.key_r", GLFW.GLFW_KEY_R, "key.categories.misc") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if(Minecraft.getInstance().player == null)return;
			if (isDownOld != isDown && isDown) {
				ErMod.PACKET_HANDLER.sendToServer(new SkillMessage(0, false));
				SkillMessage.pressAction(Minecraft.getInstance().player, 0, false);
			} else if (isDownOld != isDown && !isDown) {
				ErMod.PACKET_HANDLER.sendToServer(new SkillMessage(1, false));
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
			if(Minecraft.getInstance().player == null)return;
			if (isDownOld != isDown && isDown) {
				ErMod.PACKET_HANDLER.sendToServer(new SkillMessage(0, true));
				SkillMessage.pressAction(Minecraft.getInstance().player, 0, true);
			} else if (isDownOld != isDown && !isDown) {
				ErMod.PACKET_HANDLER.sendToServer(new SkillMessage(1, true));
				SkillMessage.pressAction(Minecraft.getInstance().player, 1, true);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SkillKey);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				SkillKey.consumeClick();
			}
		}
	}
}