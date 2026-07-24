package net.wither.er.shield;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.entity.whopperflower.Whopperflower;
import net.wither.er.init.ElementRegistry;

import java.util.UUID;

import static net.wither.er.init.ElementalAttributesRegister.DMG_ATTR;
import static net.wither.er.init.ElementalAttributesRegister.RES_ATTR;

public class PyroWhopperflowerShield extends ElementalShield{
    private static final UUID uuid =UUID.fromString("20A4D22F-0AA3-0EE3-147F-D614659A20C9");

    public float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type) {
        return damage * 0.2f;
    }

    @Override
    UUID getKbResLocation() {
        return uuid;
    }

    @Override
    Element getElement() {
        return ElementRegistry.PYRO_WHOPPERFLOWER.get();
    }

    @Override
    float getGauge() {
        return 2f;
    }

    @Override
    public void end(Entity owner) {
        super.end(owner);
        if(owner instanceof AuraContainerInterface auraContainerInterface)
            auraContainerInterface.er$getAuraContainer().remove(ElementRegistry.PYRO_WHOPPERFLOWER.get());
    }

    @Override
    public boolean tick(ShieldStack stack, Entity owner) {
        if (super.tick(stack, owner)){
            if(stack.time -- > 0) return true;
            if(owner instanceof LivingEntity living && !(owner instanceof Whopperflower)) {
                living.addEffect(new MobEffectInstance(DMG_ATTR.get(Element.Category.PYRO).get(), 1200));
                living.addEffect(new MobEffectInstance(RES_ATTR.get(Element.Category.PYRO).get(), 1200));
            }
            return false;
        }
        if(owner instanceof Whopperflower whopperflower){
            whopperflower.setAction(Whopperflower.Action.STUN);
        }
        else if(owner instanceof LivingEntity living){
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 5));
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2));
        }
        return false;
    }
}
