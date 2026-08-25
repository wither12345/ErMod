package net.wither.er.network;

import io.netty.buffer.ByteBuf;
import net.mcreator.er.init.ErModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.wither.er.block.LinkMechanismBase;
import net.wither.er.entity.LinkMechanismTelpher;

public record LineMechanismMessage(BlockPos pos) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<LineMechanismMessage> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "line_mechanism"));
    public static final StreamCodec<ByteBuf, LineMechanismMessage> STREAM_CODEC = StreamCodec.composite(
            // x
            BlockPos.STREAM_CODEC, LineMechanismMessage::pos,
            LineMechanismMessage::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final LineMechanismMessage data, final IPayloadContext context) {
        Player entity = context.player();
        Level world = entity.level();
        context.enqueueWork(() -> {
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
