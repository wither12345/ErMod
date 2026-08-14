package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@EventBusSubscriber()
public class ErCombatVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ErMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("player_combat_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ErMod.addNetworkMessage(PlayerVariablesSyncMessage.TYPE, PlayerVariablesSyncMessage.STREAM_CODEC, PlayerVariablesSyncMessage::handleData);
		ErMod.addNetworkMessage(SyncAnimationMessage.TYPE, SyncAnimationMessage.STREAM_CODEC, SyncAnimationMessage::handleData);
	}

	@EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getData(PLAYER_VARIABLES).syncPlayerVariables(event.getEntity());
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			PlayerVariables original = event.getOriginal().getData(PLAYER_VARIABLES);
			PlayerVariables clone = new PlayerVariables();
			if (!event.isWasDeath()) {
				clone.animationId = original.animationId;
				clone.animationTime = original.animationTime;
				clone.stamina = original.stamina;
				clone.staminaRecoveryCooldown = original.staminaRecoveryCooldown;
				clone.stackedMaxSkillCooldown = original.stackedMaxSkillCooldown;
				clone.skillCooldown = original.skillCooldown;
				clone.energyAmount = original.energyAmount;
				clone.burstCooldown = original.burstCooldown;
				clone.stackedMaxBurstCooldown = original.stackedMaxBurstCooldown;
				clone.skillChargingCount = original.skillChargingCount;
			}
			event.getEntity().setData(PLAYER_VARIABLES, clone);
		}
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		public int animationId = 0;
		public int animationTime = 0;
		public double stamina = 100;
		public int staminaRecoveryCooldown = 50;
		public float stackedMaxSkillCooldown = 5f;
		public float skillCooldown = 0f;
		public float energyAmount = 0f;
		public float burstCooldown = 0f;
		public float stackedMaxBurstCooldown = 5f;
		public int skillChargingCount = 1;

		@Override
		public CompoundTag serializeNBT(HolderLookup.@NotNull Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.putInt("Animation_Id", animationId);
			nbt.putInt("Animation_Time", animationTime);
			nbt.putDouble("Stamina", stamina);
			nbt.putInt("Stamina_Recovery_Cooldown", staminaRecoveryCooldown);
			nbt.putFloat("Stacked_Skill_Cooldown", stackedMaxSkillCooldown);
			nbt.putFloat("Skill_Cooldown", skillCooldown);
			nbt.putFloat("Energy_Amount", energyAmount);
			nbt.putInt("Skill_Charging_Count", skillChargingCount);
			nbt.putFloat("Burst_Cooldown", burstCooldown);
			nbt.putFloat("Stacked_Burst_Cooldown", stackedMaxBurstCooldown);
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.@NotNull Provider lookupProvider, CompoundTag nbt) {
			animationId = nbt.getInt("Animation_Id");
			animationTime = nbt.getInt("Animation_Time");
			stamina = nbt.getDouble("Stamina");
			staminaRecoveryCooldown = nbt.getInt("Stamina_Recovery_Cooldown");
			stackedMaxSkillCooldown = nbt.getFloat("Stacked_Skill_Cooldown");
			skillCooldown = nbt.getFloat("Skill_Cooldown");
			energyAmount = nbt.getFloat("Energy_Amount");
			skillChargingCount = nbt.getInt("Skill_Charging_Count");
			burstCooldown = nbt.getFloat("Burst_Cooldown");
			stackedMaxBurstCooldown = nbt.getFloat("Stacked_Burst_Cooldown");
		}

		public void syncPlayerVariables(Entity entity) {
            this.syncWithId(entity, 0b11_1111_1111);
		}

        public void syncWithId(Entity entity, long syncId){
            if (entity instanceof ServerPlayer)
                PacketDistributor.sendToAllPlayers(new PlayerVariablesSyncMessage(this, entity.getId(), syncId));
        }

        public void putToBuffer(FriendlyByteBuf buf, long syncId){
            int i = 0;
            if(test(syncId, i ++)) buf.writeInt(animationId);//                 0b--_----_---1
            if(test(syncId, i ++)) buf.writeInt(animationTime);//               0b--_----_--1-
            if(test(syncId, i ++)) buf.writeDouble(stamina);//                  0b--_----_-1--
            if(test(syncId, i ++)) buf.writeInt(staminaRecoveryCooldown);//     0b--_----_1---
            if(test(syncId, i ++)) buf.writeFloat(stackedMaxSkillCooldown);//   0b--_---1_----
            if(test(syncId, i ++)) buf.writeFloat(skillCooldown);//             0b--_--1-_----
            if(test(syncId, i ++)) buf.writeFloat(energyAmount);//              0b--_-1--_----
            if(test(syncId, i ++)) buf.writeInt(skillChargingCount);//          0b--_1---_----
            if(test(syncId, i ++)) buf.writeFloat(burstCooldown);//             0b-1_----_----
            if(test(syncId, i ++)) buf.writeFloat(stackedMaxBurstCooldown);//   0b1-_----_----
        }

        public void readFromBuffer(FriendlyByteBuf buf, long syncId){
            int i = 0;
            if(test(syncId, i ++)) animationId = buf.readInt();
            if(test(syncId, i ++)) animationTime = buf.readInt();
            if(test(syncId, i ++)) stamina = buf.readDouble();
            if(test(syncId, i ++)) staminaRecoveryCooldown = buf.readInt();
            if(test(syncId, i ++)) stackedMaxSkillCooldown = buf.readFloat();
            if(test(syncId, i ++)) skillCooldown = buf.readFloat();
            if(test(syncId, i ++)) energyAmount = buf.readFloat();
            if(test(syncId, i ++)) skillChargingCount = buf.readInt();
            if(test(syncId, i ++)) burstCooldown = buf.readFloat();
            if(test(syncId, i ++)) stackedMaxBurstCooldown = buf.readFloat();
        }

        public void copyFrom(ErCombatVariables.PlayerVariables variables, long syncId){
            int i = 0;
            if(test(syncId, i ++)) animationId = variables.animationId;
            if(test(syncId, i ++)) animationTime = variables.animationTime;
            if(test(syncId, i ++)) stamina = variables.stamina;
            if(test(syncId, i ++)) staminaRecoveryCooldown = variables.staminaRecoveryCooldown;
            if(test(syncId, i ++)) stackedMaxSkillCooldown = variables.stackedMaxSkillCooldown;
            if(test(syncId, i ++)) skillCooldown = variables.skillCooldown;
            if(test(syncId, i ++)) energyAmount = variables.energyAmount;
            if(test(syncId, i ++)) skillChargingCount = variables.skillChargingCount;
            if(test(syncId, i ++)) burstCooldown = variables.burstCooldown;
            if(test(syncId, i ++)) stackedMaxBurstCooldown = variables.stackedMaxBurstCooldown;
        }

        private boolean test(long id, int index){
            return (id & (1L << index)) > 0;
        }
	}

	public record PlayerVariablesSyncMessage(PlayerVariables data, int id, long syncId) implements CustomPacketPayload {
		public static final Type<PlayerVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "player_combat_variables_sync"));
		public static final StreamCodec<FriendlyByteBuf, PlayerVariablesSyncMessage> STREAM_CODEC = StreamCodec
				.of(
                        (FriendlyByteBuf buffer, PlayerVariablesSyncMessage message) ->
                                message.data().putToBuffer(buffer.writeInt(message.id).writeLong(message.syncId), message.syncId),
                        (FriendlyByteBuf buffer) -> {
                            PlayerVariablesSyncMessage message = new PlayerVariablesSyncMessage(new PlayerVariables(), buffer.readInt(), buffer.readLong());
                            message.data.readFromBuffer(buffer, message.syncId);
                            return message;
                        }
                );

		@Override
		public @NotNull Type<PlayerVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final PlayerVariablesSyncMessage message, final IPayloadContext context) {
            Entity entity = context.player().level().getEntity(message.id());
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null && entity instanceof Player player) {
				context.enqueueWork(() -> player.getData(PLAYER_VARIABLES).copyFrom(message.data, message.syncId)).exceptionally(e -> {
					context.connection().disconnect(Component.literal(e.getMessage()));
					return null;
				});
			}
		}
	}

	public record SyncAnimationMessage(int id, PlayerVariables data) implements CustomPacketPayload {
		public static final Type<SyncAnimationMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "player_animation_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, SyncAnimationMessage> STREAM_CODEC = StreamCodec
				.of((RegistryFriendlyByteBuf buffer, SyncAnimationMessage message) -> buffer.writeInt(message.id).writeNbt(message.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
					SyncAnimationMessage message = new SyncAnimationMessage(buffer.readInt(), new PlayerVariables());
					message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
					return message;
				});

		@Override
		public Type<SyncAnimationMessage> type() {
			return TYPE;
		}

		public static void handleData(final SyncAnimationMessage message, final IPayloadContext context) {
			if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
				Entity entity = Minecraft.getInstance().level.getEntity(message.id);
				if (entity instanceof Player player)
					context.enqueueWork(() -> player.getData(PLAYER_VARIABLES).deserializeNBT(player.registryAccess(), message.data.serializeNBT(context.player().registryAccess()))).exceptionally(e -> {
						context.connection().disconnect(Component.literal(e.getMessage()));
						return null;
					});
			}
		}
	}
}