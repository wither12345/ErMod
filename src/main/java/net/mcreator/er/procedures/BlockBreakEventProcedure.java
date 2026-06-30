package net.mcreator.er.procedures;

import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.BlockTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;

import net.mcreator.er.init.ErModItems;

import javax.annotation.Nullable;

import java.util.List;

@EventBusSubscriber
public class BlockBreakEventProcedure {
	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		execute(event, event.getLevel(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getState(), event.getPlayer());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		execute(null, world, x, y, z, blockstate, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
		if (entity == null)
			return;
		BlockState b = Blocks.AIR.defaultBlockState();
		ItemStack breaking_Item = ItemStack.EMPTY;
		ItemStack item_spawn = ItemStack.EMPTY;
		breaking_Item = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
		b = blockstate;
		if (breaking_Item.getItem() == ErModItems.PYRO_HOE.get()) {
			if (blockstate.getBlock() instanceof CropBlock) {
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.FLAME, x, y, z, 5, 0.5, 0.5, 0.5, 0);
				if (Math.random() < 0.1) {
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(Items.BONE_MEAL));
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
				}
				if (event instanceof ICancellableEvent _cancellable) {
					_cancellable.setCanceled(true);
				}
			}
		} else if (breaking_Item.getItem() == ErModItems.ELECTRO_HOE.get()) {
			if (blockstate.getBlock() instanceof CropBlock) {
				BlockPos pos = ((BlockEvent.BreakEvent) event).getPos();
				Block block = blockstate.getBlock();
				List<ItemStack> drops = Block.getDrops(blockstate, (ServerLevel) world, pos, null, entity, breaking_Item);
				for (ItemStack stack : drops) {
					ItemEntity entityToSpawn = new ItemEntity((ServerLevel) world, x, y, z, stack);
					entityToSpawn.setPickUpDelay(10);
					entityToSpawn.setUnlimitedLifetime();
					((ServerLevel) world).addFreshEntity(entityToSpawn);
				}
			}
		} else if (breaking_Item.getItem() == ErModItems.PYRO_PICKAXE.get()) {
			if (blockstate.is(BlockTags.create(ResourceLocation.parse("er:electro_block")))) {
				for (int index0 = 0; index0 < 3; index0++) {
					for (int index1 = 0; index1 < 3; index1++) {
						for (int index2 = 0; index2 < 3; index2++) {
							if (0 <= 0 && world.getBlockState(BlockPos.containing(x + index0 - 1, y + index1 - 1, z + index2 - 1)).getDestroySpeed(world, BlockPos.containing(x + index0 - 1, y + index1 - 1, z + index2 - 1)) >= 0) {
								{
									BlockPos _pos = BlockPos.containing(x + index0 - 1, y + index1 - 1, z + index2 - 1);
									Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
									world.destroyBlock(_pos, false);
								}
							}
						}
					}
				}
				if (world instanceof ServerLevel _level)
					_level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 0, 0, 0, 0, 0);
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(x, y, z), BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")), SoundSource.BLOCKS, 1, 1);
					} else {
						_level.playLocalSound(x, y, z, BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse("entity.generic.explode")), SoundSource.BLOCKS, 1, 1, false);
					}
				}
			} else {
				BlockPos pos = ((BlockEvent.BreakEvent) event).getPos();
				Block block = blockstate.getBlock();
				List<ItemStack> drops = Block.getDrops(blockstate, (ServerLevel) world, pos, null, entity, breaking_Item);
				for (ItemStack stack : drops) {
					if (world instanceof Level _level14 && _level14.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), _level14).isPresent()) {
						item_spawn = (world instanceof Level _lvlSmeltResult
								? _lvlSmeltResult.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), _lvlSmeltResult).map(recipe -> recipe.value().getResultItem(_lvlSmeltResult.registryAccess()).copy())
										.orElse(ItemStack.EMPTY)
								: ItemStack.EMPTY);
						item_spawn.setCount((int) stack.getCount());
					} else {
						item_spawn = stack;
					}
					if (world instanceof ServerLevel _level) {
						ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item_spawn);
						entityToSpawn.setPickUpDelay(10);
						_level.addFreshEntity(entityToSpawn);
					}
					if (world instanceof ServerLevel _level)
						_level.sendParticles(ParticleTypes.FLAME, x, y, z, 10, 1, 1, 1, 0);
				}
				world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				if (event instanceof ICancellableEvent _cancellable) {
					_cancellable.setCanceled(true);
				}
			}
		}
		if (entity.getPersistentData().contains("BlossomOwner")) {
			if (event instanceof ICancellableEvent _cancellable) {
				_cancellable.setCanceled(true);
			}
		}
	}
}