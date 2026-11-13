package net.wither.er.client.renderer.samachurl;

import net.mcreator.er.ErMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.client.models.SamachurlModel;
import net.wither.er.entity.samachurl.Samachurl;

public abstract class SamachurlRenderer extends HumanoidMobRenderer<Samachurl, SamachurlModel<Samachurl>> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ErMod.MODID, "samachurl"), "main");
    public SamachurlRenderer(EntityRendererProvider.Context context) {
        super(context, new SamachurlModel<>(context.bakeLayer(LAYER_LOCATION)), 0.5f);
    }
}
