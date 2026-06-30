package net.wither.er.shield;

import net.minecraft.nbt.CompoundTag;

public class ShieldStack {
	private final ErShield shield;
	public float health;
	public int time;

	public ShieldStack(ErShield shield, float health, int time) {
		this.shield = shield;
		this.health = health;
		this.time = time;
	}

	public ShieldStack(ErShield shield, CompoundTag tag) {
		this(shield, tag.getFloat("health"), tag.getInt("time"));
	}

	public ErShield getShield() {
		return this.shield;
	}

	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.putFloat("health", health);
		tag.putInt("time", time);
		return tag;
	}
}
