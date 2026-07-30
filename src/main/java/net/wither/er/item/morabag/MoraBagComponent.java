package net.wither.er.item.morabag;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public class MoraBagComponent implements TooltipComponent {
    private final List<MoraBagItemPlus.MoraVal> moraVals;
    public MoraBagComponent(List<MoraBagItemPlus.MoraVal> moraVals) {
        this.moraVals = moraVals;
    }
    public List<MoraBagItemPlus.MoraVal> getVals() { return moraVals; }
}
