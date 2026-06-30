package net.mcreator.er.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class DMGProcedure {
	public static void execute(LevelAccessor world, DamageSource damagesource, Entity entity, Entity sourceentity) {
		if (damagesource == null || entity == null || sourceentity == null)
			return;
		if (damagesource.is(TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.parse("minecraft:is_fire")))) {
			entity.hurt(new DamageSource(world.holderOrThrow(DamageTypes.ARROW), sourceentity), 1);
		}
	}
}