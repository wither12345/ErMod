package net.wither.er.mixins;

import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wither.er.entity.AnimationStater;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;
import net.wither.er.network.ErSyncGameRule;
import net.wither.er.player.ErPlayerInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class ErPlayerMixin extends LivingEntity implements AnimationStater, ErPlayerInterface {
	@Unique public AnimationState er$animationState0 = new AnimationState();
    @Unique private int er$selectedMora;

	protected ErPlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
		super(p_20966_, p_20967_);
	}

    @Override
    public int er$getMoraIndex() {
        return this.er$selectedMora;
    }

    @Override
    public void er$setMoraIndex(int m) {
        this.er$selectedMora = m ;
    }

    @Unique
	@Override
	public AnimationState getState() {
		return er$animationState0;
	}

	public void stopAnimation() {
		this.er$animationState0.animateWhen(false, this.tickCount);
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;moveCloak()V"))
	//@Inject(method = "tick", at = @At("HEAD"))
	public void onTick(CallbackInfo info) {
		ItemStack stack = this.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna;
		ErCombatVariables.PlayerVariables vars = this.getData(ErCombatVariables.PLAYER_VARIABLES);
		StellaFortunas SF = null;
		float speed = 1f;
		if (stack.getItem() instanceof StellaFortunas item) {
			SF = item;
			speed = SF.getSpeed(this, vars.animationId);
		}
		if (vars.animationTime >= 0) {
			vars.animationTime = vars.animationTime - 1;
			if (SF != null && vars.animationTime > SF.getFinishTick(this, vars.animationId, speed)) {
				this.setSprinting(false);
				this.setDeltaMovement(new Vec3(0, 0, 0));
			} else if (this.isSprinting()) {
				vars.animationId = 0;
				vars.animationTime = -1;
			}
		}
		if (vars.skillCooldown > 0)
			vars.skillCooldown = Math.max(0f, vars.skillCooldown - 0.05f);
		if (vars.burstCooldown > 0)
			vars.burstCooldown = Math.max(0f, vars.burstCooldown - 0.05f);
		if (this.isSprinting() && ErSyncGameRule.getRunningStamina()) {
			vars.stamina--;
			if (vars.stamina <= 0) {
				this.setSprinting(false);
				vars.stamina = 0;
			} else
				vars.staminaRecoveryCooldown = 50;
		} else {
			if (vars.staminaRecoveryCooldown <= 0) {
				vars.stamina += 1.1;
				if (vars.stamina > this.getAttributeValue(ErModAttributes.MAX_STAMINA)) {
					vars.stamina = this.getAttributeValue(ErModAttributes.MAX_STAMINA);
				}
			} else
				vars.staminaRecoveryCooldown--;
		}
		if (this.level().isClientSide()) {
			if (vars.animationTime > 0) {
				if (this.getPersistentData().getInt("animation_type") != vars.animationId) {
					this.er$animationState0.animateWhen(false, this.tickCount);
					this.getPersistentData().putInt("animation_type", vars.animationId);
				}
				this.er$animationState0.animateWhen(true, this.tickCount);
			} else {
				this.er$animationState0.animateWhen(false, this.tickCount);
			}
		}
		if (SF != null) {
			SF.AnimationTicking(this, vars.animationId, vars.animationTime, speed);
			SF.onTick(this.level(), this, this.getX(), this.getY(), this.getZ());
		}
	}
}