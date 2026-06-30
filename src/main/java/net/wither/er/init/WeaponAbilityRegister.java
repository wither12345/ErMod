package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.item.data.weapon.CoolSteelAbility;
import net.wither.er.item.data.weapon.WeaponAbility;

public class WeaponAbilityRegister {
    public static final DeferredRegister<WeaponAbility> WEAPON_ABILITIES = DeferredRegister.create(AdditionalRegistries.WEAPON_ABILITY_REGISTRY, ErMod.MODID);

    public static final Holder<WeaponAbility> COOL_STEEL = WEAPON_ABILITIES.register("bane_of_water_and_ice", () -> (WeaponAbility) (CoolSteelAbility::modify));
}
