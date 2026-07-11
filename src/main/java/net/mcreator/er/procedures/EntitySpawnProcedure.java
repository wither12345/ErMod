package net.mcreator.er.procedures;

import net.wither.er.network.ErItemVariables;
import net.wither.er.item.Vision;
import net.wither.er.init.ElementRegistry;
import net.wither.er.elements.ElementSourceInterface;

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
				((ElementSourceInterface) pro).er$setElement(ElementRegistry.CRYO.get(), ResourceLocation.parse("er:projectile"), 1);
			}
			if (pro.getOwner() instanceof ElectroCicinEntity) {
				((ElementSourceInterface) pro).er$setElement(ElementRegistry.ELECTRO.get(), ResourceLocation.parse("er:projectile"), 1);
			}
			if (pro.getOwner() instanceof TartagliaEntity) {
				((ElementSourceInterface) pro).er$setElement(ElementRegistry.HYDRO.get(), ResourceLocation.parse("er:projectile"), 1);
			} else if (entity instanceof WindCharge || entity instanceof BreezeWindCharge) {
				((ElementSourceInterface) pro).er$setElement(ElementRegistry.ANEMO.get(), ResourceLocation.parse("er:projectile"), 1);
			} else if (entity instanceof Arrow && pro.getOwner() instanceof Player player && player.getData(ErItemVariables.PLAYER_VARIABLES).Vision.getItem() instanceof Vision vision) {
				((ElementSourceInterface) pro).er$setElement(vision.getCategory().getDefault(), ResourceLocation.parse("er:projectile"), 1);
			}
			if (entity instanceof SmallFireball || entity instanceof LargeFireball) {
				((ElementSourceInterface) pro).er$setElement(ElementRegistry.PYRO.get(), ResourceLocation.parse("er:projectile"), 1);
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
				if (entity instanceof LivingEntity _livingEntity10 && _livingEntity10.getAttributes().hasAttribute(ErModAttributes.CRYO_RES))
					_livingEntity10.getAttribute(ErModAttributes.CRYO_RES).setBaseValue(Math.pow(25, 2));
			} else if (entity instanceof EnderMan) {
				if (entity instanceof LivingEntity _livingEntity12 && _livingEntity12.getAttributes().hasAttribute(ErModAttributes.HYDRO_RES))
					_livingEntity12.getAttribute(ErModAttributes.HYDRO_RES).setBaseValue((-100));
			} else if (entity instanceof Blaze || entity instanceof FlamingFlowerEntity) {
				if (entity instanceof LivingEntity _livingEntity15 && _livingEntity15.getAttributes().hasAttribute(ErModAttributes.PYRO_RES))
					_livingEntity15.getAttribute(ErModAttributes.PYRO_RES).setBaseValue(200);
			} else if (entity instanceof MistFlowerEntity) {
				if (entity instanceof LivingEntity _livingEntity17 && _livingEntity17.getAttributes().hasAttribute(ErModAttributes.CRYO_RES))
					_livingEntity17.getAttribute(ErModAttributes.CRYO_RES).setBaseValue(200);
			}
		}
	}
}