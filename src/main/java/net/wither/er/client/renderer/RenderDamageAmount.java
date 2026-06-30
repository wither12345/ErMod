package net.wither.er.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.ERClientConfig;
import net.mcreator.er.ErMod;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ErMod.MODID, value = Dist.CLIENT)
public class RenderDamageAmount {
    static float d_tick = 0 ;
    static float p_tick = 0 ;
    private static final List<DamageAmount> damageNumbers = new ArrayList<>();

    public static void addDamage(int damage, int color, double x, double y, double z , boolean critical){
        if (Minecraft.getInstance().level != null) {
            damageNumbers.add(new DamageAmount(Minecraft.getInstance().level.getGameTime() ,damage, color, x, y, z , critical)) ;
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if(!ERClientConfig.DAMAGE_DISPLAY.get()){
            damageNumbers.clear();
            return;
        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            d_tick = event.getPartialTick() - p_tick;
            p_tick = event.getPartialTick();
            if(d_tick < 0)
                d_tick = 0;
            if(Minecraft.getInstance().level == null)return;
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Iterator<DamageAmount> iterator = damageNumbers.iterator();
            while (iterator.hasNext()) {
                DamageAmount damageAmount = iterator.next();
                if (damageAmount.check(Minecraft.getInstance().level.getGameTime())) {
                    iterator.remove();
                }
                damageAmount.render(event.getPoseStack(),bufferSource,d_tick);
            }

            /*
            Font font = Minecraft.getInstance().font;
            font.drawInBatch("level", 0, 0, 553648127, false,new Matrix4f(), bufferSource, Font.DisplayMode.NORMAL, 0x00000000, 15728880);
            bufferSource.endBatch();
             */
        }
    }

    public static class DamageAmount{
        final long added_tick;
        final int damage;
        final int color ;
        final double x;
        final double y;
        final double z;
        final boolean critical ;
        private static final int maxTime = 20 ;
        private float size;

        public DamageAmount(long added_tick, int amount, int color, double x, double y, double z, boolean critical) {
            this.added_tick = added_tick;
            this.damage = amount;
            this.color = color;
            this.x = x;
            this.y = y;
            this.z = z;
            this.critical = critical;
            if(critical)
                this.size = 0.24f ;
            else
                this.size = 0 ;
        }

        public boolean check(long now_time){
            return added_tick + maxTime < now_time ;
        }

        public void render(PoseStack stack, MultiBufferSource.BufferSource bufferSource,float partialTick){
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            String s = String.valueOf(damage);

            renderFloatingText(stack, bufferSource , s, x, y, z, color ,calculateSize(partialTick));
        }

        public static void renderFloatingText(PoseStack poseStack, MultiBufferSource bufferSource, String string, double x, double y, double z, int color, float scaling) {
            Minecraft minecraft = Minecraft.getInstance();
            Camera camera = minecraft.gameRenderer.getMainCamera();
            if (camera.isInitialized()) {
                Font font = minecraft.font;
                double d0 = camera.getPosition().x;
                double d1 = camera.getPosition().y;
                double d2 = camera.getPosition().z;
                poseStack.pushPose();
                poseStack.translate((float) (x - d0), (float) (y - d1), (float) (z - d2));
                poseStack.mulPoseMatrix((new Matrix4f()).rotation(camera.rotation()));
                poseStack.scale(-scaling, -scaling, -scaling);
                float f =(float) (-font.width(string)) / 2.0F ;
                font.drawInBatch(string, f, 0.0F, color, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
                poseStack.popPose();
            }
        }

        @Override
        public String toString() {
            return "pos[" + x + "," + y + "," + z + "]"  + damage;
        }

        private float calculateSize(float tick){
            if(critical){
                if(size > 0.12)
                    size -= tick * 0.06f ;
            }
            else {
                if(size < 0.06)
                  size += tick * 0.03f;
            }
            return size ;
        }
    }
}
