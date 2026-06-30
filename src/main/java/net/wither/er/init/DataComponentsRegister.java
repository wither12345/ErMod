package net.wither.er.init;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.item.data.weapon.WeaponAttributeData;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Consumer;

public class DataComponentsRegister {
    public static final HashMap<String, ItemDataHolder<?>> holderHashMap = new HashMap<>();

    public static final ItemDataHolder<WeaponLevelData> WEAPON_LEVEL = ItemDataHolder.create("er:weapon_level", WeaponLevelData.WEAPON_LEVEL);
    public static final ItemDataHolder<WeaponAttributeData> WEAPON_ATTR = ItemDataHolder.create("er:weapon_attr", WeaponAttributeData.WEAPON_ATTR);
    public static final ItemDataHolder<ArtifactData> ARTIFACT = ItemDataHolder.create("er:artifact",  ArtifactData.ARTIFACT_DATA);

    public record ItemDataHolder<T>(String id, Capability<T> capability){
        public boolean itemHas(ItemStack itemStack){
            if(capability == null)
                return false;
            return itemStack.getCapability(capability).isPresent();
        }

        public @Nullable T getData(ItemStack itemStack){
            if(capability == null)
                return null;
            LazyOptional<T> capa = itemStack.getCapability(capability);
            if(capa.isPresent() && capa.resolve().isPresent())
                return capa.resolve().get();
            return null;
        }

        public void update(@NotNull ItemStack itemStack, Consumer<T> operator){
            itemStack.getCapability(capability).ifPresent(operator::accept);
        }

        public static<T> ItemDataHolder<T> create(String id, Capability<T> capability){
            ItemDataHolder<T> newHolder = new ItemDataHolder<>(id, capability) ;
            holderHashMap.put(id, newHolder);
            return newHolder;
        }
    }
}