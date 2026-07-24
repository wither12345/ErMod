package net.wither.er.client.renderer.whopperflower;

import net.mcreator.er.ErMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.wither.er.entity.whopperflower.Whopperflower;
import org.jetbrains.annotations.NotNull;

public class CryoWhopperflowerRenderer extends WhopperflowerRenderer<Whopperflower> {
    ResourceLocation LOCATION = new ResourceLocation(ErMod.MODID, "textures/entities/cryo_whopperflower.png");
    ResourceLocation SHIELD_LOCATION = new ResourceLocation(ErMod.MODID, "textures/entities/cryo_whopperflower_shield.png");

    public CryoWhopperflowerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.addLayer(new WhopperflowerShieldLayer(this, SHIELD_LOCATION));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull Whopperflower cryoWhopperflower) {
        return LOCATION;
    }
}
