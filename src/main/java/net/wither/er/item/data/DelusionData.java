package net.wither.er.item.data;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class DelusionData {
    public static final Capability<DelusionData> DELUSION = CapabilityManager.get(new CapabilityToken<>() {
    });
    public float critMulti;
    public float dmgMulti;
    public float healthConsume;

    public DelusionData(float critMulti, float dmgMulti, float healthConsume) {
        this.critMulti = critMulti;
        this.dmgMulti = dmgMulti;
        this.healthConsume = healthConsume;
    }


    public static class CapabilityProvider implements ICapabilitySerializable<CompoundTag> {
        private final DelusionData data;
        private final LazyOptional<DelusionData> instance;

        public CapabilityProvider(){
            this(new DelusionData(0.5f, 0.2f, 0.01f));
        }

        public CapabilityProvider(DelusionData data){
            this.data = data;
            this.instance = LazyOptional.of(() -> this.data);
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == DELUSION ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putFloat("crit_multi", data.critMulti);
            tag.putFloat("dmg_multi", data.dmgMulti);
            tag.putFloat("health_consume", data.healthConsume);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            data.critMulti = tag.getFloat("crit_multi");
            data.dmgMulti = tag.getFloat("dmg_multi");
            data.healthConsume = tag.getFloat("health_consume");
        }
    }
}
