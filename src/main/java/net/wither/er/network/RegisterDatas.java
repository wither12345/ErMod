package net.wither.er.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber()
public class RegisterDatas {
	@SubscribeEvent
	public static void Register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1"); // All subsequent payloads will register on the network thread
		registrar.playBidirectional(ErShieldData.TYPE, ErShieldData.STREAM_CODEC, new DirectionalPayloadHandler<>(ErShieldData::handle, ErShieldData::handle));
		registrar.playBidirectional(LeyLineLeapData.TYPE, LeyLineLeapData.STREAM_CODEC, new DirectionalPayloadHandler<>(LeyLineLeapData::handle, LeyLineLeapData::handle));
		registrar.playBidirectional(ErSyncGameRule.TYPE, ErSyncGameRule.STREAM_CODEC, new DirectionalPayloadHandler<>(ErSyncGameRule::handle, ErSyncGameRule::handle));
		registrar.playBidirectional(SyncLevelData.TYPE, SyncLevelData.STREAM_CODEC, new DirectionalPayloadHandler<>(SyncLevelData::handle, SyncLevelData::handle));
		registrar.playBidirectional(StellaFortunaData.TYPE, StellaFortunaData.STREAM_CODEC, new DirectionalPayloadHandler<>(StellaFortunaData::handle, StellaFortunaData::handle));
		registrar.playBidirectional(DamageDisplayMessage.TYPE, DamageDisplayMessage.STREAM_CODEC, new DirectionalPayloadHandler<>(DamageDisplayMessage::handle, DamageDisplayMessage::handle));
		registrar.playBidirectional(ArtifactTransmuterMessage.TYPE, ArtifactTransmuterMessage.STREAM_CODEC, new DirectionalPayloadHandler<>(ArtifactTransmuterMessage::handle, ArtifactTransmuterMessage::handle));
		registrar.playBidirectional(AlchemyConvertingSwitchMessage.TYPE, AlchemyConvertingSwitchMessage.STREAM_CODEC, new DirectionalPayloadHandler<>(AlchemyConvertingSwitchMessage::handle, AlchemyConvertingSwitchMessage::handle));
		registrar.playBidirectional(AlchemyStageSwitchMessage.TYPE, AlchemyStageSwitchMessage.STREAM_CODEC, new DirectionalPayloadHandler<>(AlchemyStageSwitchMessage::handle, AlchemyStageSwitchMessage::handle));
        registrar.playBidirectional(MoraSelectData.TYPE, MoraSelectData.STREAM_CODEC, new DirectionalPayloadHandler<>(MoraSelectData::handle, MoraSelectData::handle));
	}
}
