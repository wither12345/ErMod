package net.wither.er.item.data.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;

public record WeaponAttributeData(Holder<Attribute> attributeHolder, double baseAmount, boolean type) {
    public static final Codec<WeaponAttributeData> BASIC_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Attribute.CODEC.fieldOf("attribute").forGetter(WeaponAttributeData::attributeHolder),
                    Codec.DOUBLE.fieldOf("baseAmount").forGetter(WeaponAttributeData::baseAmount),
                    Codec.BOOL.fieldOf("type").forGetter(WeaponAttributeData::type)
            ).apply(instance, WeaponAttributeData::new)
    );

    public static final StreamCodec<ByteBuf, WeaponAttributeData> UNIT_STREAM_CODEC = StreamCodec.unit(new WeaponAttributeData(null,0, false));
}
