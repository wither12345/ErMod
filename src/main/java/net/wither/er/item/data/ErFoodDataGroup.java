package net.wither.er.item.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public record ErFoodDataGroup(List<Simple> dataMap, @Nullable ErFoodData defaultVal) implements FoodDataProvider {
    @Override
    public ErFoodData getFoodData(ItemStack itemStack) {
        for (Simple entry : dataMap) {
            if (entry.test(itemStack.getOrCreateTag()))
                return entry.data();
        }
        return this.defaultVal;
    }

    public record Simple(CompoundTag predicate, ErFoodData data) {
        private boolean test(CompoundTag itemMap) {
            for (String s : predicate.getAllKeys()) {
                if (!itemMap.contains(s) || !Objects.equals(itemMap.get(s), predicate.get(s)))
                    return false;
            }
            return true;
        }
    }

    public static class Builder {
        private final ImmutableList.Builder<Simple> listBuilder = new ImmutableList.Builder<>();
        private ErFoodData defaultBuilder;

        public void put(Simple simple) {
            this.listBuilder.add(simple);
        }

        public void setDefault(ErFoodData data) {
            this.defaultBuilder = data;
        }

        public ErFoodDataGroup build() {
            return new ErFoodDataGroup(this.listBuilder.build(), this.defaultBuilder);
        }
    }
}
