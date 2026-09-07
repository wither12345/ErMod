package net.wither.er.item.weapons;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class AbilitySword extends SwordItem implements AbilityWeapon {
    private final Object ability;
    private final RegistryObject<Item> item;
    private final ICapabilityProvider provider;

    public AbilitySword(Object ability,
                        RegistryObject<Item> item,
                        Tier tier,
                        int i,
                        float v,
                        Properties properties,
                        ICapabilityProvider provider
    ) {
        super(tier, i, v, properties);
        this.ability = ability;
        this.item = item;
        this.provider = provider;
    }

    @Override
    public Object getAbility() {
        return ability;
    }

    @Override
    public Item getRefinementItem() {
        return item.get();
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return provider;
    }
}
