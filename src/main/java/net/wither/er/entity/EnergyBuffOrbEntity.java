package net.wither.er.entity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.StellaFortunas;
import net.mcreator.er.init.ErModAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.EnergyOrbPickupAbility;
import net.wither.er.item.weapons.AbilityWeapon;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.network.ErItemVariables;

public class EnergyBuffOrbEntity extends BuffOrbEntity{
    public EnergyBuffOrbEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void onTouch(Entity entity) {
        if (entity instanceof ServerPlayer serverplayer) {
            if (serverplayer.getCapability(ErItemVariables.PLAYER_VARIABLES).orElse(new ErItemVariables.PlayerVariables()).Stella_Fortuna.getItem() instanceof StellaFortunas fortuna) {
                ErCombatVariables.PlayerVariables vars = serverplayer.getCapability(ErCombatVariables.PLAYER_VARIABLES).orElseGet(ErCombatVariables.PlayerVariables::new);
                vars.energyAmount = Math.min(fortuna.getEnergyCost(serverplayer), vars.energyAmount + 5 * (float) serverplayer.getAttributeValue(ErModAttributes.ENERGY_RECHARGE.get()) / 100);
                vars.syncWithId(serverplayer, 0b110100_0011);
                if(serverplayer instanceof ErEntityInterface erEntityInterface){
                    Object2IntMap<ArtifactEffect> map = erEntityInterface.er$getEffectMap();
                    for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                        if(effect.getKey() instanceof EnergyOrbPickupAbility ability){
                            ability.onPick(this, serverplayer, effect.getIntValue());
                        }
                    }
                }
                if(serverplayer.getMainHandItem().getItem() instanceof AbilityWeapon abilityWeapon && abilityWeapon.getAbility() instanceof EnergyOrbPickupAbility ability) {
                    CompoundTag tag = serverplayer.getMainHandItem().getOrCreateTag();
                    int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
                    ability.onPick(this, serverplayer, refinement);
                }
            }
            this.discard();
        }
    }
}
