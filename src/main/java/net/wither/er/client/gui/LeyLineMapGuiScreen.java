package net.wither.er.client.gui;

import net.mcreator.er.ErMod;
import net.mcreator.er.item.LeyLineMapItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.wither.er.network.LeyLineLeapData;
import net.wither.er.world.inventory.LeyLineMapGuiMenu;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;

public class LeyLineMapGuiScreen extends AbstractContainerScreen<LeyLineMapGuiMenu> {
	private final Level world;
	private final ItemStack mapItem;
	private final ArrayList<ImageButton> imagebuttons;

	public LeyLineMapGuiScreen(LeyLineMapGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.mapItem = container.mapItem;
		this.imageWidth = 176;
		this.imageHeight = 166;
		imagebuttons = new ArrayList<>();
	}

    private static final ResourceLocation statue = new ResourceLocation("er:textures/screens/map_statue_of_the_seven.png");
    private static final ResourceLocation waypoint = new ResourceLocation("er:textures/screens/map_teleport_waypoint.png");
    private static final ResourceLocation statueHold = new ResourceLocation("er:textures/screens/map_statue_of_the_seven_hold.png");
    private static final ResourceLocation waypointHold = new ResourceLocation("er:textures/screens/map_teleport_waypoint_hold.png");

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		if (mapItem.getItem() instanceof LeyLineMapItem) {
			Integer mapid = MapItem.getMapId(mapItem);
			MapItemSavedData mapitemsaveddata;
			if (mapid != null) {
				mapitemsaveddata = MapItem.getSavedData(mapid, this.minecraft.level);
			} else {
				mapitemsaveddata = null;
			}
			this.renderMap(guiGraphics, mapid, mapitemsaveddata, this.leftPos, this.topPos, 1.5F);
		}
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void init() {
		super.init();
		CompoundTag tag = mapItem.getOrCreateTag();
		Set<String> keys = tag.getCompound("WayPoints").getAllKeys();
		for (String key : keys) {
			String pos[] = key.split(",");
			MapItemSavedData data = MapItem.getSavedData(mapItem, world);
			int scale = 1 << data.scale;
			int posX = (Integer.parseInt(pos[0]) - tag.getInt("centerX")) * 3 / 2;
			int posZ = (Integer.parseInt(pos[2]) - tag.getInt("centerZ")) * 3 / 2;
			ResourceLocation normalLocation = statue;
			if (tag.getCompound("WayPoints").getByte(key) != 2) {
				normalLocation = waypoint;
			}
			ImageButton button  = new ImageButton(this.leftPos + posX / scale + 80, this.topPos + posZ / scale + 80, 16, 16, 0, 0, 16, normalLocation, 16, 32, e -> {
				ErMod.PACKET_HANDLER.sendToServer(new LeyLineLeapData(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2])));
			});


			imagebuttons.add(button);
			this.addRenderableWidget(button);
		}
	}

	private void renderMap(GuiGraphics graphics, int id, @Nullable MapItemSavedData data, int x, int y, float scale) {
		if (data != null) {
			graphics.pose().pushPose();
			graphics.pose().translate((float) x, (float) y, 1.0F);
			graphics.pose().scale(scale, scale, 1.0F);
            if (this.minecraft != null) {
                this.minecraft.gameRenderer.getMapRenderer().render(graphics.pose(), graphics.bufferSource(), id, data, true, 15728880);
            }
            //graphics.flush();
			graphics.pose().popPose();
		}
	}
}