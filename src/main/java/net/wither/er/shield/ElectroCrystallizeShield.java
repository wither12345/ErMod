package net.wither.er.shield;

import net.mcreator.er.init.ErModAttributes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.UUID;

public class ElectroCrystallizeShield extends ErShield implements RenderShield {
	private static final UUID uuid = UUID.fromString("41BA4F55-5363-A8F4-BC28-18322EB59BB8");

	@OnlyIn(Dist.CLIENT)
	public static class Client {
        private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("er:textures/entities/electro_crystallize_shield.png");
		private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
	}

	public float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type) {
		float multi = 1.0f;
		if(owner instanceof LivingEntity living)
			multi = (float) living.getAttribute(ErModAttributes.SHIELD_STRENGTH.get()).getValue() / 100f + 1f;
		if (elemental_type == 4)
			multi *= 2.5f;
		if (stack.health * multi > damage) {
			stack.health -= damage / multi;
			return damage;
		}
        float ret = stack.health * multi;
        stack.health = -1;
        stack.time = 0;
        return ret;
	}

	@Override
	UUID getKbResLocation() {
		return uuid;
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
