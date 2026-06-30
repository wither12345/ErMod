package net.wither.er.world.inventory;

import net.mcreator.er.init.ErModItems;
import net.mcreator.er.jei_recipes.AlchemyCraftingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wither.er.init.ErMenus;
import net.wither.er.recipe.converting.AlchemyConvertingRecipe;
import net.wither.er.recipe.converting.AlchemyConvertingRecipeListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class AlchemyGuiMenu extends AbstractContainerMenu {
    public final Player entity;
    @Nullable private AlchemyCraftingRecipe stackedCrafting = null;
    @Nullable private List<AlchemyConvertingRecipe> stackedConvertingRecipe = null ;
    private Item stackedConvertingItem ;
    private int convertingRecipeIndex ;
    private int convertingItemIndex ;
    private Stage stage ;

    public AlchemyGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ErMenus.ALCHEMY_GUI.get(), id);
        this.entity = inv.player;
        stage = Stage.CRAFTING ;
        Container container = new TransientCraftingContainer(this, 5, 1);
        Container outputContainer = new SimpleContainer( 4);
        this.addSlot(new StageMoraSlot(this, container, 0, 79, 35)) ;
        this.addSlot(new StageSlot(this, EnumSet.of(Stage.CRAFTING), container, 1, 34, 17));
        this.addSlot(new StageSlot(this, EnumSet.allOf(Stage.class), container, 2, 34, 35));
        this.addSlot(new StageSlot(this, EnumSet.of(Stage.CRAFTING), container, 3, 34, 53));
        this.addSlot(new CraftingOutput(this, outputContainer, 0, 124, 35));
        this.addSlot(new StageSlot(this, EnumSet.of(Stage.CONVERTING), container, 4, 79, 35));
        this.addSlot(new ConvertingOutput(this, outputContainer, 1, 124, 17));
        this.addSlot(new ConvertingOutput(this, outputContainer, 2, 124, 35));
        this.addSlot(new ConvertingOutput(this, outputContainer, 3, 124, 53));

        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index < 9) {
                if (!this.moveItemStackTo(itemStack1, 9, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemStack1, itemstack);
            } else if (!this.moveItemStackTo(itemStack1, 0, 9, false)) {
                if (index < 9 + 27) {
                    if (!this.moveItemStackTo(itemStack1, 9 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemStack1, 9, 9 + 27, false))
                        return ItemStack.EMPTY;
                }
                return ItemStack.EMPTY;
            }
            if (itemStack1.getCount() == 0)
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
            if (itemStack1.getCount() == itemstack.getCount())
                return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(@NotNull ItemStack itemStack, int p_38905_, int p_38906_, boolean p_38907_) {
        boolean flag = false;
        int i = p_38905_;
        if (p_38907_) {
            i = p_38906_ - 1;
        }
        if (itemStack.isStackable()) {
            while (!itemStack.isEmpty() && (p_38907_ ? i >= p_38905_ : i < p_38906_)) {
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, itemstack)) {
                    int j = itemstack.getCount() + itemStack.getCount();
                    int k = slot.getMaxStackSize(itemstack);
                    if (j <= k) {
                        itemStack.setCount(0);
                        itemstack.setCount(j);
                        slot.set(itemstack);
                        flag = true;
                    } else if (itemstack.getCount() < k) {
                        itemStack.shrink(k - itemstack.getCount());
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
        if (!itemStack.isEmpty()) {
            if (p_38907_) {
                i = p_38906_ - 1;
            } else {
                i = p_38905_;
            }
            while (p_38907_ ? i >= p_38905_ : i < p_38906_) {
                Slot slot1 = this.slots.get(i);
                ItemStack itemStack1 = slot1.getItem();
                if (itemStack1.isEmpty() && slot1.mayPlace(itemStack)) {
                    int l = slot1.getMaxStackSize(itemStack);
                    slot1.setByPlayer(itemStack.split(Math.min(itemStack.getCount(), l)));
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
        if (playerIn instanceof ServerPlayer serverPlayer) {
            dropCrafting(serverPlayer, false) ;
            dropConverting(serverPlayer);
        }
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        changing(entity) ;
        super.slotsChanged(container);
    }

    public Stage getStage() {
        return stage;
    }

    public void changing(Player entity) {
        if (entity == null)
            return;
        if (entity.containerMenu instanceof AlchemyGuiMenu menu) {
            if(menu.stage == Stage.CRAFTING) {
                ItemStack stack = getCraftingResult(entity.level()).copy();
                if(stack != ItemStack.EMPTY) {
                    slots.get(4).set(stack);
                    entity.containerMenu.broadcastChanges();
                }
            }
            else if(menu.stage == Stage.CONVERTING){
                getConverting() ;
                entity.containerMenu.broadcastChanges();
            }
        }
    }

    public void switchStage(Stage stage){
        if(entity instanceof ServerPlayer player) {
            if (stage == Stage.CRAFTING)
                dropConverting(player);
            else if (stage == Stage.CONVERTING)
                dropCrafting(player, true);
        }
        this.stage = stage ;
    }

    private void dropCrafting(ServerPlayer serverPlayer, boolean ignore2){
        if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected())
            for (int i = 0; i < 4; ++i) {
                if(ignore2 && i == 2)
                    continue;
                serverPlayer.drop(slots.get(i).getItem(), false);
                slots.get(i).set(ItemStack.EMPTY);
            }
        else
            for (int i = 0; i < 4; ++i) {
                if(ignore2 && i == 2)
                    continue;
                serverPlayer.getInventory().placeItemBackInInventory(slots.get(i).getItem());
                slots.get(i).set(ItemStack.EMPTY);
            }
    }

    private void dropConverting(ServerPlayer serverPlayer){
        if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()){
            serverPlayer.drop(slots.get(2).getItem(), false);
            serverPlayer.drop(slots.get(5).getItem(), false);
        }
        else{
            serverPlayer.getInventory().placeItemBackInInventory(slots.get(2).getItem());
            serverPlayer.getInventory().placeItemBackInInventory(slots.get(5).getItem());
        }
        slots.get(2).set(ItemStack.EMPTY);
        slots.get(5).set(ItemStack.EMPTY);
    }

    private ItemStack getCraftingResult(Level world) {
        if (stackedCrafting != null && test(stackedCrafting))
            return stackedCrafting.getResultItem();
        net.minecraft.world.item.crafting.RecipeManager rm = world.getRecipeManager();
        Stream<AlchemyCraftingRecipe> recipes = rm.getAllRecipesFor(AlchemyCraftingRecipe.Type.INSTANCE).stream().filter(this::test);
        Optional<AlchemyCraftingRecipe> optional = recipes.findFirst() ;
        if (optional.isPresent()) {
            stackedCrafting = optional.get();
            return stackedCrafting.getResultItem();
        }
        return ItemStack.EMPTY;
    }

    private void getConverting(){
        if(slots.get(2).getItem().getItem() != stackedConvertingItem || this.stackedConvertingRecipe == null) {
            stackedConvertingItem = slots.get(2).getItem().getItem() ;
            this.stackedConvertingRecipe = AlchemyConvertingRecipeListener.getRecipes(stackedConvertingItem);
            firstConverting();
        }
        placeConvertingItems() ;
    }

    public void nextConverting(){
        if(this.stackedConvertingRecipe == null || this.stackedConvertingRecipe.isEmpty())
            return;
        if(convertingItemIndex * 3 + 3 >= this.stackedConvertingRecipe.get(convertingRecipeIndex).getSize()) {
            int size = this.stackedConvertingRecipe.size();
            convertingItemIndex = 0;
            for (int i = (convertingRecipeIndex + 1) % size; i != convertingRecipeIndex; i = (i + 1) % size) {
                if (this.stackedConvertingRecipe.get(i).test(slots)) {
                    convertingRecipeIndex = i;
                    changing(entity);
                    return;
                }
            }
            changing(entity);
            return;
        }
        convertingItemIndex ++ ;
        changing(entity);
    }

    public void preConverting(){
        if(this.stackedConvertingRecipe == null || this.stackedConvertingRecipe.isEmpty())
            return;
        if(convertingItemIndex == 0) {
            int size = this.stackedConvertingRecipe.size();
            for (int i = (convertingRecipeIndex - 1) % size; i != convertingRecipeIndex; i = (i - 1) % size) {
                if (this.stackedConvertingRecipe.get(i).test(slots)) {
                    convertingRecipeIndex = i;
                    convertingItemIndex = (this.stackedConvertingRecipe.get(i).getSize() - 1) / 3;
                    changing(entity);
                    return;
                }
            }
            convertingItemIndex = (this.stackedConvertingRecipe.get(convertingRecipeIndex).getSize() - 1) / 3;
            changing(entity);
            return;
        }
        convertingItemIndex -- ;
        changing(entity);
    }

    private void firstConverting(){
        if(this.stackedConvertingRecipe == null || this.stackedConvertingRecipe.isEmpty())
            return;
        int size = this.stackedConvertingRecipe.size();
        for (int i = 0; i < size; i ++) {
            if (this.stackedConvertingRecipe.get(i).test(slots)) {
                convertingRecipeIndex = i;
                convertingItemIndex = 0;
                return;
            }
        }
    }

    private void placeConvertingItems(){
        int i  = 0 ;
        if (this.stackedConvertingRecipe != null && !this.stackedConvertingRecipe.isEmpty()) {
            AlchemyConvertingRecipe recipe = this.stackedConvertingRecipe.get(convertingRecipeIndex) ;
            if(recipe.test(slots)) {
                int maxIndex = Math.min(recipe.getSize(), convertingItemIndex * 3 + 3) - convertingItemIndex * 3;
                for (; i < maxIndex; i++) {
                    slots.get(i + 6).set(recipe.getItemStack(i + convertingItemIndex * 3));
                }
            }
        }
        for(; i < 3 ; i ++)
            slots.get(i + 6).set(ItemStack.EMPTY);
    }

    private boolean test(AlchemyCraftingRecipe recipe) {
        if(slots.get(0).getItem().getOrCreateTag().getInt("moras") < recipe.getMora())
            return false;
        NonNullList<ItemStack> ingredients = recipe.getIngredient();
        for(int index = 1 ; index <= 3 ; index ++) {
            Slot slot = slots.get(index);
            if (ingredients.size() < index)
                return slot.getItem().isEmpty();
            ItemStack item = ingredients.get(index - 1);
            if(slot.getItem().getCount() < item.getCount() || item.getItem() != slot.getItem().getItem())
                return false ;
        }
        return true;
    }

    public enum Stage{
        CRAFTING,
        CONVERTING;
    }

    private static class StageSlot extends Slot{
        private final EnumSet<Stage> stages ;
        private final AlchemyGuiMenu menu ;

        public StageSlot(AlchemyGuiMenu menu, EnumSet<Stage> stages, Container container, int id, int x, int y) {
            super(container, id, x, y);
            this.menu = menu;
            this.stages = stages;
        }

        @Override
        public boolean isActive() {
            return this.stages.contains(menu.stage);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack itemStack) {
            return this.stages.contains(menu.stage);
        }
    }

    private static class CraftingOutput extends StageSlot{
        public CraftingOutput(AlchemyGuiMenu menu, Container container, int id, int x, int y) {
            super(menu, EnumSet.of(Stage.CRAFTING), container, id, x, y);
        }

        @Override
        public void onTake(@NotNull Player entity, @NotNull ItemStack stack) {
            super.onTake(entity, stack);
            if (entity.containerMenu instanceof AlchemyGuiMenu menu) {
                if (menu.stage == Stage.CRAFTING && menu.stackedCrafting != null) {
                    for (int i = 1; i <= menu.stackedCrafting.getIngredient().size(); i++) {
                        menu.getSlot(i).remove(menu.stackedCrafting.getIngredient().get(i - 1).getCount());
                    }
                    ItemStack moraBag = menu.getSlot(0).getItem();
                    moraBag.getOrCreateTag().putInt("moras", (moraBag.getOrCreateTag().getInt("moras")) - menu.stackedCrafting.getMora());
                }
                menu.slotsChanged(container);
            }
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }

    private static class StageMoraSlot extends StageSlot{
        public StageMoraSlot(AlchemyGuiMenu menu, Container container, int id, int x, int y) {
            super(menu, EnumSet.of(Stage.CRAFTING), container, id, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return ErModItems.MORA_BAG.get() == stack.getItem() && super.mayPlace(stack);
        }
    }

    private static class ConvertingOutput extends StageSlot{
        public ConvertingOutput(AlchemyGuiMenu menu, Container container, int id, int x, int y) {
            super(menu, EnumSet.of(Stage.CONVERTING), container, id, x, y);
        }

        @Override
        public void onTake(@NotNull Player entity, @NotNull ItemStack stack) {
            super.onTake(entity, stack);
            if (entity.containerMenu instanceof AlchemyGuiMenu menu && menu.stackedConvertingRecipe != null && !menu.stackedConvertingRecipe.isEmpty()) {
                if (menu.stage == Stage.CONVERTING) {
                    menu.slots.get(5).remove(menu.stackedConvertingRecipe.get(menu.convertingRecipeIndex).count()) ;
                    menu.slots.get(2).remove(1) ;
                }
                menu.slotsChanged(container);
            }
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }
}