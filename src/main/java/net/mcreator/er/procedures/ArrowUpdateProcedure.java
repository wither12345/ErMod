package net.mcreator.er.procedures;

import net.wither.er.network.ErItemVariables;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.er.init.ErModParticleTypes;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.entity.TartagliaEntity;
import net.mcreator.er.entity.ElectroCicinEntity;

import java.util.ArrayList;

@EventBusSubscriber
public class ArrowUpdateProcedure {
	private static final ArrayList<Entity>[] entits = new ArrayList[7];

	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		for (int i = 0; i < 7; i++) {
			if (entits[i] == null)
				entits[i] = new ArrayList<Entity>();
		}
		Entity entity = event.getEntity();
		if (entity instanceof Projectile pro) {
			if (pro.getOwner() instanceof Stray) {
				entits[1].add(entity);
				entity.getPersistentData().putInt("Element", 2);
			}
			if (pro.getOwner() instanceof ElectroCicinEntity) {
				entits[3].add(entity);
				entity.getPersistentData().putInt("Element", 4);
			}
			if (pro.getOwner() instanceof TartagliaEntity) {
				entits[5].add(entity);
				entity.getPersistentData().putInt("Element", 6);
			} else if (entity instanceof WindCharge) {
				entity.getPersistentData().putInt("Element", 1);
			} else if (entity instanceof Arrow && pro.getOwner() instanceof Player) {
				ErItemVariables.PlayerVariables vars = entity.getData(ErItemVariables.PLAYER_VARIABLES);
				Item VisionItem = vars.Vision.getItem();
				if (VisionItem == ErModItems.ANEMO_VISION.get()) {
					entits[0].add(entity);
					entity.getPersistentData().putInt("Element", 1);
				} else if (VisionItem == ErModItems.CRYO_VISION.get()) {
					entits[1].add(entity);
					entity.getPersistentData().putInt("Element", 2);
				} else if (VisionItem == ErModItems.DENDRO_VISION.get()) {
					entits[2].add(entity);
					entity.getPersistentData().putInt("Element", 3);
				} else if (VisionItem == ErModItems.ELECTRO_VISION.get()) {
					entits[3].add(entity);
					entity.getPersistentData().putInt("Element", 4);
				} else if (VisionItem == ErModItems.GEO_VISION.get()) {
					entits[4].add(entity);
					entity.getPersistentData().putInt("Element", 5);
				} else if (VisionItem == ErModItems.HYDRO_VISION.get()) {
					entits[5].add(entity);
					entity.getPersistentData().putInt("Element", 6);
				} else if (VisionItem == ErModItems.PYRO_VISION.get()) {
					entits[6].add(entity);
					entity.getPersistentData().putInt("Element", 7);
				}
			}
			if (entity instanceof SmallFireball || entity instanceof LargeFireball) {
				entits[6].add(entity);
				entity.getPersistentData().putInt("Element", 7);
			}
		}
	}

	@SubscribeEvent
	public static void OnTick(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		if (entity.getPersistentData().getInt("Element") != 0 && entity instanceof Projectile && entity.level() instanceof ServerLevel _level)
			_level.sendParticles(getParticle(entity.getPersistentData().getInt("Element") - 1), entity.getX(), entity.getY(), entity.getZ(), 1, 0, 0, 0, 0);
	}

	public static SimpleParticleType getParticle(int i) {
		if (i == 0)
			return ErModParticleTypes.SMALL_ANEMO_PARTICLE.get();
		if (i == 1)
			return ErModParticleTypes.SMALL_CRYO_PARTICLE.get();
		if (i == 2)
			return ErModParticleTypes.SMALL_DENDRO_PARTICLE.get();
		if (i == 3)
			return ErModParticleTypes.SMALL_ELECTRO_PARTICLE.get();
		if (i == 4)
			return ErModParticleTypes.SMALL_GEO_PARTICLE.get();
		if (i == 5)
			return ErModParticleTypes.SMALL_HYDRO_PARTICLE.get();
		return ErModParticleTypes.SMALL_PYRO_PARTICLE.get();
	}
}