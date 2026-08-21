package net.wither.er.entity.outcrop;

import net.mcreator.er.ErMod;
import net.mcreator.er.entity.TrounceBlossomEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.network.SyncLevelData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static java.util.Comparator.comparingInt;
import static net.mcreator.er.EntityHurtEvent.getEntityLevel;
import static net.mcreator.er.ErMod.LOGGER;
import static net.mcreator.er.init.ErModEntities.TROUNCE_BLOSSOM;
import static net.mcreator.er.init.ErModMobEffects.DISORDER_OMEN;
import static net.mcreator.er.procedures.ApplyErlevelProcedure.execute;
import static net.minecraft.core.BlockPos.containing;
import static net.minecraft.network.syncher.EntityDataSerializers.INT;
import static net.minecraft.network.syncher.SynchedEntityData.defineId;
import static net.minecraft.util.Mth.randomBetweenInclusive;
import static net.minecraft.util.RandomSource.create;
import static net.minecraft.world.BossEvent.BossBarColor.YELLOW;
import static net.minecraft.world.BossEvent.BossBarOverlay.PROGRESS;
import static net.minecraft.world.InteractionResult.sidedSuccess;
import static net.minecraft.world.effect.MobEffects.BAD_OMEN;
import static net.minecraft.world.entity.MobSpawnType.MOB_SUMMONED;
import static net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import static net.minecraft.world.entity.ai.attributes.Attributes.*;
import static net.wither.er.entity.outcrop.OutcropWave.EntityWithModifier;
import static net.wither.er.entity.outcrop.OutcropWaveDataListener.getAllWaves;

