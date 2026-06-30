package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.wither.er.elements.*;

import java.util.function.Supplier;

public class ElementRegistry {
    public static final DeferredRegister<Element> ELEMENTS = DeferredRegister.create(AdditionalRegistries.ELEMENT_REGISTRY, ErMod.MODID);
    public static final Supplier<Element> PYRO = ELEMENTS.register("pyro", Pyro::new);
    public static final Supplier<Element> HYDRO = ELEMENTS.register("hydro", Hydro::new);
    public static final Supplier<Element> ANEMO = ELEMENTS.register("anemo", Anemo::new);
    public static final Supplier<Element> ELECTRO = ELEMENTS.register("electro", Electro::new);
    public static final Supplier<Element> DENDRO = ELEMENTS.register("dendro", Dendro::new);
    public static final Supplier<Element> QUICKEN = ELEMENTS.register("quicken", Quicken::new);
    public static final Supplier<Element> CRYO = ELEMENTS.register("cryo", Cryo::new);
    public static final Supplier<Element> FROZEN = ELEMENTS.register("frozen", Frozen::new);
    public static final Supplier<Element> GEO = ELEMENTS.register("geo", Geo::new);
    public static final Supplier<Element> BURNING = ELEMENTS.register("burning", Burning::new);
    public static final Supplier<Element> THUNDER_SHIELD = ELEMENTS.register("thunder_shield", ThunderShieldElement::new);
    public static final Supplier<Element> SLIME_DENDRO = ELEMENTS.register("slime_dendro", Dendro::new);
}
