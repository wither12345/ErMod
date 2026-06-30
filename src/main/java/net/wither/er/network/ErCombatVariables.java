package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ErCombatVariables {
	public static final Capability<ErCombatVariables.PlayerVariables> PLAYER_VARIABLES = CapabilityManager.get(new CapabilityToken<>() {
	});

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getCapability(PLAYER_VARIABLES).ifPresent(capability -> ErMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ErCombatVariables.CombatVariablesSyncMessage(player.getId(), capability)));
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getCapability(PLAYER_VARIABLES).ifPresent(capability -> ErMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ErCombatVariables.CombatVariablesSyncMessage(player.getId(), capability)));
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer player)
				player.getCapability(PLAYER_VARIABLES).ifPresent(capability -> ErMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), new ErCombatVariables.CombatVariablesSyncMessage(player.getId(), capability)));
		}


		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			event.getOriginal().getCapability(PLAYER_VARIABLES).ifPresent(original -> {
				event.getEntity().getCapability(PLAYER_VARIABLES).ifPresent(clone -> {
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
				});
			});
		}
	}

	public static class PlayerVariablesProvider implements ICapabilitySerializable<CompoundTag> {
		private final PlayerVariables playerVariables = new PlayerVariables();
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
			return cap == PLAYER_VARIABLES ? instance.cast() : LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			return playerVariables.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			playerVariables.deserializeNBT(nbt);
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
		public CompoundTag serializeNBT() {
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
		public void deserializeNBT(CompoundTag nbt) {
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
			if (entity instanceof ServerPlayer serverPlayer)
				entity.getCapability(PLAYER_VARIABLES).ifPresent(capability -> ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new CombatVariablesSyncMessage(serverPlayer.getId(), capability)));
		}

		public void syncAnimation(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				entity.getCapability(PLAYER_VARIABLES).ifPresent(capability -> ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SyncAnimationMessage(capability.animationId, capability.animationTime, serverPlayer.getId())));
		}
	}

	public record CombatVariablesSyncMessage(int id, PlayerVariables data) {
		public CombatVariablesSyncMessage(@NotNull FriendlyByteBuf buffer) {
			this(buffer.readInt(), new PlayerVariables());
			data.deserializeNBT(buffer.readNbt());
		}

		public static void buffer(CombatVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeInt(message.id);
			buffer.writeNbt(message.data().serializeNBT());
		}

		public static void handleData(final CombatVariablesSyncMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
            if (Minecraft.getInstance().level == null) {
                ErMod.LOGGER.error("ErCombatVariables.class found null instance level");
                return;
            }
			Entity entity = Minecraft.getInstance().level.getEntity(message.id);
            if(entity == null) return;
            context.enqueueWork(() -> {
				if (entity instanceof Player player && !context.getDirection().getReceptionSide().isServer() && message.data != null)
					player.getCapability(PLAYER_VARIABLES).ifPresent(cap -> {
						PlayerVariables data = message.data();
						cap.animationId = data.animationId;
						cap.animationTime = data.animationTime;
						cap.stamina = data.stamina;
						cap.staminaRecoveryCooldown = data.staminaRecoveryCooldown;
						cap.stackedMaxSkillCooldown = data.stackedMaxSkillCooldown;
						cap.skillCooldown = data.skillCooldown;
						cap.energyAmount = data.energyAmount;
						cap.burstCooldown = data.burstCooldown;
						cap.stackedMaxBurstCooldown = data.stackedMaxBurstCooldown;
						cap.skillChargingCount = data.skillChargingCount;
					});
			});
			context.setPacketHandled(true);
		}
	}

	public record SyncAnimationMessage(int aId, int time, int pId) {
		public SyncAnimationMessage(FriendlyByteBuf buffer) {
			this(buffer.readInt(), buffer.readInt(), buffer.readInt());
		}

		public static void buffer(SyncAnimationMessage message, FriendlyByteBuf buffer) {
			buffer.writeInt(message.aId()).writeInt(message.time()).writeInt(message.pId());
		}

		public static void handleData(final SyncAnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
            if (Minecraft.getInstance().level == null) {
                ErMod.LOGGER.error("ErCombatVariables.class found null instance level");
                return;
            }
            Entity entity = Minecraft.getInstance().level.getEntity(message.pId());
            if(entity == null) return;

			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer())
                    entity.getCapability(PLAYER_VARIABLES).ifPresent(cap -> {
                        cap.animationTime = message.time();
                        cap.animationId = message.aId();
                    });
			});
			context.setPacketHandled(true);
		}
	}
}