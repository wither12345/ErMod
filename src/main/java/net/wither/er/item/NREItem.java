package net.wither.er.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.wither.er.item.data.NREData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NREItem extends Item {
    public NREItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack bag, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        if (bag.getCount() != 1 || action != ClickAction.PRIMARY)
            return false;
        ItemStack itemStack = slot.getItem();
        List<ItemStack> items = NREData.read(itemStack);
        if (itemStack.isEmpty()) {
            while (!items.isEmpty() && items.get(0).isEmpty())
                items.remove(0);
            if (items.isEmpty())
                return false;

            ItemStack itemStack1 = items.remove(0);
            this.playRemoveOneSound(player);
            slot.safeInsert(itemStack1);
            NREData.encode(bag, items);
            return true;
        } else if (itemStack.getFoodProperties(player) != null) {
            this.playInsertSound(player);
            NREData.tryPush(items, itemStack, player);
            NREData.encode(bag, items);
            return true;
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack bag, @NotNull ItemStack input_item, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (bag.getCount() != 1 || action != ClickAction.PRIMARY || !slot.allowModification(player))
            return false;
        List<ItemStack> items = NREData.read(bag);

        if (input_item.isEmpty()) {
            while (!items.isEmpty() && items.get(0).isEmpty())
                items.remove(0);
            if (items.isEmpty())
                return false;

            ItemStack itemStack1 = items.remove(0);
            this.playRemoveOneSound(player);
            access.set(itemStack1);
            NREData.encode(bag, items);
            return true;
        } else if (input_item.getFoodProperties(player) != null) {
            this.playInsertSound(player);
            NREData.tryPush(items, input_item, player);
            NREData.encode(bag, items);
            return true;
        }

        return true;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        List<ItemStack> items = NREData.read(itemStack);
        return NREData.getDuration(items);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        List<ItemStack> items = NREData.read(itemstack);
        if(NREData.isAvailable(items, player)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        List<ItemStack> items = NREData.read(itemStack);
        while (!items.isEmpty() && items.get(0).isEmpty())
            items.remove(0);
        if(!items.isEmpty()){
            ItemStack rest = items.get(0).finishUsingItem(level, livingEntity);
            if(rest != items.get(0) && livingEntity instanceof Player player)
                player.addItem(rest);

            while(!items.isEmpty() && items.get(0).isEmpty()) {
                items.remove(0);
            }
        }
        NREData.encode(itemStack, items);
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        List<ItemStack> items = NREData.read(itemStack);
        NREData.putTooltip(items, components);
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    private void playRemoveOneSound(Entity player) {
        player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity player) {
        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
    }
}
