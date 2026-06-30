package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.wither.er.combat.CombatAnimation;
import net.wither.er.item.data.weapon.WeaponAttributeData;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.MainAffix;
import net.wither.er.item.data.artifactdata.MinorAffix;
import net.wither.er.network.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterDatas {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ErMod.addNetworkMessage(ErShieldData.class, ErShieldData::buffer, ErShieldData::new, ErShieldData::handle);
		ErMod.addNetworkMessage(LeyLineLeapData.class, LeyLineLeapData::buffer, LeyLineLeapData::new, LeyLineLeapData::handle);
		ErMod.addNetworkMessage(ErSyncGameRule.class, ErSyncGameRule::buffer, ErSyncGameRule::new, ErSyncGameRule::handle);
		ErMod.addNetworkMessage(StellaFortunaData.class, StellaFortunaData::buffer, StellaFortunaData::new, StellaFortunaData::handle);
		ErMod.addNetworkMessage(SyncLevelData.class, SyncLevelData::buffer, SyncLevelData::new, SyncLevelData::handle);
		ErMod.addNetworkMessage(DamageDisplayMessage.class, DamageDisplayMessage::buffer, DamageDisplayMessage::new, DamageDisplayMessage::handle);
		ErMod.addNetworkMessage(ArtifactTransmuterMessage.class, ArtifactTransmuterMessage::buffer, ArtifactTransmuterMessage::new, ArtifactTransmuterMessage::handle);
		ErMod.addNetworkMessage(AlchemyConvertingSwitchMessage.class, AlchemyConvertingSwitchMessage::buffer, AlchemyConvertingSwitchMessage::new, AlchemyConvertingSwitchMessage::handle);
		ErMod.addNetworkMessage(AlchemyStageSwitchMessage.class, AlchemyStageSwitchMessage::buffer, AlchemyStageSwitchMessage::new, AlchemyStageSwitchMessage::handle);
		ErMod.addNetworkMessage(SkillMessage.class, SkillMessage::buffer, SkillMessage::new, SkillMessage::handle);
		ErMod.addNetworkMessage(CombatAnimation.AnimationMessage.class, CombatAnimation.AnimationMessage::buffer, CombatAnimation.AnimationMessage::new, CombatAnimation.AnimationMessage::handleData);
		ErMod.addNetworkMessage(CombatAnimation.ChargedAttackMessage.class, CombatAnimation.ChargedAttackMessage::buffer, CombatAnimation.ChargedAttackMessage::new, CombatAnimation.ChargedAttackMessage::handleData);
		ErMod.addNetworkMessage(ErItemVariables.ItemVariablesSyncMessage.class, ErItemVariables.ItemVariablesSyncMessage::buffer, ErItemVariables.ItemVariablesSyncMessage::new, ErItemVariables.ItemVariablesSyncMessage::handleData);
		ErMod.addNetworkMessage(ErCombatVariables.CombatVariablesSyncMessage.class, ErCombatVariables.CombatVariablesSyncMessage::buffer, ErCombatVariables.CombatVariablesSyncMessage::new, ErCombatVariables.CombatVariablesSyncMessage::handleData);
		ErMod.addNetworkMessage(ErCombatVariables.SyncAnimationMessage.class, ErCombatVariables.SyncAnimationMessage::buffer, ErCombatVariables.SyncAnimationMessage::new, ErCombatVariables.SyncAnimationMessage::handleData);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event){
		event.register(ErCombatVariables.PlayerVariables.class);
		event.register(ErItemVariables.PlayerVariables.class);

		event.register(WeaponLevelData.class);
		event.register(WeaponAttributeData.class);
		event.register(ArtifactData.class);
		event.register(MainAffix.class);
		event.register(MinorAffix.class);
	}
}
