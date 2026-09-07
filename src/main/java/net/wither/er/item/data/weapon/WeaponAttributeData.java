package net.wither.er.item.data.weapon;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public record WeaponAttributeData(Attribute attribute, double baseAmount, boolean type) {
    public static final ResourceLocation LOCATION = new ResourceLocation("er", "weapon_attr");
    public static final Capability<WeaponAttributeData> WEAPON_ATTR = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static class CapabilityProvider implements ICapabilityProvider {
        public CapabilityProvider(WeaponAttributeData data){
            this.data = data;
            instance = LazyOptional.of(() -> data);
        }

        public CapabilityProvider(Attribute attribute, double baseAmount, boolean type){
            this(new WeaponAttributeData(attribute, baseAmount, type));
        }

        private final WeaponAttributeData data ;
        private final LazyOptional<WeaponAttributeData> instance  ;

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == WEAPON_ATTR ? instance.cast() : LazyOptional.empty();
        }
    }

    public AttributeModifier getModifier(UUID uuid, int level){
        double multi = 1 + (level / 5) * 0.2d ;
        return new AttributeModifier(
                uuid, "secondary", this.getFinalAmount(baseAmount * multi), this.type ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION
        );
    }

    private double getFinalAmount(double amount){
        if(type) {
            BigDecimal bd = new BigDecimal(amount);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        return (int)(amount + 0.5);
    }
}
