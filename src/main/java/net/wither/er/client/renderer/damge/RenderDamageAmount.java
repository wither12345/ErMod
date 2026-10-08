package net.wither.er.client.renderer.damge;

import com.mojang.blaze3d.systems.RenderSystem;
import net.mcreator.er.ERClientConfig;
import net.mcreator.er.ErMod;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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
    private static final int MAX_TIME = 20;

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

            if(Minecraft.getInstance().level != null) {
                long nowTime = Minecraft.getInstance().level.getGameTime();
                while (iterator.hasNext()) {
                    DamageAmount damageAmount = iterator.next();
                    if (damageAmount.check(nowTime))
                        iterator.remove();
                    else if (ERClientConfig.DAMAGE_DISPLAY.get())
                        damageAmount.render(event.getCamera(), bufferSource, event.getPartialTick().getGameTimeDeltaPartialTick(true), nowTime);
                }
            }
        }
    }

    public record DamageAmount(
            long added_tick,
            int damage,
            int color,
            double x,
            double y,
            double z,
            boolean critical,
            DamageDisplayType type
    ) {

        public boolean check(long now_time) {
                return added_tick + MAX_TIME < now_time;
        }

        public void render(Camera camera, MultiBufferSource.BufferSource bufferSource, float partialTick, long nowTime) {
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            String s = String.valueOf(damage);

            RenderSpecialDamage.renderLunarText(bufferSource, camera, s, x, y, z, color, calculateSize(partialTick, nowTime), type);
        }

        private float calculateSize(float partialTick, long nowTime) {
            if (this.critical) return Math.max(0.06f, 0.12f - (nowTime + partialTick - added_tick) * 0.04f * ERClientConfig.CRITICAL_SCALE.get().floatValue());
            return Math.min(0.03f, (nowTime + partialTick - added_tick) * 0.03f * ERClientConfig.DAMAGE_SCALE.get().floatValue());
        }
    }

    public enum DamageDisplayType{
        NORMAL,
        LUNAR,
        STELLAR
    }
}
