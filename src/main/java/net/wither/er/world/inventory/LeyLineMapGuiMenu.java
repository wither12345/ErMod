
package net.wither.er.world.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.ErMenus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LeyLineMapGuiMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
	public final static HashMap<String, Object> guistate = new HashMap<>();
	public final Level world;
	public final Player entity;
	public final ItemStack mapItem;
	private final Map<Integer, Slot> customSlots = new HashMap<>();

	public LeyLineMapGuiMenu(int id, Inventory inv , FriendlyByteBuf extraData) {
		super(ErMenus.LEY_LINE_MAP.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.mapItem = entity.getMainHandItem() ;
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return true;
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
		return ItemStack.EMPTY;
	}

	public Map<Integer, Slot> get() {
		return customSlots;
	}
}
