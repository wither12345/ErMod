package net.wither.er.init;

import net.mcreator.er.ERConfig;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModTabs;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

import static net.mcreator.er.init.ErModItems.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ExtraTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ErMod.MODID);
    public static final RegistryObject<CreativeModeTab> ER_MATERIALS = REGISTRY.register("vision",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.er.vision")).icon(
                    () -> new ItemStack(ErModItems.UNOWNED_VISION.get())).displayItems((parameters, tabData) -> {
                addVisions(tabData, 0);
                addVisions(tabData, 1);
                addVisions(tabData, 2);
            }).build());

	@SubscribeEvent
	public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
		if (tabData.getTabKey() == ErModTabs.ARTIFACTS.getKey()) {
			tabData.accept(ErModItems.LUCKY_DOGS_CLOVER);
			tabData.accept(ErModItems.LUCKY_DOGS_EAGLE_FEATHER);
			tabData.accept(ErModItems.LUCKY_DOGS_HOURGLASS);
			tabData.accept(ErModItems.LUCKY_DOGS_GOBLET);
			tabData.accept(ErModItems.LUCKY_DOGS_SILVER_CIRCLET);
			tabData.accept(ErModItems.ADVENTURERS_FLOWER);
			tabData.accept(ErModItems.ADVENTURERS_TAIL_FEATHER);
			tabData.accept(ErModItems.ADVENTURERS_POCKET_WATCH);
			tabData.accept(ErModItems.ADVENTURERS_GOLDEN_GOBLET);
			tabData.accept(ErModItems.ADVENTURERS_BANDANA);
			tabData.accept(ErModItems.TRAVELING_DOCTORS_SILVER_LOTUS);
			tabData.accept(ErModItems.TRAVELING_DOCTORS_OWL_FEATHER);
			tabData.accept(ErModItems.TRAVELING_DOCTORS_POCKET_WATCH);
			tabData.accept(ErModItems.TRAVELING_DOCTORS_MEDICINE_POT);
			tabData.accept(ErModItems.TRAVELING_DOCTORS_HANDKERCHIEF);
            tabData.accept(ErModItems.BERSERKERS_ROSE);
            tabData.accept(ErModItems.BERSERKERS_INDIGO_FEATHER);
            tabData.accept(ErModItems.BERSERKERS_TIMEPIECE);
            tabData.accept(ErModItems.BERSERKERS_BONE_GOBLET);
            tabData.accept(ErModItems.BERSERKERS_BATTLE_MASK);

			List<String> attrs = new ArrayList<>();
			addMainToTab(ERConfig.FLOWER_OF_LIFE_MAIN_ATTR.get(), tabData, attrs);
			addMainToTab(ERConfig.PLUME_OF_DEATH_ATTR.get(), tabData, attrs);
			addMainToTab(ERConfig.SANDS_OF_EON_ATTR.get(), tabData, attrs);
			addMainToTab(ERConfig.GOBLET_OF_EONOTHEM_ATTR.get(), tabData, attrs);
			addMainToTab(ERConfig.CIRCLET_OF_LOGOS_ATTR.get(), tabData, attrs);
			addMinorToTab(ERConfig.MINOR_ATTR.get(), tabData, new ArrayList<>());
		} else if (tabData.getTabKey() == ErModTabs.ER_MATERIALS.getKey()) {
            tabData.accept(WANDERERS_ADVICE);
            tabData.accept(ADVENTURES_EXPERIENCE);
            tabData.accept(HEROS_WIT);
            tabData.accept(FROG);
            tabData.accept(LIZARD_TAIL);
            tabData.accept(DUST_OF_AZOTH);
            tabData.accept(SLIME_CONDENSATE);
            tabData.accept(SLIME_SECRETIONS);
            tabData.accept(SLIME_CONCENTRATE);
            tabData.accept(ENCHANTED_MYSTIC_ENHANCEMENT_ORE);
            ItemStack moraBag = new ItemStack(MORA_BAG.get());
			moraBag.getOrCreateTag().putInt("moras", 999999999);
            tabData.accept(moraBag);
            tabData.accept(SANCTIFYING_UNCTION);
            tabData.accept(SANCTIFYING_ESSENCE);
        } else if(tabData.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
			tabData.accept(ErModItems.ELECTRO_SLIME_SPAWN_EGG);
			tabData.accept(ErModItems.GEO_SLIME_SPAWN_EGG);
			tabData.accept(ErModItems.PYRO_SLIME_SPAWN_EGG);
			tabData.accept(ErModItems.HYDRO_SLIME_SPAWN_EGG);
			tabData.accept(ErModItems.CRYO_SLIME_SPAWN_EGG);
			tabData.accept(ErModItems.DENDRO_SLIME_SPAWN_EGG);
			tabData.accept(ErModItems.ANEMO_SLIME_SPAWN_EGG);
		}else if(tabData.getTabKey() == ErModTabs.ER_WEAPON.getKey()) {
			tabData.accept(ErModItems.DULL_BLADE);
			tabData.accept(ErModItems.WASTER_GREATSWORD);
			tabData.accept(ErModItems.SILVER_SWORD);
            tabData.accept(ErModItems.COOL_STEEL);
            tabData.accept(ErModItems.DARK_IRON_SWORD);
		}
	}
    
    private static void addVisions(CreativeModeTab.Output tabData, int frame){
        addVision(tabData, ErModItems.PYRO_VISION, frame);
        addVision(tabData, ErModItems.HYDRO_VISION, frame);
        addVision(tabData, ErModItems.ANEMO_VISION, frame);
        addVision(tabData, ErModItems.ELECTRO_VISION, frame);
        addVision(tabData, ErModItems.DENDRO_VISION, frame);
        addVision(tabData, ErModItems.CRYO_VISION, frame);
        addVision(tabData, ErModItems.GEO_VISION, frame);
    }

    private static void addVision(CreativeModeTab.Output tabData, RegistryObject<Item> item, int frame){
        ItemStack itemStack = new ItemStack(item.get());
        itemStack.getOrCreateTag().putInt("frame", frame);
        tabData.accept(itemStack);
    }

	private static void addMainToTab(List<? extends String> mainType, BuildCreativeModeTabContentsEvent tabData, List<String> attrs) {
		String[] effects_type;
		effects_type = mainType.toArray(new String[0]);
		for (int i = effects_type.length - 1; i >= 0; i--) {
            ItemStack main_affix = new ItemStack(MAIN_AFFIX_SHARD.get());
            if (attrs.contains(effects_type[i].replaceAll(" ", "")))
                continue;
            attrs.add(effects_type[i].replaceAll(" ", ""));
			main_affix.getOrCreateTag().putString("affix", effects_type[i]);
			tabData.accept(main_affix);
        }
	}

	private static void addMinorToTab(List<? extends String> minorType, BuildCreativeModeTabContentsEvent tabData, List<String> attrs) {
		String[] effects_type;
		effects_type = minorType.toArray(new String[0]);
		for (int i = effects_type.length - 1; i >= 0; i--) {
            ItemStack minor_affix = new ItemStack(MINOR_AFFIX_SHARD.get());
            if (attrs.contains(effects_type[i].replaceAll(" ", "")))
                continue;
            attrs.add(effects_type[i].replaceAll(" ", ""));
			minor_affix.getOrCreateTag().putString("affix", effects_type[i]);
			tabData.accept(minor_affix);
        }
	}
}