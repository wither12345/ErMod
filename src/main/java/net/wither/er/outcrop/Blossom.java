package net.wither.er.outcrop;

import net.mcreator.er.EntityHurtEvent;
import net.mcreator.er.ErMod;
import net.mcreator.er.entity.TrounceBlossomEntity;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModMobEffects;
import net.mcreator.er.procedures.ApplyErlevelProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wither.er.network.SyncLevelData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public abstract class Blossom extends Mob {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.YELLOW, ServerBossEvent.BossBarOverlay.PROGRESS);
	public static final EntityDataAccessor<Integer> DATA_OmenLevel = SynchedEntityData.defineId(Blossom.class, EntityDataSerializers.INT);
	private ArrayList<OutcropWave> waves = new ArrayList<OutcropWave>();
	private int wave_count = 0;
	private int mob_left = 0;

	protected Blossom(EntityType<? extends Mob> type, Level level) {
		super(type, level);
		xpReward = 0;
		setNoAi(true);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_OmenLevel, 0);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("DataOmenLevel", this.entityData.get(DATA_OmenLevel));
		compound.putInt("wave_count" , wave_count);
		compound.putInt("mob_left" , mob_left);
	}

	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("DataOmenLevel"))
			this.entityData.set(DATA_OmenLevel, compound.getInt("DataOmenLevel"));
		if (compound.contains("wave_count"))
			wave_count =  compound.getInt("wave_count");
		if (compound.contains("mob_left"))
			mob_left =  compound.getInt("mob_left");
	}
	@Override
	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
		super.mobInteract(sourceentity, hand);
		if(sourceentity instanceof ServerPlayer serverPlayer) {
			this.bossInfo.addPlayer(serverPlayer);
			if (waves.isEmpty()) {
				if (sourceentity.hasEffect(ErModMobEffects.DISORDER_OMEN)) {
					if (sourceentity.hasEffect(MobEffects.BAD_OMEN)) {
						if (!sourceentity.level().isClientSide())
							sourceentity.addEffect(
									new MobEffectInstance(ErModMobEffects.DISORDER_OMEN, 18000 * sourceentity.getEffect(MobEffects.BAD_OMEN).getAmplifier() + 18000,
											sourceentity.getEffect(MobEffects.BAD_OMEN).getAmplifier()));
					}
					this.getEntityData().set(Blossom.DATA_OmenLevel, sourceentity.getEffect(ErModMobEffects.DISORDER_OMEN).getAmplifier() + 1);
					this.getPersistentData().putInt("erLevel", this.getPersistentData().getInt("erLevel") + this.getEntityData().get(Blossom.DATA_OmenLevel));
					PacketDistributor.sendToAllPlayers(new SyncLevelData(this.getId(), this.getPersistentData().getInt("erLevel")));
				} else if (sourceentity.hasEffect(MobEffects.BAD_OMEN)) {
					if (!sourceentity.level().isClientSide())
						sourceentity.addEffect(
								new MobEffectInstance(ErModMobEffects.DISORDER_OMEN, 18000 * (sourceentity.getEffect(MobEffects.BAD_OMEN).getAmplifier()) + 18000, sourceentity.getEffect(MobEffects.BAD_OMEN).getAmplifier()));
					this.getEntityData().set(Blossom.DATA_OmenLevel, sourceentity.getEffect(MobEffects.BAD_OMEN).getAmplifier() + 1);
					sourceentity.removeEffect(MobEffects.BAD_OMEN);
					this.getPersistentData().putInt("erLevel", this.getPersistentData().getInt("erLevel") + this.getEntityData().get(Blossom.DATA_OmenLevel));
					PacketDistributor.sendToAllPlayers(new SyncLevelData(this.getId(), this.getPersistentData().getInt("erLevel")));

				}
				if (this.level() instanceof ServerLevel) {
					roll_waves(waves, getQuality(this.getEntityData().get(Blossom.DATA_OmenLevel)), getWavesCount(this.getEntityData().get(Blossom.DATA_OmenLevel)));
				}
				if (!waves.isEmpty()) {
					Collection<OutcropWave.EntityWithModifier> pools = waves.get(wave_count).getPools();
					if (this.level() instanceof ServerLevel serverLevel) {
						for (OutcropWave.EntityWithModifier entity : pools) {
							entity.spawn(serverLevel, (int) this.getX(), (int) this.getY(), (int) this.getZ(), 4, this.getPersistentData().getInt("erLevel"), this);
						}
					}
				}
			}
		}
		return retval;
	}

	private static void roll_waves(ArrayList<OutcropWave> wave_list, int max_quality , int count){
		int min_quality = max_quality / 4 ;
		ArrayList<OutcropWave> all_waves = OutcropWaveDataListener.getAllWaves();
		int l = 0 , r = all_waves.size() - 1 ;
        while (l < r) {
            int mid = (l + r - 1) / 2 ;
            if(all_waves.get(mid).quality < min_quality){
                l = mid + 1 ;
            }
            else {
                r = mid ;
            }
        }
        int left = l ;
		l = left ;
		r = all_waves.size() - 1 ;
        while (l < r) {
            int mid =  l + (r - l + 1) / 2;
            if(all_waves.get(mid).quality > max_quality){
                r = mid - 1 ;
            }
            else {
                l = mid ;
            }
        }
        int right = r ;
		if(r < l){
			ErMod.LOGGER.error("cannot find any waves for blossom!");
			return;
		}
		for(int i = 0 ; i < count * 2 ; i ++){
			wave_list.add(all_waves.get(Mth.randomBetweenInclusive(RandomSource.create(),left , right)));
		}
		wave_list.sort(Comparator.comparingInt(wave -> wave.quality));
		Iterator<OutcropWave> iterator = wave_list.iterator() ;
		int cnt = 0 ;
		boolean first = true ;
		while (iterator.hasNext()) {
			OutcropWave wave = iterator.next();
			if(wave.quality > max_quality || wave_list.size() > count ||(wave.quality == max_quality && first && Math.random() < 0.5))
				iterator.remove();
			else {
				max_quality -= wave.quality ;
				cnt ++ ;
			}
			first = false ;
		}
	}

	private int getQuality(int level){
		if(level == 0) return 200;
		if(level == 1) return 300;
		if(level == 2) return 500;
		if(level == 3) return 750;
		if(level == 4) return 1000;
		return 2500;
	}

	private int getWavesCount(int level){
		if(level <= 2) return 2 ;
		if(level <= 4) return 3 ;
		return 5 ;
	}

	public abstract LootTable getLoot() ;

	public void spawn(ServerLevel level, int x, int y, int z, int range, int entity_level) {
		ArrayList<OutcropWave> waves = OutcropWaveDataListener.getAllWaves();
		int index = Mth.randomBetweenInclusive(RandomSource.create(), 0, waves.size() - 1);
		OutcropWave wave = waves.get(index);
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
		builder = builder.add(Attributes.MAX_HEALTH, 100);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
		builder = builder.add(Attributes.FOLLOW_RANGE, 0);
		builder = builder.add(Attributes.STEP_HEIGHT, 0);
		return builder;
	}


	@Override
	public boolean hurt(DamageSource damagesource, float amount) {
		if(damagesource.getEntity() != null)
			return false;
		return super.hurt(damagesource,amount);
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
	public void baseTick() {
		super.baseTick();
	}

	public void addMobLeft(int count){
		mob_left += count ;
	}


	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	public void DeclineMobLeft(){
		mob_left -- ;
		if(mob_left == 0){
			wave_count ++ ;
			if(this.level() instanceof ServerLevel serverLevel) {
				if (wave_count >= waves.size()) {
					TrounceBlossomEntity trounceBlossom = ErModEntities.TROUNCE_BLOSSOM.get().spawn(serverLevel, BlockPos.containing(this.getX(), this.getY(), this.getZ()), MobSpawnType.MOB_SUMMONED);
                    if (trounceBlossom != null) {
                        trounceBlossom.setLootTable(this.getLoot());
						trounceBlossom.setOmenLevel(this.entityData.get(DATA_OmenLevel));
						ApplyErlevelProcedure.execute(trounceBlossom , EntityHurtEvent.getEntityLevel(this));
					}
					this.bossInfo.removeAllPlayers();
					this.discard();
				}
				else {
					this.bossInfo.setProgress(1 - (float) wave_count / waves.size());
					Collection<OutcropWave.EntityWithModifier> pools = waves.get(wave_count).getPools();
					for (OutcropWave.EntityWithModifier entity : pools) {
						entity.spawn(serverLevel, (int) this.getX(), (int) this.getY(), (int) this.getZ(), 4, this.getPersistentData().getInt("erLevel"), this);
					}
				}
			}
		}
	}
}