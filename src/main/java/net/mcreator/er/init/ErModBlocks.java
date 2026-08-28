/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.wither.er.block.WhopperflowerCrop;
import net.wither.er.block.StorageDevice;
import net.wither.er.block.LinkMechanismBase;
import net.wither.er.block.ElementalFarmBlock;
import net.wither.er.block.BurningDirt;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;

import net.mcreator.er.block.*;
import net.mcreator.er.ErMod;

public class ErModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(ErMod.MODID);
	public static final DeferredBlock<Block> ELECTRO_CRYSTAL_ORE;
	public static final DeferredBlock<Block> SUMERU_ROSE;
	public static final DeferredBlock<Block> COR_LAPIS_ORE;
	public static final DeferredBlock<Block> LOTUS_HEAD;
	public static final DeferredBlock<Block> ELEMENT_ANVIL;
	public static final DeferredBlock<Block> WHITE_IRON_ORE;
	public static final DeferredBlock<Block> DEEPSLATE_WHITE_IRON_ORE;
	public static final DeferredBlock<Block> CRATE;
	public static final DeferredBlock<Block> CUIHUA_LOG;
	public static final DeferredBlock<Block> CUIHUA_LEAVES;
	public static final DeferredBlock<Block> CUIHUA_PLANKS;
	public static final DeferredBlock<Block> CUIHUA_STAIRS;
	public static final DeferredBlock<Block> CUIHUA_SLAB;
	public static final DeferredBlock<Block> CUIHUA_FENCE;
	public static final DeferredBlock<Block> CUIHUA_FENCE_GATE;
	public static final DeferredBlock<Block> CRAFTING_BENCH;
	public static final DeferredBlock<Block> CUIHUA_SAPLING;
	public static final DeferredBlock<Block> STATUEOF_THE_SEVEN;
	public static final DeferredBlock<Block> STATUEOF_THE_SEVEN_CORE;
	public static final DeferredBlock<Block> STATUEOF_THE_SEVEN_2;
	public static final DeferredBlock<Block> STATUEOF_THE_SEVEN_3;
	public static final DeferredBlock<Block> TELEPORT_WAYPOINT_BASE;
	public static final DeferredBlock<Block> TELEPORT_WAYPOINT;
	public static final DeferredBlock<Block> ARTIFACT_TRANSMUTER;
	static {
		ELECTRO_CRYSTAL_ORE = REGISTRY.register("electro_crystal_ore", ElectroCrystalOreBlock::new);
		SUMERU_ROSE = REGISTRY.register("sumeru_rose", SumeruRoseBlock::new);
		COR_LAPIS_ORE = REGISTRY.register("cor_lapis_ore", CorLapisOreBlock::new);
		LOTUS_HEAD = REGISTRY.register("lotus_head", LotusHeadBlock::new);
		ELEMENT_ANVIL = REGISTRY.register("element_anvil", ElementAnvilBlock::new);
		WHITE_IRON_ORE = REGISTRY.register("white_iron_ore", WhiteIronOreBlock::new);
		DEEPSLATE_WHITE_IRON_ORE = REGISTRY.register("deepslate_white_iron_ore", DeepslateWhiteIronOreBlock::new);
		CRATE = REGISTRY.register("crate", CrateBlock::new);
		CUIHUA_LOG = REGISTRY.register("cuihua_log", CuihuaLogBlock::new);
		CUIHUA_LEAVES = REGISTRY.register("cuihua_leaves", CuihuaLeavesBlock::new);
		CUIHUA_PLANKS = REGISTRY.register("cuihua_planks", CuihuaPlanksBlock::new);
		CUIHUA_STAIRS = REGISTRY.register("cuihua_stairs", CuihuaStairsBlock::new);
		CUIHUA_SLAB = REGISTRY.register("cuihua_slab", CuihuaSlabBlock::new);
		CUIHUA_FENCE = REGISTRY.register("cuihua_fence", CuihuaFenceBlock::new);
		CUIHUA_FENCE_GATE = REGISTRY.register("cuihua_fence_gate", CuihuaFenceGateBlock::new);
		CRAFTING_BENCH = REGISTRY.register("crafting_bench", CraftingBenchBlock::new);
		CUIHUA_SAPLING = REGISTRY.register("cuihua_sapling", CuihuaSaplingBlock::new);
		STATUEOF_THE_SEVEN = REGISTRY.register("statueof_the_seven", StatueofTheSevenBlock::new);
		STATUEOF_THE_SEVEN_CORE = REGISTRY.register("statueof_the_seven_core", StatueofTheSevenCoreBlock::new);
		STATUEOF_THE_SEVEN_2 = REGISTRY.register("statueof_the_seven_2", StatueofTheSeven2Block::new);
		STATUEOF_THE_SEVEN_3 = REGISTRY.register("statueof_the_seven_3", StatueofTheSeven3Block::new);
		TELEPORT_WAYPOINT_BASE = REGISTRY.register("teleport_waypoint_base", TeleportWaypointBaseBlock::new);
		TELEPORT_WAYPOINT = REGISTRY.register("teleport_waypoint", TeleportWaypointBlock::new);
		ARTIFACT_TRANSMUTER = REGISTRY.register("artifact_transmuter", ArtifactTransmuterBlock::new);
	}
	// Start of user code block custom blocks
	public static final DeferredBlock<Block> BURNING_DIRT = REGISTRY.register("burning_dirt", BurningDirt::new);
	public static final DeferredBlock<Block> WHOPPERFLOWER_CROP = REGISTRY.register("whopperflower_crop",
			() -> new WhopperflowerCrop(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> ELEMENTAL_FARMLAND = REGISTRY.register("elemental_farmland",
			() -> new ElementalFarmBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).randomTicks().strength(0.6F).sound(SoundType.GRAVEL).isViewBlocking(ErModBlocks::always).isSuffocating(ErModBlocks::always)));
	public static final DeferredBlock<Block> SWEET_FLOWER = REGISTRY.register("sweet_flower",
			() -> new FlowerBlock(MobEffects.MOVEMENT_SPEED, 10, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
	public static final DeferredBlock<Block> LINK_MECHANISM_BASE = REGISTRY.register("link_mechanism", LinkMechanismBase::new);
	public static final DeferredBlock<Block> STORAGE_DEVICE_BASE = REGISTRY.register("storage_device", StorageDevice::new);

	private static boolean always(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

	// End of user code block custom blocks
	@EventBusSubscriber(Dist.CLIENT)
	public static class BlocksClientSideHandler {
		@SubscribeEvent
		public static void blockColorLoad(RegisterColorHandlersEvent.Block event) {
			CuihuaLeavesBlock.blockColorLoad(event);
		}
	}
}