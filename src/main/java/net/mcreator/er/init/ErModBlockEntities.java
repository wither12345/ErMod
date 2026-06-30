/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

import net.mcreator.er.block.entity.StatueofTheSevenCoreBlockEntity;
import net.mcreator.er.block.entity.BurningDirtBlockEntity;
import net.mcreator.er.ErMod;

public class ErModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ErMod.MODID);
	public static final RegistryObject<BlockEntityType<BurningDirtBlockEntity>> BURNING_DIRT = register("burning_dirt", ErModBlocks.BURNING_DIRT, BurningDirtBlockEntity::new);
	public static final RegistryObject<BlockEntityType<StatueofTheSevenCoreBlockEntity>> STATUEOF_THE_SEVEN_CORE = register("statueof_the_seven_core", ErModBlocks.STATUEOF_THE_SEVEN_CORE, StatueofTheSevenCoreBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}