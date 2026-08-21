package net.wither.er.entity.outcrop;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeGiver extends EntityModifier{
    private final Holder<Attribute> attribute;
    private final AttributeModifier modifier;

    public AttributeGiver(Holder<Attribute> attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public static AttributeGiver read(JsonElement element) {
        Holder<Attribute> attribute = Attribute.CODEC.parse(JsonOps.INSTANCE, element.getAsJsonObject().get("attribute")).getOrThrow();
        AttributeModifier modifier = AttributeModifier.CODEC.parse(JsonOps.INSTANCE, element.getAsJsonObject().get("modifier")).getOrThrow();
        return new AttributeGiver(attribute, modifier);
    }

    @Override
    public void apply(Entity entity, int level) {
        if(entity instanceof LivingEntity living){
            AttributeInstance instance = living.getAttribute(attribute);
            if(instance != null)
                instance.addPermanentModifier(modifier);
        }
    }
}
