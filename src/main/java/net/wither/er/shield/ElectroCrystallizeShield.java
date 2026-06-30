package net.wither.er.shield;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class ElectroCrystallizeShield extends ErShield implements HeartChangingShield, RenderShield {
	private static final ResourceLocation resourcelocation = ResourceLocation.parse("er:electro_crystallize.kb_res");

	@OnlyIn(Dist.CLIENT)
	public static class Client {
		private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.parse("er:textures/entities/electro_crystallize_shield.png");
		private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
		public static final EnumProxy<Gui.HeartType> ELECTRO = new EnumProxy<>(Gui.HeartType.class, ResourceLocation.parse("er:heart/electro_crystallize/full"), ResourceLocation.parse("er:heart/electro_crystallize/full_blinking"),
				ResourceLocation.parse("er:heart/electro_crystallize/half"), ResourceLocation.parse("er:heart/electro_crystallize/half_blinking"), ResourceLocation.parse("er:heart/electro_crystallize/hardcore_full"),
				ResourceLocation.parse("er:heart/electro_crystallize/hardcore_full_blinking"), ResourceLocation.parse("er:heart/electro_crystallize/hardcore_half"), ResourceLocation.parse("er:heart/electro_crystallize/hardcore_half_blinking"));
	}

	@OnlyIn(Dist.CLIENT)
	public Gui.HeartType getType() {
		return Client.ELECTRO.getValue();
	}

	public float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type) {
		float multi = 1.0f;
		if(owner instanceof LivingEntity living)
			multi = (float) living.getAttribute(ErModAttributes.SHIELD_STRENGTH).getValue() / 100f + 1f;
		if (elemental_type == 4)
			multi *= 2.5;
		if (stack.health * multi > damage) {
			stack.health -= damage / multi;
			return damage;
		}
		stack.health = -1;
		stack.time = 0;
		return stack.health * multi;
	}

	@Override
	ResourceLocation getKbResLocation() {
		return resourcelocation;
	}

	@Override
	public boolean tick(ShieldStack stack, Entity owner) {
		return super.tick(stack,owner) && stack.health > 0 ;
	}

	@OnlyIn(Dist.CLIENT)
	public RenderType getRender() {
		return Client.RENDER_TYPE;
	}
}
