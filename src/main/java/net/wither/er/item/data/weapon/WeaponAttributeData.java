package net.wither.er.item.data.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record WeaponAttributeData(Holder<Attribute> attributeHolder, double baseAmount, boolean type) {
    public static final Codec<WeaponAttributeData> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Attribute.CODEC.fieldOf("attribute").forGetter(WeaponAttributeData::attributeHolder),
                    Codec.DOUBLE.fieldOf("baseAmount").forGetter(WeaponAttributeData::baseAmount),
                    Codec.BOOL.fieldOf("type").forGetter(WeaponAttributeData::type)
            ).apply(instance, WeaponAttributeData::new)
    );

    public static final StreamCodec<ByteBuf, WeaponAttributeData> UNIT_STREAM_CODEC = StreamCodec.unit(new WeaponAttributeData(null,0, false));

    public AttributeModifier getModifier(ResourceLocation location, int level){
        double multi = 1 + (level / 5) * 0.2d ;
        return new AttributeModifier(
                location, this.getFinalAmount(baseAmount * multi), this.type ? AttributeModifier.Operation.ADD_MULTIPLIED_BASE : AttributeModifier.Operation.ADD_VALUE
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
