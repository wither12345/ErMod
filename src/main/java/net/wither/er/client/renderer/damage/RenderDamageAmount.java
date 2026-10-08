package net.wither.er.client.renderer.damage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mcreator.er.ERClientConfig;
import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ErMod.MODID, value = Dist.CLIENT)
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
        if(!ERClientConfig.DAMAGE_DISPLAY.get()){
            damageNumbers.clear();
            return;
        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            float p_tick = event.getPartialTick();
            if(Minecraft.getInstance().level == null)return;
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Iterator<DamageAmount> iterator = damageNumbers.iterator();
            long now_time = Minecraft.getInstance().level.getGameTime();

            while (iterator.hasNext()) {
                DamageAmount damageAmount = iterator.next();
                if (damageAmount.check(now_time)) {
                    iterator.remove();
                }
                damageAmount.render(event.getPoseStack(),bufferSource, p_tick, now_time);
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

        public void render(PoseStack stack, MultiBufferSource.BufferSource bufferSource, float partialTick, long nowTime) {
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            String s = String.valueOf(damage);

            RenderSpecialDamage.renderLunarText(stack, bufferSource, s, x, y, z, color, calculateSize(partialTick, nowTime), type);
        }

        private float calculateSize(float partialTick, long nowTime) {
            if (this.critical)
                return Math.max(0.06f, 0.12f - (nowTime + partialTick - added_tick) * 0.04f * ERClientConfig.CRITICAL_SCALE.get().floatValue());
            return Math.min(0.03f, (nowTime + partialTick - added_tick) * 0.03f * ERClientConfig.DAMAGE_SCALE.get().floatValue());
        }
    }

    public enum DamageDisplayType{
        NORMAL,
        LUNAR,
        STELLAR
    }
}
