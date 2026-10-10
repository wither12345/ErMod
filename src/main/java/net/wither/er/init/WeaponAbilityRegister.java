package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.item.data.weapon.*;

public class WeaponAbilityRegister {
    public static final DeferredRegister<Object> WEAPON_ABILITIES = DeferredRegister.create(AdditionalRegistries.WEAPON_ABILITY_REGISTRY, ErMod.MODID);

    public static final Holder<Object> ANEMO_INFUSION = WEAPON_ABILITIES.register("anemo_infusion", () -> (InfusionAbility)(FunctionalAbilities::anemoInfusion));
    public static final Holder<Object> PYRO_INFUSION = WEAPON_ABILITIES.register("pyro_infusion", () -> (InfusionAbility)(FunctionalAbilities::pyroInfusion));
    public static final Holder<Object> HYDRO_INFUSION = WEAPON_ABILITIES.register("hydro_infusion", () -> (InfusionAbility)(FunctionalAbilities::hydroInfusion));
    public static final Holder<Object> DENDRO_INFUSION = WEAPON_ABILITIES.register("dendro_infusion", () -> (InfusionAbility)(FunctionalAbilities::dendroInfusion));
    public static final Holder<Object> CRYO_INFUSION = WEAPON_ABILITIES.register("cryo_infusion", () -> (InfusionAbility)(FunctionalAbilities::cryoInfusion));
    public static final Holder<Object> ELECTRO_INFUSION = WEAPON_ABILITIES.register("electro_infusion", () -> (InfusionAbility)(FunctionalAbilities::electroInfusion));
    public static final Holder<Object> GEO_INFUSION = WEAPON_ABILITIES.register("geo_infusion", () -> (InfusionAbility)(FunctionalAbilities::geoInfusion));
    public static final Holder<Object> WOODEN_CLUB = WEAPON_ABILITIES.register("wooden_club", FunctionalAbilities.WoodenClub::new);

    public static final Holder<Object> COOL_STEEL = WEAPON_ABILITIES.register("bane_of_water_and_ice", () -> (DamageAbility) (FunctionalAbilities::coolSteel));
    public static final Holder<Object> DARK_IRON = WEAPON_ABILITIES.register("overloaded", () -> (ReactionAbility) (FunctionalAbilities::darkIronSword));
    public static final Holder<Object> JOURNEY = WEAPON_ABILITIES.register("journey", () -> (EnergyOrbPickupAbility) (FunctionalAbilities::travelersHandySword));
    public static final Holder<Object> DETERMINATION = WEAPON_ABILITIES.register("determination", () -> (OnBurstAbility) (FunctionalAbilities::skyriderSword));
    public static final Holder<Object> GASH = WEAPON_ABILITIES.register("gash", () -> (DamageAbility) (FunctionalAbilities::filletBlade));
}
