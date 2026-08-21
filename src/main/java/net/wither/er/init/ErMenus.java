package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.world.inventory.*;

public class ErMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, ErMod.MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<WeaponEnhanceGuiMenu>> WEAPON_ENHANCE_GUI = REGISTRY.register("weapon_enhance_gui", () -> IMenuTypeExtension.create(WeaponEnhanceGuiMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ArtifactTransmuterGuiMenu>> ARTIFACT_TRANSMUTER_GUI = REGISTRY.register("artifact_transmuter_gui", () -> IMenuTypeExtension.create(ArtifactTransmuterGuiMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<AlchemyGuiMenu>> ALCHEMY_GUI = REGISTRY.register("alchemy", () -> IMenuTypeExtension.create(AlchemyGuiMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<LeyLineMapGuiMenu>> LEY_LINE_MAP = REGISTRY.register("ley_line_map", () -> IMenuTypeExtension.create(LeyLineMapGuiMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<ErEquipmentGUIMenu>> EQUIPMENT = REGISTRY.register("equipment", () -> IMenuTypeExtension.create(ErEquipmentGUIMenu::new));
}
