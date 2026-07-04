/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.wither.er.item.weapons.ErTiers;
import net.wither.er.item.weapons.Claymore;
import net.wither.er.item.weapons.AbilitySword;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.data.weapon.ReactionAbility;
import net.wither.er.item.data.weapon.FunctionalAbilities;
import net.wither.er.item.data.weapon.DamageAbility;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.artifact_effect.ArtifactEffectRegistry;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.item.ItemProperties;

import net.mcreator.er.procedures.WoodenClubPropertyValueProviderProcedure;
import net.mcreator.er.procedures.RarityGemstone_CountProcedure;
import net.mcreator.er.item.*;
import net.mcreator.er.ErMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ErModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ErMod.MODID);
	public static final RegistryObject<Item> MIST_FLOWER_SPAWN_EGG;
	public static final RegistryObject<Item> FLAMING_FLOWER_SPAWN_EGG;
	public static final RegistryObject<Item> MIST_FLOWER_COROLLA;
	public static final RegistryObject<Item> FLAMING_FLOWER_STAMEN;
	public static final RegistryObject<Item> PYRO_ARMOR_HELMET;
	public static final RegistryObject<Item> PYRO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> PYRO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> PYRO_ARMOR_BOOTS;
	public static final RegistryObject<Item> HUNTERS_BOW;
	public static final RegistryObject<Item> CRYSTAL_CORE;
	public static final RegistryObject<Item> ANEMO_CRYSTALFLY_SPAWN_EGG;
	public static final RegistryObject<Item> CONDENSED_PYRO;
	public static final RegistryObject<Item> MORA;
	public static final RegistryObject<Item> MORA_BAG;
	public static final RegistryObject<Item> CONDENSED_CRYO;
	public static final RegistryObject<Item> CRYO_ARMOR_HELMET;
	public static final RegistryObject<Item> CRYO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> CRYO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> CRYO_ARMOR_BOOTS;
	public static final RegistryObject<Item> CRYO_SWORD;
	public static final RegistryObject<Item> PYRO_SWORD;
	public static final RegistryObject<Item> POLAR_STAR;
	public static final RegistryObject<Item> TARTAGLIA_SPAWN_EGG;
	public static final RegistryObject<Item> UNOWNED_VISION;
	public static final RegistryObject<Item> PYRO_HOE;
	public static final RegistryObject<Item> PYRO_PICKAXE;
	public static final RegistryObject<Item> ELECTRO_CRYSTAL_ORE;
	public static final RegistryObject<Item> ELECTRO_CRYSTAL;
	public static final RegistryObject<Item> CONDENSED_ELECTRO;
	public static final RegistryObject<Item> ELECTRO_ARMOR_HELMET;
	public static final RegistryObject<Item> ELECTRO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> ELECTRO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> ELECTRO_ARMOR_BOOTS;
	public static final RegistryObject<Item> SUMERU_ROSE;
	public static final RegistryObject<Item> ELECTRO_SWORD;
	public static final RegistryObject<Item> ELECTRO_HOE;
	public static final RegistryObject<Item> DENDRO_ARMOR_HELMET;
	public static final RegistryObject<Item> DENDRO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> DENDRO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> DENDRO_ARMOR_BOOTS;
	public static final RegistryObject<Item> DENDRO_SWORD;
	public static final RegistryObject<Item> COR_LAPIS_ORE;
	public static final RegistryObject<Item> COR_LAPIS;
	public static final RegistryObject<Item> CONDENSED_GEO;
	public static final RegistryObject<Item> GEO_ARMOR_HELMET;
	public static final RegistryObject<Item> GEO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> GEO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> GEO_ARMOR_BOOTS;
	public static final RegistryObject<Item> GEO_SWORD;
	public static final RegistryObject<Item> GEO_PICKAXE;
	public static final RegistryObject<Item> DANDELION_SEED;
	public static final RegistryObject<Item> CONDENSED_ANEMO;
	public static final RegistryObject<Item> CONDENSED_DENDRO;
	public static final RegistryObject<Item> ANEMO_ARMOR_HELMET;
	public static final RegistryObject<Item> ANEMO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> ANEMO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> ANEMO_ARMOR_BOOTS;
	public static final RegistryObject<Item> ANEMO_SWORD;
	public static final RegistryObject<Item> LOTUS_HEAD;
	public static final RegistryObject<Item> CONDENSED_HYDRO;
	public static final RegistryObject<Item> HYDRO_ARMOR_HELMET;
	public static final RegistryObject<Item> HYDRO_ARMOR_CHESTPLATE;
	public static final RegistryObject<Item> HYDRO_ARMOR_LEGGINGS;
	public static final RegistryObject<Item> HYDRO_ARMOR_BOOTS;
	public static final RegistryObject<Item> HYDRO_SWORD;
	public static final RegistryObject<Item> MAIN_AFFIX_SHARD;
	public static final RegistryObject<Item> MINOR_AFFIX_SHARD;
	public static final RegistryObject<Item> RARITY_GEMSTONE;
	public static final RegistryObject<Item> MINOR_UPGRADES;
	public static final RegistryObject<Item> ELEMENT_ANVIL;
	public static final RegistryObject<Item> IRON_CHUNK;
	public static final RegistryObject<Item> WHITE_IRON_CHUNK;
	public static final RegistryObject<Item> WHITE_IRON_ORE;
	public static final RegistryObject<Item> DEEPSLATE_WHITE_IRON_ORE;
	public static final RegistryObject<Item> CRYSTAL_CHUNK;
	public static final RegistryObject<Item> MYSTIC_ENHANCEMENT_ORE;
	public static final RegistryObject<Item> FINE_ENHANCEMENT_ORE;
	public static final RegistryObject<Item> ENHANCEMENT_ORE;
	public static final RegistryObject<Item> BURNING_DIRT;
	public static final RegistryObject<Item> PYRO_VISION;
	public static final RegistryObject<Item> ANEMO_VISION;
	public static final RegistryObject<Item> CRYO_VISION;
	public static final RegistryObject<Item> HYDRO_VISION;
	public static final RegistryObject<Item> ELECTRO_VISION;
	public static final RegistryObject<Item> DENDRO_VISION;
	public static final RegistryObject<Item> GEO_VISION;
	public static final RegistryObject<Item> HILICHURL_SPAWN_EGG;
	public static final RegistryObject<Item> WOODEN_CLUB;
	public static final RegistryObject<Item> CRATE;
	public static final RegistryObject<Item> CUIHUA_LOG;
	public static final RegistryObject<Item> CUIHUA_LEAVES;
	public static final RegistryObject<Item> CUIHUA_PLANKS;
	public static final RegistryObject<Item> CUIHUA_STAIRS;
	public static final RegistryObject<Item> CUIHUA_SLAB;
	public static final RegistryObject<Item> CUIHUA_FENCE;
	public static final RegistryObject<Item> CUIHUA_FENCE_GATE;
	public static final RegistryObject<Item> DAMAGED_MASK;
	public static final RegistryObject<Item> STAINED_MASK;
	public static final RegistryObject<Item> OMINOUS_MASK;
	public static final RegistryObject<Item> SUNSETTIA;
	public static final RegistryObject<Item> ELECTRO_CICIN_SPAWN_EGG;
	public static final RegistryObject<Item> FATUI_ELECTRO_CICIN_MAGE_SPAWN_EGG;
	public static final RegistryObject<Item> ELECTRO_MIST_GRASS_LANTERN;
	public static final RegistryObject<Item> MIST_GRASS_POLLEN;
	public static final RegistryObject<Item> MIST_GRASS;
	public static final RegistryObject<Item> MIST_GRASS_WICK;
	public static final RegistryObject<Item> CRAFTING_BENCH;
	public static final RegistryObject<Item> CUIHUA_SAPLING;
	public static final RegistryObject<Item> LEY_LINE_MAP;
	public static final RegistryObject<Item> EMPTY_LEY_LINE_MAP;
	public static final RegistryObject<Item> STATUEOF_THE_SEVEN;
	public static final RegistryObject<Item> STATUEOF_THE_SEVEN_CORE;
	public static final RegistryObject<Item> STATUEOF_THE_SEVEN_2;
	public static final RegistryObject<Item> STATUEOF_THE_SEVEN_3;
	public static final RegistryObject<Item> TELEPORT_WAYPOINT_BASE;
	public static final RegistryObject<Item> TELEPORT_WAYPOINT;
	public static final RegistryObject<Item> MEMORYOF_ROVING_GALES;
	public static final RegistryObject<Item> PRIMOGEM;
	public static final RegistryObject<Item> A_BAG_OF_MORA;
	public static final RegistryObject<Item> BLOSSOM_OF_WEALTH_SPAWN_EGG;
	public static final RegistryObject<Item> BLOSSOM_OF_REVELATION_SPAWN_EGG;
	public static final RegistryObject<Item> BUTTERFLY_SPAWN_EGG;
	public static final RegistryObject<Item> BUTTERFLY_WINGS;
	public static final RegistryObject<Item> ARTIFACT_TRANSMUTER;
	static {
		MIST_FLOWER_SPAWN_EGG = REGISTRY.register("mist_flower_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.MIST_FLOWER, -16711681, -6684673, new Item.Properties()));
		FLAMING_FLOWER_SPAWN_EGG = REGISTRY.register("flaming_flower_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.FLAMING_FLOWER, -65536, -39424, new Item.Properties()));
		MIST_FLOWER_COROLLA = REGISTRY.register("mist_flower_corolla", MistFlowerCorollaItem::new);
		FLAMING_FLOWER_STAMEN = REGISTRY.register("flaming_flower_stamen", FlamingFlowerStamenItem::new);
		PYRO_ARMOR_HELMET = REGISTRY.register("pyro_armor_helmet", PyroArmorItem.Helmet::new);
		PYRO_ARMOR_CHESTPLATE = REGISTRY.register("pyro_armor_chestplate", PyroArmorItem.Chestplate::new);
		PYRO_ARMOR_LEGGINGS = REGISTRY.register("pyro_armor_leggings", PyroArmorItem.Leggings::new);
		PYRO_ARMOR_BOOTS = REGISTRY.register("pyro_armor_boots", PyroArmorItem.Boots::new);
		HUNTERS_BOW = REGISTRY.register("hunters_bow", HuntersBowItem::new);
		CRYSTAL_CORE = REGISTRY.register("crystal_core", CrystalCoreItem::new);
		ANEMO_CRYSTALFLY_SPAWN_EGG = REGISTRY.register("anemo_crystalfly_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.ANEMO_CRYSTALFLY, -16718409, -16719617, new Item.Properties()));
		CONDENSED_PYRO = REGISTRY.register("condensed_pyro", CondensedPyroItem::new);
		MORA = REGISTRY.register("mora", MoraItem::new);
		MORA_BAG = REGISTRY.register("mora_bag", MoraBagItem::new);
		CONDENSED_CRYO = REGISTRY.register("condensed_cryo", CondensedCryoItem::new);
		CRYO_ARMOR_HELMET = REGISTRY.register("cryo_armor_helmet", CryoArmorItem.Helmet::new);
		CRYO_ARMOR_CHESTPLATE = REGISTRY.register("cryo_armor_chestplate", CryoArmorItem.Chestplate::new);
		CRYO_ARMOR_LEGGINGS = REGISTRY.register("cryo_armor_leggings", CryoArmorItem.Leggings::new);
		CRYO_ARMOR_BOOTS = REGISTRY.register("cryo_armor_boots", CryoArmorItem.Boots::new);
		CRYO_SWORD = REGISTRY.register("cryo_sword", CryoSwordItem::new);
		PYRO_SWORD = REGISTRY.register("pyro_sword", PyroSwordItem::new);
		POLAR_STAR = REGISTRY.register("polar_star", PolarStarItem::new);
		TARTAGLIA_SPAWN_EGG = REGISTRY.register("tartaglia_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.TARTAGLIA, -6724096, -16737793, new Item.Properties()));
		UNOWNED_VISION = REGISTRY.register("unowned_vision", UnownedVisionItem::new);
		PYRO_HOE = REGISTRY.register("pyro_hoe", PyroHoeItem::new);
		PYRO_PICKAXE = REGISTRY.register("pyro_pickaxe", PyroPickaxeItem::new);
		ELECTRO_CRYSTAL_ORE = block(ErModBlocks.ELECTRO_CRYSTAL_ORE);
		ELECTRO_CRYSTAL = REGISTRY.register("electro_crystal", ElectroCrystalItem::new);
		CONDENSED_ELECTRO = REGISTRY.register("condensed_electro", CondensedElectroItem::new);
		ELECTRO_ARMOR_HELMET = REGISTRY.register("electro_armor_helmet", ElectroArmorItem.Helmet::new);
		ELECTRO_ARMOR_CHESTPLATE = REGISTRY.register("electro_armor_chestplate", ElectroArmorItem.Chestplate::new);
		ELECTRO_ARMOR_LEGGINGS = REGISTRY.register("electro_armor_leggings", ElectroArmorItem.Leggings::new);
		ELECTRO_ARMOR_BOOTS = REGISTRY.register("electro_armor_boots", ElectroArmorItem.Boots::new);
		SUMERU_ROSE = block(ErModBlocks.SUMERU_ROSE);
		ELECTRO_SWORD = REGISTRY.register("electro_sword", ElectroSwordItem::new);
		ELECTRO_HOE = REGISTRY.register("electro_hoe", ElectroHoeItem::new);
		DENDRO_ARMOR_HELMET = REGISTRY.register("dendro_armor_helmet", DendroArmorItem.Helmet::new);
		DENDRO_ARMOR_CHESTPLATE = REGISTRY.register("dendro_armor_chestplate", DendroArmorItem.Chestplate::new);
		DENDRO_ARMOR_LEGGINGS = REGISTRY.register("dendro_armor_leggings", DendroArmorItem.Leggings::new);
		DENDRO_ARMOR_BOOTS = REGISTRY.register("dendro_armor_boots", DendroArmorItem.Boots::new);
		DENDRO_SWORD = REGISTRY.register("dendro_sword", DendroSwordItem::new);
		COR_LAPIS_ORE = block(ErModBlocks.COR_LAPIS_ORE);
		COR_LAPIS = REGISTRY.register("cor_lapis", CorLapisItem::new);
		CONDENSED_GEO = REGISTRY.register("condensed_geo", CondensedGeoItem::new);
		GEO_ARMOR_HELMET = REGISTRY.register("geo_armor_helmet", GeoArmorItem.Helmet::new);
		GEO_ARMOR_CHESTPLATE = REGISTRY.register("geo_armor_chestplate", GeoArmorItem.Chestplate::new);
		GEO_ARMOR_LEGGINGS = REGISTRY.register("geo_armor_leggings", GeoArmorItem.Leggings::new);
		GEO_ARMOR_BOOTS = REGISTRY.register("geo_armor_boots", GeoArmorItem.Boots::new);
		GEO_SWORD = REGISTRY.register("geo_sword", GeoSwordItem::new);
		GEO_PICKAXE = REGISTRY.register("geo_pickaxe", GeoPickaxeItem::new);
		DANDELION_SEED = REGISTRY.register("dandelion_seed", DandelionSeedItem::new);
		CONDENSED_ANEMO = REGISTRY.register("condensed_anemo", CondensedAnemoItem::new);
		CONDENSED_DENDRO = REGISTRY.register("condensed_dendro", CondensedDendroItem::new);
		ANEMO_ARMOR_HELMET = REGISTRY.register("anemo_armor_helmet", AnemoArmorItem.Helmet::new);
		ANEMO_ARMOR_CHESTPLATE = REGISTRY.register("anemo_armor_chestplate", AnemoArmorItem.Chestplate::new);
		ANEMO_ARMOR_LEGGINGS = REGISTRY.register("anemo_armor_leggings", AnemoArmorItem.Leggings::new);
		ANEMO_ARMOR_BOOTS = REGISTRY.register("anemo_armor_boots", AnemoArmorItem.Boots::new);
		ANEMO_SWORD = REGISTRY.register("anemo_sword", AnemoSwordItem::new);
		LOTUS_HEAD = block(ErModBlocks.LOTUS_HEAD);
		CONDENSED_HYDRO = REGISTRY.register("condensed_hydro", CondensedHydroItem::new);
		HYDRO_ARMOR_HELMET = REGISTRY.register("hydro_armor_helmet", HydroArmorItem.Helmet::new);
		HYDRO_ARMOR_CHESTPLATE = REGISTRY.register("hydro_armor_chestplate", HydroArmorItem.Chestplate::new);
		HYDRO_ARMOR_LEGGINGS = REGISTRY.register("hydro_armor_leggings", HydroArmorItem.Leggings::new);
		HYDRO_ARMOR_BOOTS = REGISTRY.register("hydro_armor_boots", HydroArmorItem.Boots::new);
		HYDRO_SWORD = REGISTRY.register("hydro_sword", HydroSwordItem::new);
		MAIN_AFFIX_SHARD = REGISTRY.register("main_affix_shard", MainAffixShardItem::new);
		MINOR_AFFIX_SHARD = REGISTRY.register("minor_affix_shard", MinorAffixShardItem::new);
		RARITY_GEMSTONE = REGISTRY.register("rarity_gemstone", RarityGemstoneItem::new);
		MINOR_UPGRADES = REGISTRY.register("minor_upgrades", MinorUpgradesItem::new);
		ELEMENT_ANVIL = block(ErModBlocks.ELEMENT_ANVIL);
		IRON_CHUNK = REGISTRY.register("iron_chunk", IronChunkItem::new);
		WHITE_IRON_CHUNK = REGISTRY.register("white_iron_chunk", WhiteIronChunkItem::new);
		WHITE_IRON_ORE = block(ErModBlocks.WHITE_IRON_ORE);
		DEEPSLATE_WHITE_IRON_ORE = block(ErModBlocks.DEEPSLATE_WHITE_IRON_ORE);
		CRYSTAL_CHUNK = REGISTRY.register("crystal_chunk", CrystalChunkItem::new);
		MYSTIC_ENHANCEMENT_ORE = REGISTRY.register("mystic_enhancement_ore", MysticEnhancementOreItem::new);
		FINE_ENHANCEMENT_ORE = REGISTRY.register("fine_enhancement_ore", FineEnhancementOreItem::new);
		ENHANCEMENT_ORE = REGISTRY.register("enhancement_ore", EnhancementOreItem::new);
		BURNING_DIRT = block(ErModBlocks.BURNING_DIRT);
		PYRO_VISION = REGISTRY.register("pyro_vision", PyroVisionItem::new);
		ANEMO_VISION = REGISTRY.register("anemo_vision", AnemoVisionItem::new);
		CRYO_VISION = REGISTRY.register("cryo_vision", CryoVisionItem::new);
		HYDRO_VISION = REGISTRY.register("hydro_vision", HydroVisionItem::new);
		ELECTRO_VISION = REGISTRY.register("electro_vision", ElectroVisionItem::new);
		DENDRO_VISION = REGISTRY.register("dendro_vision", DendroVisionItem::new);
		GEO_VISION = REGISTRY.register("geo_vision", GeoVisionItem::new);
		HILICHURL_SPAWN_EGG = REGISTRY.register("hilichurl_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.HILICHURL, -13421773, -6780581, new Item.Properties()));
		WOODEN_CLUB = REGISTRY.register("wooden_club", WoodenClubItem::new);
		CRATE = block(ErModBlocks.CRATE);
		CUIHUA_LOG = block(ErModBlocks.CUIHUA_LOG);
		CUIHUA_LEAVES = block(ErModBlocks.CUIHUA_LEAVES);
		CUIHUA_PLANKS = block(ErModBlocks.CUIHUA_PLANKS);
		CUIHUA_STAIRS = block(ErModBlocks.CUIHUA_STAIRS);
		CUIHUA_SLAB = block(ErModBlocks.CUIHUA_SLAB);
		CUIHUA_FENCE = block(ErModBlocks.CUIHUA_FENCE);
		CUIHUA_FENCE_GATE = block(ErModBlocks.CUIHUA_FENCE_GATE);
		DAMAGED_MASK = REGISTRY.register("damaged_mask", DamagedMaskItem::new);
		STAINED_MASK = REGISTRY.register("stained_mask", StainedMaskItem::new);
		OMINOUS_MASK = REGISTRY.register("ominous_mask", OminousMaskItem::new);
		SUNSETTIA = REGISTRY.register("sunsettia", SunsettiaItem::new);
		ELECTRO_CICIN_SPAWN_EGG = REGISTRY.register("electro_cicin_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.ELECTRO_CICIN, -10092340, -13434778, new Item.Properties()));
		FATUI_ELECTRO_CICIN_MAGE_SPAWN_EGG = REGISTRY.register("fatui_electro_cicin_mage_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.FATUI_ELECTRO_CICIN_MAGE, -13434778, -6736897, new Item.Properties()));
		ELECTRO_MIST_GRASS_LANTERN = REGISTRY.register("electro_mist_grass_lantern", ElectroMistGrassLanternItem::new);
		MIST_GRASS_POLLEN = REGISTRY.register("mist_grass_pollen", MistGrassPollenItem::new);
		MIST_GRASS = REGISTRY.register("mist_grass", MistGrassItem::new);
		MIST_GRASS_WICK = REGISTRY.register("mist_grass_wick", MistGrassWickItem::new);
		CRAFTING_BENCH = block(ErModBlocks.CRAFTING_BENCH);
		CUIHUA_SAPLING = block(ErModBlocks.CUIHUA_SAPLING);
		LEY_LINE_MAP = REGISTRY.register("ley_line_map", LeyLineMapItem::new);
		EMPTY_LEY_LINE_MAP = REGISTRY.register("empty_ley_line_map", EmptyLeyLineMapItem::new);
		STATUEOF_THE_SEVEN = block(ErModBlocks.STATUEOF_THE_SEVEN);
		STATUEOF_THE_SEVEN_CORE = block(ErModBlocks.STATUEOF_THE_SEVEN_CORE);
		STATUEOF_THE_SEVEN_2 = block(ErModBlocks.STATUEOF_THE_SEVEN_2);
		STATUEOF_THE_SEVEN_3 = block(ErModBlocks.STATUEOF_THE_SEVEN_3);
		TELEPORT_WAYPOINT_BASE = block(ErModBlocks.TELEPORT_WAYPOINT_BASE);
		TELEPORT_WAYPOINT = block(ErModBlocks.TELEPORT_WAYPOINT);
		MEMORYOF_ROVING_GALES = REGISTRY.register("memoryof_roving_gales", MemoryofRovingGalesItem::new);
		PRIMOGEM = REGISTRY.register("primogem", PrimogemItem::new);
		A_BAG_OF_MORA = REGISTRY.register("a_bag_of_mora", ABagofMoraItem::new);
		BLOSSOM_OF_WEALTH_SPAWN_EGG = REGISTRY.register("blossom_of_wealth_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.BLOSSOM_OF_WEALTH, -256, -3368704, new Item.Properties()));
		BLOSSOM_OF_REVELATION_SPAWN_EGG = REGISTRY.register("blossom_of_revelation_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.BLOSSOM_OF_REVELATION, -6684673, -10040065, new Item.Properties()));
		BUTTERFLY_SPAWN_EGG = REGISTRY.register("butterfly_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.BUTTERFLY, -16729601, -15654060, new Item.Properties()));
		BUTTERFLY_WINGS = REGISTRY.register("butterfly_wings", ButterflyWingsItem::new);
		ARTIFACT_TRANSMUTER = block(ErModBlocks.ARTIFACT_TRANSMUTER);
	}
	// Start of user code block custom items
	public static final RegistryObject<Item> WANDERERS_ADVICE = REGISTRY.register("wanderers_advice", () -> new ExperienceBook(new Item.Properties(), 40));
	public static final RegistryObject<Item> ADVENTURES_EXPERIENCE = REGISTRY.register("adventurers_experience", () -> new ExperienceBook(new Item.Properties(), 200));
	public static final RegistryObject<Item> HEROS_WIT = REGISTRY.register("heros_wit", () -> new ExperienceBook(new Item.Properties().rarity(Rarity.UNCOMMON), 800));
	public static final RegistryObject<Item> LIZARD_TAIL = REGISTRY.register("lizard_tail", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FROG = REGISTRY.register("frog", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ENCHANTED_MYSTIC_ENHANCEMENT_ORE = REGISTRY.register("enchanted_mystic_enhancement_ore", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> LUCKY_DOGS_CLOVER = REGISTRY.register("lucky_dogs_clover", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.LUCKY_DOG));
	public static final RegistryObject<Item> LUCKY_DOGS_EAGLE_FEATHER = REGISTRY.register("lucky_dogs_eagle_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.LUCKY_DOG));
	public static final RegistryObject<Item> LUCKY_DOGS_HOURGLASS = REGISTRY.register("lucky_dogs_hourglass", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.LUCKY_DOG));
	public static final RegistryObject<Item> LUCKY_DOGS_GOBLET = REGISTRY.register("lucky_dogs_goblet", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.LUCKY_DOG));
	public static final RegistryObject<Item> LUCKY_DOGS_SILVER_CIRCLET = REGISTRY.register("lucky_dogs_silver_circlet", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.LUCKY_DOG));
	public static final RegistryObject<Item> SANCTIFYING_UNCTION = REGISTRY.register("sanctifying_unction", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SANCTIFYING_ESSENCE = REGISTRY.register("sanctifying_essence", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ELECTRO_SLIME_SPAWN_EGG = REGISTRY.register("electro_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.ELECTRO_SLIME, 0x9933ff, 0x6600cc, new Item.Properties()));
	public static final RegistryObject<Item> PYRO_SLIME_SPAWN_EGG = REGISTRY.register("pyro_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.PYRO_SLIME, 0xff6600, 0xcc0000, new Item.Properties()));
	public static final RegistryObject<Item> GEO_SLIME_SPAWN_EGG = REGISTRY.register("geo_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.GEO_SLIME, 0xcc9900, 0x996600, new Item.Properties()));
	public static final RegistryObject<Item> CRYO_SLIME_SPAWN_EGG = REGISTRY.register("cryo_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.CRYO_SLIME, 0x00ffff, 0xccffff, new Item.Properties()));
	public static final RegistryObject<Item> HYDRO_SLIME_SPAWN_EGG = REGISTRY.register("hydro_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.HYDRO_SLIME, 0x3366ff, 0x0000cc, new Item.Properties()));
	public static final RegistryObject<Item> DENDRO_SLIME_SPAWN_EGG = REGISTRY.register("dendro_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.DENDRO_SLIME, 0x00ff00, 0x009900, new Item.Properties()));
	public static final RegistryObject<Item> ANEMO_SLIME_SPAWN_EGG = REGISTRY.register("anemo_slime_spawn_egg", () -> new ForgeSpawnEggItem(ErModEntities.ANEMO_SLIME, 0x33ffcc, 0xccffcc, new Item.Properties()));
	public static final RegistryObject<Item> ADVENTURERS_FLOWER = REGISTRY.register("adventurers_flower", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.ADVENTURER));
	public static final RegistryObject<Item> ADVENTURERS_TAIL_FEATHER = REGISTRY.register("adventurers_tail_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.ADVENTURER));
	public static final RegistryObject<Item> ADVENTURERS_POCKET_WATCH = REGISTRY.register("adventurers_pocket_watch", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.ADVENTURER));
	public static final RegistryObject<Item> ADVENTURERS_GOLDEN_GOBLET = REGISTRY.register("adventurers_golden_goblet", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.ADVENTURER));
	public static final RegistryObject<Item> ADVENTURERS_BANDANA = REGISTRY.register("adventurers_bandana", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.ADVENTURER));
	public static final RegistryObject<Item> SLIME_CONDENSATE = REGISTRY.register("slime_condensate", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SLIME_SECRETIONS = REGISTRY.register("slime_secretions", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> SLIME_CONCENTRATE = REGISTRY.register("slime_concentrate", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> TRAVELING_DOCTORS_SILVER_LOTUS = REGISTRY.register("traveling_doctors_silver_lotus", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final RegistryObject<Item> TRAVELING_DOCTORS_OWL_FEATHER = REGISTRY.register("traveling_doctors_owl_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final RegistryObject<Item> TRAVELING_DOCTORS_POCKET_WATCH = REGISTRY.register("traveling_doctors_pocket_watch", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final RegistryObject<Item> TRAVELING_DOCTORS_MEDICINE_POT = REGISTRY.register("traveling_doctors_medicine_pot", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final RegistryObject<Item> TRAVELING_DOCTORS_HANDKERCHIEF = REGISTRY.register("traveling_doctors_handkerchief", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final RegistryObject<Item> DULL_BLADE = REGISTRY.register("dull_blade", () -> new SwordItem(ErTiers.STAR_1, 1, -2.4f, new Item.Properties()));
	public static final RegistryObject<Item> SILVER_SWORD = REGISTRY.register("silver_sword", () -> new SwordItem(ErTiers.STAR_2, 2, -2.4f, new Item.Properties()));
	public static final RegistryObject<Item> WASTER_GREATSWORD = REGISTRY.register("waster_greatsword", () -> new Claymore(ErTiers.STAR_1, new Item.Properties()));
	public static final RegistryObject<Item> DUST_OF_AZOTH = REGISTRY.register("dust_of_azoth", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> BERSERKERS_ROSE = REGISTRY.register("berserkers_rose", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.BERSERKER));
	public static final RegistryObject<Item> BERSERKERS_INDIGO_FEATHER = REGISTRY.register("berserkers_indigo_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.BERSERKER));
	public static final RegistryObject<Item> BERSERKERS_TIMEPIECE = REGISTRY.register("berserkers_timepiece", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.BERSERKER));
	public static final RegistryObject<Item> BERSERKERS_BONE_GOBLET = REGISTRY.register("berserkers_bone_goblet", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.BERSERKER));
	public static final RegistryObject<Item> BERSERKERS_BATTLE_MASK = REGISTRY.register("berserkers_battle_mask", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.BERSERKER));
	public static final RegistryObject<Item> COOL_STEEL = REGISTRY.register("cool_steel", () -> new AbilitySword((DamageAbility) FunctionalAbilities::coolSteel, ErModItems.COOL_STEEL, ErTiers.STAR_3, 3, -2.4f, new Item.Properties()));
	public static final RegistryObject<Item> DARK_IRON_SWORD = REGISTRY.register("dark_iron_sword",
			() -> new AbilitySword((ReactionAbility) FunctionalAbilities::darkIronSword, ErModItems.DARK_IRON_SWORD, ErTiers.STAR_3, 3, -2.4f, new Item.Properties()));

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class BowItemsClientSideHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void clientLoad(FMLClientSetupEvent event) {
			registerBowItem(HUNTERS_BOW.get());
			registerBowItem(POLAR_STAR.get());
			registerWeapon(DULL_BLADE.get());
			registerWeapon(SILVER_SWORD.get());
			registerWeapon(WASTER_GREATSWORD.get());
			registerWeapon(COOL_STEEL.get());
			registerWeapon(DARK_IRON_SWORD.get());
		}

		private static void registerBowItem(Item bowItem) {
			ItemProperties.register(bowItem, new ResourceLocation("er:ascension"), (itemStackToRender, clientWorld, entity, itemEntityId) -> getAscension(itemStackToRender));
			ItemProperties.register(bowItem, new ResourceLocation("er:pull"), (itemStackToRender, clientWorld, entity, itemEntityId) -> {
				if (entity == null) {
					return 0.0F;
				} else {
					return entity.getUseItem() != itemStackToRender ? 0.0F : (float) (itemStackToRender.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
				}
			});
			ItemProperties.register(bowItem, new ResourceLocation("er:pulling"), (itemStackToRender, clientWorld, entity, itemEntityId) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemStackToRender ? 1.0F : 0.0F);
		}

		private static void registerWeapon(Item item) {
			ItemProperties.register(item, new ResourceLocation("er:ascension"), (itemStackToRender, clientWorld, entity, itemEntityId) -> getAscension(itemStackToRender));
		}

		private static int getAscension(ItemStack itemStack) {
			WeaponLevelData data = DataComponentsRegister.WEAPON_LEVEL.getData(itemStack);
			if (data == null)
				return 0;
			return data.ascension();
		}
	}

	// End of user code block custom items
	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return block(block, new Item.Properties());
	}

	private static RegistryObject<Item> block(RegistryObject<Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			ItemProperties.register(RARITY_GEMSTONE.get(), new ResourceLocation("er:rarity_gemstone_item_count"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) RarityGemstone_CountProcedure.execute(itemStackToRender));
			ItemProperties.register(WOODEN_CLUB.get(), new ResourceLocation("er:wooden_club_pyro"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) WoodenClubPropertyValueProviderProcedure.execute(itemStackToRender));
		});
	}
}