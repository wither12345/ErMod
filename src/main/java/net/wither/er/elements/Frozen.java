package net.wither.er.elements;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.UUID;

public class Frozen extends Element{
    private static final UUID FROZEN_SPEED = UUID.fromString("B4CD8EE0-E3FA-79B0-EA46-4A48F94A725C");
    private static final UUID FROZEN_JUMP = UUID.fromString("9DE69BB6-CD62-B49D-A662-08603492FCDF");
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
            if(!living.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(new AttributeModifier(FROZEN_SPEED, "frozen", -100, AttributeModifier.Operation.MULTIPLY_TOTAL)))
                living.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(FROZEN_SPEED, "frozen", -100, AttributeModifier.Operation.MULTIPLY_TOTAL));
            if(living.getAttribute(Attributes.JUMP_STRENGTH) != null && !living.getAttribute(Attributes.JUMP_STRENGTH).hasModifier(new AttributeModifier(FROZEN_JUMP, "frozen", -100, AttributeModifier.Operation.MULTIPLY_TOTAL)))
                living.getAttribute(Attributes.JUMP_STRENGTH).addTransientModifier(new AttributeModifier(FROZEN_JUMP, "frozen", -100, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    @Override
    public void end(AuraContainer container){
        if(container.getOwner() instanceof LivingEntity living) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(FROZEN_SPEED);
            if (living.getAttribute(Attributes.JUMP_STRENGTH) != null)
                living.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(FROZEN_JUMP);
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
