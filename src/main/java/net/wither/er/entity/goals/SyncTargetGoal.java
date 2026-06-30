package net.wither.er.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;

public class SyncTargetGoal extends TargetGoal {
	private LivingEntity ownerLastHurt;
	private int timestamp;

	public SyncTargetGoal(Mob mob) {
		super(mob, false);
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		if (mob instanceof OwnableEntity ownable) {
			LivingEntity livingentity = ownable.getOwner();
			if (livingentity == null) {
				return false;
			} else {
				this.ownerLastHurt = livingentity.getLastHurtMob();
				int i = livingentity.getLastHurtMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
			}
		}
		return false;
	}

	@Override
	public void start() {
		if (mob instanceof OwnableEntity ownable) {
			this.mob.setTarget(this.ownerLastHurt);
			LivingEntity livingentity = ownable.getOwner();
			if (livingentity != null) {
				this.timestamp = livingentity.getLastHurtMobTimestamp();
			}
		}
		super.start();
	}
}
