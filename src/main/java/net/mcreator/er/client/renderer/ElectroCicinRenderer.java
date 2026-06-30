
package net.mcreator.er.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.mcreator.er.entity.ElectroCicinEntity;
import net.mcreator.er.client.model.Modelelectro_cicin;

public class ElectroCicinRenderer extends MobRenderer<ElectroCicinEntity, Modelelectro_cicin<ElectroCicinEntity>> {
	public ElectroCicinRenderer(EntityRendererProvider.Context context) {
		super(context, new Modelelectro_cicin(context.bakeLayer(Modelelectro_cicin.LAYER_LOCATION)), 0.3f);
	}

	@Override
	public ResourceLocation getTextureLocation(ElectroCicinEntity entity) {
        return new ResourceLocation("er:textures/entities/electro_cicin.png");
	}
}
