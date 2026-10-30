package net.mcreator.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.wither.er.item.fortuna.MemoryOfRovingGalesItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.wither.er.combat.VacuumField;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.elements.IAuraContainerOwner;
import net.wither.er.init.EffectRegister;
import net.wither.er.init.ElementRegistry;
import net.wither.er.item.data.FortunaData;
import net.wither.er.item.fortuna.StellaFortuna;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TravelerTornadoEntity extends Monster implements OwnableEntity {
	public static final EntityDataAccessor<Integer> DATA_ABSORPTION = SynchedEntityData.defineId(TravelerTornadoEntity.class, EntityDataSerializers.INT);
    public UUID ownerUUID;
    private int aliveTick ;
    private LivingEntity stackedOwner;

	public TravelerTornadoEntity(EntityType<TravelerTornadoEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_ABSORPTION, 0);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
		return SoundEvents.GENERIC_HURT;
	}

	@Override
	public @NotNull SoundEvent getDeathSound() {
		return SoundEvents.GENERIC_DEATH;
	}

	@Override
	public boolean hurt(@NotNull DamageSource damagesource, float amount) {
		return false;
	}

	@Override
	public boolean ignoreExplosion(@NotNull Explosion explosion) {
		return true;
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
        compound.putInt("DataAbsorption", this.entityData.get(DATA_ABSORPTION));
        if(getOwner() != null)
            compound.putUUID("ownerUUID", this.getOwner().getUUID());
        else if(ownerUUID != null)
            compound.putUUID("ownerUUID", ownerUUID);
        compound.putInt("aliveTick", this.aliveTick);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DataAbsorption"))
			this.entityData.set(DATA_ABSORPTION, compound.getInt("DataAbsorption"));
		if (compound.contains("ownerUUID"))
            this.ownerUUID = compound.getUUID("ownerUUID");
        if (compound.contains("aliveTick"))
            this.aliveTick = compound.getInt("aliveTick");
	}

	@Override
	public void baseTick() {
		super.baseTick();
        Entity owner = this.getOwner();
        this.aliveTick ++ ;
        if (this.aliveTick > 5) {
            if(this.level() instanceof ServerLevel)
                VacuumField.vacuum(this.level(), this.getPosition(2.5f), owner, 10, 2);
            this.setDeltaMovement(new Vec3(((-1) * (Math.sin((this.getYRot() / 180d) * Math.PI) / 5)), (this.getDeltaMovement().y()), (Math.cos((this.getYRot() / 180d) * Math.PI) / 5)));
            if (aliveTick % 10 == 0) {
                if (owner instanceof LivingEntity living && living.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna.getItem() instanceof MemoryOfRovingGalesItem item) {
                    Holder<DamageType> type = this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortuna.BURST) ;
                    FortunaData data = FortunaData.get(owner);

                    final int absorption = this.getEntityData().get(TravelerTornadoEntity.DATA_ABSORPTION);
                    if (absorption == 0) {
                        int absorb = 0;
                        for (Entity entity : this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(3), e -> true)) {
                            if (entity instanceof IAuraContainerOwner auraOwner) {
                                if (!auraOwner.er$getAuraContainer().getAura().get(Element.Category.ELECTRO.getId()).isEmpty())
                                    absorb = Math.max(absorb, 1);
                                if (!auraOwner.er$getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty())
                                    absorb = Math.max(absorb, 2);
                                if (!auraOwner.er$getAuraContainer().getAura().get(Element.Category.PYRO.getId()).isEmpty())
                                    absorb = Math.max(absorb, 3);
                                if (!auraOwner.er$getAuraContainer().getAura().get(Element.Category.CRYO.getId()).isEmpty())
                                    absorb = 4;
                            }
                        }
                        if (absorb != 0)
                            this.getEntityData().set(TravelerTornadoEntity.DATA_ABSORPTION, absorb);
                    }

                    this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2), e -> EntityHurtEvent.shouldHurt(owner, e)).forEach(entity -> {
                        entity.hurt(
                                ElementSource.createDamageSource(type, owner, new ElementSource(ElementRegistry.ANEMO.get(), MemoryOfRovingGalesItem.TRAVELER_BURST, 2, false)),
                                (float) ((living.getAttributeValue(Attributes.ATTACK_DAMAGE)) * MemoryOfRovingGalesItem.DamageMulti(30, 0, data)));
                        if(absorption != 0){
                            entity.hurt(
                                    ElementSource.createDamageSource(type, owner, MemoryOfRovingGalesItem.absorbElement(absorption)),
                                    (float) ((living.getAttributeValue(Attributes.ATTACK_DAMAGE)) * MemoryOfRovingGalesItem.DamageMulti(30, 1, data)));
                        }
                        if(data.constellation() >= 6)
                            entity.addEffect(new MobEffectInstance(EffectRegister.GUST_SURGE_REDUCE_RES, 600, absorption));
                    });
                }
            }
            if(this.aliveTick >= 120)
                this.discard();
        }
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void doPush(@NotNull Entity entityIn) {
	}

	@Override
	protected void pushEntities() {
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 1.1);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		return builder;
	}

    @Override
    public @Nullable UUID getOwnerUUID() {
        if(this.getOwner() != null)
            return this.getOwner().getUUID();
        return this.ownerUUID;
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        if(this.stackedOwner != null) return this.stackedOwner;
        this.stackedOwner = this.level().getPlayerByUUID(uuid);
        return this.stackedOwner;
    }

    public void setOwner(LivingEntity entity){
        this.stackedOwner = entity;
        this.ownerUUID = entity.getUUID();
    }

    public int getAliveTick() {
        return aliveTick;
    }
}