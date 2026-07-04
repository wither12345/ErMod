package net.wither.er.elements;

import java.util.Map;

public class Quicken extends Element{
    public Quicken() {
        super(Map.of());
    }

    @Override
    public Category getCategory() {
        return Category.DENDRO;
    }


    @Override
    public boolean overrideReduceRate() {
        return true;
    }

    @Override
    public float getReduceRate(float gauge) {
        return 1/(gauge * 5 + 6) ;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.QUICKEN;
    }
}
