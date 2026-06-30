package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

public class Frozen extends Element{
    private static final ResourceLocation frozenSpeed = ResourceLocation.withDefaultNamespace("er.frozen.speed") ;
    private static final ResourceLocation frozenJUMP = ResourceLocation.withDefaultNamespace("er.frozen.jump") ;
    @Override
    public Category getCategory() {
        return Category.CRYO ;
    }

    @Override
    public float reactWith(AuraContainer container, SingleElementalContainer singleElementalContainer, float strength, LevelAccessor accessor, double x, double y, double z, int level, double elemental_mastery, EntityHurtEvent.DamageModifier damageModifier, @Nullable Entity applier) {
        return 0;
    }

    @Override
    public void start(AuraContainer container){
        if(container.getOwner() instanceof LivingEntity living) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).addOrUpdateTransientModifier(new AttributeModifier(frozenSpeed, -100, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            if(living.getAttribute(Attributes.JUMP_STRENGTH) != null)
                living.getAttribute(Attributes.JUMP_STRENGTH).addOrUpdateTransientModifier(new AttributeModifier(frozenJUMP, -100, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    @Override
    public void end(AuraContainer container){
        if(container.getOwner() instanceof LivingEntity living) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(frozenSpeed);
            if(living.getAttribute(Attributes.JUMP_STRENGTH) != null)
                living.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(frozenJUMP);
        }
    }

    @Override
    public void tick(AuraContainer container, ElementalAura aura, LevelAccessor accessor, double x, double y, double z, int level, boolean naturalReduction) {
        super.tick(container, aura, accessor, x, y, z, level, naturalReduction);
        aura.addReduceRate(0.05f);
    }

    @Override
    public boolean independent() {
        return true;
    }

    @Override
    public RenderId getRenderId() {
        return RenderId.FROZEN ;
    }

}
