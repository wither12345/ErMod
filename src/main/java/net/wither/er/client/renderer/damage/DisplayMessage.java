package net.wither.er.client.renderer.damage;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Quaternionf;

public abstract class DisplayMessage {
    protected static final int MAX_TIME = 20;
    protected final long added_tick;
    protected final double x;
    protected final double y;
    protected final double z;
    protected final int color;
    protected final RenderDamageAmount.Display type;

    protected DisplayMessage(long addedTick, double x, double y, double z, int color, RenderDamageAmount.Display type) {
        added_tick = addedTick;
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.type = type;
    }

    public boolean check(long now_time) {
        return added_tick + MAX_TIME < now_time ;
    }

    public abstract String getDisplayText();

    public abstract float getSize(float partialTick, long nowTime);

    public void render(Camera camera, MultiBufferSource.BufferSource bufferSource, PoseStack pose, float partialTick, long nowTime) {
        String s = this.getDisplayText();
        pose.pushPose();
        pose.translate(x, y, z);

        float scaling = -this.getSize(partialTick, nowTime);
        pose.mulPose(new Quaternionf(camera.rotation()));

        pose.scale(scaling, scaling, scaling);

        RenderSpecialDamage.renderText(bufferSource, camera, pose, s, color, type);
        pose.popPose();
    }
}