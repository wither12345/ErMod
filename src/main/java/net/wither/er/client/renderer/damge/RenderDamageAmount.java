package net.wither.er.client.renderer.damge;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.ERClientConfig;
import net.mcreator.er.ErMod;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EventBusSubscriber(modid = ErMod.MODID, value = Dist.CLIENT)
public class RenderDamageAmount {
    private static final List<DamageAmount> damageNumbers = new ArrayList<>();

    public static void addDamage(int damage, int color, double x, double y, double z , boolean critical, DamageDisplayType type){
        if (Minecraft.getInstance().level != null) {
            damageNumbers.add(new DamageAmount(Minecraft.getInstance().level.getGameTime() ,damage, color, x, y, z , critical, type)) ;
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Iterator<DamageAmount> iterator = damageNumbers.iterator();
            while (iterator.hasNext()) {
                DamageAmount damageAmount = iterator.next();
                if (Minecraft.getInstance().level != null && damageAmount.check(Minecraft.getInstance().level.getGameTime())) {
                    iterator.remove();
                }
                else if(ERClientConfig.DAMAGE_DISPLAY.get())
                    damageAmount.render(event.getCamera(),bufferSource,event.getPartialTick().getGameTimeDeltaTicks());
            }
        }
    }

    public static class DamageAmount{
        private final long added_tick;
        private final int damage;
        private final int color ;
        private final double x;
        private final double y;
        private final double z;
        private final boolean critical ;
        private static final int maxTime = 20 ;
        private float size;
        private final DamageDisplayType type;

        public DamageAmount(long added_tick, int amount, int color, double x, double y, double z, boolean critical, DamageDisplayType type) {
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
            this.type = type;
        }

        public boolean check(long now_time){
            return added_tick + maxTime < now_time ;
        }

        public void render(Camera camera , MultiBufferSource.BufferSource bufferSource,float partialTick){
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            String s = String.valueOf(damage);

            RenderSpecialDamage.renderLunarText(bufferSource, camera, s, x, y, z, color ,calculateSize(partialTick), type);
        }

        private static void renderFloatingText(MultiBufferSource source , Camera camera, String string, double x, double y, double z, int color, float scaling) {
            Minecraft minecraft = Minecraft.getInstance();
            if (camera.isInitialized()) {
                PoseStack pose = new PoseStack() ;
                Font font = minecraft.font;
                double d0 = camera.getPosition().x;
                double d1 = camera.getPosition().y;
                double d2 = camera.getPosition().z;
                pose.pushPose();
                pose.translate((float) (x - d0), (float) (y - d1) + 0.07F, (float) (z - d2));
                pose.mulPose(camera.rotation());
                pose.scale(scaling, -scaling, scaling);
                float f =(float) (-font.width(string)) / 2.0F ;

                ShaderInstance oldShader = RenderSystem.getShader();
                font.drawInBatch(string, f, 0.0F, color, false, pose.last().pose(), source, Font.DisplayMode.SEE_THROUGH , 0, 15728880);
                RenderSystem.setShader(() -> oldShader);
                pose.popPose();
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

    public enum DamageDisplayType{
        NORMAL,
        LUNAR,
        STELLAR
    }
}
