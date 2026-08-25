package net.wither.er.network;

import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.wither.er.block.LinkMechanismBase;
import net.wither.er.entity.LinkMechanismTelpher;

import java.util.function.Supplier;

public record LineMechanismMessage(BlockPos pos) {

	public LineMechanismMessage(FriendlyByteBuf buffer) {
		this(buffer.readBlockPos());
	}

	public static void buffer(LineMechanismMessage message, FriendlyByteBuf buffer) {
        buffer.writeBlockPos(message.pos);
	}


	public static void handle(final LineMechanismMessage data, final Supplier<NetworkEvent.Context> contextSupplier) {
		Player entity = contextSupplier.get().getSender();
		if(entity == null) return;
		Level world = entity.level();

        contextSupplier.get().enqueueWork(() -> {
            if (world.getBlockState(data.pos).is(ErModBlocks.LINK_MECHANISM_BASE.get())){
                Vec3 center = LinkMechanismBase.getCenter(data.pos, world.getBlockState(data.pos).getValue(BlockStateProperties.HORIZONTAL_FACING));
                double d = entity.distanceToSqr(center);
                Entity vehicle = entity.getVehicle();
                if(vehicle == null && d <= 25){
                    LinkMechanismTelpher telpher = new LinkMechanismTelpher(entity, data.pos);
                    world.addFreshEntity(telpher);
                }
                else if(vehicle instanceof LinkMechanismTelpher telpher && d <= 256){
                    telpher.setDestination(data.pos);
                }
            }
        });
	}
}