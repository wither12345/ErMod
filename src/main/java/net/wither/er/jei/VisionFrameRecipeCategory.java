package net.wither.er.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.Vision;
import net.wither.er.recipe.VisionFrameRecipe;
import org.jetbrains.annotations.NotNull;

import static net.wither.er.recipe.VisionFrameRecipe.UID;

public class VisionFrameRecipeCategory implements IRecipeCategory<VisionFrameRecipe> {
    public static final RecipeType<VisionFrameRecipe> TYPE = new RecipeType<>(
            UID, VisionFrameRecipe.class
    );

    private final IDrawable background;
    private final IDrawable icon;

    public VisionFrameRecipeCategory(IGuiHelper guiHelper) {
        // 工作台 3x3 背景，大小 116x54（标准工作台配方背景）
        this.background = guiHelper.createDrawable(
                ResourceLocation.withDefaultNamespace("textures/gui/container/crafting_table.png"),
                28, 15, 116, 54
        );
        // 使用 Vision 物品作为图标
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(ErModItems.PYRO_VISION.get())
        );
    }

    @Override
    public @NotNull RecipeType<VisionFrameRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("recipe.er.vision_frame");
    }

    @Override
    public void draw(VisionFrameRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 116;
    }

    @Override
    public int getHeight() {
        return 54;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, VisionFrameRecipe recipe, @NotNull IFocusGroup focuses) {
        // 1. 获取 Vision 物品（作为输入）
        ItemStack visionStack = new ItemStack(ErModItems.PYRO_VISION.get());

        // 2. 获取边框材料（根据 frame 从 Tag 中获取一个代表性物品）
        ItemStack frameStack = getRepresentativeFrameItem(recipe.getFrame());

        // 3. 输出：合成后的 Vision（带有对应 Frame）
        ItemStack outputStack = createOutput(recipe);

        // 4. 布局：3x3 网格中，Vision 放在 (0,0)，边框材料放在 (1,0)
        //    输出放在右侧 (4,1) 位置（对应 GUI 偏移）
        //    注意：JEI 的坐标是相对于背景图的左上角，背景是 116x54
        //    标准工作台：输入区域从 (2,2) 开始，每个格子 18x18，输出在 (94,18)
        //    更精确的做法是使用蓝图绘制，这里简化
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
                .addItemStack(visionStack);

        builder.addSlot(RecipeIngredientRole.INPUT, 20, 2)
                .addItemStack(frameStack);

        // 空的中间格（占位），表示其他格为空
        // 或者你可以添加一个提示，但不需要显式添加空槽

        builder.addSlot(RecipeIngredientRole.OUTPUT, 94, 18)
                .addItemStack(outputStack);
    }

    /**
     * 获取对应 Frame 的代表物品（从 Tag 中取第一个）
     * 注意：如果 Tag 包含多个物品，这里只取第一个作为展示
     */
    private ItemStack getRepresentativeFrameItem(Vision.Frame frame) {
        // 根据 frame 名称获取对应的 Tag
        var tagKey = TagKey.create(
                net.minecraft.core.registries.Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(ErMod.MODID, frame.getName())
        );
        // 从 Tag 中获取第一个物品
        var holder = net.minecraft.core.registries.BuiltInRegistries.ITEM.getTag(tagKey)
                .flatMap(tag -> tag.stream().findFirst())
                .orElse(null);
        if (holder != null) {
            return new ItemStack(holder.value());
        }
        return ItemStack.EMPTY;
    }

    /**
     * 创建输出物品：复制 Vision 并设置 Frame
     */
    private ItemStack createOutput(VisionFrameRecipe recipe) {
        ItemStack vision = new ItemStack(ErModItems.PYRO_VISION.get());
        vision.set(DataComponentsRegister.VISION_FRAME.get(), recipe.getFrame());
        return vision;
    }
}