package net.wither.er.effect;

import net.mcreator.er.ErMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class TastedStrong extends MobEffect {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "food_effect.dmg");
    public TastedStrong() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(@NotNull AttributeMap map, int amp) {
        AttributeInstance dmgInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(dmgInstance != null)
            dmgInstance.addOrReplacePermanentModifier(new AttributeModifier(LOCATION, 4 + amp * 6, AttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void removeAttributeModifiers(AttributeMap map) {
        AttributeInstance dmgInstance = map.getInstance(Attributes.ATTACK_DAMAGE);
        if(dmgInstance != null)
            dmgInstance.removeModifier(LOCATION);
    }

    @Override
    public void createModifiers(int amp, @NotNull BiConsumer<Holder<Attribute>, AttributeModifier> consumer) {
        consumer.accept(Attributes.ATTACK_DAMAGE, new AttributeModifier(LOCATION, 4 + amp * 6, AttributeModifier.Operation.ADD_VALUE));
    }
}
