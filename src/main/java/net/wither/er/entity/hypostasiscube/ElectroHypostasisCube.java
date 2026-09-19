package net.wither.er.entity.hypostasiscube;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public class ElectroHypostasisCube extends HypostasisCube {
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.PURPLE, ServerBossEvent.BossBarOverlay.PROGRESS);

    public int clampCd = 0;
    public int fistCd = 0;
    public int fingerCd = 0;
    public int shootCd = 0;
    public int drillCd = 0;
    public ElectroHypostasisCube(EntityType<? extends HypostasisCube> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tryRespawn() {
        for(int i = 0; i < this.respawnCounter; i ++) {
            ElectroCubeCrystal cubeCrystal = new ElectroCubeCrystal(this);
            cubeCrystal.moveTo(this.position().add(3 * Math.cos(i * Math.PI * 2 / 3), 0, 3 * Math.sin(i * Math.PI * 2 / 3)));
            this.level().addFreshEntity(cubeCrystal);
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ClampGoal(this));
        this.goalSelector.addGoal(2, new FingerGuessGoal(this));
        this.goalSelector.addGoal(2, new FistGoal(this));
        this.goalSelector.addGoal(2, new DrillGoal(this));
        this.goalSelector.addGoal(2, new ShootingGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        int cdCount = 0;
        if(this.drillCd > 0) {
            cdCount++;
            this.drillCd--;
        }
        if(this.shootCd > 0) {
            cdCount++;
            this.shootCd--;
        }
        if(this.fingerCd > 0 && cdCount ++ < 2) {
            this.fingerCd--;
        }
        if(this.fistCd > 0 && cdCount ++ < 2) {
            this.fistCd--;
        }
        if(this.clampCd > 0 && cdCount ++ < 2) {
            this.clampCd--;
        }
    }

    @Override
    public LootTable getLoot() {
        return this.level().getServer().getLootData().getLootTable(this.getLootTable());
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }
}
