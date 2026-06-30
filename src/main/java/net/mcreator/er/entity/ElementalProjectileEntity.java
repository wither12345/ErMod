
package net.mcreator.er.entity;


import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class ElementalProjectileEntity extends AbstractArrow implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM_0 = new ItemStack(Blocks.GLASS);
	private int knockback = 0;

	public ElementalProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(ErModEntities.ELEMENTAL_PROJECTILE.get(), world);
	}

	public ElementalProjectileEntity(EntityType<? extends ElementalProjectileEntity> type, Level world) {
		super(type, world);
	}

	public ElementalProjectileEntity(EntityType<? extends ElementalProjectileEntity> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public ElementalProjectileEntity(EntityType<? extends ElementalProjectileEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}


	public ItemStack getDefaultPickupItem() {
		return PROJECTILE_ITEM_0;
	}

	@Override
	public ItemStack getItem() {
		return PROJECTILE_ITEM_0;
	}

	@Override
	protected boolean canHitEntity(Entity entity) {
		return super.canHitEntity(entity) && EntityHurtEvent.shouldHurt(this.getOwner(), entity);
	}

	@Override
	protected ItemStack getPickupItem() {
		return null;
	}

	@Override
	protected void doPostHurtEffects(LivingEntity entity) {
		super.doPostHurtEffects(entity);
		entity.setArrowCount(entity.getArrowCount() - 1);
	}

	public void setKnockback(int knockback) {
		this.knockback = knockback;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.inGround || this.getOwner() == null || !this.getOwner().isAlive())
			this.discard();
	}

	public static ElementalProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 0.5f, 3, 0);
	}

	public static ElementalProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
		return shoot(world, entity, source, pullingPower * 0.5f, 3, 0);
	}

	public static ElementalProjectileEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		ElementalProjectileEntity entityarrow = new ElementalProjectileEntity(ErModEntities.ELEMENTAL_PROJECTILE.get(), entity, world);
		entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setCritArrow(false);
		entityarrow.setBaseDamage(damage);
		entityarrow.setKnockback(knockback);
		world.addFreshEntity(entityarrow);
		return entityarrow;
	}

	public static ElementalProjectileEntity shoot(LivingEntity entity, LivingEntity target) {
		ElementalProjectileEntity entityarrow = new ElementalProjectileEntity(ErModEntities.ELEMENTAL_PROJECTILE.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 0.5f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setBaseDamage(3);
		entityarrow.setKnockback(0);
		entityarrow.setCritArrow(false);
		entity.level().addFreshEntity(entityarrow);
		return entityarrow;
	}
}
