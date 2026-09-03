package net.wither.er.init;

import net.mcreator.er.ERConfig;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.item.ElementalArmorItem;
import net.wither.er.item.Vision;
import net.wither.er.item.data.artifactdata.MainAffix;
import net.wither.er.item.data.artifactdata.MinorAffix;

import java.util.ArrayList;
import java.util.List;

import static net.mcreator.er.init.ErModItems.*;

@EventBusSubscriber()
public class ExtraTabs {

    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ErMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VISION = REGISTRY.register("vision",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.er.vision")).icon(
                    () -> new ItemStack(UNOWNED_VISION.get())).displayItems((parameters, tabData) -> {
                addVisions(tabData, Vision.Frame.MONDSTADT);
                addVisions(tabData, Vision.Frame.LIYUE);
                addVisions(tabData, Vision.Frame.MOON_WHEEL);
            }).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARTIFACTS = REGISTRY.register("artifacts",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.er.artifacts")).icon(() -> new ItemStack(LUCKY_DOGS_CLOVER.get())).displayItems((parameters, tabData) -> {
                tabData.accept(RARITY_GEMSTONE);
                tabData.accept(MINOR_UPGRADES);
                tabData.accept(LUCKY_DOGS_CLOVER);
                tabData.accept(LUCKY_DOGS_EAGLE_FEATHER);
                tabData.accept(LUCKY_DOGS_HOURGLASS);
                tabData.accept(LUCKY_DOGS_GOBLET);
                tabData.accept(LUCKY_DOGS_SILVER_CIRCLET);
                tabData.accept(ADVENTURERS_FLOWER);
                tabData.accept(ADVENTURERS_TAIL_FEATHER);
                tabData.accept(ADVENTURERS_POCKET_WATCH);
                tabData.accept(ADVENTURERS_GOLDEN_GOBLET);
                tabData.accept(ADVENTURERS_BANDANA);
                tabData.accept(TRAVELING_DOCTORS_SILVER_LOTUS);
                tabData.accept(TRAVELING_DOCTORS_OWL_FEATHER);
                tabData.accept(TRAVELING_DOCTORS_POCKET_WATCH);
                tabData.accept(TRAVELING_DOCTORS_MEDICINE_POT);
                tabData.accept(TRAVELING_DOCTORS_HANDKERCHIEF);
                tabData.accept(BERSERKERS_ROSE);
                tabData.accept(BERSERKERS_INDIGO_FEATHER);
                tabData.accept(BERSERKERS_TIMEPIECE);
                tabData.accept(BERSERKERS_BONE_GOBLET);
                tabData.accept(BERSERKERS_BATTLE_MASK);
                tabData.accept(SCHOLARS_BOOKMARK);
                tabData.accept(SCHOLARS_QUILL_PEN);
                tabData.accept(SCHOLARS_CLOCK);
                tabData.accept(SCHOLARS_INK_CUP);
                tabData.accept(SCHOLARS_LENS);
                tabData.accept(GAMBLERS_BROOCH);
                tabData.accept(GAMBLERS_FEATHER_ACCESSORY);
                tabData.accept(GAMBLERS_POCKET_WATCH);
                tabData.accept(GAMBLERS_DICE_CUP);
                tabData.accept(GAMBLERS_EARRINGS);
                tabData.accept(INSTRUCTORS_BROOCH);
                tabData.accept(INSTRUCTORS_FEATHER_ACCESSORY);
                tabData.accept(INSTRUCTORS_POCKET_WATCH);
                tabData.accept(INSTRUCTORS_TEA_CUP);
                tabData.accept(INSTRUCTORS_CAP);
                tabData.accept(MEDAL_OF_THE_BRAVE);
                tabData.accept(PROSPECT_OF_THE_BRAVE);
                tabData.accept(FORTITUDE_OF_THE_BRAVE);
                tabData.accept(OUTSET_OF_THE_BRAVE);
                tabData.accept(CROWN_OF_THE_BRAVE);
                List<String> attrs = new ArrayList<>();
                addMainToTab(ERConfig.FLOWER_OF_LIFE_MAIN_ATTR.get(), tabData, attrs);
                addMainToTab(ERConfig.PLUME_OF_DEATH_ATTR.get(), tabData, attrs);
                addMainToTab(ERConfig.SANDS_OF_EON_ATTR.get(), tabData, attrs);
                addMainToTab(ERConfig.GOBLET_OF_EONOTHEM_ATTR.get(), tabData, attrs);
                addMainToTab(ERConfig.CIRCLET_OF_LOGOS_ATTR.get(), tabData, attrs);
                addMinorToTab(ERConfig.MINOR_ATTR.get(), tabData, new ArrayList<>());
            }).withSearchBar().withTabsBefore(ErModTabs.ER_WEAPON.getId()).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == ErModTabs.ER_MATERIALS.getKey()) {
            tabData.accept(SWEET_FLOWER);
            tabData.accept(WHOPPERFLOWER_SEED);
            tabData.accept(PRIME_ICE_RESOURCE);
            tabData.accept(FRAGILE_RESIN);
            tabData.accept(ORIGINAL_RESIN);
			tabData.accept(WANDERERS_ADVICE);
			tabData.accept(ADVENTURES_EXPERIENCE);
			tabData.accept(HEROS_WIT);
			tabData.accept(FROG);
			tabData.accept(LIZARD_TAIL);
			tabData.accept(DUST_OF_AZOTH);
			tabData.accept(SLIME_CONDENSATE);
			tabData.accept(SLIME_SECRETIONS);
			tabData.accept(SLIME_CONCENTRATE);
            tabData.accept(FIRM_ARROWHEAD);
            tabData.accept(SHARP_ARROWHEAD);
            tabData.accept(WEATHERED_ARROWHEAD);
            tabData.accept(WHOPPERFLOWER_NECTAR);
            tabData.accept(SHIMMERING_NECTAR);
            tabData.accept(ENERGY_NECTAR);
			tabData.accept(ENCHANTED_MYSTIC_ENHANCEMENT_ORE);
            tabData.accept(PYRO_WHOPPERFLOWER_FRUIT);
            tabData.accept(CRYO_WHOPPERFLOWER_FRUIT);
			ItemStack moraBag = new ItemStack(MORA_BAG.get()) ;
            moraBag.set(DataComponentsRegister.MORA_BAG.get(), 999999999);
			tabData.accept(moraBag);
			tabData.accept(SANCTIFYING_UNCTION);
			tabData.accept(SANCTIFYING_ESSENCE);
		} else if(tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(ELECTRO_SLIME_SPAWN_EGG);
			tabData.accept(GEO_SLIME_SPAWN_EGG);
			tabData.accept(PYRO_SLIME_SPAWN_EGG);
			tabData.accept(HYDRO_SLIME_SPAWN_EGG);
			tabData.accept(CRYO_SLIME_SPAWN_EGG);
			tabData.accept(DENDRO_SLIME_SPAWN_EGG);
			tabData.accept(ANEMO_SLIME_SPAWN_EGG);
            tabData.accept(PYRO_FLOWER_SPAWN_EGG);
            tabData.accept(CRYO_FLOWER_SPAWN_EGG);
		}else if(tabData.getTabKey() == ErModTabs.ER_WEAPON.getKey()) {
			tabData.accept(DULL_BLADE);
			tabData.accept(WASTER_GREATSWORD);
			tabData.accept(SILVER_SWORD);
            tabData.accept(COOL_STEEL);
            tabData.accept(DARK_IRON_SWORD);
		}else if(tabData.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS){
            tabData.accept(SWEET_FLOWER);
            tabData.accept(BURNING_DIRT);
            tabData.accept(WHOPPERFLOWER_SEED);
        } else if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            tabData.accept(PYRO_HOE);
            tabData.accept(ELECTRO_HOE);
            tabData.accept(CRYO_HOE);
            tabData.accept(GEO_HOE);
            tabData.accept(ANEMO_HOE);
            tabData.accept(DENDRO_HOE);
            tabData.accept(HYDRO_HOE);
        } else if (tabData.getTabKey() == CreativeModeTabs.COMBAT) {
            addArmorGroup(tabData, ANEMO_ARMOR);
            addArmorGroup(tabData, HYDRO_ARMOR);
            addArmorGroup(tabData, CRYO_ARMOR);
            addArmorGroup(tabData, ELECTRO_ARMOR);
            addArmorGroup(tabData, PYRO_ARMOR);
            addArmorGroup(tabData, DENDRO_ARMOR);
            addArmorGroup(tabData, GEO_ARMOR);
        } else if (tabData.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            tabData.accept(LINK_MECHANISM);
            tabData.accept(STORAGE_DEVICE);
            tabData.accept(AMBIENT_BOLLARD);
        }
    }

    private static void addVisions(CreativeModeTab.Output tabData, Vision.Frame frame){
        addVision(tabData, UNOWNED_VISION, frame);
        addVision(tabData, PYRO_VISION, frame);
        addVision(tabData, HYDRO_VISION, frame);
        addVision(tabData, ANEMO_VISION, frame);
        addVision(tabData, ELECTRO_VISION, frame);
        addVision(tabData, DENDRO_VISION, frame);
        addVision(tabData, CRYO_VISION, frame);
        addVision(tabData, GEO_VISION, frame);
    }

    private static void addVision(CreativeModeTab.Output tabData, DeferredItem<Item> item, Vision.Frame frame){
        ItemStack itemStack = new ItemStack(item.get());
        itemStack.set(DataComponentsRegister.VISION_FRAME, frame);
        tabData.accept(itemStack);
    }

    private static void addArmorGroup(CreativeModeTab.Output tabData, ElementalArmorItem.Group group){
        tabData.accept(group.helmet());
        tabData.accept(group.chest());
        tabData.accept(group.leggings());
        tabData.accept(group.boots());
    }

	private static void addMainToTab(List<? extends String> mainType, CreativeModeTab.Output tabData, List<String> attrs) {
		String[] effects_type;
		effects_type = mainType.toArray(new String[0]);
		for (int i = effects_type.length - 1; i >= 0; i--) {
			ItemStack main_affix = new ItemStack(MAIN_AFFIX_SHARD.get());
			if (attrs.contains(effects_type[i].replaceAll(" ", "")))
				continue;
			attrs.add(effects_type[i]);
			String[] type = effects_type[i].replaceAll(" ", "").split(",");
			MainAffix affix = MainAffix.create(type);
			if (affix.amount() == 0)
				continue;
			main_affix.update(DataComponentsRegister.ARTIFACT_MAIN.get(),affix,a -> affix);
			tabData.accept(main_affix);
		}
	}

	private static void addMinorToTab(List<? extends String> minorType, CreativeModeTab.Output tabData, List<String> attrs) {
		String[] effects_type;
		effects_type = minorType.toArray(new String[0]);
		for (int i = effects_type.length - 1; i >= 0; i--) {
			ItemStack minor_affix = new ItemStack(MINOR_AFFIX_SHARD.get());
			if (attrs.contains(effects_type[i].replaceAll(" ", "")))
				continue;
			attrs.add(effects_type[i]);
			String[] type = effects_type[i].replaceAll(" ", "").split(",");
			MinorAffix affix = MinorAffix.create(type);
			if(affix.amount() == 0)
				continue;
			minor_affix.update(DataComponentsRegister.ARTIFACT_MINOR.get(),affix,a -> affix);
			tabData.accept(minor_affix);
		}
	}
}