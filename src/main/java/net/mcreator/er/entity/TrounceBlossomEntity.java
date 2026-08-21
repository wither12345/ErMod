package net.mcreator.er.entity;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Difficulty;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.init.ErModEntities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

public class TrounceBlossomEntity extends PathfinderMob {
    public static final LootContextParam<Double> BLOSSOM_MULTI = new LootContextParam<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "multi"));
    public final AnimationState animationState0 = new AnimationState();
	private LootTable loot;
	private int omenLevel = 0;
	private int restTime = -1;

	public TrounceBlossomEntity(EntityType<TrounceBlossomEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.restTime > 0) {
			this.restTime--;
			if (this.restTime == 0)
				this.discard();
		}
		if (this.level().isClientSide()) {
			this.animationState0.animateWhen(this.getPersistentData().getBoolean("Opened"), this.tickCount);
		}
	}

	public void setLootTable(LootTable loot) {
		this.loot = loot;
	}

	public LootTable getLoot() {
		return loot;
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.getLoot() != null) {
			tag.putString("OutcropLoot", this.getLoot().getLootTableId().toString());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("OutcropLoot")) {
			this.setLootTable(this.level().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse(tag.getString("OutcropLoot")))));
		}
	}

	@Override
	public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
		this.getPersistentData().putBoolean("Opened", true);
		if (!this.level().isClientSide()) {
			CompoundTag tag = this.getPersistentData().getCompound("OpenedPlayers");
			if (tag.contains(player.getStringUUID())) {
				return InteractionResult.FAIL;
			}
			tag.putBoolean(player.getStringUUID(), true);
			this.getPersistentData().put("OpenedPlayers", tag);
			this.restTime = 200;
			if (loot == null)
				return InteractionResult.FAIL;
			LootParams.Builder builder = new LootParams.Builder((ServerLevel) this.level());
			builder.withOptionalParameter(LootContextParams.THIS_ENTITY, this);
            ItemStack itemStack = player.getMainHandItem();
            if(itemStack.is(ErModItems.FRAGILE_RESIN.get())) {
                builder.withOptionalParameter(TrounceBlossomEntity.BLOSSOM_MULTI, 3d);
                itemStack.shrink(1);
            } else if (itemStack.is(ErModItems.ORIGINAL_RESIN.get())) {
                builder.withOptionalParameter(TrounceBlossomEntity.BLOSSOM_MULTI, 1d);
                itemStack.shrink(1);
            }
            else
                builder.withOptionalParameter(TrounceBlossomEntity.BLOSSOM_MULTI, 0.4);
			builder.withLuck(player.getLuck());
			ObjectArrayList<ItemStack> loots = loot.getRandomItems(builder.create(LootContextParamSets.EMPTY));
			for (ItemStack lootItem : loots) {
				ItemHandlerHelper.giveItemToPlayer(player, lootItem);
			}
		}
		return InteractionResult.SUCCESS;
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
		event.register(ErModEntities.TROUNCE_BLOSSOM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
				RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.STEP_HEIGHT, 0.6);
		return builder;
	}

	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		if (damagesource.getEntity() != null)
			return false;
		return super.hurt(damagesource, amount);
	}

	public void setOmenLevel(int level) {
		this.omenLevel = level;
	}

	public int getOmenLevel() {
		return omenLevel;
	}
}