package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.world.inventory.*;
import net.wither.er.world.inventory.ErEquipmentGUIMenu;

public class ErMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, ErMod.MODID);
    public static final RegistryObject<MenuType<WeaponEnhanceGuiMenu>> WEAPON_ENHANCE_GUI = REGISTRY.register("weapon_enhance_gui", () -> IForgeMenuType.create(WeaponEnhanceGuiMenu::new));
    public static final RegistryObject<MenuType<ArtifactTransmuterGuiMenu>> ARTIFACT_TRANSMUTER_GUI = REGISTRY.register("artifact_transmuter_gui", () -> IForgeMenuType.create(ArtifactTransmuterGuiMenu::new));
    public static final RegistryObject<MenuType<AlchemyGuiMenu>> ALCHEMY_GUI = REGISTRY.register("alchemy", () -> IForgeMenuType.create(AlchemyGuiMenu::new));
    public static final RegistryObject<MenuType<AlchemyCraftGuiMenu>> ALCHEMY_CRAFT = REGISTRY.register("alchemy_craft", () -> IForgeMenuType.create(AlchemyCraftGuiMenu::new));
    public static final RegistryObject<MenuType<LeyLineMapGuiMenu>> LEY_LINE_MAP = REGISTRY.register("ley_line_map", () -> IForgeMenuType.create(LeyLineMapGuiMenu::new));
    public static final RegistryObject<MenuType<ErEquipmentGUIMenu>> EQUIPMENT = REGISTRY.register("equipment", () -> IForgeMenuType.create(ErEquipmentGUIMenu::new));
}
