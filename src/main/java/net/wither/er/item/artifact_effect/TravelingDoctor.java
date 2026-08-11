package net.wither.er.item.artifact_effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.item.data.weapon.OnBurstAbility;

import java.util.UUID;

public class TravelingDoctor extends TwoSetAttrEffect implements OnBurstAbility {
    public TravelingDoctor() {
        super(ErModAttributes.INCOMING_HEALING_BONUS.get(), new AttributeModifier(UUID.fromString("CA95A88C-A643-4928-22FB-CBC73183C84A"), "traveling_doctor" , 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
    }


    @Override
    public void onBurst(LivingEntity entity, int level) {
        if(level > 3)
            entity.heal(entity.getMaxHealth() * 0.2f);
    }
}
