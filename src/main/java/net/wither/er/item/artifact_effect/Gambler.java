package net.wither.er.item.artifact_effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.EffectRegister;
import net.wither.er.item.data.weapon.KillAbility;
import net.wither.er.network.ErCombatVariables;

public class Gambler extends AttrArtifactEffect implements KillAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.parse("er:gambler"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public Gambler() {
        super(ErModAttributes.ELEMENTAL_SKILL_DMG, MODIFIER);
    }

    @Override
    public void onKill(DamageSource source, LivingEntity entity, int level) {
        if(level > 3 && source.getEntity() instanceof LivingEntity living && living.hasData(ErCombatVariables.PLAYER_VARIABLES) && !living.hasEffect(EffectRegister.GAMBLER_CD)) {
            ErCombatVariables.PlayerVariables playerVariables = living.getData(ErCombatVariables.PLAYER_VARIABLES);
            if (playerVariables.skillCooldown > 0) {
                living.addEffect(new MobEffectInstance(EffectRegister.GAMBLER_CD, 300, 0));
                playerVariables.skillCooldown = 0;
                playerVariables.syncWithId(living, 0b00_0010_0000);
            }
        }
    }
}
