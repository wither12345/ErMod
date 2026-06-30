package net.wither.er.world.inventory;

import net.mcreator.er.init.ErModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.init.ErMenus;
import net.wither.er.item.data.weapon.WeaponLevelData;
import net.wither.er.item.data.weapon.WeaponRefinement;
import net.wither.er.recipe.ascension.AscensionRecipe;
import net.wither.er.recipe.ascension.AscensionRecipeListener;
import org.jetbrains.annotations.NotNull;

import static net.wither.er.item.data.weapon.WeaponLevelData.getMaxLevel;
import static net.wither.er.item.data.weapon.WeaponLevelData.not_enhanceable;

public class WeaponEnhanceGuiMenu extends AbstractContainerMenu {
    /*
    public final Map<String, Object> menuState = new HashMap<>() {
        @Override
        public Object put(String key, Object value) {
            if (!this.containsKey(key) && this.size() >= 7)
                return null;
            return super.put(key, value);
        }
    };
     */
    public final Level world;
    public final Player entity;
    private final IItemHandler internal;
    private final Container container = new TransientCraftingContainer(this,5,1);
    private AscensionRecipe.Single ascensionRecipe ;
    private int mora_use ;

    public WeaponEnhanceGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ErMenus.WEAPON_ENHANCE_GUI.get(), id);
        this.entity = inv.player;
        this.world = inv.player.level();

        this.internal = new ItemStackHandler(1);
        this.addSlot(new MoraSlot(container, 0, 79, 17));
        this.addSlot(new Slot(container, 1, 25, 35));
        this.addSlot(new Slot(container, 2, 61, 53));
        this.addSlot(new Slot(container, 3, 79, 53));
        this.addSlot(new Slot(container, 4, 97, 53));
        this.addSlot(new SlotItemHandler(internal, 0, 133, 35) {
            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                super.onTake(player, stack);
                if (player.containerMenu instanceof WeaponEnhanceGuiMenu menu) {
                    menu.getSlot(1).set(ItemStack.EMPTY);
                    ItemStack mora_bag = WeaponEnhanceGuiMenu.this.getSlot(0).getItem();

                    if(ascensionRecipe == null) {
                        for (int index0 = 2; index0 < 5; index0++) {
                            menu.getSlot(index0).set(ItemStack.EMPTY);
                        }
                    }
                    else {
                        for (int index0 = 2; index0 < 5; index0++) {
                            if(ascensionRecipe.getInput(index0 - 2) != null)
                                menu.getSlot(index0).getItem().shrink(ascensionRecipe.getInput(index0 - 2).getCount());
                        }
                    }

                    if (mora_bag.getComponents().has(DataComponents.CUSTOM_DATA)) {
                        final int mora = mora_bag.getComponents().get(DataComponents.CUSTOM_DATA).copyTag().getInt("moras") - mora_use;
                        CustomData.update(DataComponents.CUSTOM_DATA, mora_bag, tag -> tag.putInt("moras", mora));
                    }

                    player.containerMenu.broadcastChanges();
                }
            }

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));
    }



    @Override
    public void slotsChanged(@NotNull Container container) {
        setChanged() ;
        super.slotsChanged(container);
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
                for (int j = 0; j < 5; ++j) {
                    playerIn.drop(container.getItem(j), false);
                }
            } else {
                for (int i = 0; i < 5; ++i) {
                    playerIn.getInventory().placeItemBackInInventory(container.getItem(i));
                }
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }


    public void setChanged() {
        ItemStack item_0 = this.getSlot(1).getItem();
        ItemStack output = ItemStack.EMPTY ;
        WeaponLevelData weaponLevelData = item_0.getComponents().get(DataComponentsRegister.WEAPON_LEVEL.get());
        WeaponRefinement weaponRefinement = item_0.getComponents().get(DataComponentsRegister.WEAPON_REFINEMENT.get());
        int refine = 0;
        if (weaponLevelData != null && !item_0.is(not_enhanceable)) {
            int level = weaponLevelData.level();
            int experience = weaponLevelData.experience();
            int total_experience = weaponLevelData.total_experience();
            int ascension = weaponLevelData.ascension();
            int star = WeaponLevelData.getItemWeaponStar(item_0) ;
            if(weaponRefinement != null){
                for (int index2 = 2; index2 <= 4; index2++) {
                    ItemStack slotItem = this.getSlot(index2).getItem() ;
                    if(weaponRefinement.refinementItem().value() == slotItem.getItem()){
                        WeaponRefinement refinement = slotItem.get(DataComponentsRegister.WEAPON_REFINEMENT.get());
                        refine += (refinement == null ? 1 : refinement.refineLevel()) ;
                    }
                }
            }
            if(this.getSlot(2).getItem().getItem() == ErModItems.ENCHANTED_MYSTIC_ENHANCEMENT_ORE.get()){
                mora_use = 0 ;
                ascensionRecipe = null ;

                if(AscensionRecipeListener.get(item_0.getItem()) != null)
                    while(AscensionRecipeListener.get(item_0.getItem()).getRecipe(ascension) != null) ascension ++;
                output = item_0.copy();

                final int final_level = getMaxLevel(ascension);
                final int final_experience = 0;
                final int final_total_experience = 0;
                final int final_ascension = ascension;
                output.update(DataComponentsRegister.WEAPON_LEVEL.get(), weaponLevelData, d -> new WeaponLevelData(final_level, final_ascension, final_experience, final_total_experience));
            }
            else if(level < getMaxLevel(ascension)) {
                mora_use = 0 ;
                ascensionRecipe = null ;
                for (int index2 = 2; index2 <= 4; index2++) {
                    ItemStack slotItem = this.getSlot(index2).getItem() ;
                    int exp = WeaponLevelData.getBasicExperience(slotItem) * this.getSlot(index2).getItem().getCount();
                    experience += exp ;
                    total_experience += exp ;
                    mora_use += exp / 10;
                    if(slotItem.getComponents().has(DataComponentsRegister.WEAPON_LEVEL.get()))
                        experience += (int) (slotItem.getComponents().get(DataComponentsRegister.WEAPON_LEVEL.get()).total_experience() * 0.8);
                    total_experience += exp;
                }
                ItemStack mora_bag = this.getSlot(0).getItem();
                if (mora_bag.getComponents().has(DataComponents.CUSTOM_DATA) && mora_use <= mora_bag.getComponents().get(DataComponents.CUSTOM_DATA).copyTag().getInt("moras")) {
                    output = item_0.copy();
                    while (experience >= WeaponLevelData.getMaxExp(level, star) && level < getMaxLevel(ascension)) {
                        experience -= WeaponLevelData.getMaxExp(level, star);
                        level += 1;
                    }
                    final int final_level = level;
                    final int final_experience = experience;
                    final int final_total_experience = total_experience;
                    final int final_ascension = ascension;
                    output.update(DataComponentsRegister.WEAPON_LEVEL.get(), weaponLevelData, d -> new WeaponLevelData(final_level, final_ascension, final_experience, final_total_experience));
                }
            }
            else {
                if(AscensionRecipeListener.get(item_0.getItem()) != null)
                    ascensionRecipe = AscensionRecipeListener.get(item_0.getItem()).getRecipe(ascension) ;
                else ascensionRecipe = null ;
                if(test()){
                    ascension ++ ;
                    output = item_0.copy();
                    final int final_level = level;
                    final int final_experience = experience;
                    final int final_total_experience = total_experience;
                    final int final_ascension = ascension;
                    output.update(DataComponentsRegister.WEAPON_LEVEL.get(), weaponLevelData, d -> new WeaponLevelData(final_level, final_ascension, final_experience, final_total_experience));
                }
            }
        }
        if(refine > 0){
            if(output == ItemStack.EMPTY)
                output = item_0.copy();
            int finalRefine = refine;
            output.update(DataComponentsRegister.WEAPON_REFINEMENT.get(), weaponRefinement, d -> d.refine(finalRefine));
        }
        this.getSlot(5).set(output);
        this.broadcastChanges();
    }

    private boolean test(){
        if(ascensionRecipe == null)
            return false ;
        ItemStack mora_bag = this.getSlot(0).getItem();
        if (!mora_bag.getComponents().has(DataComponents.CUSTOM_DATA) || ascensionRecipe.getMora() > mora_bag.getComponents().get(DataComponents.CUSTOM_DATA).copyTag().getInt("moras"))
            return false ;
        for (int index = 0; index < 3; index++) {
            if(ascensionRecipe.getInput(index) != null && !ascensionRecipe.getInput(index).match(this.getSlot(index + 2).getItem()))
                return false ;
        }
        return true ;
    }
}