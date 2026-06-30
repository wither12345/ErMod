package net.wither.er.network;

import net.mcreator.er.ErMod;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ErItemVariables {
	public static final Capability<ErItemVariables.PlayerVariables> PLAYER_VARIABLES = CapabilityManager.get(new CapabilityToken<>() {
    });

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
		public ItemStack Stella_Fortuna = ItemStack.EMPTY;
		public ItemStack Vision = ItemStack.EMPTY;

		@Override
		public CompoundTag serializeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.put("Stella_Fortuna", Stella_Fortuna.save(new CompoundTag()));
			nbt.put("Vision", Vision.save(new CompoundTag()));
			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			Stella_Fortuna = ItemStack.of(nbt.getCompound("Stella_Fortuna"));
			Vision = ItemStack.of(nbt.getCompound("Vision"));
		}

		public void syncToClient(Player player){
			ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new ItemVariablesSyncMessage(this, player.getId()));
		}
	}

	public record ItemVariablesSyncMessage(PlayerVariables data, int id) {
		public ItemVariablesSyncMessage(FriendlyByteBuf buffer) {
			this(new PlayerVariables(), buffer.readInt());
			data.deserializeNBT(buffer.readNbt());
		}

		public static void buffer(ItemVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeInt(message.id());
			buffer.writeNbt(message.data().serializeNBT());
		}

		public static void handleData(final ItemVariablesSyncMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
            if(Minecraft.getInstance().level == null) {
                ErMod.LOGGER.error("ErItemVariables.class found null instance level");
                return;
            }
			Entity entity = Minecraft.getInstance().level.getEntity(message.id);
            PlayerVariables data = message.data();
            if(entity == null) return;
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer() && data != null)
					entity.getCapability(PLAYER_VARIABLES).ifPresent(cap -> {
						cap.Stella_Fortuna = data.Stella_Fortuna;
						cap.Vision = data.Vision;
					});
			});
            context.setPacketHandled(true);
		}
	}
}