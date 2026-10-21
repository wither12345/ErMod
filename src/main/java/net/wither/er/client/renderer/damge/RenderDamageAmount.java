package net.wither.er.client.renderer.damge;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.ERClientConfig;
import net.mcreator.er.ErMod;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.wither.er.elements.Reaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = ErMod.MODID, value = Dist.CLIENT)
public class RenderDamageAmount {
    private static final List<DisplayMessage> DISPLAY_MESSAGES = new ArrayList<>();

    public static void addDamage(int damage, int color, double x, double y, double z , boolean critical, Display type){
        if (Minecraft.getInstance().level != null) {
            DISPLAY_MESSAGES.add(new DamageDisplay(Minecraft.getInstance().level.getGameTime(), damage, x, y, z, color, critical, type)) ;
        }
    }

    public static void addReaction(Reaction reaction, double x, double y, double z, int color, Display type){
        if (Minecraft.getInstance().level != null) {
            DISPLAY_MESSAGES.add(new ReactionDisplay(reaction, Minecraft.getInstance().level.getGameTime(), x, y, z, color, type)) ;
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if(!ERClientConfig.DAMAGE_DISPLAY.get()){
            DISPLAY_MESSAGES.clear();
            return;
        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            if(Minecraft.getInstance().level == null) return;
            float pTick = event.getPartialTick().getGameTimeDeltaPartialTick(true);
            long nowTime = Minecraft.getInstance().level.getGameTime();

            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Iterator<DisplayMessage> iterator = DISPLAY_MESSAGES.iterator();

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            PoseStack pose = new PoseStack();
            Camera camera = event.getCamera();

            double d0 = camera.getPosition().x;
            double d1 = camera.getPosition().y;
            double d2 = camera.getPosition().z;
            pose.pushPose();
            pose.translate(-d0, -d1, -d2);

            while (iterator.hasNext()) {
                DisplayMessage display = iterator.next();
                if (display.check(nowTime))
                    iterator.remove();
                else
                    display.render(camera, bufferSource, pose, pTick, nowTime);
            }
            pose.popPose();
        }
    }

    public enum Display {
        NORMAL,
        LUNAR,
        STELLAR
    }
}
