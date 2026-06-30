package net.mcreator.er.procedures;

import net.wither.er.network.ErItemVariables;

import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.entity.projectile.windcharge.BreezeWindCharge;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.entity.TartagliaEntity;
import net.mcreator.er.entity.MistFlowerEntity;
import net.mcreator.er.entity.FlamingFlowerEntity;
import net.mcreator.er.entity.ElectroCicinEntity;

import javax.annotation.Nullable;

@EventBusSubscriber
public class EntitySpawnProcedure {
	@SubscribeEvent
	public static void onEntitySpawned(EntityJoinLevelEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Projectile pro) {
			if (pro.getOwner() instanceof Stray) {
				entity.getPersistentData().putDouble("Element", 2);
			}
			if (pro.getOwner() instanceof ElectroCicinEntity) {
				entity.getPersistentData().putDouble("Element", 4);
			}
			if (pro.getOwner() instanceof TartagliaEntity) {
				entity.getPersistentData().putDouble("Element", 6);
			} else if (entity instanceof WindCharge || entity instanceof BreezeWindCharge) {
				entity.getPersistentData().putDouble("Element", 1);
			} else if (entity instanceof Arrow && pro.getOwner() instanceof Player) {
				if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.ANEMO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 1);
				} else if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.CRYO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 2);
				} else if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.DENDRO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 3);
				} else if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.ELECTRO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 4);
				} else if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.GEO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 5);
				} else if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.HYDRO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 6);
				} else if (pro.getOwner().getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() == ErModItems.PYRO_VISION.get()) {
					entity.getPersistentData().putDouble("Element", 7);
				}
			}
			if (entity instanceof SmallFireball || entity instanceof LargeFireball) {
				entity.getPersistentData().putDouble("Element", 7);
			}
		} else if (entity instanceof LivingEntity && !entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("er:no_level")))) {
			int level = 1;
			if (entity.getPersistentData().getInt("erLevel") == 0 && !(entity instanceof Player) && entity.level() instanceof ServerLevel) {
				BlockPos pos = entity.blockPosition();
				int level_time = (int) (entity.level().getChunkAt(pos).getInhabitedTime() / 72000);
				int spawn_distance = (int) Math.sqrt(Math.pow(entity.getX() - entity.level().getLevelData().getSpawnPos().getX(), 2) + Math.pow(entity.getZ() - entity.level().getLevelData().getSpawnPos().getZ(), 2));
				level = Mth.clamp(level_time + spawn_distance / 128, spawn_distance / 256, Math.min(spawn_distance / 32, 89)) + 1;
				ApplyErlevelProcedure.execute(entity, level);
			}
			if (entity instanceof Stray) {
				if (entity instanceof LivingEntity _livingEntity31 && _livingEntity31.getAttributes().hasAttribute(ErModAttributes.CRYO_RES))
					_livingEntity31.getAttribute(ErModAttributes.CRYO_RES).setBaseValue(Math.pow(25, 2));
			} else if (entity instanceof EnderMan) {
				if (entity instanceof LivingEntity _livingEntity33 && _livingEntity33.getAttributes().hasAttribute(ErModAttributes.HYDRO_RES))
					_livingEntity33.getAttribute(ErModAttributes.HYDRO_RES).setBaseValue((-100));
			} else if (entity instanceof Blaze || entity instanceof FlamingFlowerEntity) {
				if (entity instanceof LivingEntity _livingEntity36 && _livingEntity36.getAttributes().hasAttribute(ErModAttributes.PYRO_RES))
					_livingEntity36.getAttribute(ErModAttributes.PYRO_RES).setBaseValue(200);
			} else if (entity instanceof MistFlowerEntity) {
				if (entity instanceof LivingEntity _livingEntity38 && _livingEntity38.getAttributes().hasAttribute(ErModAttributes.CRYO_RES))
					_livingEntity38.getAttribute(ErModAttributes.CRYO_RES).setBaseValue(200);
			}
		}
	}
}