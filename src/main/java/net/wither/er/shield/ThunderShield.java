package net.wither.er.shield;

import net.mcreator.er.ErMod;
import net.mcreator.er.init.ErModEntities;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.wither.er.elements.Element;
import net.wither.er.elements.ElementSource;
import net.wither.er.init.ElementRegistry;
import net.wither.er.entity.ArcEntity;

import java.util.UUID;

public class ThunderShield extends ElementalShield implements RenderShield {
	private static final UUID uuid = UUID.fromString("027AE91C-6050-D873-834D-0A37B43CB026");

	//health = 240
	@OnlyIn(Dist.CLIENT)
	public static class Client {
        private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("er:textures/entities/electro_crystallize_shield.png");
		private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
	}

	public float onHurt(ShieldStack stack, Entity owner, DamageSource source, float damage, int elemental_type) {
		return damage;
	}

	@Override
    UUID getKbResLocation() {
		return uuid;
	}

	@Override
	Element getElement() {
		return ElementRegistry.THUNDER_SHIELD.get();
	}

	@Override
	float getGauge() {
		return 8f;
	}

	@Override
	public boolean tick(ShieldStack stack, Entity owner) {
		stack.health += 1f;
		if (stack.health > 10) {
			stack.health = 0f;
			if (owner instanceof Targeting own && owner instanceof LivingEntity living) {
				LivingEntity target = own.getTarget();
				if (target != null && target.distanceToSqr(owner) <= 32) {
					target.hurt(ElementSource.createDamageSource(owner.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK),
                                    owner, new ElementSource(ElementRegistry.ELECTRO.get(), new ResourceLocation(ErMod.MODID, "thunder_shield"), 1, true)),
                            0.25f * (float) living.getAttributeValue(Attributes.ATTACK_DAMAGE));
					if (owner.level() instanceof ServerLevel _level) {
						ArcEntity entityToSpawn = ErModEntities.ARC.get().spawn(_level, BlockPos.containing(owner.getX(), owner.getY() + 2, owner.getZ()), MobSpawnType.MOB_SUMMONED);
                        if (entityToSpawn != null) {
                            entityToSpawn.setActiveTarget(target.getId());
							entityToSpawn.setOnlyEffect(true);
                        }
					}
				}
			}
		}
		return super.tick(stack, owner);
	}

	@OnlyIn(Dist.CLIENT)
	public RenderType getRender() {
		return Client.RENDER_TYPE;
	}
}
