package net.wither.er.client.renderer.whopperflower;

import net.mcreator.er.ErMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.client.models.WhopperflowerModel;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;

public class PyroWhopperflowerRenderer extends WhopperflowerRenderer<Whopperflower> {
    ResourceLocation LOCATION = new ResourceLocation(ErMod.MODID, "textures/entities/pyro_whopperflower.png");
    ResourceLocation SHIELD_LOCATION = new ResourceLocation(ErMod.MODID, "textures/entities/pyro_whopperflower_shield.png");

    public PyroWhopperflowerRenderer(EntityRendererProvider.Context context) {
        super(context, new WhopperflowerModel<>(context.bakeLayer(WhopperflowerModel.PYRO)));
        this.addLayer(new WhopperflowerShieldLayer(this, SHIELD_LOCATION));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull Whopperflower pyroWhopperflower) {
        return LOCATION;
    }
}
