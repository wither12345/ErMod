/*
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.er as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.er;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

import io.netty.buffer.ByteBuf;

public record ErData(int entityID, int time) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ErData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("er", "frozen_data"));
	// Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
	// 'name' will be encoded and decoded as a string
	// 'age' will be encoded and decoded as an integer
	// The final parameter takes in the previous parameters in the order they are provided to construct the payload object
	public static final StreamCodec<ByteBuf, ErData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ErData::entityID, ByteBufCodecs.VAR_INT, ErData::time, ErData::new);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
