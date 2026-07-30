package net.wither.er.elements;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LevelAccessor;

import java.util.Map;
import java.util.UUID;

public class Frozen extends Cryo{
    private static final UUID FROZEN_SPEED = UUID.fromString("588FF75A-C891-E049-650A-EB529F4BFBD5");
    private static final AttributeModifier SPEED = new AttributeModifier(FROZEN_SPEED, "frozen", -100, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final UUID FROZEN_JUMP = UUID.fromString("588FF75A-C891-E049-650A-EB529F4BFBD5");
    private static final AttributeModifier JUMP = new AttributeModifier(FROZEN_JUMP, "frozen", -100, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public Frozen() {
        super(Map.of(
                Category.PYRO, Element::amplifying2,
                Category.ELECTRO, Element::superconduct,
                Category.GEO, Geo::cryo,
                Category.ANEMO, Anemo::swirl
        ));
    }

    @Override
    public void start(AuraContainer container){
        if(container.getOwner() instanceof LivingEntity living) {
            AttributeInstance speed = living.getAttribute(Attributes.MOVEMENT_SPEED);
            if(speed != null && !speed.hasModifier(SPEED))
                speed.addTransientModifier(SPEED);

            AttributeInstance jump = living.getAttribute(Attributes.MOVEMENT_SPEED);
            if(jump != null && !jump.hasModifier(JUMP))
                jump.addTransientModifier(JUMP);
        }
    }

    @Override
    public void end(AuraContainer container){
        if(container.getOwner() instanceof LivingEntity living) {
            living.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(FROZEN_SPEED);
            if(living.getAttribute(Attributes.JUMP_STRENGTH) != null)
                living.getAttribute(Attributes.JUMP_STRENGTH).removeModifier(FROZEN_JUMP);
        }
    }

    @Override
    public void tick(AuraContainer container, ElementalAura aura, LevelAccessor accessor, double x, double y, double z, boolean naturalReduction) {
        super.tick(container, aura, accessor, x, y, z, naturalReduction);
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
