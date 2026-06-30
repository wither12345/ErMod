package net.wither.er.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin extends Monster implements RangedAttackMob {
	@Shadow
	private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);
	@Shadow
	private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false);

	protected AbstractSkeletonMixin(EntityType<? extends AbstractSkeleton> p_32133_, Level p_32134_) {
		super(p_32133_, p_32134_);
	}

	@Inject(method = "reassessWeaponGoal", at = @At("TAIL"))
	public void reassessWeaponGoal(CallbackInfo info) {
        if (!this.level().isClientSide) {
			this.goalSelector.removeGoal(this.meleeGoal);
			this.goalSelector.removeGoal(this.bowGoal);
			ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem));
			if (itemstack.getItem() instanceof BowItem) {
				int i = 20;
				if (this.level().getDifficulty() != Difficulty.HARD) {
					i = 40;
				}
				this.bowGoal.setMinAttackInterval(i);
				this.goalSelector.addGoal(4, this.bowGoal);
			} else {
				this.goalSelector.addGoal(4, this.meleeGoal);
			}
		}
	}

	@Inject(method = "performRangedAttack" , at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shoot(DDDFF)V"),locals = LocalCapture.CAPTURE_FAILSOFT)
	public void performRangedAttack(LivingEntity entity, float velocity , CallbackInfo info ,@Local(ordinal = 0) AbstractArrow abstractarrow){
		abstractarrow.setBaseDamage(this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3);
	}
}
