package net.wither.er.item.artifact_effect;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.wither.er.init.MobEffectRegister;
import net.wither.er.item.data.weapon.KillAbility;
import net.wither.er.network.ErCombatVariables;

import java.util.UUID;

public class Gambler extends AttrArtifactEffect implements KillAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("4E6C456B-A2DD-7E17-7F71-AC6D2890DBA5"), "er:gambler", 0.2, AttributeModifier.Operation.MULTIPLY_BASE);

    public Gambler() {
        super(ErModAttributes.ELEMENTAL_SKILL_DMG.get(), MODIFIER);
    }

    @Override
    public void onKill(DamageSource source, LivingEntity entity, int level) {
        if (source.getEntity() instanceof LivingEntity living) {
            living.getCapability(ErCombatVariables.PLAYER_VARIABLES).ifPresent(
                    playerVariables -> {
                        if (level > 3 && !living.hasEffect(MobEffectRegister.GAMBLER_CD.get()) && playerVariables.skillCooldown > 0) {
                            living.addEffect(new MobEffectInstance(MobEffectRegister.GAMBLER_CD.get(), 300, 0));
                            playerVariables.skillCooldown = 0;
                            playerVariables.syncWithId(living, 0b00_0010_0000);
                        }
                    }
            );
        }
    }
}
