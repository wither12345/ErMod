package net.wither.er.world.inventory;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.jei_recipes.AlchemyCraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.wither.er.init.ErMenus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER;

public class AlchemyCraftGuiMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
	public final static HashMap<String, Object> guiState = new HashMap<>();
	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private IItemHandler internal;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private BlockEntity boundBlockEntity = null;
	private AlchemyCraftingRecipe stackRecipe = null;

	public AlchemyCraftGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(ErMenus.ALCHEMY_CRAFT.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.internal = new ItemStackHandler(5);
		BlockPos pos = null;
        if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}
		if (pos != null) {
			if (extraData.readableBytes() == 1) { // bound to item
				byte hand = extraData.readByte();
				ItemStack itemstack = hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem();
				this.boundItemMatcher = () -> itemstack == (hand == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem());
				itemstack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
					this.internal = capability;
					this.bound = true;
				});

			} else if (extraData.readableBytes() > 1) { // bound to entity
				extraData.readByte(); // drop padding
				boundEntity = world.getEntity(extraData.readVarInt());
				if (boundEntity != null) {
					boundEntity.getCapability(ITEM_HANDLER, null).ifPresent(cap -> {
						this.internal = cap;
						this.bound = true;
					});
				}
			} else { // might be bound to block
				boundBlockEntity = this.world.getBlockEntity(pos);
				if (boundBlockEntity instanceof BaseContainerBlockEntity baseContainerBlockEntity) {
					this.internal = new InvWrapper(baseContainerBlockEntity);
					this.bound = true;
				}
			}
		}
		this.customSlots.put(0, this.addSlot(new SlotItemHandler(internal, 0, 79, 35) {
			@Override
			public void setChanged() {
				super.setChanged();
				changing(entity);
			}

			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return ErModItems.MORA_BAG.get() == stack.getItem();
			}
		}));
		this.customSlots.put(1, this.addSlot(new SlotItemHandler(internal, 1, 34, 17) {
			@Override
			public void setChanged() {
				super.setChanged();
				changing(entity);
			}
		}));
		this.customSlots.put(2, this.addSlot(new SlotItemHandler(internal, 2, 34, 35) {
			@Override
			public void setChanged() {
				super.setChanged();
				changing(entity);
			}
		}));
		this.customSlots.put(3, this.addSlot(new SlotItemHandler(internal, 3, 34, 53) {
			@Override
			public void setChanged() {
				super.setChanged();
				changing(entity);
			}
		}));
		this.customSlots.put(4, this.addSlot(new SlotItemHandler(internal, 4, 124, 35) {
			@Override
			public void onTake(@NotNull Player entity, @NotNull ItemStack stack) {
				super.onTake(entity, stack);
				if (stackRecipe == null)
					return;
				if (entity.containerMenu instanceof Supplier<?> _current && _current.get() instanceof Map _slots) {
                    for (int i = 1; i <= stackRecipe.getIngredient().size(); i++) {
                        ((Slot) _slots.get(i)).remove(stackRecipe.getIngredient().get(i - 1).getCount());
                    }
                    final int _tagValue = (((Slot) _slots.get(0)).getItem().getOrCreateTag().getInt("moras")) - stackRecipe.getMora();
					((Slot) _slots.get(0)).getItem().getOrCreateTag().putInt("moras", _tagValue);
                }
				changing(entity);
			}

			@Override
			public boolean mayPlace(@NotNull ItemStack stack) {
				return false;
			}
		}));
		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 8 + si * 18, 142));
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			else if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			else if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return true;
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 5) {
				if (!this.moveItemStackTo(itemstack1, 5, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, 5, false)) {
				if (index < 5 + 27) {
					if (!this.moveItemStackTo(itemstack1, 5 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else {
					if (!this.moveItemStackTo(itemstack1, 5, 5 + 27, false))
						return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0)
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@Override
	protected boolean moveItemStackTo(@NotNull ItemStack p_38904_, int p_38905_, int p_38906_, boolean p_38907_) {
		boolean flag = false;
		int i = p_38905_;
		if (p_38907_) {
			i = p_38906_ - 1;
		}
		if (p_38904_.isStackable()) {
			while (!p_38904_.isEmpty() && (p_38907_ ? i >= p_38905_ : i < p_38906_)) {
				Slot slot = this.slots.get(i);
				ItemStack itemstack = slot.getItem();
				if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(p_38904_, itemstack)) {
					int j = itemstack.getCount() + p_38904_.getCount();
					int k = slot.getMaxStackSize(itemstack);
					if (j <= k) {
						p_38904_.setCount(0);
						itemstack.setCount(j);
						slot.set(itemstack);
						flag = true;
					} else if (itemstack.getCount() < k) {
						p_38904_.shrink(k - itemstack.getCount());
						itemstack.setCount(k);
						slot.set(itemstack);
						flag = true;
					}
				}
				if (p_38907_) {
					i--;
				} else {
					i++;
				}
			}
		}
		if (!p_38904_.isEmpty()) {
			if (p_38907_) {
				i = p_38906_ - 1;
			} else {
				i = p_38905_;
			}
			while (p_38907_ ? i >= p_38905_ : i < p_38906_) {
				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(p_38904_)) {
					int l = slot1.getMaxStackSize(p_38904_);
					slot1.setByPlayer(p_38904_.split(Math.min(p_38904_.getCount(), l)));
					slot1.setChanged();
					flag = true;
					break;
				}
				if (p_38907_) {
					i--;
				} else {
					i++;
				}
			}
		}
		return flag;
	}

	@Override
	public void removed(@NotNull Player playerIn) {
		super.removed(playerIn);
		if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
			if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
				for (int j = 0; j < internal.getSlots(); ++j) {
					if (j == 4)
						continue;
					playerIn.drop(internal.getStackInSlot(j), false);
					if (internal instanceof IItemHandlerModifiable ihm)
						ihm.setStackInSlot(j, ItemStack.EMPTY);
				}
			} else {
				for (int i = 0; i < internal.getSlots(); ++i) {
					if (i == 4)
						continue;
					playerIn.getInventory().placeItemBackInInventory(internal.getStackInSlot(i));
					if (internal instanceof IItemHandlerModifiable ihm)
						ihm.setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}

	public Map<Integer, Slot> get() {
		return customSlots;
	}

	public void changing(Player entity) {
		if (entity == null)
			return;
		if (entity.containerMenu instanceof Supplier<?> _current && _current.get() instanceof Map _slots) {
			ItemStack _setstack = getResult(_slots, entity.level()).copy();
			((Slot) _slots.get(4)).set(_setstack);
			entity.containerMenu.broadcastChanges();
		}
	}

	private ItemStack getResult(Map slots, Level world) {
		net.minecraft.world.item.crafting.RecipeManager rm = world.getRecipeManager();
		List<AlchemyCraftingRecipe> recipes = rm.getAllRecipesFor(AlchemyCraftingRecipe.Type.INSTANCE).stream().toList();
		for (AlchemyCraftingRecipe recipe : recipes) {
			NonNullList<Ingredient> ingredients = recipe.getIngredients();
			if (test(slots, 1, recipe))
				continue;
			if (test(slots, 2, recipe))
				continue;
			if (test(slots, 3, recipe))
				continue;
			if (((Slot) slots.get(0)).getItem().getOrCreateTag().getInt("moras") < recipe.getMora())
				continue;
			stackRecipe = recipe;
			return recipe.getResultItem(null);
		}
		stackRecipe = null;
		return ItemStack.EMPTY;
	}

	private static boolean test(Map slots, int index, AlchemyCraftingRecipe recipe) {
		NonNullList<ItemStack> ingredients = recipe.getIngredient();
		Slot slot = (Slot) slots.get(index);
		if (ingredients.size() < index)
			return slot.getItem().getItem() != ItemStack.EMPTY.getItem();
		ItemStack item = ingredients.get(index - 1);
		return slot.getItem().getCount() < item.getCount() || !(item.getItem() == slot.getItem().getItem());
	}
}