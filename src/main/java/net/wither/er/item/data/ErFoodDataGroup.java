package net.wither.er.item.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ErFoodDataGroup(List<Simple> dataMap, @Nullable ErFoodData defaultVal) implements FoodDataProvider {
    @Override
    public ErFoodData getFoodData(ItemStack itemStack) {
        for (Simple entry : dataMap) {
            if (entry.test(itemStack.getComponents()))
                return entry.data();
        }
        return this.defaultVal;
    }

    @Override
    public boolean replacePotion() {
        return true;
    }

    public record Simple(DataComponentPredicate map, ErFoodData data) {
        private boolean test(DataComponentMap itemMap) {
            return map.test(itemMap);
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
