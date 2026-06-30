package net.mcreator.er.procedures;

import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.BlockPos;

import net.mcreator.er.init.ErModBlocks;
import net.mcreator.er.entity.FlamingFlowerEntity;

import java.util.Comparator;

public class FlamingFlowerTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (world instanceof Level level && level.getGameTime() % 20 == 0 && entity.isAlive()) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if (!(entityiterator instanceof FlamingFlowerEntity) && entityiterator instanceof LivingEntity) {
						entityiterator.hurt(ElementSource.createDamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), entity,
								new ElementSource(ElementRegistry.PYRO.get(), new ResourceLocation("er:mob_attack"), 1, true)), 3);
					}
				}
			}
			for (int i = -1; i <= 1; i++)
				for (int j = -1; j <= 1; j++)
					for (int k = -1; k <= 1; k++)
						if ((world.getBlockState(BlockPos.containing(x + i, y + k, z + j))).getBlock() == Blocks.GRASS_BLOCK) {
							world.setBlock(BlockPos.containing(x + i, y + k, z + j), ErModBlocks.BURNING_DIRT.get().defaultBlockState(), 3);
							if (!world.isClientSide()) {
								BlockPos _bp = BlockPos.containing(x + i, y + k, z + j);
								BlockEntity _blockEntity = world.getBlockEntity(_bp);
								BlockState _bs = world.getBlockState(_bp);
								if (_blockEntity != null) {
									_blockEntity.getPersistentData().putString("UUID", entity.getStringUUID());
								}
								if (world instanceof Level _level)
									_level.sendBlockUpdated(_bp, _bs, _bs, 3);
							}
						}
		}
	}
}