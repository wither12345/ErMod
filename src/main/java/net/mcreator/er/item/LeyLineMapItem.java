package net.mcreator.er.item;

import org.jetbrains.annotations.NotNull;

import net.wither.er.world.inventory.LeyLineMapGuiMenu;

import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.init.ErModBlocks;

import io.netty.buffer.Unpooled;

public class LeyLineMapItem extends MapItem {
	public LeyLineMapItem() {
		super(new Item.Properties());
		//super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}

	private static int createNewSavedData(Level p_151121_, int p_151122_, int p_151123_, int p_151124_, boolean p_151125_, boolean p_151126_, ResourceKey<Level> p_151127_) {
		MapItemSavedData mapitemsaveddata = MapItemSavedData.createFresh(p_151122_, p_151123_, (byte) p_151124_, p_151125_, p_151126_, p_151127_);
		int i = p_151121_.getFreeMapId();
		p_151121_.setMapData(makeKey(i), mapitemsaveddata);
		return i;
	}

	public static ItemStack create(Level level, int p_42888_, int p_42889_, byte p_42890_, boolean p_42891_, boolean p_42892_) {
		ItemStack itemstack = new ItemStack(ErModItems.LEY_LINE_MAP.get());
		createAndStoreSavedData(itemstack, level, p_42888_, p_42889_, p_42890_, p_42891_, p_42892_, level.dimension());
		return itemstack;
	}

	private static void createAndStoreSavedData(ItemStack p_151112_, Level p_151113_, int p_151114_, int p_151115_, int p_151116_, boolean p_151117_, boolean p_151118_, ResourceKey<Level> p_151119_) {
		int i = createNewSavedData(p_151113_, p_151114_, p_151115_, p_151116_, p_151117_, p_151118_, p_151119_);
		storeMapData(p_151112_, i);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		super.useOn(context);
		BlockPos locate = context.getClickedPos();
		if ((context.getLevel().getBlockState(BlockPos.containing(locate.getX(), locate.getY(), locate.getZ()))).getBlock() == ErModBlocks.TELEPORT_WAYPOINT.get())
			putWaypoint(context.getItemInHand(), context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), false);
		return InteractionResult.SUCCESS;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level p_41145_, Player player, @NotNull InteractionHand p_41147_) {
		ItemStack itemstack = player.getItemInHand(p_41147_);
		if (player instanceof ServerPlayer _ent) {
			BlockPos _bpos = BlockPos.containing(player.getX(), player.getY(), player.getZ());
			_ent.openMenu(new MenuProvider() {
				@Override
				public @NotNull Component getDisplayName() {
					return Component.literal("LeyLineMapGui");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
					return new LeyLineMapGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
				}
			});
		}
		return InteractionResultHolder.success(itemstack);
	}

	private static MapItemSavedData getMapData(ItemStack map, Level level) {
		Integer mapid = MapItem.getMapId(map);
		return getSavedData(mapid, level);
	}

	public static void putWaypoint(ItemStack map, Level level, int x, int y, int z, boolean type) {
		//0 teleport 1 statue
		if (map.getItem() instanceof LeyLineMapItem) {
			MapItemSavedData data = getMapData(map, level);
			if (data.dimension != level.dimension())
				return;
			int range = 64 << data.scale;
			if (x <= data.centerX - range || x >= data.centerX + range || z <= data.centerZ - range || z >= data.centerZ + range)
				return;
			CompoundTag tag = map.getOrCreateTag().getCompound("WayPoints");
			tag.putByte(x + "," + y + "," + z, type ? (byte) 2 : (byte) 1);
			map.getOrCreateTag().put("WayPoints", tag);
		}
	}

	private static void storeMapData(ItemStack itemStack, int id) {
		itemStack.getOrCreateTag().putInt("map", id);
	}
	//public static teleportTo(ItemStack map , int x , int y , int z)
}