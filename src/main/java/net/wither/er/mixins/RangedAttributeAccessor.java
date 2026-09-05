package net.wither.er.mixins;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttribute.class)
public interface RangedAttributeAccessor {
    @Accessor("minValue") @Mutable void er$setMinValue(double minValue);

    @Accessor("maxValue") @Mutable void er$setMaxValue(double maxValue);
}
