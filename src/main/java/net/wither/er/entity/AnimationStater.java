package net.wither.er.entity;

import net.minecraft.world.entity.AnimationState;

public interface AnimationStater {
	AnimationState getState();

	void stopAnimation();
}
