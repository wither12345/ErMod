package net.wither.er.item.artifact_effect;

import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.wither.er.entity.EnergyOrb;
import net.wither.er.item.data.weapon.EnergyOrbPickupAbility;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

import java.util.UUID;

public class Scholar extends TwoSetAttrEffect implements EnergyOrbPickupAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(UUID.fromString("BF970958-ACD0-149D-FF77-5BC577278B24"), "er:scholar", 0.2, AttributeModifier.Operation.MULTIPLY_BASE);

    public Scholar() {
        super(ErModAttributes.ENERGY_RECHARGE.get(), MODIFIER);
    }

    @Override
    public void onPick(EnergyOrb orb, LivingEntity picker, int level) {
        if(
                level > 3 &&
                picker.getMainHandItem().getItem() instanceof ProjectileWeaponItem &&
                picker instanceof ServerPlayer serverPlayer &&
                picker.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables()).Stella_Fortuna.getItem() instanceof StellaFortunas fortuna)
        {
            ErCombatVariables.PlayerVariables vars = serverPlayer.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
            vars.energyAmount = Math.min(fortuna.getEnergyCost(picker), vars.energyAmount + 3 * (float) picker.getAttributeValue(ErModAttributes.ENERGY_RECHARGE.get()) / 100);
            vars.syncPlayerVariables(picker);
        }
    }
}
