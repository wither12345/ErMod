package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public record WeaponAttributeData(Attribute attribute, double baseAmount, boolean type) {
    public static final Capability<WeaponAttributeData> WEAPON_ATTR = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Codec<WeaponAttributeData> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.ATTRIBUTES.getCodec().fieldOf("attribute").forGetter(WeaponAttributeData::attribute),
                    Codec.DOUBLE.fieldOf("baseAmount").forGetter(WeaponAttributeData::baseAmount),
                    Codec.BOOL.fieldOf("type").forGetter(WeaponAttributeData::type)
            ).apply(instance, WeaponAttributeData::new)
    );

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
}
