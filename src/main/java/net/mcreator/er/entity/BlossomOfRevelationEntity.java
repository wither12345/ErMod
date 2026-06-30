package net.mcreator.er.entity;

import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.procedures.BlossomOfWealthTickingProcedure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.network.PlayMessages;
import net.wither.er.outcrop.Blossom;

public class BlossomOfRevelationEntity extends Blossom {
    ResourceLocation location = new ResourceLocation("er:outcrop/revelation");
	public BlossomOfRevelationEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ErModEntities.BLOSSOM_OF_REVELATION.get(), world);
	}

	public BlossomOfRevelationEntity(EntityType<BlossomOfRevelationEntity> type, Level world) {
		super(type, world);
		xpReward = 0;
		setNoAi(true);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		BlossomOfWealthTickingProcedure.execute(this.level(), this);
	}

	public static void init() {
		SpawnPlacements.register(ErModEntities.BLOSSOM_OF_REVELATION.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && world.getRawBrightness(pos, 0) > 8));

	}

	@Override
	public LootTable getLoot() {
		return this.level().getServer().getLootData().getLootTable(location);
	}
}