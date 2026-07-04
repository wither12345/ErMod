package net.wither.er.item.weapons;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.registries.RegistryObject;

public class AbilitySword extends SwordItem implements AbilityWeapon{
    private final Object ability ;
    private final RegistryObject<Item> item;
    public AbilitySword(Object ability, RegistryObject<Item> item, Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
        this.ability = ability;
        this.item = item;
    }

    @Override
    public Object getAbility() {
        return ability;
    }

    @Override
    public Item getRefinementItem() {
        return item.get();
    }
}
