package net.wither.er.shield;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.wither.er.elements.Element;
import net.wither.er.entity.whopperflower.Whopperflower;
import net.wither.er.init.ElementRegistry;

import static net.wither.er.init.ElementalAttributesRegister.DMG_ATTR;
import static net.wither.er.init.ElementalAttributesRegister.RES_ATTR;

public class PyroWhopperflowerShield extends ElementalShield{
    private static final ResourceLocation resourcelocation = ResourceLocation.parse("er:pyro_whopperflower_shield.kb_res");

    public float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type) {
        return damage * 0.2f;
    }

    @Override
    ResourceLocation getKbResLocation() {
        return resourcelocation;
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
    public boolean tick(ShieldStack stack, Entity owner) {
        if (super.tick(stack, owner)){
            if(stack.time -- > 0) return true;
            if(owner instanceof LivingEntity living && !(owner instanceof Whopperflower)) {
                living.addEffect(new MobEffectInstance(DMG_ATTR.get(Element.Category.PYRO), 1200));
                living.addEffect(new MobEffectInstance(RES_ATTR.get(Element.Category.PYRO), 1200));
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
