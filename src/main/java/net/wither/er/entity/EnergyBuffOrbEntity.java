package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.EnergyOrbPickupAbility;
import net.wither.er.item.data.weapon.WeaponRefinement;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

public class EnergyBuffOrbEntity extends BuffOrbEntity{
    public EnergyBuffOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onTouch(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.getData(ErItemVariables.PLAYER_VARIABLES).Stella_Fortuna.getItem() instanceof StellaFortunas fortuna) {
                ErCombatVariables.PlayerVariables vars = serverPlayer.getData(ErCombatVariables.PLAYER_VARIABLES);
                vars.energyAmount = Math.min(fortuna.getEnergyCost(serverPlayer), vars.energyAmount + 5 * (float) serverPlayer.getAttributeValue(ErModAttributes.ENERGY_RECHARGE) / 100);
                vars.syncWithId(serverPlayer, 0b00_0100_0000);
                if(serverPlayer instanceof ErEntityInterface erEntityInterface){
                    Object2IntMap<Holder<ArtifactEffect>> map = erEntityInterface.er$getEffectMap();
                    for(Object2IntMap.Entry<Holder<ArtifactEffect>> effect : map.object2IntEntrySet()){
                        if(effect.getKey().value() instanceof EnergyOrbPickupAbility ability){
                            ability.onPick(this, serverPlayer, effect.getIntValue());
                        }
                    }
                }
                {
                    WeaponRefinement refinement = serverPlayer.getMainHandItem().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
                    if (refinement != null && refinement.getAbility() instanceof EnergyOrbPickupAbility ability)
                        ability.onPick(this, serverPlayer, refinement.refineLevel());
                }
            }
            this.discard();
        }
    }
}
