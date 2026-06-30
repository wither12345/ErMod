package net.wither.er.world.inventory;

import net.mcreator.er.init.ErModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ErMenus;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.ArtifactLevel;
import org.jetbrains.annotations.NotNull;

import static net.wither.er.init.DataComponentsRegister.ARTIFACT;

public class ArtifactTransmuterGuiMenu extends AbstractContainerMenu {
    private final Container container ;
    private int newExp ;
    private int newTotalExp ;
    private int newLv ;
    private int minorUpgrade ;
    private final int[] shrink = {0,0,0,0,0};


    public ArtifactTransmuterGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ErMenus.ARTIFACT_TRANSMUTER_GUI.get(), id);
        container = new TransientCraftingContainer(this,6,1) ;

        this.addSlot(new Slot(container, 0, 43, 8) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return ErModItems.MORA_BAG.get() == stack.getItem();
            }
        });
        this.addSlot(new Slot(container, 1, 43, 44));
        this.addSlot(new Slot(container, 2, 7, 44));
        this.addSlot(new Slot(container, 3, 16, 17));
        this.addSlot(new Slot(container, 4, 70, 17));
        this.addSlot(new Slot(container, 5, 79, 44));

        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));

    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 6) {
                if (!this.moveItemStackTo(itemstack1, 6, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, 6, false)) {
                if (index < 6 + 27) {
                    if (!this.moveItemStackTo(itemstack1, 6 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemstack1, 6, 6 + 27, false))
                        return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        if (playerIn instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
                for (int j = 0; j < 6; ++j) {
                    playerIn.drop(container.getItem(j), false);
                }
            } else {
                for (int i = 0; i < 6; ++i) {
                    playerIn.getInventory().placeItemBackInInventory(container.getItem(i));
                }
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }


    @Override
    public void slotsChanged(@NotNull Container container) {
        this.predict() ;
        super.slotsChanged(container);
    }

    private void predict(){
        ItemStack slot1 = this.getSlot(1).getItem() ;
        ItemStack slot0 = this.getSlot(0).getItem() ;
        ArtifactData artifactData = DataComponentsRegister.ARTIFACT.getData(slot1);
        if(artifactData != null && slot0.getOrCreateTag().contains("moras")){
            int experience = artifactData.level().experience() ;
            int total_experience = artifactData.level().total_experience() ;
            final int mora = slot0.getOrCreateTag().getInt("moras") ;
            shrink[0] = 0 ;
            minorUpgrade = 0 ;
            int lv = artifactData.level().level() ;
            int rarity = artifactData.rarity() ;
            for(int i = 2 ; i < 6 ; i ++){
                shrink[i - 1] = 0 ;
                if(lv >= ArtifactData.getMaxLevel(rarity))
                    break;

                ItemStack item = this.getSlot(i).getItem() ;
                ArtifactData artifactData1 = DataComponentsRegister.ARTIFACT.getData(item);
                int exp_per = getExp(artifactData1) ;
                if(item.getItem() == ErModItems.SANCTIFYING_UNCTION.get())
                    exp_per = 2500 ;
                if(item.getItem() == ErModItems.SANCTIFYING_ESSENCE.get())
                    exp_per = 10000 ;

                if(exp_per > 0) {
                    while(exp_per + shrink[0] <= mora && item.getCount() > shrink[i - 1] && lv < ArtifactData.getMaxLevel(rarity)) {
                        if(artifactData1 != null) {
                            experience += (int) (artifactData1.level().total_experience() * 0.8);
                            total_experience += (int) (artifactData1.level().total_experience() * 0.8);
                        }
                        experience += exp_per ;
                        total_experience += exp_per ;
                        shrink[0] += exp_per ;
                        shrink[i - 1] ++ ;
                        while (experience >= ArtifactData.getMaxExp(lv, rarity) && lv < ArtifactData.getMaxLevel(rarity)){
                            experience -= ArtifactData.getMaxExp(lv, rarity) ;
                            lv ++ ;
                            if(lv % 4 == 0)
                                minorUpgrade ++ ;
                        }
                    }
                }
            }

            newExp = experience;
            newTotalExp = total_experience;
            newLv = lv ;
        }
    }

    public void enhance(){
        ItemStack slot1 = this.getSlot(1).getItem() ;
        ItemStack slot0 = this.getSlot(0).getItem() ;
        ArtifactData artifactData = DataComponentsRegister.ARTIFACT.getData(slot1);
        if(artifactData != null && slot0.getOrCreateTag().contains("moras")) {
            final int mora = slot0.getOrCreateTag().getInt("moras");
            for (int i = 2; i < 6; i++) {
                this.getSlot(i).getItem().shrink(shrink[i - 1]);
            }
            slot0.getOrCreateTag().putInt("moras", mora - shrink[0]);
            ARTIFACT.update(slot1, d -> d.setLevel(new ArtifactLevel(newLv, newExp, newTotalExp), minorUpgrade));
        }
        this.sendAllDataToRemote();
    }

    public static int getExp(ArtifactData artifactData){
        if(artifactData == null) return 0;
        if(artifactData.rarity() <= 2)
            return artifactData.rarity() * 420 ;
        return artifactData.rarity() * 1260 - 2520 ;
    }

    public int getNewExp() {
        return newExp;
    }

    public int getNewLv() {
        return newLv;
    }

    public int getMinorUpgrade() {
        return minorUpgrade;
    }
}
