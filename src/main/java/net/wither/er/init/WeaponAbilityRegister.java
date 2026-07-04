package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.item.data.weapon.FunctionalAbilities;
import net.wither.er.item.data.weapon.DamageAbility;
import net.wither.er.item.data.weapon.ReactionAbility;

public class WeaponAbilityRegister {
    public static final DeferredRegister<Object> WEAPON_ABILITIES = DeferredRegister.create(AdditionalRegistries.WEAPON_ABILITY_REGISTRY, ErMod.MODID);

    public static final Holder<Object> COOL_STEEL = WEAPON_ABILITIES.register("bane_of_water_and_ice", () -> (DamageAbility) (FunctionalAbilities::coolSteel));
    public static final Holder<Object> DARK_IRON = WEAPON_ABILITIES.register("overloaded", () -> (ReactionAbility) (FunctionalAbilities::darkIronSword));
}
