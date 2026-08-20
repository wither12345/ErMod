package net.mcreator.er.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class TrounceBlossomEntity extends PathfinderMob {
	public final AnimationState animationState0 = new AnimationState();
	private LootTable loot;
	private int omenLevel = 0;
	private int restTime = -1;

	public TrounceBlossomEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ErModEntities.TROUNCE_BLOSSOM.get(), world);
	}

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
			this.setLootTable(this.level().getServer().getLootData().getLootTable((new ResourceLocation(tag.getString("OutcropLoot")))));
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
	protected void doPush(Entity entityIn) {
	}

	@Override
	protected void pushEntities() {
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
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