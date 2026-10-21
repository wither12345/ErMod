package net.wither.er.client.renderer.damage;

import net.mcreator.er.ERClientConfig;

public class DamageDisplay extends DisplayMessage {
    private final int damage;
    private final boolean critical;
    public DamageDisplay(long addedTick, int damage, double x, double y, double z, int color, boolean critical, RenderDamageAmount.Display type) {
        super(addedTick, x, y, z, color, type);
        this.damage = damage;
        this.critical = critical;
    }

    @Override
    public String getDisplayText() {
        return String.valueOf(damage);
    }

    @Override
    public float getSize(float partialTick, long nowTime) {
        if (this.critical) return Math.max(0.06f, 0.12f - (nowTime + partialTick - this.added_tick) * 0.04f * ERClientConfig.CRITICAL_SCALE.get().floatValue());
        return Math.min(0.03f, (nowTime + partialTick - this.added_tick) * 0.03f * ERClientConfig.DAMAGE_SCALE.get().floatValue());
    }
}
