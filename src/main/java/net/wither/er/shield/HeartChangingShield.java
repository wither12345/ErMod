package net.wither.er.shield;

import net.minecraft.client.gui.Gui;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface HeartChangingShield {
	@OnlyIn(Dist.CLIENT)
	Gui.HeartType getType();
}
