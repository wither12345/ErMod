/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.BuiltInRegistries;

import net.mcreator.er.block.entity.StatueofTheSevenCoreBlockEntity;
import net.mcreator.er.block.entity.BurningDirtBlockEntity;
import net.mcreator.er.ErMod;

@EventBusSubscriber
public class ErModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ErMod.MODID);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BurningDirtBlockEntity>> BURNING_DIRT = register("burning_dirt", ErModBlocks.BURNING_DIRT, BurningDirtBlockEntity::new);
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StatueofTheSevenCoreBlockEntity>> STATUEOF_THE_SEVEN_CORE = register("statueof_the_seven_core", ErModBlocks.STATUEOF_THE_SEVEN_CORE, StatueofTheSevenCoreBlockEntity::new);

	// Start of user code block custom block entities
	// End of user code block custom block entities
	private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String registryname, DeferredHolder<Block, Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BURNING_DIRT.get(), SidedInvWrapper::new);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, STATUEOF_THE_SEVEN_CORE.get(), SidedInvWrapper::new);
	}
}