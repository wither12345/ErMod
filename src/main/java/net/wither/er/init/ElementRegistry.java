package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.elements.*;

import java.util.function.Supplier;

import static net.mcreator.er.ErMod.MODID;

public class ElementRegistry {

    public static final ResourceKey<Registry<Element>> ELEMENT = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "elements"));
    public static final DeferredRegister<Element> ELEMENTS = DeferredRegister.create(ELEMENT, ErMod.MODID);

    public static Supplier<IForgeRegistry<Element>> ELEMENT_SUPP = ElementRegistry.ELEMENTS.makeRegistry(
            () -> new RegistryBuilder<Element>()
                    .setName(ELEMENT.location()));


    public static final RegistryObject<Element> PYRO = ELEMENTS.register("pyro", Pyro::new);
    public static final RegistryObject<Element> HYDRO = ELEMENTS.register("hydro", Hydro::new);
    public static final RegistryObject<Element> ANEMO = ELEMENTS.register("anemo", Anemo::new);
    public static final RegistryObject<Element> ELECTRO = ELEMENTS.register("electro", Electro::new);
    public static final RegistryObject<Element> DENDRO = ELEMENTS.register("dendro", Dendro::new);
    public static final RegistryObject<Element> QUICKEN = ELEMENTS.register("quicken", Quicken::new);
    public static final RegistryObject<Element> CRYO = ELEMENTS.register("cryo", Cryo::new);
    public static final RegistryObject<Element> FROZEN = ELEMENTS.register("frozen", Frozen::new);
    public static final RegistryObject<Element> GEO = ELEMENTS.register("geo", Geo::new);
    public static final RegistryObject<Element> BURNING = ELEMENTS.register("burning", Burning::new);
    public static final RegistryObject<Element> THUNDER_SHIELD = ELEMENTS.register("thunder_shield", ThunderShieldElement::new);
    public static final RegistryObject<Element> SLIME_DENDRO = ELEMENTS.register("slime_dendro", Dendro::new);
    public static final RegistryObject<Element> PYRO_WHOPPERFLOWER = ELEMENTS.register("pyro_whopper", Pyro::new);
    public static final RegistryObject<Element> CRYO_WHOPPERFLOWER = ELEMENTS.register("cryo_whopper", CryoWhopperflowerShieldElement::new);
}
