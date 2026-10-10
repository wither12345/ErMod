package net.wither.er.item.weapons;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class AbilitySword extends SwordItem {
    private final Supplier<ICapabilityProvider> provider;

    public AbilitySword(Tier tier,
                        int attackDamage,
                        float attackSpeed,
                        Properties properties,
                        Supplier<ICapabilityProvider> provider
    ) {
        super(tier, attackDamage, attackSpeed, properties);
        this.provider = provider;
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return provider.get();
    }
}
