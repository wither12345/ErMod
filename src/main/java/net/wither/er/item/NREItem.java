package net.wither.er.item;

import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.NREData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NREItem extends Item {
    public NREItem() {
        super(new Properties().stacksTo(1).component(DataComponentsRegister.NRE.get(), new NREData(List.of())));
    }


    @Override
    public boolean overrideStackedOnOther(ItemStack bag, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
        if (bag.getCount() != 1 || action != ClickAction.PRIMARY)
            return false;
        ItemStack itemStack = slot.getItem();
        NREData data = bag.get(DataComponentsRegister.NRE);
        if(data != null){
            if(itemStack.isEmpty()){
                ArrayList<ItemStack> list = new ArrayList<>(data.items());
                while(!list.isEmpty() && list.getFirst().isEmpty())
                    list.removeFirst();
                if(list.isEmpty())
                    return false;

                ItemStack itemStack1 = list.removeFirst();
                this.playRemoveOneSound(player);
                slot.safeInsert(itemStack1);
                bag.set(DataComponentsRegister.NRE, new NREData(list));
                return true;
            }
            else if(itemStack.has(DataComponents.FOOD)){
                this.playInsertSound(player);
                bag.set(DataComponentsRegister.NRE, data.tryPush(itemStack));
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack bag, @NotNull ItemStack input_item, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
        if (action == ClickAction.PRIMARY && slot.allowModification(player)) {
            NREData data = bag.get(DataComponentsRegister.NRE);
            if(data != null){
                if(input_item.isEmpty()){
                    ArrayList<ItemStack> list = new ArrayList<>(data.items());
                    while(!list.isEmpty() && list.getFirst().isEmpty())
                        list.removeFirst();
                    if(list.isEmpty())
                        return false;

                    ItemStack itemStack1 = list.removeFirst();
                    this.playRemoveOneSound(player);
                    access.set(itemStack1);
                    bag.set(DataComponentsRegister.NRE, new NREData(list));
                    return true;
                }
                else if(input_item.has(DataComponents.FOOD)){
                    this.playInsertSound(player);
                    bag.set(DataComponentsRegister.NRE, data.tryPush(input_item));
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack, @NotNull LivingEntity living) {
        NREData data = itemStack.get(DataComponentsRegister.NRE);
        if(data != null) return data.getDuration(living);
        return super.getUseDuration(itemStack, living);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        NREData data = itemstack.get(DataComponentsRegister.NRE);
        if(data != null && data.isAvailable(player)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemstack);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        NREData data = itemStack.get(DataComponentsRegister.NRE);
        if(data != null){
            ArrayList<ItemStack> items = new ArrayList<>(data.items());
            while(!items.isEmpty() && items.getFirst().isEmpty())
                items.removeFirst();
            if(!items.isEmpty()){
                ItemStack rest = items.getFirst().finishUsingItem(level, livingEntity);
                if(rest != items.getFirst() && livingEntity instanceof Player player)
                    player.addItem(rest);

                while(!items.isEmpty() && items.getFirst().isEmpty())
                    items.removeFirst();
                itemStack.set(DataComponentsRegister.NRE, new NREData(items));
            }
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        NREData data = itemStack.get(DataComponentsRegister.NRE);
        if(data != null)
            data.putTooltip(components);
        super.appendHoverText(itemStack, tooltipContext, components, tooltipFlag);
    }

    private void playRemoveOneSound(Entity player) {
        player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity player) {
        player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
    }
}
