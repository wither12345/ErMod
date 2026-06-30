package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

import java.util.function.Supplier;

@EventBusSubscriber()
public class ErItemVariables {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ErMod.MODID);
	public static final Supplier<AttachmentType<PlayerVariables>> PLAYER_VARIABLES = ATTACHMENT_TYPES.register("item_variables", () -> AttachmentType.serializable(() -> new PlayerVariables()).build());

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		ErMod.addNetworkMessage(ItemVariablesSyncMessage.TYPE, ItemVariablesSyncMessage.STREAM_CODEC, ItemVariablesSyncMessage::handleData);
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
			clone.Stella_Fortuna = original.Stella_Fortuna;
			clone.Vision = original.Vision;
			clone.Flower_Of_Life = original.Flower_Of_Life;
			clone.Plume_of_Death = original.Plume_of_Death;
			clone.Sands_Of_Eon = original.Sands_Of_Eon;
			clone.Goblet_Of_Eonothem = original.Goblet_Of_Eonothem;
			clone.Circlet_Of_Logos = original.Circlet_Of_Logos;
			if (!event.isWasDeath()) {
			}
			event.getEntity().setData(PLAYER_VARIABLES, clone);
		}
	}

	public static class PlayerVariables implements INBTSerializable<CompoundTag> {
		public ItemStack Stella_Fortuna = ItemStack.EMPTY;
		public ItemStack Vision = ItemStack.EMPTY;
		public ItemStack Flower_Of_Life = ItemStack.EMPTY;
		public ItemStack Plume_of_Death = ItemStack.EMPTY;
		public ItemStack Sands_Of_Eon = ItemStack.EMPTY;
		public ItemStack Goblet_Of_Eonothem = ItemStack.EMPTY;
		public ItemStack Circlet_Of_Logos = ItemStack.EMPTY;

		@Override
		public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
			CompoundTag nbt = new CompoundTag();
			nbt.put("Stella_Fortuna", Stella_Fortuna.saveOptional(lookupProvider));
			nbt.put("Vision", Vision.saveOptional(lookupProvider));
			nbt.put("Flower_Of_Life", Flower_Of_Life.saveOptional(lookupProvider));
			nbt.put("Plume_of_Death", Plume_of_Death.saveOptional(lookupProvider));
			nbt.put("Sands_Of_Eon", Sands_Of_Eon.saveOptional(lookupProvider));
			nbt.put("Goblet_Of_Eonothem", Goblet_Of_Eonothem.saveOptional(lookupProvider));
			nbt.put("Circlet_Of_Logos", Circlet_Of_Logos.saveOptional(lookupProvider));
			return nbt;
		}

		@Override
		public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
			Stella_Fortuna = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Stella_Fortuna"));
			Vision = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Vision"));
			Flower_Of_Life = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Flower_Of_Life"));
			Plume_of_Death = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Plume_of_Death"));
			Sands_Of_Eon = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Sands_Of_Eon"));
			Goblet_Of_Eonothem = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Goblet_Of_Eonothem"));
			Circlet_Of_Logos = ItemStack.parseOptional(lookupProvider, nbt.getCompound("Circlet_Of_Logos"));
		}

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				PacketDistributor.sendToAllPlayers(new ItemVariablesSyncMessage(entity.getId(), this));
		}
	}

	public record ItemVariablesSyncMessage(int id, PlayerVariables data) implements CustomPacketPayload {
		public static final Type<ItemVariablesSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "item_variables_sync"));
		public static final StreamCodec<RegistryFriendlyByteBuf, ItemVariablesSyncMessage> STREAM_CODEC = StreamCodec
				.of((RegistryFriendlyByteBuf buffer, ItemVariablesSyncMessage message) -> buffer.writeInt(message.id).writeNbt(message.data().serializeNBT(buffer.registryAccess())), (RegistryFriendlyByteBuf buffer) -> {
					ItemVariablesSyncMessage message = new ItemVariablesSyncMessage(buffer.readInt(), new PlayerVariables());
					message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
					return message;
				});

		@Override
		public Type<ItemVariablesSyncMessage> type() {
			return TYPE;
		}

		public static void handleData(final ItemVariablesSyncMessage message, final IPayloadContext context) {
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