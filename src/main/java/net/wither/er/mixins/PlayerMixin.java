package net.wither.er.mixins;

import net.minecraft.util.Mth;
import net.wither.er.item.fortuna.StellaFortuna;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wither.er.init.ErAttributeRegister;
import net.wither.er.init.MobEffectRegister;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;
import net.wither.er.network.ErSyncGameRule;
import net.wither.er.player.IPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IPlayer {
    @Unique public AnimationState er$animationState0 = new AnimationState();
    @Unique private int er$selectedMora;

	protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
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
	public AnimationState er$getState() {
		return er$animationState0;
	}

	@Override
	public void er$stopAnimation() {
		this.er$animationState0.animateWhen(false, this.tickCount);
	}

    @Inject(method = "canEat", at = @At("RETURN"), cancellable = true)
    public void canEatInject(boolean b, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(cir.getReturnValue() || this.hasEffect(MobEffectRegister.APPETIZER.get()));
    }

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    public void modifyScale(float ticker, CallbackInfoReturnable<Float> cir){
        this.getCapability(ErItemVariables.PLAYER_VARIABLES).ifPresent(vars ->{
            if(vars.Stella_Fortuna.getItem() instanceof StellaFortuna item && item.hasAnimation(this)){
                this.getCapability(ErCombatVariables.PLAYER_VARIABLES).ifPresent(combat -> {
                    float t = combat.animationTime;
                    float total = item.getAnimationTick(this, combat.animationId);
                    float stop = item.getFinishTick(this, combat.animationId);
                    cir.setReturnValue(Mth.clamp((total - t) / (total - stop), 0f, 1f));
                });
            }
        });
    }

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;moveCloak()V"))
	//@Inject(method = "tick", at = @At("HEAD"))
	public void ontick(CallbackInfo info) {
		ItemStack stack = this.getCapability(ErItemVariables.PLAYER_VARIABLES).orElseGet(ErItemVariables.PlayerVariables::new).Stella_Fortuna;
		ErCombatVariables.PlayerVariables vars = this.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
		StellaFortuna SF = null;
		float speed = 1f;
		if (stack.getItem() instanceof StellaFortuna item) {
			SF = item;
			speed = SF.getSpeed(this, vars.animationId);
            SF.animationTicking(this, vars.animationId, vars.animationTime, speed);
		}
		if (vars.animationTime >= 0) {
			vars.animationTime -= speed;
			if (SF != null && vars.animationTime > SF.getFinishTick(this, vars.animationId)) {
				this.setSprinting(false);
				this.setDeltaMovement(new Vec3(0, 0, 0));
			} else if (this.isSprinting()) {
				vars.animationId = 0;
				vars.animationTime = -1;
			}
		}
		if (vars.skillCooldown > 0) {
			vars.skillCooldown = Math.max(0f, vars.skillCooldown - 0.05f);
		}
		if (vars.burstCooldown > 0) {
			vars.burstCooldown = Math.max(0f, vars.burstCooldown - 0.05f);
		}
		if (this.isSprinting() && ErSyncGameRule.getRunningStamina()) {
			vars.stamina -= this.getAttributeValue(ErAttributeRegister.STAMINA_COST.get());
			if (vars.stamina <= 0) {
				this.setSprinting(false);
				vars.stamina = 0;
			} else
				vars.staminaRecoveryCooldown = 50;
		} else {
			if (vars.staminaRecoveryCooldown <= 0) {
				vars.stamina += 1.1;
				if (vars.stamina > this.getAttributeValue(ErModAttributes.MAX_STAMINA.get())) {
					vars.stamina = this.getAttributeValue(ErModAttributes.MAX_STAMINA.get());
				}
			} else
				vars.staminaRecoveryCooldown--;
		}
		if (this.level().isClientSide()) {
			if (vars.animationTime > 0) {
				if (this.getPersistentData().getInt("animation_type") != vars.animationId) {
                    this.er$stopAnimation();
					//this.er$animationState0.animateWhen(false, this.tickCount);
					this.getPersistentData().putInt("animation_type", vars.animationId);
				}
                this.er$animationState0.animateWhen(true, this.tickCount);
			} else {
				this.er$animationState0.animateWhen(false, this.tickCount);
			}
		}
	}
}