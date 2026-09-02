/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.wither.er.block.entity.WhopperflowerCropEntity;
import net.wither.er.block.entity.StorageDeviceEntity;
import net.wither.er.block.entity.LinkMechanismBaseEntity;
import net.wither.er.block.entity.BurningDirtEntity;
import net.wither.er.block.entity.AmbientBollardEntity;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;

import net.mcreator.er.block.entity.StatueofTheSevenCoreBlockEntity;
import net.mcreator.er.ErMod;

public class ErModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ErMod.MODID);
	public static final RegistryObject<BlockEntityType<StatueofTheSevenCoreBlockEntity>> STATUEOF_THE_SEVEN_CORE = register("statueof_the_seven_core", ErModBlocks.STATUEOF_THE_SEVEN_CORE, StatueofTheSevenCoreBlockEntity::new);
	// Start of user code block custom block entities
	public static final RegistryObject<BlockEntityType<BurningDirtEntity>> BURNING_DIRT = register("burning_dirt", ErModBlocks.BURNING_DIRT, BurningDirtEntity::new);
	public static final RegistryObject<BlockEntityType<WhopperflowerCropEntity>> WHOPPERFLOWER_CROP = register("whopperflower_crop", ErModBlocks.WHOPPERFLOWER_CROP, WhopperflowerCropEntity::new);
	public static final RegistryObject<BlockEntityType<LinkMechanismBaseEntity>> LINK_MECHANISM_ENTITY = register("link_mechanism_entity", ErModBlocks.LINK_MECHANISM_BASE, LinkMechanismBaseEntity::new);
	public static final RegistryObject<BlockEntityType<StorageDeviceEntity>> STORAGE_DEVICE = register("storage_device", ErModBlocks.STORAGE_DEVICE_BASE, StorageDeviceEntity::new);
	public static final RegistryObject<BlockEntityType<AmbientBollardEntity>> AMBIENT_BOLLARD = register("ambient_bollard", ErModBlocks.AMBIENT_BOLLARD, AmbientBollardEntity::new);

	// End of user code block custom block entities
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<T> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}