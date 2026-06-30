package net.wither.er.entity.slimes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;

public class ElectroSlime extends ElementalSlime{
    public ElectroSlime(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RechargeElementalGoal(this, 10, 2, 2, 20));
    }

    @Override
    Element getElement() {
        return ElementRegistry.ELECTRO.get();
    }

    @Override
    boolean isTiny() {
        return true;
    }
}
