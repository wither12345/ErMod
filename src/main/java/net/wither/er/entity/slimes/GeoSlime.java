package net.wither.er.entity.slimes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;

public class GeoSlime extends ElementalSlime{
    public GeoSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    Element getElement() {
        return ElementRegistry.GEO.get();
    }

    @Override
    boolean isTiny() {
        return true;
    }
}
