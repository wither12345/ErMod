package net.wither.er.item.morabag;

import net.mcreator.er.init.ErModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.player.ErPlayerInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MoraBagItemPlus extends Item {
    private static List<MoraVal> moraVals;

    public MoraBagItemPlus() {
		super(new Properties().stacksTo(1).component(DataComponentsRegister.MORA_BAG.get(), 1));
	}

    public static @NotNull List<MoraVal> getVals(){
        if(moraVals == null)
            moraVals = MoraValueListener.moraVals;
        return moraVals;
    }

    @Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext context, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemStack, context, list, flag);
		String hoverText = String.valueOf(itemStack.getOrDefault(DataComponentsRegister.MORA_BAG.get(), 0));
        list.add(Component.literal(hoverText));
	}

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack itemStack) {
        return Optional.of(new MoraBagComponent(getVals()));
    }

    @Override
	public boolean overrideStackedOnOther(ItemStack bag, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (bag.getCount() != 1 || action != ClickAction.SECONDARY)
			return false;
        tryReadFromOld(bag);
        ItemStack itemStack = slot.getItem();
        if (itemStack.isEmpty()) {
            ItemStack itemStack1 = getItemFromBag(bag, ((ErPlayerInterface)player).er$getMoraIndex());
            this.playRemoveOneSound(player);
            slot.safeInsert(itemStack1);
            return true;
        }
        int val = getVal(itemStack);
        if (val > 0) {
            putMora(bag, val * itemStack.getCount());
            itemStack.setCount(0);
            this.playInsertSound(player);
            return true;
        }
        int count_ = getMoraFromCustomData(itemStack);
        if (count_ > 0) {
            itemStack.shrink(1);
            putMora(bag, count_);
            this.playInsertSound(player);
            return true;
        }
        return false;
	}

	@Override
	public boolean overrideOtherStackedOnMe(@NotNull ItemStack bag, @NotNull ItemStack input_item, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
		if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            tryReadFromOld(bag);
            if (input_item.isEmpty()) {
                //int consume = Math.min(mora_count, 64);
                //bag.update(DataComponentsRegister.MORA_BAG.get(), 0, c -> c - consume);
                ItemStack itemStack = getItemFromBag(bag, ((ErPlayerInterface)player).er$getMoraIndex());
				this.playRemoveOneSound(player);
				access.set(itemStack);
                return true;
			}
            int val = getVal(input_item);
            if (val > 0) {
                putMora(bag, val * input_item.getCount());
                input_item.setCount(0);
                this.playInsertSound(player);
                return true;
            }
            int count_ = getMoraFromCustomData(input_item);
            if (count_ > 0) {
                input_item.shrink(1);
                putMora(bag, count_);
                this.playInsertSound(player);
                return true;
            }
        }
        return false;
	}

	private void playRemoveOneSound(Entity player) {
		player.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(Entity player) {
		player.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + player.level().getRandom().nextFloat() * 0.4F);
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player entity, @NotNull InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        ItemStack itemStack = entity.getItemInHand(hand);
        tryReadFromOld(itemStack);
        int count = 0;
        if (entity.isShiftKeyDown()) {
            if (entity.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable _modHandlerIter) {
                for (int _idx = 0; _idx < _modHandlerIter.getSlots(); _idx++) {
                    ItemStack itemStack_ = _modHandlerIter.getStackInSlot(_idx);
                    if (itemStack_.getItem() == ErModItems.MORA.get()) {
                        count = count + itemStack_.getCount();
                        itemStack_.shrink(itemStack_.getCount());
                    }
                    int c = itemStack_.getOrDefault(DataComponentsRegister.MORA_BAG.get(), 0);
                    if(c > 0){
                        count += c;
                        itemStack_.shrink(1);
                    }
                }
            }
            putMora(itemStack, count);
        } else {
            int c = itemStack.getOrDefault(DataComponentsRegister.MORA_BAG, 0);
            if (c >= 64) {
                ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ErModItems.MORA.get(), 64));
                putMora(itemStack, -64);
            }
            else {
                ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ErModItems.MORA.get(), c));
                itemStack.set(DataComponentsRegister.MORA_BAG.get(), 0);
            }
        }
		return ar;
	}

    private static ItemStack getItemFromBag(ItemStack bag, int index){
        List<MoraVal> vals = getVals();
        if(index < vals.size()){
            int total = bag.getOrDefault(DataComponentsRegister.MORA_BAG.get(), 0);
            MoraVal val = vals.get(index);
            int get = Math.min(total / val.val(), val.item().getDefaultMaxStackSize());
            putMora(bag, - get * val.val());
            return new ItemStack(val.item(), get);
        }
        return ItemStack.EMPTY;
    }

    private static int getMoraFromCustomData(ItemStack itemStack){
        CompoundTag data = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return data.contains("moras") ? data.getInt("moras") : 0 ;
    }

    private static void putMora(ItemStack stack, int count){
        stack.update(DataComponentsRegister.MORA_BAG.get(), Math.max(0, count), c -> c + count);
    }

    private static void tryReadFromOld(ItemStack itemStack){
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        if(data != null && data.copyTag().contains("moras")){
            itemStack.set(DataComponentsRegister.MORA_BAG.get(), data.copyTag().getInt("moras"));
            CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> tag.remove("moras"));
        }
    }

    public static int getVal(ItemStack itemStack){
        for(MoraVal val : getVals()){
            if(itemStack.is(val.item()))
                return val.val();
        }
        return 0;
    }

    public record MoraVal(Item item, int val){
    }
}