public abstract class Blossom extends Mob {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), YELLOW, PROGRESS);
	public static final EntityDataAccessor<Integer> DATA_OMEN_LEVEL = defineId(Blossom.class, INT);
	private final ArrayList<OutcropWave> waves = new ArrayList<OutcropWave>();
	private int wave_count = 0;
	private int mob_left = 0;

	protected Blossom(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		xpReward = 0;
		setNoAi(true);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_OMEN_LEVEL, 0);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("DataOmenLevel", this.entityData.get(DATA_OMEN_LEVEL));
		compound.putInt("wave_count", wave_count);
		compound.putInt("mob_left", mob_left);
        if(!this.waves.isEmpty()){
            int i = 0;
            CompoundTag waveTag = new CompoundTag();
            for(OutcropWave wave : waves)
                waveTag.putString(String.valueOf(i ++), wave.getLocation().toString());
            compound.put("waves", waveTag);
        }
	}

	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DataOmenLevel"))
			this.entityData.set(DATA_OMEN_LEVEL, compound.getInt("DataOmenLevel"));
		if (compound.contains("wave_count"))
			wave_count = compound.getInt("wave_count");
		if (compound.contains("mob_left"))
			mob_left = compound.getInt("mob_left");
        if(compound.contains("waves")){
            CompoundTag waveTag = compound.getCompound("waves");
            int i = 0 ;
            while(waveTag.contains(String.valueOf(i))){
                ResourceLocation location = new ResourceLocation(waveTag.getString(String.valueOf(i ++)));
                OutcropWave newWave = OutcropWaveDataListener.getByLocation(location);
                if(newWave != null)
                    this.waves.add(newWave);
            }
        }
	}

	@Override
	public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
		InteractionResult result = sidedSuccess(this.level().isClientSide());
		super.mobInteract(player, hand);
		if (player instanceof ServerPlayer serverPlayer) {
			this.bossInfo.addPlayer(serverPlayer);
			if (waves.isEmpty()) {
				if (player.hasEffect(DISORDER_OMEN.get())) {
					if (player.hasEffect(BAD_OMEN) && !player.level().isClientSide()) {
                        int amp_bad = player.getEffect(BAD_OMEN).getAmplifier();
                        player.addEffect(new MobEffectInstance(DISORDER_OMEN.get(), 18000 * amp_bad + 18000, amp_bad));
                        player.removeEffect(BAD_OMEN);
                    }
					this.getEntityData().set(DATA_OMEN_LEVEL, player.getEffect(DISORDER_OMEN.get()).getAmplifier() + 1);
					this.getPersistentData().putInt("erLevel", this.getPersistentData().getInt("erLevel") + this.getEntityData().get(DATA_OMEN_LEVEL));
					ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SyncLevelData(this.getId(), this.getPersistentData().getInt("erLevel")));
				} else if (player.hasEffect(BAD_OMEN)) {
                    int amp = player.getEffect(BAD_OMEN).getAmplifier();
					if (!player.level().isClientSide())
						player.addEffect(new MobEffectInstance(DISORDER_OMEN.get(), 18000 * amp + 18000, amp));
					this.getEntityData().set(DATA_OMEN_LEVEL, amp + 1);
					player.removeEffect(BAD_OMEN);
					this.getPersistentData().putInt("erLevel", this.getPersistentData().getInt("erLevel") + this.getEntityData().get(DATA_OMEN_LEVEL));
					ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SyncLevelData(this.getId(), this.getPersistentData().getInt("erLevel")));
				}
				if (this.level() instanceof ServerLevel) {
					this.roll_waves(this.getQuality(), this.getWavesCount());
				}
				if (!waves.isEmpty()) {
					Collection<EntityWithModifier> pools = waves.get(wave_count).getPools();
					if (this.level() instanceof ServerLevel serverLevel) {
						for (EntityWithModifier entity : pools) {
							entity.spawn(serverLevel, (int) this.getX(), (int) this.getY(), (int) this.getZ(), 4, this.getPersistentData().getInt("erLevel"), this);
						}
					}
				}
			}
		}
		return result;
	}

	private void roll_waves(int max_quality, int count) {
		int min_quality = max_quality / 4;
		ArrayList<OutcropWave> all_waves = getAllWaves();
		int l = 0, r = all_waves.size() - 1;
		while (l < r) {
			int mid = (l + r - 1) / 2;
			if (all_waves.get(mid).quality < min_quality) {
				l = mid + 1;
			} else {
				r = mid;
			}
		}
		int left = l;
        r = all_waves.size() - 1;
		while (l < r) {
			int mid = l + (r - l + 1) / 2;
			if (all_waves.get(mid).quality > max_quality) {
				r = mid - 1;
			} else {
				l = mid;
			}
		}
		int right = r;
		if (r < l) {
			LOGGER.error("cannot find any waves for blossom!");
			return;
		}
		for (int i = 0; i < count * 2; i++) {
			waves.add(all_waves.get(randomBetweenInclusive(create(), left, right)));
		}
		waves.sort(comparingInt(wave -> wave.quality));
		Iterator<OutcropWave> iterator = waves.iterator();
		int cnt = 0;
		boolean first = true;
		while (iterator.hasNext()) {
			OutcropWave wave = iterator.next();
			if (wave.quality > max_quality || waves.size() > count || (wave.quality == max_quality && first && Math.random() < 0.5))
				iterator.remove();
			else {
				max_quality -= wave.quality;
				cnt++;
			}
			first = false;
		}
	}

	private int getQuality() {
        return switch (this.getEntityData().get(DATA_OMEN_LEVEL)){
            case 0 -> 200;
            case 1 -> 300;
            case 2 -> 500;
            case 3 -> 750;
            case 4 -> 1000;
            default -> 2500;
        };
	}

	private int getWavesCount() {
       return switch (this.getEntityData().get(DATA_OMEN_LEVEL)){
            case 0, 1, 2 -> 2;
            case 3, 4 -> 3;
           default -> 5;
        };
	}

	public abstract LootTable getLoot();

	public void spawn(ServerLevel level, int x, int y, int z, int range, int entity_level) {
		ArrayList<OutcropWave> waves = getAllWaves();
		int index = randomBetweenInclusive(create(), 0, waves.size() - 1);
		OutcropWave wave = waves.get(index);
	}

	public static Builder createAttributes() {
		Builder builder = createMobAttributes();
		builder = builder.add(MOVEMENT_SPEED, 0);
		builder = builder.add(MAX_HEALTH, 100);
		builder = builder.add(ARMOR, 0);
		builder = builder.add(ATTACK_DAMAGE, 0);
		builder = builder.add(FOLLOW_RANGE, 0);
		return builder;
	}


	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		if (damagesource.getEntity() != null)
			return false;
		return super.hurt(damagesource, amount);
	}

	@Override
	public boolean ignoreExplosion() {
		return true;
	}

	@Override
	public boolean fireImmune() {
		return true;
	}

	@Override
	public void baseTick() {
		super.baseTick();
	}

	public void addMobLeft(int count) {
		mob_left += count;
	}


	@Override
	public void stopSeenByPlayer(@NotNull ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	public void DeclineMobLeft() {
		mob_left--;
		if (mob_left == 0) {
			wave_count++;
			if (this.level() instanceof ServerLevel serverLevel) {
				if (wave_count >= waves.size()) {
					TrounceBlossomEntity trounceBlossom = TROUNCE_BLOSSOM.get().spawn(serverLevel, containing(this.getX(), this.getY(), this.getZ()), MOB_SUMMONED);
					if (trounceBlossom != null) {
						trounceBlossom.setLootTable(this.getLoot());
						trounceBlossom.setOmenLevel(this.entityData.get(DATA_OMEN_LEVEL));
						execute(trounceBlossom, getEntityLevel(this));
					}
					this.bossInfo.removeAllPlayers();
					this.discard();
				} else {
					this.bossInfo.setProgress(1 - (float) wave_count / waves.size());
					Collection<EntityWithModifier> pools = waves.get(wave_count).getPools();
					for (EntityWithModifier entity : pools) {
						entity.spawn(serverLevel, (int) this.getX(), (int) this.getY(), (int) this.getZ(), 4, this.getPersistentData().getInt("erLevel"), this);
					}
				}
			}
		}
	}
}