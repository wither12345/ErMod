package net.wither.er.item.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record NREData(List<ItemStack> items) {
    public static final Codec<NREData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemStack.CODEC.listOf().fieldOf("items").forGetter(NREData::items)
            ).apply(instance, NREData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, NREData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(
                    ArrayList::new,
                    ItemStack.STREAM_CODEC
            ), NREData::items,
            NREData::new
    );

    public NREData tryPush(ItemStack itemStack){
        ArrayList<ItemStack> builder = new ArrayList<>(items);
        if(itemStack.has(DataComponents.FOOD) && !items.isEmpty()) {
            for (ItemStack stack: items) {
                if (ItemStack.isSameItem(stack, itemStack)) {
                    int total = stack.getCount() + itemStack.getCount();
                    int count = Math.min(stack.getMaxStackSize(), total);
                    stack.setCount(count);
                    itemStack.setCount(total - count);
                    if(total == count) return new NREData(this.items);;
                }
            }
        }
        if(items.size() < 16) {
            builder.add(itemStack.copyAndClear());
            return new NREData(builder);
        }
        return this;
    }

    public boolean isAvailable(Player player){
        if(items.isEmpty()) return false;
        ItemStack itemStack = items.getFirst();
        FoodProperties properties = itemStack.getFoodProperties(player);
        return properties != null && player.canEat(properties.canAlwaysEat());
    }

    public int getDuration(LivingEntity living) {
        if(items.isEmpty()) return 0;
        return items.getFirst().getUseDuration(living);
    }

    public void putTooltip(List<Component> components){
        for(ItemStack itemStack: this.items){
            components.add(itemStack.getDisplayName().copy().append(Component.literal("x" + itemStack.getCount())));
        }
    }
}
