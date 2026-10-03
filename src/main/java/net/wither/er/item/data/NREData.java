package net.wither.er.item.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NREData{
    public static void tryPush(List<ItemStack> items, ItemStack itemStack, LivingEntity living){
        if(itemStack.getFoodProperties(living) != null && !items.isEmpty()) {
            for (ItemStack stack: items) {
                if (ItemStack.isSameItem(stack, itemStack)) {
                    int total = stack.getCount() + itemStack.getCount();
                    int count = Math.min(stack.getMaxStackSize(), total);
                    stack.setCount(count);
                    itemStack.setCount(total - count);
                    if(total == count) return;
                }
            }
        }
        if(items.size() < 16) {
            items.add(itemStack.copyAndClear());
        }
    }

    public static boolean isAvailable(List<ItemStack> items, Player player){
        if(items.isEmpty()) return false;
        ItemStack itemStack = items.get(0);
        FoodProperties properties = itemStack.getFoodProperties(player);
        return properties != null && player.canEat(properties.canAlwaysEat());
    }

    public static int getDuration(List<ItemStack> items) {
        if(items.isEmpty()) return 0;
        return items.get(0).getUseDuration();
    }

    public static void putTooltip(List<ItemStack> items, List<Component> components){
        for(ItemStack itemStack: items){
            components.add(itemStack.getDisplayName().copy().append(Component.literal("x" + itemStack.getCount())));
        }
    }

    public static @NotNull List<ItemStack> read(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(tag.contains("nre_data")){
            return new ArrayList<>(ItemStack.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("nre_data")).result().orElse(new ArrayList<>()));
        }
        return new ArrayList<>();
    }

    public static void encode(ItemStack itemStack, List<ItemStack> items){
        if(items == null)return;
        CompoundTag tag = itemStack.getOrCreateTag();
        ItemStack.CODEC.listOf().encodeStart(NbtOps.INSTANCE, items).result().ifPresent(t -> {
            tag.put("nre_data", t);
        });
    }
}
