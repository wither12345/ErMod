package net.wither.er.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum ArtifactSlot implements StringRepresentable {
    FLOWER_OF_LIFE(0, "flower_of_life"),
    PLUME_OF_DEATH(1, "plume_of_death"),
    SAND_OF_EON(2, "sand_of_eon"),
    GOBLET_OF_EONOTHEM(3, "goblet_of_eonothem"),
    CIRCLET_OF_LOGOS(4, "circlet_of_logos");


    public static final IntFunction<ArtifactSlot> BY_ID = ByIdMap.continuous((slotGroup) -> slotGroup.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StringRepresentable.EnumCodec<ArtifactSlot> CODEC = StringRepresentable.fromEnum(ArtifactSlot::values);
    public static final StreamCodec<ByteBuf, ArtifactSlot> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, (slotGroup) -> slotGroup.id);

    private final int id ;
    private final String name;

    ArtifactSlot(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static ArtifactSlot byName(String p_20748_) {
        ArtifactSlot artifactSlot = CODEC.byName(p_20748_);
        if (artifactSlot != null) {
            return artifactSlot;
        } else {
            throw new IllegalArgumentException("Invalid slot '" + p_20748_ + "'");
        }
    }
}
