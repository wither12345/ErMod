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
    static float d_tick = 0 ;
    static float p_tick = 0 ;
    private static final List<DamageAmount> damageNumbers = new ArrayList<>();

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

        public void render(PoseStack stack, MultiBufferSource.BufferSource bufferSource,float partialTick){
            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            String s = String.valueOf(damage);

            RenderSpecialDamage.renderLunarText(stack, bufferSource , s, x, y, z, color ,calculateSize(partialTick), type);
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
