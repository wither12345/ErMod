package net.wither.er.item.data.artifactdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.mcreator.er.init.ErModAttributes;
import net.mcreator.er.item.Artifact;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.wither.er.entity.ArtifactSlot;

import java.util.Optional;

public record MainAffix(Holder<Attribute> attribute, double amount, boolean multi) {
    public static final Codec<MainAffix> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, MainAffix> STREAM_CODEC;

    static {
        CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Attribute.CODEC.fieldOf("attribute").forGetter(MainAffix::attribute),
                        Codec.DOUBLE.fieldOf("amount").forGetter(MainAffix::amount),
                        Codec.BOOL.fieldOf("multi").forGetter(MainAffix::multi)
                ).apply(instance, MainAffix::new));
        STREAM_CODEC = StreamCodec.composite(
                Attribute.STREAM_CODEC, MainAffix::attribute,
                ByteBufCodecs.DOUBLE, MainAffix::amount,
                ByteBufCodecs.BOOL, MainAffix::multi,
                MainAffix::new
        ) ;
    }

    public void remove(ArtifactSlot slot, LivingEntity entity){
        AttributeInstance instance = entity.getAttribute(attribute) ;
        if (instance != null) {
            instance.removeModifier(getResourceLocation(slot)) ;
        }
    }

    public void apply(ArtifactSlot slot, LivingEntity entity, int level, int rarity){
        AttributeInstance instance = entity.getAttribute(attribute) ;
        if (instance != null) {
            instance.addOrUpdateTransientModifier(new AttributeModifier(getResourceLocation(slot), amount * Artifact.getScaling(level, rarity, this.getAtrType()), multi ? AttributeModifier.Operation.ADD_MULTIPLIED_BASE : AttributeModifier.Operation.ADD_VALUE)); ;
        }
    }

    private ResourceLocation getResourceLocation(ArtifactSlot slot){
        return ResourceLocation.parse("er:artifact." + slot.toString().toLowerCase() + ".main") ;
    }

    public String toString(){
        return toString(0,5);
    }

    public String toString(int level, int rarity){
        return Component.translatable(attribute.value().getDescriptionId()).getString() + ":"
                + new java.text.DecimalFormat(attribute == ErModAttributes.CRIT_RATE.getDelegate() || attribute == ErModAttributes.CRIT_DAMAGE.getDelegate() || multi ? "##.#%" : "##.#")
                .format(calculate(level,rarity));
    }

    public double calculate(int level, int rarity){
        return amount * Artifact.getScaling(level, rarity, this.getAtrType());
    }

    private int getAtrType(){
        if(multi){
            if(attribute == Attributes.ATTACK_DAMAGE)
                return 1 ;
            if(attribute == Attributes.MAX_HEALTH)
                return 2 ;
        }
        return  0 ;
    }

    public static MainAffix create(String[] type){
        Optional<Holder.Reference<Attribute>> attributeHolder = BuiltInRegistries.ATTRIBUTE.getHolder(ResourceLocation.parse(type[0]));
        if(attributeHolder.isEmpty() || type.length <= 2)
            return new MainAffix(Attributes.MAX_HEALTH,0,false) ;
        return new MainAffix(attributeHolder.get(), Double.parseDouble(type[1]), type[2].equals("1")) ;
    }
}
