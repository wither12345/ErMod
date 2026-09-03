package net.wither.er.item.artifact_effect;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.wither.er.item.data.weapon.DamageAbility;

public class BraveHeart extends TwoSetAttrEffect implements DamageAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.parse("er:brave_heart"), 0.18, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public BraveHeart() {
        super(Attributes.ATTACK_DAMAGE, MODIFIER);
    }

    @Override
    public void onHurt(DamageSource source, LivingEntity entity, EntityHurtEvent.DamageModifier modifier, int level) {
        if(level > 3 && entity.getHealth() >= entity.getMaxHealth() * 0.5f)
            modifier.common_multiply += 0.3f;
    }
}
