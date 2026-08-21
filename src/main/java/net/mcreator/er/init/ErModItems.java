/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.wither.er.item.weapons.ErTiers;
import net.wither.er.item.weapons.Claymore;
import net.wither.er.item.morabag.MoraBagItemPlus;
import net.wither.er.item.data.weapon.WeaponRefinement;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.item.*;
import net.wither.er.init.WeaponAbilityRegister;
import net.wither.er.init.ShieldRegistry;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ArmorMaterialsRegister;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.elements.Element;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.renderer.item.ItemProperties;

import net.mcreator.er.procedures.WoodenClubPropertyValueProviderProcedure;
import net.mcreator.er.procedures.RarityGemstone_CountProcedure;
import net.mcreator.er.item.*;
import net.mcreator.er.ErMod;

public class ErModItems {
	public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(ErMod.MODID);
	public static final DeferredItem<Item> MIST_FLOWER_SPAWN_EGG;
	public static final DeferredItem<Item> FLAMING_FLOWER_SPAWN_EGG;
	public static final DeferredItem<Item> MIST_FLOWER_COROLLA;
	public static final DeferredItem<Item> FLAMING_FLOWER_STAMEN;
	public static final DeferredItem<Item> HUNTERS_BOW;
	public static final DeferredItem<Item> CRYSTAL_CORE;
	public static final DeferredItem<Item> ANEMO_CRYSTALFLY_SPAWN_EGG;
	public static final DeferredItem<Item> CONDENSED_PYRO;
	public static final DeferredItem<Item> MORA;
	public static final DeferredItem<Item> CONDENSED_CRYO;
	public static final DeferredItem<Item> CRYO_SWORD;
	public static final DeferredItem<Item> PYRO_SWORD;
	public static final DeferredItem<Item> POLAR_STAR;
	public static final DeferredItem<Item> TARTAGLIA_SPAWN_EGG;
	public static final DeferredItem<Item> PYRO_PICKAXE;
	public static final DeferredItem<Item> ELECTRO_CRYSTAL_ORE;
	public static final DeferredItem<Item> ELECTRO_CRYSTAL;
	public static final DeferredItem<Item> CONDENSED_ELECTRO;
	public static final DeferredItem<Item> SUMERU_ROSE;
	public static final DeferredItem<Item> ELECTRO_SWORD;
	public static final DeferredItem<Item> DENDRO_SWORD;
	public static final DeferredItem<Item> COR_LAPIS_ORE;
	public static final DeferredItem<Item> COR_LAPIS;
	public static final DeferredItem<Item> CONDENSED_GEO;
	public static final DeferredItem<Item> GEO_SWORD;
	public static final DeferredItem<Item> GEO_PICKAXE;
	public static final DeferredItem<Item> DANDELION_SEED;
	public static final DeferredItem<Item> CONDENSED_ANEMO;
	public static final DeferredItem<Item> CONDENSED_DENDRO;
	public static final DeferredItem<Item> ANEMO_SWORD;
	public static final DeferredItem<Item> LOTUS_HEAD;
	public static final DeferredItem<Item> CONDENSED_HYDRO;
	public static final DeferredItem<Item> HYDRO_SWORD;
	public static final DeferredItem<Item> MAIN_AFFIX_SHARD;
	public static final DeferredItem<Item> MINOR_AFFIX_SHARD;
	public static final DeferredItem<Item> RARITY_GEMSTONE;
	public static final DeferredItem<Item> MINOR_UPGRADES;
	public static final DeferredItem<Item> ELEMENT_ANVIL;
	public static final DeferredItem<Item> IRON_CHUNK;
	public static final DeferredItem<Item> WHITE_IRON_CHUNK;
	public static final DeferredItem<Item> WHITE_IRON_ORE;
	public static final DeferredItem<Item> DEEPSLATE_WHITE_IRON_ORE;
	public static final DeferredItem<Item> CRYSTAL_CHUNK;
	public static final DeferredItem<Item> MYSTIC_ENHANCEMENT_ORE;
	public static final DeferredItem<Item> FINE_ENHANCEMENT_ORE;
	public static final DeferredItem<Item> ENHANCEMENT_ORE;
	public static final DeferredItem<Item> HILICHURL_SPAWN_EGG;
	public static final DeferredItem<Item> WOODEN_CLUB;
	public static final DeferredItem<Item> CRATE;
	public static final DeferredItem<Item> CUIHUA_LOG;
	public static final DeferredItem<Item> CUIHUA_LEAVES;
	public static final DeferredItem<Item> CUIHUA_PLANKS;
	public static final DeferredItem<Item> CUIHUA_STAIRS;
	public static final DeferredItem<Item> CUIHUA_SLAB;
	public static final DeferredItem<Item> CUIHUA_FENCE;
	public static final DeferredItem<Item> CUIHUA_FENCE_GATE;
	public static final DeferredItem<Item> DAMAGED_MASK;
	public static final DeferredItem<Item> STAINED_MASK;
	public static final DeferredItem<Item> OMINOUS_MASK;
	public static final DeferredItem<Item> SUNSETTIA;
	public static final DeferredItem<Item> ELECTRO_CICIN_SPAWN_EGG;
	public static final DeferredItem<Item> FATUI_ELECTRO_CICIN_MAGE_SPAWN_EGG;
	public static final DeferredItem<Item> ELECTRO_MIST_GRASS_LANTERN;
	public static final DeferredItem<Item> MIST_GRASS_POLLEN;
	public static final DeferredItem<Item> MIST_GRASS;
	public static final DeferredItem<Item> MIST_GRASS_WICK;
	public static final DeferredItem<Item> CRAFTING_BENCH;
	public static final DeferredItem<Item> CUIHUA_SAPLING;
	public static final DeferredItem<Item> LEY_LINE_MAP;
	public static final DeferredItem<Item> EMPTY_LEY_LINE_MAP;
	public static final DeferredItem<Item> STATUEOF_THE_SEVEN;
	public static final DeferredItem<Item> STATUEOF_THE_SEVEN_CORE;
	public static final DeferredItem<Item> STATUEOF_THE_SEVEN_2;
	public static final DeferredItem<Item> STATUEOF_THE_SEVEN_3;
	public static final DeferredItem<Item> TELEPORT_WAYPOINT_BASE;
	public static final DeferredItem<Item> TELEPORT_WAYPOINT;
	public static final DeferredItem<Item> MEMORYOF_ROVING_GALES;
	public static final DeferredItem<Item> PRIMOGEM;
	public static final DeferredItem<Item> A_BAG_OF_MORA;
	public static final DeferredItem<Item> BLOSSOM_OF_WEALTH_SPAWN_EGG;
	public static final DeferredItem<Item> BLOSSOM_OF_REVELATION_SPAWN_EGG;
	public static final DeferredItem<Item> BUTTERFLY_SPAWN_EGG;
	public static final DeferredItem<Item> BUTTERFLY_WINGS;
	public static final DeferredItem<Item> ARTIFACT_TRANSMUTER;
	static {
		MIST_FLOWER_SPAWN_EGG = REGISTRY.register("mist_flower_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.MIST_FLOWER, -16711681, -6684673, new Item.Properties()));
		FLAMING_FLOWER_SPAWN_EGG = REGISTRY.register("flaming_flower_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.FLAMING_FLOWER, -65536, -39424, new Item.Properties()));
		MIST_FLOWER_COROLLA = REGISTRY.register("mist_flower_corolla", MistFlowerCorollaItem::new);
		FLAMING_FLOWER_STAMEN = REGISTRY.register("flaming_flower_stamen", FlamingFlowerStamenItem::new);
		HUNTERS_BOW = REGISTRY.register("hunters_bow", HuntersBowItem::new);
		CRYSTAL_CORE = REGISTRY.register("crystal_core", CrystalCoreItem::new);
		ANEMO_CRYSTALFLY_SPAWN_EGG = REGISTRY.register("anemo_crystalfly_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.ANEMO_CRYSTALFLY, -16718409, -16719617, new Item.Properties()));
		CONDENSED_PYRO = REGISTRY.register("condensed_pyro", CondensedPyroItem::new);
		MORA = REGISTRY.register("mora", MoraItem::new);
		CONDENSED_CRYO = REGISTRY.register("condensed_cryo", CondensedCryoItem::new);
		CRYO_SWORD = REGISTRY.register("cryo_sword", CryoSwordItem::new);
		PYRO_SWORD = REGISTRY.register("pyro_sword", PyroSwordItem::new);
		POLAR_STAR = REGISTRY.register("polar_star", PolarStarItem::new);
		TARTAGLIA_SPAWN_EGG = REGISTRY.register("tartaglia_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.TARTAGLIA, -6724096, -16737793, new Item.Properties()));
		PYRO_PICKAXE = REGISTRY.register("pyro_pickaxe", PyroPickaxeItem::new);
		ELECTRO_CRYSTAL_ORE = block(ErModBlocks.ELECTRO_CRYSTAL_ORE);
		ELECTRO_CRYSTAL = REGISTRY.register("electro_crystal", ElectroCrystalItem::new);
		CONDENSED_ELECTRO = REGISTRY.register("condensed_electro", CondensedElectroItem::new);
		SUMERU_ROSE = block(ErModBlocks.SUMERU_ROSE);
		ELECTRO_SWORD = REGISTRY.register("electro_sword", ElectroSwordItem::new);
		DENDRO_SWORD = REGISTRY.register("dendro_sword", DendroSwordItem::new);
		COR_LAPIS_ORE = block(ErModBlocks.COR_LAPIS_ORE);
		COR_LAPIS = REGISTRY.register("cor_lapis", CorLapisItem::new);
		CONDENSED_GEO = REGISTRY.register("condensed_geo", CondensedGeoItem::new);
		GEO_SWORD = REGISTRY.register("geo_sword", GeoSwordItem::new);
		GEO_PICKAXE = REGISTRY.register("geo_pickaxe", GeoPickaxeItem::new);
		DANDELION_SEED = REGISTRY.register("dandelion_seed", DandelionSeedItem::new);
		CONDENSED_ANEMO = REGISTRY.register("condensed_anemo", CondensedAnemoItem::new);
		CONDENSED_DENDRO = REGISTRY.register("condensed_dendro", CondensedDendroItem::new);
		ANEMO_SWORD = REGISTRY.register("anemo_sword", AnemoSwordItem::new);
		LOTUS_HEAD = block(ErModBlocks.LOTUS_HEAD);
		CONDENSED_HYDRO = REGISTRY.register("condensed_hydro", CondensedHydroItem::new);
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
		HILICHURL_SPAWN_EGG = REGISTRY.register("hilichurl_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.HILICHURL, -13421773, -6780581, new Item.Properties()));
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
		ELECTRO_CICIN_SPAWN_EGG = REGISTRY.register("electro_cicin_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.ELECTRO_CICIN, -10092340, -13434778, new Item.Properties()));
		FATUI_ELECTRO_CICIN_MAGE_SPAWN_EGG = REGISTRY.register("fatui_electro_cicin_mage_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.FATUI_ELECTRO_CICIN_MAGE, -13434778, -6736897, new Item.Properties()));
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
		BLOSSOM_OF_WEALTH_SPAWN_EGG = REGISTRY.register("blossom_of_wealth_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.BLOSSOM_OF_WEALTH, -256, -3368704, new Item.Properties()));
		BLOSSOM_OF_REVELATION_SPAWN_EGG = REGISTRY.register("blossom_of_revelation_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.BLOSSOM_OF_REVELATION, -6684673, -10040065, new Item.Properties()));
		BUTTERFLY_SPAWN_EGG = REGISTRY.register("butterfly_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.BUTTERFLY, -16729601, -15654060, new Item.Properties()));
		BUTTERFLY_WINGS = REGISTRY.register("butterfly_wings", ButterflyWingsItem::new);
		ARTIFACT_TRANSMUTER = block(ErModBlocks.ARTIFACT_TRANSMUTER);
	}
	// Start of user code block custom items
	public static final DeferredItem<Item> WANDERERS_ADVICE = REGISTRY.register("wanderers_advice", () -> new ExperienceBook(new Item.Properties(), 40));
	public static final DeferredItem<Item> ADVENTURES_EXPERIENCE = REGISTRY.register("adventurers_experience", () -> new ExperienceBook(new Item.Properties(), 200));
	public static final DeferredItem<Item> HEROS_WIT = REGISTRY.register("heros_wit", () -> new ExperienceBook(new Item.Properties().rarity(Rarity.UNCOMMON), 800));
	public static final DeferredItem<Item> LIZARD_TAIL = REGISTRY.register("lizard_tail", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> FROG = REGISTRY.register("frog", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> ENCHANTED_MYSTIC_ENHANCEMENT_ORE = REGISTRY.register("enchanted_mystic_enhancement_ore", () -> new Item(new Item.Properties().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
	public static final DeferredItem<Item> LUCKY_DOGS_CLOVER = REGISTRY.register("lucky_dogs_clover", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.LUCKY_DOG));
	public static final DeferredItem<Item> LUCKY_DOGS_EAGLE_FEATHER = REGISTRY.register("lucky_dogs_eagle_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.LUCKY_DOG));
	public static final DeferredItem<Item> LUCKY_DOGS_HOURGLASS = REGISTRY.register("lucky_dogs_hourglass", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.LUCKY_DOG));
	public static final DeferredItem<Item> LUCKY_DOGS_GOBLET = REGISTRY.register("lucky_dogs_goblet", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.LUCKY_DOG));
	public static final DeferredItem<Item> LUCKY_DOGS_SILVER_CIRCLET = REGISTRY.register("lucky_dogs_silver_circlet", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.LUCKY_DOG));
	public static final DeferredItem<Item> SANCTIFYING_UNCTION = REGISTRY.register("sanctifying_unction", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SANCTIFYING_ESSENCE = REGISTRY.register("sanctifying_essence", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> ELECTRO_SLIME_SPAWN_EGG = REGISTRY.register("electro_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.ELECTRO_SLIME, 0x9933ff, 0x6600cc, new Item.Properties()));
	public static final DeferredItem<Item> PYRO_SLIME_SPAWN_EGG = REGISTRY.register("pyro_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.PYRO_SLIME, 0xff6600, 0xcc0000, new Item.Properties()));
	public static final DeferredItem<Item> GEO_SLIME_SPAWN_EGG = REGISTRY.register("geo_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.GEO_SLIME, 0xcc9900, 0x996600, new Item.Properties()));
	public static final DeferredItem<Item> CRYO_SLIME_SPAWN_EGG = REGISTRY.register("cryo_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.CRYO_SLIME, 0x00ffff, 0xccffff, new Item.Properties()));
	public static final DeferredItem<Item> HYDRO_SLIME_SPAWN_EGG = REGISTRY.register("hydro_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.HYDRO_SLIME, 0x3366ff, 0x0000cc, new Item.Properties()));
	public static final DeferredItem<Item> DENDRO_SLIME_SPAWN_EGG = REGISTRY.register("dendro_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.DENDRO_SLIME, 0x00ff00, 0x009900, new Item.Properties()));
	public static final DeferredItem<Item> ANEMO_SLIME_SPAWN_EGG = REGISTRY.register("anemo_slime_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.ANEMO_SLIME, 0x33ffcc, 0xccffcc, new Item.Properties()));
	public static final DeferredItem<Item> ADVENTURERS_FLOWER = REGISTRY.register("adventurers_flower", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.ADVENTURER));
	public static final DeferredItem<Item> ADVENTURERS_TAIL_FEATHER = REGISTRY.register("adventurers_tail_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.ADVENTURER));
	public static final DeferredItem<Item> ADVENTURERS_POCKET_WATCH = REGISTRY.register("adventurers_pocket_watch", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.ADVENTURER));
	public static final DeferredItem<Item> ADVENTURERS_GOLDEN_GOBLET = REGISTRY.register("adventurers_golden_goblet", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.ADVENTURER));
	public static final DeferredItem<Item> ADVENTURERS_BANDANA = REGISTRY.register("adventurers_bandana", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.ADVENTURER));
	public static final DeferredItem<Item> SLIME_CONDENSATE = REGISTRY.register("slime_condensate", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SLIME_SECRETIONS = REGISTRY.register("slime_secretions", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SLIME_CONCENTRATE = REGISTRY.register("slime_concentrate", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> TRAVELING_DOCTORS_SILVER_LOTUS = REGISTRY.register("traveling_doctors_silver_lotus", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final DeferredItem<Item> TRAVELING_DOCTORS_OWL_FEATHER = REGISTRY.register("traveling_doctors_owl_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final DeferredItem<Item> TRAVELING_DOCTORS_POCKET_WATCH = REGISTRY.register("traveling_doctors_pocket_watch", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final DeferredItem<Item> TRAVELING_DOCTORS_MEDICINE_POT = REGISTRY.register("traveling_doctors_medicine_pot", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final DeferredItem<Item> TRAVELING_DOCTORS_HANDKERCHIEF = REGISTRY.register("traveling_doctors_handkerchief", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.TRAVELING_DOCTOR));
	public static final DeferredItem<Item> DULL_BLADE = REGISTRY.register("dull_blade", () -> new SwordItem(ErTiers.STAR_1, new Item.Properties().attributes(SwordItem.createAttributes(ErTiers.STAR_1, 3f, -2.4f))));
	public static final DeferredItem<Item> SILVER_SWORD = REGISTRY.register("silver_sword", () -> new SwordItem(ErTiers.STAR_2, new Item.Properties().attributes(SwordItem.createAttributes(ErTiers.STAR_2, 3f, -2.4f))));
	public static final DeferredItem<Item> WASTER_GREATSWORD = REGISTRY.register("waster_greatsword", () -> new Claymore(ErTiers.STAR_1, new Item.Properties().attributes(Claymore.createAttributes(ErTiers.STAR_1, 3f, -2.8f))));
	public static final DeferredItem<Item> DUST_OF_AZOTH = REGISTRY.register("dust_of_azoth", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> BERSERKERS_ROSE = REGISTRY.register("berserkers_rose", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.BERSERKER));
	public static final DeferredItem<Item> BERSERKERS_INDIGO_FEATHER = REGISTRY.register("berserkers_indigo_feather", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.BERSERKER));
	public static final DeferredItem<Item> BERSERKERS_TIMEPIECE = REGISTRY.register("berserkers_timepiece", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.BERSERKER));
	public static final DeferredItem<Item> BERSERKERS_BONE_GOBLET = REGISTRY.register("berserkers_bone_goblet", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.BERSERKER));
	public static final DeferredItem<Item> BERSERKERS_BATTLE_MASK = REGISTRY.register("berserkers_battle_mask", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.BERSERKER));
	public static final DeferredItem<Item> COOL_STEEL = REGISTRY.register("cool_steel", () -> new SwordItem(ErTiers.STAR_3,
			new Item.Properties().component(DataComponentsRegister.WEAPON_REFINEMENT.get(), new WeaponRefinement(WeaponAbilityRegister.COOL_STEEL, ErModItems.COOL_STEEL, 1)).attributes(SwordItem.createAttributes(ErTiers.STAR_3, 3f, -2.4f))));
	public static final DeferredItem<Item> DARK_IRON_SWORD = REGISTRY.register("dark_iron_sword", () -> new SwordItem(ErTiers.STAR_3,
			new Item.Properties().component(DataComponentsRegister.WEAPON_REFINEMENT.get(), new WeaponRefinement(WeaponAbilityRegister.DARK_IRON, ErModItems.DARK_IRON_SWORD, 1)).attributes(SwordItem.createAttributes(ErTiers.STAR_3, 3f, -2.4f))));
	public static final DeferredItem<Item> UNOWNED_VISION = REGISTRY.register("unowned_vision", EmptyVision::new);
	public static final DeferredItem<Item> PYRO_VISION = REGISTRY.register("pyro_vision", () -> new Vision(Element.Category.PYRO));
	public static final DeferredItem<Item> CRYO_VISION = REGISTRY.register("cryo_vision", () -> new Vision(Element.Category.CRYO));
	public static final DeferredItem<Item> ANEMO_VISION = REGISTRY.register("anemo_vision", () -> new Vision(Element.Category.ANEMO));
	public static final DeferredItem<Item> GEO_VISION = REGISTRY.register("geo_vision", () -> new Vision(Element.Category.GEO));
	public static final DeferredItem<Item> HYDRO_VISION = REGISTRY.register("hydro_vision", () -> new Vision(Element.Category.HYDRO));
	public static final DeferredItem<Item> DENDRO_VISION = REGISTRY.register("dendro_vision", () -> new Vision(Element.Category.DENDRO));
	public static final DeferredItem<Item> ELECTRO_VISION = REGISTRY.register("electro_vision", () -> new Vision(Element.Category.ELECTRO));
	public static final DeferredItem<Item> FIRM_ARROWHEAD = REGISTRY.register("firm_arrowhead", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SHARP_ARROWHEAD = REGISTRY.register("sharp_arrowhead", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> WEATHERED_ARROWHEAD = REGISTRY.register("weathered_arrowhead", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> PYRO_WHOPPERFLOWER_FRUIT = REGISTRY.register("pyro_whopperflower_fruit", () -> new WhopperflowerFruit(ShieldRegistry.PYRO_WHOPPERFLOWER));
	public static final DeferredItem<Item> WHOPPERFLOWER_NECTAR = REGISTRY.register("whopperflower_nectar", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SHIMMERING_NECTAR = REGISTRY.register("shimmering_nectar", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> ENERGY_NECTAR = REGISTRY.register("energy_nectar", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> PYRO_FLOWER_SPAWN_EGG = REGISTRY.register("pyro_flower_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.PYRO_WHOPPERFLOWER, 0xff0000, 0xff9900, new Item.Properties()));
	public static final DeferredItem<Item> BURNING_DIRT = block(ErModBlocks.BURNING_DIRT);
	public static final DeferredItem<Item> PYRO_HOE = REGISTRY.register("pyro_hoe", () -> new ElementalHoe(Element.Category.PYRO));
	public static final DeferredItem<Item> ELECTRO_HOE = REGISTRY.register("electro_hoe", () -> new ElementalHoe(Element.Category.ELECTRO));
	public static final DeferredItem<Item> CRYO_HOE = REGISTRY.register("cryo_hoe", () -> new ElementalHoe(Element.Category.CRYO));
	public static final DeferredItem<Item> SWEET_FLOWER = block(ErModBlocks.SWEET_FLOWER);
	public static final DeferredItem<Item> WHOPPERFLOWER_SEED = REGISTRY.register("whopperflower_seed", WhopperflowerSeedItem::new);
	public static final DeferredItem<Item> CRYO_WHOPPERFLOWER_FRUIT = REGISTRY.register("cryo_whopperflower_fruit", () -> new WhopperflowerFruit(ShieldRegistry.CRYO_WHOPPERFLOWER));
	public static final ElementalArmorItem.Group ANEMO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.ANEMO, Element.Category.ANEMO);
	public static final ElementalArmorItem.Group HYDRO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.HYDRO, Element.Category.HYDRO);
	public static final ElementalArmorItem.Group CRYO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.CRYO, Element.Category.CRYO);
	public static final ElementalArmorItem.Group ELECTRO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.ELECTRO, Element.Category.ELECTRO);
	public static final ElementalArmorItem.Group PYRO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.PYRO, Element.Category.PYRO);
	public static final ElementalArmorItem.Group DENDRO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.DENDRO, Element.Category.DENDRO);
	public static final ElementalArmorItem.Group GEO_ARMOR = ElementalArmorItem.Group.createBasic(REGISTRY, ArmorMaterialsRegister.GEO, Element.Category.GEO);
	public static final DeferredItem<Item> CRYO_FLOWER_SPAWN_EGG = REGISTRY.register("cryo_flower_spawn_egg", () -> new DeferredSpawnEggItem(ErModEntities.CRYO_WHOPPERFLOWER, 0x00ccff, 0xccffff, new Item.Properties()));
	public static final DeferredItem<Item> MORA_BAG = REGISTRY.register("mora_bag", MoraBagItemPlus::new);
	public static final DeferredItem<Item> SCHOLARS_BOOKMARK = REGISTRY.register("scholars_bookmark", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.SCHOLAR));
	public static final DeferredItem<Item> SCHOLARS_QUILL_PEN = REGISTRY.register("scholars_quill_pen", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.SCHOLAR));
	public static final DeferredItem<Item> SCHOLARS_CLOCK = REGISTRY.register("scholars_clock", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.SCHOLAR));
	public static final DeferredItem<Item> SCHOLARS_INK_CUP = REGISTRY.register("scholars_ink_cup", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.SCHOLAR));
	public static final DeferredItem<Item> SCHOLARS_LENS = REGISTRY.register("scholars_lens", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.SCHOLAR));
	public static final DeferredItem<Item> GAMBLERS_BROOCH = REGISTRY.register("gamblers_brooch", () -> new Artifact(ArtifactSlot.FLOWER_OF_LIFE, ArtifactEffectRegistry.GAMBLER));
	public static final DeferredItem<Item> GAMBLERS_FEATHER_ACCESSORY = REGISTRY.register("gamblers_feather_accessory", () -> new Artifact(ArtifactSlot.PLUME_OF_DEATH, ArtifactEffectRegistry.GAMBLER));
	public static final DeferredItem<Item> GAMBLERS_POCKET_WATCH = REGISTRY.register("gamblers_pocket_watch", () -> new Artifact(ArtifactSlot.SAND_OF_EON, ArtifactEffectRegistry.GAMBLER));
	public static final DeferredItem<Item> GAMBLERS_DICE_CUP = REGISTRY.register("gamblers_dice_cup", () -> new Artifact(ArtifactSlot.GOBLET_OF_EONOTHEM, ArtifactEffectRegistry.GAMBLER));
	public static final DeferredItem<Item> GAMBLERS_EARRINGS = REGISTRY.register("gamblers_earrings", () -> new Artifact(ArtifactSlot.CIRCLET_OF_LOGOS, ArtifactEffectRegistry.GAMBLER));
	public static final DeferredItem<Item> FRAGILE_RESIN = REGISTRY.register("fragile_resin", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> ORIGINAL_RESIN = REGISTRY.register("original_resin", () -> new Item(new Item.Properties()));

	@EventBusSubscriber(value = Dist.CLIENT)
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
			registerVision(UNOWNED_VISION.get());
			registerVision(PYRO_VISION.get());
			registerVision(CRYO_VISION.get());
			registerVision(ANEMO_VISION.get());
			registerVision(GEO_VISION.get());
			registerVision(HYDRO_VISION.get());
			registerVision(DENDRO_VISION.get());
			registerVision(ELECTRO_VISION.get());
		}

		private static void registerVision(Item vision) {
			ItemProperties.register(vision, ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "vision_frame"), (itemStackToRender, clientWorld, entity, itemEntityId) -> Vision.Frame.getId(itemStackToRender));
		}

		private static void registerBowItem(Item bowItem) {
			ItemProperties.register(bowItem, ResourceLocation.parse("er:ascension"), (itemStackToRender, clientWorld, entity, itemEntityId) -> getAscension(itemStackToRender));
			ItemProperties.register(bowItem, ResourceLocation.parse("er:pull"), (itemStackToRender, clientWorld, entity, itemEntityId) -> {
				if (entity == null) {
					return 0.0F;
				} else {
					return entity.getUseItem() != itemStackToRender ? 0.0F : (float) (itemStackToRender.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
				}
			});
			ItemProperties.register(bowItem, ResourceLocation.parse("er:pulling"), (itemStackToRender, clientWorld, entity, itemEntityId) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemStackToRender ? 1.0F : 0.0F);
		}

		private static void registerWeapon(Item item) {
			ItemProperties.register(item, ResourceLocation.parse("er:ascension"), (itemStackToRender, clientWorld, entity, itemEntityId) -> getAscension(itemStackToRender));
		}

		private static int getAscension(ItemStack itemStack) {
			WeaponLevelData data = itemStack.get(DataComponentsRegister.WEAPON_LEVEL);
			if (data == null)
				return 0;
			return data.ascension();
		}
	}

	// End of user code block custom items
	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block) {
		return block(block, new Item.Properties());
	}

	private static DeferredItem<Item> block(DeferredHolder<Block, Block> block, Item.Properties properties) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), properties));
	}

	@EventBusSubscriber(Dist.CLIENT)
	public static class ItemsClientSideHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void clientLoad(FMLClientSetupEvent event) {
			event.enqueueWork(() -> {
				ItemProperties.register(RARITY_GEMSTONE.get(), ResourceLocation.parse("er:rarity_gemstone_item_count"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) RarityGemstone_CountProcedure.execute(itemStackToRender));
				ItemProperties.register(WOODEN_CLUB.get(), ResourceLocation.parse("er:wooden_club_pyro"), (itemStackToRender, clientWorld, entity, itemEntityId) -> (float) WoodenClubPropertyValueProviderProcedure.execute(itemStackToRender));
			});
		}
	}
}