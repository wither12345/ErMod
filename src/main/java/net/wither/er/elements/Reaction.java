package net.wither.er.elements;

import net.minecraft.network.chat.Component;

public enum Reaction {
    MELT("melt"),
    BURNING("burning"),
    VAPORIZE("vaporize"),
    QUICKEN("quicken"),
    OVERLOADED("overloaded"),
    AGGRAVATE("aggravate"),
    SPREAD("spread"),
    CRYSTALLIZE("crystalize"),
    ELECTRO_CHARGED("electro_charged"),
    FROZEN("frozen"),
    SWIRL("swirl"),
    BLOOM("bloom"),
    BURGEON("burgeon"),
    HYPERBLOOM("hyperbloom"),
    LUNAR_CRYSTALLIZE("lunar_crystallize"),
    LUNAR_CHARGED("lunar_charged"),
    SUPERCONDUCT("superconduct");

    private final String key;
    Reaction(String name) {
        this.key = "er.reaction." + name;
    }

    public String getTranslated(){
        return Component.translatable(key).getString();
    }
}
