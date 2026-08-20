package net.mcreator.er.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.wither.er.entity.outcrop.Blossom;

import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.BlockTags;

import net.mcreator.er.procedures.BlossomOfWealthTickingProcedure;
import net.mcreator.er.init.ErModEntities;

public class BlossomOfWealthEntity extends Blossom {
	ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.parse("er:outcrop/wealth")) ;

	public BlossomOfWealthEntity(EntityType<BlossomOfWealthEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		BlossomOfWealthTickingProcedure.execute(this.level(), this);
	}

	public static void init(RegisterSpawnPlacementsEvent event) {
		event.register(ErModEntities.BLOSSOM_OF_WEALTH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && world.getRawBrightness(pos, 0) > 8), RegisterSpawnPlacementsEvent.Operation.REPLACE);
	}

	@Override
	public LootTable getLoot() {
		return this.level().getServer().reloadableRegistries().getLootTable(key);
	}
}