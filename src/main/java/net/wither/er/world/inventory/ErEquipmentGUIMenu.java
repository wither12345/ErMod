package net.wither.er.world.inventory;

import com.mojang.datafixers.util.Pair;
import net.mcreator.er.ErMod;
import net.mcreator.er.StellaFortunas;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ErMenus;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.network.ErItemVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ErEquipmentGUIMenu extends AbstractContainerMenu{
	public final Map<String, Object> menuState = new HashMap<>() {
		@Override
		public Object put(String key, Object value) {
			if (!this.containsKey(key) && this.size() >= 7)
				return null;
			return super.put(key, value);
		}
	};
	public final Level world;
	public final Player entity;
	public int x, y, z;

	public ErEquipmentGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(ErMenus.EQUIPMENT.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
        Container container = new SimpleContainer( 7);
        this.addSlot(new VisionSlot(entity, container, 0, 43, 53));
        this.addSlot(new StellaFortunaSlot(entity, container, 1, 115, 53));
        if(this.entity instanceof ErEntityInterface erEntityInterface) {
            this.addSlot(new ArtifactItemSlot(erEntityInterface, ArtifactSlot.FLOWER_OF_LIFE, container, 2, 7, 26));
            this.addSlot(new ArtifactItemSlot(erEntityInterface, ArtifactSlot.PLUME_OF_DEATH, container, 3, 43, 26));
            this.addSlot(new ArtifactItemSlot(erEntityInterface, ArtifactSlot.SAND_OF_EON, container, 4, 79, 26));
            this.addSlot(new ArtifactItemSlot(erEntityInterface, ArtifactSlot.GOBLET_OF_EONOTHEM, container, 5, 115, 26));
            this.addSlot(new ArtifactItemSlot(erEntityInterface, ArtifactSlot.CIRCLET_OF_LOGOS, container, 6, 151, 26));
        }
        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return player.isAlive();
	}

	@Override
	public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack itemStack = slot.getItem();
			itemstack = itemStack.copy();
			if (index < 7) {
				if (!this.moveItemStackTo(itemStack, 7, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemStack, itemstack);
			} else if (!this.moveItemStackTo(itemStack, 0, 7, false)) {
				if (index < 7 + 27) {
					if (!this.moveItemStackTo(itemStack, 7 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else {
					if (!this.moveItemStackTo(itemStack, 7, 7 + 27, false))
						return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (itemStack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(playerIn, itemStack);
		}
		return itemstack;
	}

    private static class VisionSlot extends ItemTagSlot {
        private static final TagKey<Item> VISION_TAG = ItemTags.create(new ResourceLocation("er:vision"));
        private final Player player;

        public VisionSlot(Player player, Container container, int id, int x, int y) {
            super(VISION_TAG, container, id, x, y);
            LazyOptional<ErItemVariables.PlayerVariables> lazyOpt = player.getCapability(ErItemVariables.PLAYER_VARIABLES);
            if(lazyOpt.isPresent() && lazyOpt.resolve().isPresent())
                this.container.setItem(this.getSlotIndex(), lazyOpt.resolve().get().Vision.copy());
            this.player = player;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            ErItemVariables.PlayerVariables _vars = player.getCapability(ErItemVariables.PLAYER_VARIABLES).resolve().get();
            _vars.Vision = this.getItem().copy();
            _vars.syncToClient(player);
        }
    }

    private static class StellaFortunaSlot extends ItemTagSlot{
        private static final TagKey<Item> STELLA_FORTUNA_TAG = ItemTags.create(new ResourceLocation("er:stella_fortuna"));
        private final Player player;

        public StellaFortunaSlot(Player player, Container container, int id, int x, int y) {
            super(STELLA_FORTUNA_TAG, container, id, x, y);
            LazyOptional<ErItemVariables.PlayerVariables> lazyOpt = player.getCapability(ErItemVariables.PLAYER_VARIABLES);
            if(lazyOpt.isPresent() && lazyOpt.resolve().isPresent())
                this.container.setItem(this.getSlotIndex(), lazyOpt.resolve().get().Stella_Fortuna);
            this.player = player;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            if(player.level() instanceof ServerLevel) {
                ErItemVariables.PlayerVariables _vars = player.getCapability(ErItemVariables.PLAYER_VARIABLES).resolve().get();
                _vars.Stella_Fortuna = this.getItem().copy();
                StellaFortunas.applyAttr(player, _vars.Stella_Fortuna);
                _vars.syncToClient(player);
            }
        }
    }

    private static class ArtifactItemSlot extends Slot{
        private final ArtifactSlot slot;
        private final ErEntityInterface erEntityInterface;
        private final ResourceLocation empty;

        public ArtifactItemSlot(ErEntityInterface player, ArtifactSlot slot, Container container, int id, int x, int y) {
            super(container, id, x, y);
            this.slot = slot;
            this.erEntityInterface = player;
            this.container.setItem(this.getSlotIndex(), erEntityInterface.er$getArtifact(slot).copy());
            this.empty = new ResourceLocation(ErMod.MODID, "item/" + slot.getSerializedName() + "_empty");
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack itemStack) {
            ArtifactData data = DataComponentsRegister.ARTIFACT.getData(itemStack);
            return data != null && data.slot() == this.slot;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            this.erEntityInterface.er$setArtifact(this.slot, this.getItem());
            erEntityInterface.er$updateArtifact();
        }

        @Override
        public @Nullable Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(InventoryMenu.BLOCK_ATLAS, empty);
        }
    }
}