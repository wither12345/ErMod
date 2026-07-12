/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.er.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.ErMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ErModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ErMod.MODID);
	public static final RegistryObject<CreativeModeTab> ER_MATERIALS = REGISTRY.register("er_materials",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.er.er_materials")).icon(() -> new ItemStack(ErModItems.MORA.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ErModItems.MIST_FLOWER_COROLLA.get());
				tabData.accept(ErModItems.FLAMING_FLOWER_STAMEN.get());
				tabData.accept(ErModItems.CRYSTAL_CORE.get());
				tabData.accept(ErModItems.CONDENSED_PYRO.get());
				tabData.accept(ErModItems.CONDENSED_CRYO.get());
				tabData.accept(ErModItems.ELECTRO_CRYSTAL.get());
				tabData.accept(ErModItems.CONDENSED_ELECTRO.get());
				tabData.accept(ErModBlocks.SUMERU_ROSE.get().asItem());
				tabData.accept(ErModItems.COR_LAPIS.get());
				tabData.accept(ErModItems.CONDENSED_GEO.get());
				tabData.accept(ErModItems.DANDELION_SEED.get());
				tabData.accept(ErModItems.CONDENSED_ANEMO.get());
				tabData.accept(ErModItems.CONDENSED_DENDRO.get());
				tabData.accept(ErModItems.CONDENSED_HYDRO.get());
				tabData.accept(ErModBlocks.ELEMENT_ANVIL.get().asItem());
				tabData.accept(ErModItems.IRON_CHUNK.get());
				tabData.accept(ErModItems.WHITE_IRON_CHUNK.get());
				tabData.accept(ErModItems.CRYSTAL_CHUNK.get());
				tabData.accept(ErModItems.MYSTIC_ENHANCEMENT_ORE.get());
				tabData.accept(ErModItems.FINE_ENHANCEMENT_ORE.get());
				tabData.accept(ErModItems.ENHANCEMENT_ORE.get());
				tabData.accept(ErModBlocks.BURNING_DIRT.get().asItem());
				tabData.accept(ErModItems.DAMAGED_MASK.get());
				tabData.accept(ErModItems.STAINED_MASK.get());
				tabData.accept(ErModItems.OMINOUS_MASK.get());
				tabData.accept(ErModItems.SUNSETTIA.get());
				tabData.accept(ErModItems.MIST_GRASS_POLLEN.get());
				tabData.accept(ErModItems.MIST_GRASS.get());
				tabData.accept(ErModItems.MIST_GRASS_WICK.get());
				tabData.accept(ErModBlocks.CRAFTING_BENCH.get().asItem());
				tabData.accept(ErModItems.EMPTY_LEY_LINE_MAP.get());
				tabData.accept(ErModItems.PRIMOGEM.get());
				tabData.accept(ErModItems.BUTTERFLY_WINGS.get());
				tabData.accept(ErModBlocks.ARTIFACT_TRANSMUTER.get().asItem());
			}).build());
	public static final RegistryObject<CreativeModeTab> ER_WEAPON = REGISTRY.register("er_weapon",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.er.er_weapon")).icon(() -> new ItemStack(Items.DIAMOND_SWORD)).displayItems((parameters, tabData) -> {
				tabData.accept(ErModItems.HUNTERS_BOW.get());
				tabData.accept(ErModItems.POLAR_STAR.get());
				tabData.accept(ErModItems.WOODEN_CLUB.get());
				tabData.accept(ErModItems.ELECTRO_MIST_GRASS_LANTERN.get());
			}).withTabsBefore(ER_MATERIALS.getId()).build());
	public static final RegistryObject<CreativeModeTab> STELLA_FORTUNA = REGISTRY.register("stella_fortuna",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.er.stella_fortuna")).icon(() -> new ItemStack(ErModItems.MEMORYOF_ROVING_GALES.get())).displayItems((parameters, tabData) -> {
				tabData.accept(ErModItems.MEMORYOF_ROVING_GALES.get());
			}).withSearchBar().withTabsBefore(ER_WEAPON.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(ErModItems.MIST_FLOWER_SPAWN_EGG.get());
			tabData.accept(ErModItems.FLAMING_FLOWER_SPAWN_EGG.get());
			tabData.accept(ErModItems.ANEMO_CRYSTALFLY_SPAWN_EGG.get());
			tabData.accept(ErModItems.TARTAGLIA_SPAWN_EGG.get());
			tabData.accept(ErModItems.HILICHURL_SPAWN_EGG.get());
			tabData.accept(ErModItems.ELECTRO_CICIN_SPAWN_EGG.get());
			tabData.accept(ErModItems.FATUI_ELECTRO_CICIN_MAGE_SPAWN_EGG.get());
			tabData.accept(ErModItems.BLOSSOM_OF_WEALTH_SPAWN_EGG.get());
			tabData.accept(ErModItems.BLOSSOM_OF_REVELATION_SPAWN_EGG.get());
			tabData.accept(ErModItems.BUTTERFLY_SPAWN_EGG.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
			tabData.accept(ErModItems.PYRO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.PYRO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.PYRO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.PYRO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.CRYO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.CRYO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.CRYO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.CRYO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.CRYO_SWORD.get());
			tabData.accept(ErModItems.PYRO_SWORD.get());
			tabData.accept(ErModItems.ELECTRO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.ELECTRO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.ELECTRO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.ELECTRO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.ELECTRO_SWORD.get());
			tabData.accept(ErModItems.DENDRO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.DENDRO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.DENDRO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.DENDRO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.DENDRO_SWORD.get());
			tabData.accept(ErModItems.GEO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.GEO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.GEO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.GEO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.GEO_SWORD.get());
			tabData.accept(ErModItems.ANEMO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.ANEMO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.ANEMO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.ANEMO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.ANEMO_SWORD.get());
			tabData.accept(ErModItems.HYDRO_ARMOR_HELMET.get());
			tabData.accept(ErModItems.HYDRO_ARMOR_CHESTPLATE.get());
			tabData.accept(ErModItems.HYDRO_ARMOR_LEGGINGS.get());
			tabData.accept(ErModItems.HYDRO_ARMOR_BOOTS.get());
			tabData.accept(ErModItems.HYDRO_SWORD.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
			tabData.accept(ErModItems.MORA.get());
			tabData.accept(ErModItems.MORA_BAG.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			tabData.accept(ErModItems.PYRO_HOE.get());
			tabData.accept(ErModItems.PYRO_PICKAXE.get());
			tabData.accept(ErModItems.ELECTRO_HOE.get());
			tabData.accept(ErModItems.GEO_PICKAXE.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
			tabData.accept(ErModBlocks.ELECTRO_CRYSTAL_ORE.get().asItem());
			tabData.accept(ErModBlocks.COR_LAPIS_ORE.get().asItem());
			tabData.accept(ErModBlocks.LOTUS_HEAD.get().asItem());
			tabData.accept(ErModBlocks.WHITE_IRON_ORE.get().asItem());
			tabData.accept(ErModBlocks.DEEPSLATE_WHITE_IRON_ORE.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_LOG.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_LEAVES.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_SAPLING.get().asItem());
			tabData.accept(ErModBlocks.STATUEOF_THE_SEVEN.get().asItem());
			tabData.accept(ErModBlocks.STATUEOF_THE_SEVEN_CORE.get().asItem());
			tabData.accept(ErModBlocks.STATUEOF_THE_SEVEN_2.get().asItem());
			tabData.accept(ErModBlocks.STATUEOF_THE_SEVEN_3.get().asItem());
			tabData.accept(ErModBlocks.TELEPORT_WAYPOINT_BASE.get().asItem());
			tabData.accept(ErModBlocks.TELEPORT_WAYPOINT.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
			tabData.accept(ErModBlocks.CRATE.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_PLANKS.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_STAIRS.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_SLAB.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_FENCE.get().asItem());
			tabData.accept(ErModBlocks.CUIHUA_FENCE_GATE.get().asItem());
		} else if (tabData.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
			tabData.accept(ErModItems.SUNSETTIA.get());
		} else if (tabData.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
			tabData.accept(ErModBlocks.ARTIFACT_TRANSMUTER.get().asItem());
		}
	}
}