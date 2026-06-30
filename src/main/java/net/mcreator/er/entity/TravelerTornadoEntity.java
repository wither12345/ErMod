package net.mcreator.er.entity;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.StellaFortunas;
import net.mcreator.er.item.MemoryofRovingGalesItem;
import net.mcreator.er.procedures.VacuumFieldsDisplacingProcedure;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.UUID;

public class TravelerTornadoEntity extends Monster implements OwnableEntity {
	public static final EntityDataAccessor<Integer> DATA_Absorption = SynchedEntityData.defineId(TravelerTornadoEntity.class, EntityDataSerializers.INT);
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
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_Absorption, 0);
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
	public SoundEvent getHurtSound(DamageSource ds) {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.death"));
	}

	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		return false;
	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
        compound.putInt("DataAbsorption", this.entityData.get(DATA_Absorption));
        if(getOwner() != null)
            compound.putUUID("Dataowner", this.getOwner().getUUID());
        else if(ownerUUID != null)
            compound.putUUID("Dataowner", ownerUUID);
        compound.putInt("aliveTick", this.aliveTick);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DataAbsorption"))
			this.entityData.set(DATA_Absorption, compound.getInt("DataAbsorption"));
		if (compound.contains("Dataowner"))
            this.ownerUUID = compound.getUUID("Dataowner");
        if (compound.contains("aliveTick"))
            this.aliveTick = compound.getInt("aliveTick");
	}

	@Override
	public void baseTick() {
		super.baseTick();
        Entity owner = this.getOwner();
        this.aliveTick ++ ;
        if (this.aliveTick > 5) {
            final Vec3 _center = new Vec3(this.getX(), this.getY() + 2.5, this.getZ());
            if(this.level() instanceof ServerLevel)
                VacuumFieldsDisplacingProcedure.execute(this.level(), this.getX(), this.getY() + 2.5, this.getZ(), owner, 10, 0.5);
            this.setDeltaMovement(new Vec3(((-1) * (Math.sin((this.getYRot() / 180d) * Math.PI) / 5)), (this.getDeltaMovement().y()), (Math.cos((this.getYRot() / 180d) * Math.PI) / 5)));
            if (aliveTick % 10 == 0) {
                if (owner instanceof Player player && player.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna.getItem() instanceof MemoryofRovingGalesItem item) {
                    Holder<DamageType> type = this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(StellaFortunas.BURST) ;
                    for (LivingEntity entityiterator : this.level().getEntitiesOfClass(LivingEntity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().toList()) {
                        if (EntityHurtEvent.shouldHurt(owner, entityiterator) && entityiterator instanceof LivingEntity && !(this == entityiterator)) {
                            entityiterator.hurt(
                                    ElementSource.createDamageSource(type, owner, new ElementSource(ElementRegistry.ANEMO.get(), MemoryofRovingGalesItem.TRAVELER_BURST, 2, false)),
                                    (float) ((player.getAttributeValue(Attributes.ATTACK_DAMAGE)) * item.DamageMulti(30, 0)));
                        }
                    }
                    int absorption = this.getEntityData().get(TravelerTornadoEntity.DATA_Absorption);
                    if (absorption == 0) {
                        for (Entity entityiterator : this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
                            if (entityiterator instanceof AuraContainerInterface auraContainerInterface) {
                                if (!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.ELECTRO.getId()).isEmpty())
                                    absorption = Math.max(absorption, 1);
                                if (!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty())
                                    absorption = Math.max(absorption, 2);
                                if (!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.PYRO.getId()).isEmpty())
                                    absorption = Math.max(absorption, 3);
                                if (!auraContainerInterface.er$getAuraContainer().getAura().get(Element.Category.CRYO.getId()).isEmpty())
                                    absorption = 4;
                            }
                        }
                        if (absorption != 0)
                            this.getEntityData().set(TravelerTornadoEntity.DATA_Absorption, absorption);
                    } else {
                        for (Entity entityiterator : this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
                            if (EntityHurtEvent.shouldHurt(owner, entityiterator) && entityiterator instanceof LivingEntity && !(this == entityiterator)) {
                                entityiterator.hurt(
                                        ElementSource.createDamageSource(type, owner, MemoryofRovingGalesItem.absorbElement(absorption)),
                                        (float) ((player.getAttributeValue(Attributes.ATTACK_DAMAGE)) * item.DamageMulti(30, 1)));
                            }
                        }
                    }
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