package net.wither.er.elements;

import java.util.Map;

public class CryoWhopperflowerShieldElement extends Cryo{
    public CryoWhopperflowerShieldElement() {
        super(Map.of(
                Category.PYRO, Element::amplifying2,
                Category.ELECTRO, Element::superconduct,
                Category.GEO, Geo::cryo,
                Category.ANEMO, Anemo::swirl
        ));
    }
}
