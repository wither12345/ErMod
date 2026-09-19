package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.wither.er.client.renderer.hypostasiscube.HypostasisCubeState;

import java.util.Optional;
import java.util.function.Supplier;

public class SerializerRegister {
    public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, ErMod.MODID);

    public static final Supplier<EntityDataSerializer<Optional<HypostasisCubeState.Turning>>> TURNING_SERIALIZER =
            SERIALIZERS.register("cube_turning", () ->
                    EntityDataSerializer.simple(HypostasisCubeState.Turning::encode, HypostasisCubeState.Turning::decode)
            );
}
