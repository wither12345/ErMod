package net.wither.er.item.data.weapon;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.wither.er.elements.ElementSource;

@FunctionalInterface
public interface InfusionAbility {
    ElementSource getInfusion(ItemStack itemstack, Entity entity, int level);
}
