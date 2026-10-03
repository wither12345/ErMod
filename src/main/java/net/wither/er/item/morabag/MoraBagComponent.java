package net.wither.er.item.morabag;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public class MoraBagComponent implements TooltipComponent {
    private final List<MoraBagItem.MoraVal> moraVals;
    public MoraBagComponent(List<MoraBagItem.MoraVal> moraVals) {
        this.moraVals = moraVals;
    }
    public List<MoraBagItem.MoraVal> getVals() { return moraVals; }
}
