package net.wither.er.init;


import com.mojang.serialization.Codec;
import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.item.data.weapon.*;

import java.util.function.Supplier;

import static net.mcreator.er.ErMod.MODID;

public class WeaponAbilityRegister {
    public static final ResourceKey<Registry<Object>> ABILITY = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "ability"));
    public static final DeferredRegister<Object> ABILITIES = DeferredRegister.create(ABILITY, ErMod.MODID);
    public static Supplier<IForgeRegistry<Object>> ABILITY_SUPP ;
    public static IForgeRegistry<Object> ABILITY_REGISTRY ;

    public static final Codec<Holder<Object>> CODEC  = RegistryFixedCodec.create(ABILITY);

    static {
        ABILITY_SUPP = ABILITIES.makeRegistry(
                () -> new RegistryBuilder<>()
                        .setName(ABILITY.location())
        );
    }

    public static final RegistryObject<Object> EMPTY = ABILITIES.register("empty", Object::new);

    public static final RegistryObject<Object> ANEMO_INFUSION = ABILITIES.register("anemo_infusion", () -> (InfusionAbility)(FunctionalAbilities::anemoInfusion));
    public static final RegistryObject<Object> PYRO_INFUSION = ABILITIES.register("pyro_infusion", () -> (InfusionAbility)(FunctionalAbilities::pyroInfusion));
    public static final RegistryObject<Object> HYDRO_INFUSION = ABILITIES.register("hydro_infusion", () -> (InfusionAbility)(FunctionalAbilities::hydroInfusion));
    public static final RegistryObject<Object> DENDRO_INFUSION = ABILITIES.register("dendro_infusion", () -> (InfusionAbility)(FunctionalAbilities::dendroInfusion));
    public static final RegistryObject<Object> CRYO_INFUSION = ABILITIES.register("cryo_infusion", () -> (InfusionAbility)(FunctionalAbilities::cryoInfusion));
    public static final RegistryObject<Object> ELECTRO_INFUSION = ABILITIES.register("electro_infusion", () -> (InfusionAbility)(FunctionalAbilities::electroInfusion));
    public static final RegistryObject<Object> GEO_INFUSION = ABILITIES.register("geo_infusion", () -> (InfusionAbility)(FunctionalAbilities::geoInfusion));
    public static final RegistryObject<Object> WOODEN_CLUB = ABILITIES.register("wooden_club", FunctionalAbilities.WoodenClub::new);

    public static final RegistryObject<Object> COOL_STEEL = ABILITIES.register("bane_of_water_and_ice", () -> (DamageAbility) (FunctionalAbilities::coolSteel));
    public static final RegistryObject<Object> DARK_IRON = ABILITIES.register("overloaded", () -> (ReactionAbility) (FunctionalAbilities::darkIronSword));
    public static final RegistryObject<Object> JOURNEY = ABILITIES.register("journey", () -> (EnergyOrbPickupAbility) (FunctionalAbilities::travelersHandySword));
    public static final RegistryObject<Object> DETERMINATION = ABILITIES.register("determination", () -> (OnBurstAbility) (FunctionalAbilities::skyriderSword));
    public static final RegistryObject<Object> GASH = ABILITIES.register("gash", () -> (DamageAbility) (FunctionalAbilities::filletBlade));
}