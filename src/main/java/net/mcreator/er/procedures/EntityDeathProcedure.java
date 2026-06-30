package net.mcreator.er.procedures;

import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.wither.er.entity.EnergyOrb;
import net.wither.er.outcrop.Blossom;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

@EventBusSubscriber
public class EntityDeathProcedure {
	private static final ResourceKey<LootTable> artifactLoot1  =ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("er:artifact/star1")) ;
	private static final ResourceKey<LootTable> artifactLoot2  =ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("er:artifact/star2")) ;
	private static final ResourceKey<LootTable> artifactLoot3  =ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("er:artifact/star3")) ;

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event) {
		if (event != null) {
            execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity(), event.getSource().getEntity());
        }
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		execute(null, world, x, y, z, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
		if (entity == null)
			return;
		ItemStack artifact_drop = ItemStack.EMPTY;
		ItemStack item_drop  = new ItemStack(ErModItems.MORA.get());
		item_drop.setCount((int) Math.min((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 2, 20));
		if (world instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item_drop);
			entityToSpawn.setPickUpDelay(10);
			_level.addFreshEntity(entityToSpawn);
			Entity _entityToSpawn = ErModEntities.ENERGY_ORB.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
			if (_entityToSpawn instanceof EnergyOrb orb) {
				orb.setType(0, 6);
				orb.push(0.1 - Math.random() * 0.2, 0.3, 0.1 - Math.random() * 0.2);
			}
		}
		if (world instanceof ServerLevel _level && Math.random() < 0.01 * (1 + (sourceentity instanceof Player ? ((LivingEntity) sourceentity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.LUCK).getValue() : 0))) {
			if (Math.random() < 0.8) {
				dropArtifact(_level, entity.getOnPos(), artifactLoot1);
			} else if (Math.random() < 0.5) {
				dropArtifact(_level, entity.getOnPos(), artifactLoot2);
			} else {
				dropArtifact(_level, entity.getOnPos(), artifactLoot3);
			}
		}
		if (sourceentity == null)
			return;
		ItemStack hand_item = sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
		int GreedLevel = hand_item.getEnchantmentLevel(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("er:greed"))));
		if (GreedLevel != 0 && Math.random() < GreedLevel * 0.18) {
			item_drop = new ItemStack(ErModItems.MORA.get());
			item_drop.setCount(GreedLevel);
			if (world instanceof ServerLevel _level) {
				ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item_drop);
				entityToSpawn.setPickUpDelay(10);
				_level.addFreshEntity(entityToSpawn);
			}
		}
	}

	private static void dropArtifact(ServerLevel level, BlockPos pos, ResourceKey<LootTable> tableKey){
		for (ItemStack itemStack : level.getServer().reloadableRegistries().getLootTable(tableKey)
					.getRandomItems(new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
							.withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos)).create(LootContextParamSets.EMPTY))) {
			ItemEntity entityToSpawn = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
			entityToSpawn.setPickUpDelay(10);
			level.addFreshEntity(entityToSpawn);
		}

	}

	@SubscribeEvent
	public static void onEntityLeave(EntityLeaveLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity.level() instanceof ServerLevel serverLevel && entity.getPersistentData().contains("BlossomOwner")) {
			final Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
			List<Blossom> ent_found = serverLevel.getEntitiesOfClass(Blossom.class, new AABB(center, center).inflate(32), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();
			for (Blossom entity_iterator : ent_found) {
				if (entity_iterator.getUUID().equals(entity.getPersistentData().getUUID("BlossomOwner"))) {
					entity_iterator.DeclineMobLeft();
					break;
				}
			}
		}
	}
}