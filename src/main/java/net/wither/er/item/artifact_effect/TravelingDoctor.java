package net.wither.er.item.artifact_effect;

import net.wither.er.item.fortuna.StellaFortuna;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.item.data.ability.OnBurstAbility;

public class TravelingDoctor extends AttrArtifactEffect implements OnBurstAbility {
    public TravelingDoctor() {
        super(ErModAttributes.INCOMING_HEALING_BONUS, new AttributeModifier(ResourceLocation.parse("er:traveling_doctor"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }


    @Override
    public void onBurst(LivingEntity entity, StellaFortuna stellaFortuna, int level) {
        if(level > 3)
            entity.heal(entity.getMaxHealth() * 0.2f);
    }
}
