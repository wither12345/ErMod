package net.wither.er.item.artifact_effect;

import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.wither.er.item.data.weapon.EnergyOrbPickupAbility;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

public class Scholar extends AttrArtifactEffect implements EnergyOrbPickupAbility {
    private static final AttributeModifier MODIFIER = new AttributeModifier(ResourceLocation.parse("er:scholar"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public Scholar() {
        super(ErModAttributes.ENERGY_RECHARGE, MODIFIER);
    }

    @Override
    public void onPick(Entity orb, LivingEntity picker, int level) {
        if(
                level > 3 &&
                picker.getMainHandItem().getItem() instanceof ProjectileWeaponItem &&
                picker instanceof ServerPlayer serverPlayer &&
                picker.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna.getItem() instanceof StellaFortunas fortuna)
        {
            ErCombatVariables.PlayerVariables vars = serverPlayer.getData(ErCombatVariables.PLAYER_VARIABLES);
            vars.energyAmount = Math.min(fortuna.getEnergyCost(picker), vars.energyAmount + 3 * (float) picker.getAttributeValue(ErModAttributes.ENERGY_RECHARGE) / 100);
            vars.syncWithId(serverPlayer, 0b00_0100_0000);
        }
    }
}
