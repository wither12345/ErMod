package net.wither.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModParticleTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class BloomEntityEntity extends LivingEntity implements TraceableEntity{
	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;
	private int surviveTime ;
	private final NonNullList<ItemStack> armorItems;

	public BloomEntityEntity(EntityType<BloomEntityEntity> type, Level world) {
		super(type, world);
		this.armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
	}

	@Override
	public boolean shouldShowName() {
		return false;
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return armorItems;
	}

	public ItemStack getItemBySlot(EquipmentSlot p_21467_) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.LEFT;
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
		builder = builder.add(Attributes.MAX_HEALTH, 6);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 100);
		builder = builder.add(Attributes.STEP_HEIGHT, 0);
		builder = builder.add(NeoForgeMod.SWIM_SPEED, 0);
		return builder;
	}

	public void setOwner(@Nullable Entity p_37263_) {
		if (p_37263_ != null) {
			this.ownerUUID = p_37263_.getUUID();
			this.cachedOwner = p_37263_;
		}

	}

	@Override
	public Entity getOwner() {
		if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
			return this.cachedOwner;
		} else {
			if (this.ownerUUID != null) {
				Level var2 = this.level();
				if (var2 instanceof ServerLevel serverlevel) {
                    this.cachedOwner = serverlevel.getEntity(this.ownerUUID);
					return this.cachedOwner;
				}
			}

			return null;
		}
	}
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		if (this.ownerUUID != null) {
			tag.putUUID("Owner", this.ownerUUID);
		}
		tag.putInt("SurviveTime" , surviveTime);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		if (tag.hasUUID("Owner")) {
			this.ownerUUID = tag.getUUID("Owner");
			this.cachedOwner = null;
		}
		if(tag.contains("SurviveTime")){
			this.surviveTime = tag.getInt("SurviveTime");
		}
	}


	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level().isClientSide())
			this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 114, false, false));
		if (this instanceof AuraContainerInterface auraContainerInterface) {
			if(!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.PYRO.getId()).isEmpty()) {
				if (this.level() instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (ErModParticleTypes.P_DENDRO_EXPLOSION.get()), this.getX(), (this.getY() + 0.2), this.getZ(), 1, 0, 0, 0, 0);
				this.explode(9f, this.getOwner());
			}
			else if(!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.ELECTRO.getId()).isEmpty()){
				if (this.level() instanceof ServerLevel _level) {
					Hyperbloom hyperbloom = ErModEntities.HYPERBLOOM.get().spawn(_level, this.getOnPos().above(2), MobSpawnType.MOB_SUMMONED);
					if (hyperbloom != null) {
						hyperbloom.setOwner(this.getOwner());
						hyperbloom.push(0, 0.5, 0);
					}
				}
				this.discard();
			}
		}
		if(this.surviveTime ++ > 120){
			this.explode(6f, this.getOwner());
		}
	}

	public void explode(float basicDamage, Entity damager){
		if(damager == null)
			damager = this.getOwner() ;
		List<LivingEntity> ent_found = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(this.getPosition(0),this.getPosition(0)).inflate(2.5), e -> true).stream()
				.sorted(Comparator.comparingDouble(e -> e.distanceToSqr(this.getPosition(0)))).toList();
		for (LivingEntity entity_iterator : ent_found) {
			entity_iterator.hurt(
					new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(Element.BLOOM) , this),
                    basicDamage
							* EntityHurtEvent.getLevelMultiply(damager)
							* (EntityHurtEvent.shouldHurt(damager,entity_iterator) ? 1 : 0.05f)
			);
		}
		this.discard();
	}
}