/**
 * The code of this mod element is always locked.
 *
 * You can register new events in this class too.
 *
 * If you want to make a plain independent class, create it using
 * Project Browser -> New... and make sure to make the class
 * outside net.mcreator.er as this package is managed by MCreator.
 *
 * If you change workspace package, modid or prefix, you will need
 * to manually adapt this file to these changes or remake it.
 *
 * This class will be added in the mod root package.
*/
package net.mcreator.er;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.item.data.weapon.OnBurstAbility;
import net.wither.er.item.weapons.AbilityWeapon;
import net.wither.er.network.ErItemVariables;
import net.wither.er.network.StellaFortunaData;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static net.minecraft.core.registries.Registries.DAMAGE_TYPE;

public abstract class StellaFortunas extends Item {
	public final float health;
	public final float damage;
	public final float armor;
	public final boolean levelType;
	private static final UUID locationHealth = UUID.fromString("247D9294-24BF-E2C2-8991-6F7B17B51674") ;
	private static final UUID locationDamage = UUID.fromString("7367D0DC-A141-2726-38D6-5174E16252FB") ;
	private static final UUID locationArmor = UUID.fromString("BA6074B7-A319-664A-BDF0-9FF25C2F844D") ;
    public static final ResourceKey<DamageType> SKILL = ResourceKey.create(DAMAGE_TYPE, new ResourceLocation("er:elemental_skill"));
    public static final ResourceKey<DamageType> BURST = ResourceKey.create(DAMAGE_TYPE, new ResourceLocation("er:elemental_burst"));

	public StellaFortunas(Properties properties, float health, float damage, float armor, boolean levelType) {
		super(properties.stacksTo(1));
		this.health = health;
		this.damage = damage;
		this.armor = armor;
		this.levelType = levelType;
	}

	public abstract void ElementalSkillStart(LivingEntity entity);

	public abstract void ElementalSkillEnd(LivingEntity entity);

	public abstract float getEnergyCost(LivingEntity entity);

	public abstract void ElementalBurstStart(LivingEntity entity);

	public abstract void ElementalBurstEnd(LivingEntity entity);

	public abstract void onTick(LevelAccessor world, LivingEntity entity, double x, double y, double z);

	public abstract boolean hasAnimation(LivingEntity entity);

	public abstract int getMaxCombo(LivingEntity entity);

	public abstract int getAnimationTick(LivingEntity entity, int combo, float speed);

	public abstract void AnimationTicking(LivingEntity entity, int combo, int time, float speed);

	public abstract int getFinishTick(LivingEntity entity, int combo, float speed);

	public abstract AnimationDefinition getAnimation(int combo);

	public abstract void receiveMessage(LivingEntity entity, CompoundTag message);

	public void onBurst(LivingEntity entity){
        if(entity instanceof ErEntityInterface erEntityInterface){
            Object2IntMap<ArtifactEffect> map = erEntityInterface.er$getEffectMap();
            for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                if(effect.getKey() instanceof OnBurstAbility ability){
                    ability.onBurst(entity, effect.getIntValue());
                }
            }
        }
        if(entity.getMainHandItem().getItem() instanceof AbilityWeapon abilityWeapon && abilityWeapon.getAbility() instanceof OnBurstAbility ability){
            CompoundTag tag = entity.getMainHandItem().getOrCreateTag();
            int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
            ability.onBurst(entity, refinement);
        }
	}

	@Override
	public void appendHoverText(ItemStack itemstack, @Nullable Level access, List<Component> list, TooltipFlag flag) {
		if (itemstack.getItem() instanceof StellaFortunas stella) {
			list.add(Component.literal("+" + new java.text.DecimalFormat("##").format(itemstack.getOrCreateTag().getInt("level"))));
			int level = itemstack.getOrCreateTag().getInt("level") + 1;
			int experience = itemstack.getOrCreateTag().getInt("experience");
			int percent = Math.max(Math.min((int) (40 * experience / getExpReq(level)), 40), 0);
			int ascension = itemstack.getOrCreateTag().getInt("ascension");
			list.add(Component.literal(experience + "/" + getExpReq(level)));
			String greenBars = "|".repeat(percent);
			String whiteBars = "|".repeat(40 - percent);
			list.add(Component.literal("§a" + greenBars + "§f" + whiteBars));
			list.add(Component.literal(Component.translatable("attribute.name.generic.max_health").getString() + ":" + new java.text.DecimalFormat("##.#").format(stella.health * levelScaling(level, stella.levelType, ascension))));
			list.add(Component.literal(Component.translatable("attribute.name.generic.attack_damage").getString() + ":" + new java.text.DecimalFormat("##.#").format(stella.damage * levelScaling(level, stella.levelType, ascension))));
			list.add(Component.literal(Component.translatable("attribute.name.generic.armor").getString() + ":" + new java.text.DecimalFormat("##.#").format(stella.armor * levelScaling(level, stella.levelType, ascension))));
		}
	}

	public float getSpeed(LivingEntity entity, int combo) {
		if (combo < this.getMaxCombo(entity)) {
			return (float) (entity.getAttribute(Attributes.ATTACK_SPEED).getValue());
		}
		return 1f;
	}

	public static boolean addExptoItem(ItemStack item, int amount) {
		int experience = item.getOrCreateTag().getInt("experience") + amount;
		int level = item.getOrCreateTag().getInt("level") + 1;
		int ascension = item.getOrCreateTag().getInt("ascension");
		boolean changed = false;
		while (experience >= getExpReq(level) && level < getMaxLevel(ascension)) {
			experience -= getExpReq(level);
			level += 1;
			changed = true;
		}
		final int exp = experience;
		final int lv = level - 1;
		item.getOrCreateTag().putInt("experience", exp);
		item.getOrCreateTag().putInt("level", level);
		return changed;
	}

	public static void addExptoPlayer(Player entity, int amount) {
		ErItemVariables.PlayerVariables _vars = entity.getCapability(ErItemVariables.PLAYER_VARIABLES).orElseGet(ErItemVariables.PlayerVariables::new);
		if (_vars.Stella_Fortuna.getItem() instanceof StellaFortunas) {
			if (addExptoItem(_vars.Stella_Fortuna, amount)) {
				applyAttr(entity, _vars.Stella_Fortuna);
			}
		}
	}

	public static int getExpReq(int level) {
		if (level <= 20)
			return (int) (0.402 * level * level + 12.31 * level + 27.49);
		if (level <= 40)
			return (int) (0.555 * level * level + 17.56 * level - 46.63);
		if (level <= 60)
			return (int) (1.014 * level * level + 29.00 * level - 421.2);
		if (level <= 80)
			return (int) (1.815 * level * level + 43.60 * level - 1012);
		return (int) (2300 * Math.exp(0.165 * (level - 80)));
	}

	public static int getMaxLevel(int ascension) {
		return 90;
	}

	public static void applyAttr(LivingEntity entity, ItemStack item) {
		entity.getAttribute(Attributes.MAX_HEALTH).removeModifier(locationHealth);
		entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(locationDamage);
		entity.getAttribute(Attributes.ARMOR).removeModifier(locationArmor);
		if (item.getItem() instanceof StellaFortunas stella) {
			int level = item.getOrCreateTag().getInt("level") + 1;
			int ascension = item.getOrCreateTag().getInt("ascension");
			entity.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(locationHealth, "stella_health" ,stella.health * levelScaling(level, stella.levelType, ascension) - 20, AttributeModifier.Operation.ADDITION));
			entity.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier(locationDamage, "stella_damage", stella.damage * levelScaling(level, stella.levelType, ascension) - 1, AttributeModifier.Operation.ADDITION));
			entity.getAttribute(Attributes.ARMOR).addPermanentModifier(new AttributeModifier(locationArmor, "stella_armor", stella.armor * levelScaling(level, stella.levelType, ascension), AttributeModifier.Operation.ADDITION));
		}
	}

	public static float levelScaling(int level, boolean type, int ascension) {
		float level_multiplier = (100f + 9 * level) / 109;
		float ascension_mulyiplier = 0;
		if (ascension == 1)
			ascension_mulyiplier = 38f / 182;
		else if (ascension == 2)
			ascension_mulyiplier = 65f / 182;
		else if (ascension == 3)
			ascension_mulyiplier = 101f / 182;
		else if (ascension >= 4)
			ascension_mulyiplier = (27f * ascension + 20) / 182;
		if (type) {
			level_multiplier *= (1900f + level) / 1901;
		}
		return level_multiplier + ascension_mulyiplier;
	}

	public boolean vision(ItemStack item) {
		return true;
	}

	public abstract int elementType();

	public static void sendMessage(LivingEntity entity, CompoundTag message) {
		ErMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new StellaFortunaData(entity.getId(), message));
	}

	/*
		public static void PerformAttack(LivingEntity entity, double RangeMulti, double RectWidth, double RectHeight, float DamageMulti) {
			Level world = entity.level();
			double attackRange = (entity instanceof Player ? entity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE) : 3) * RangeMulti;
			Vec3 lookVec = entity.getLookAngle().normalize();
			Vec3 eyePos = entity.getEyePosition();
			Vec3 forward = lookVec.scale(attackRange);
			Vec3 right = new Vec3(-lookVec.z, 0, lookVec.x).normalize().scale(RectWidth / 2);
			Vec3 up = new Vec3(0, RectHeight / 2, 0);
			Vec3[] corners = {eyePos.add(forward).add(right).add(up), eyePos.add(forward).add(right).subtract(up), eyePos.add(forward).subtract(right).add(up), eyePos.add(forward).subtract(right).subtract(up)};
			AABB roughArea = new AABB(eyePos, eyePos.add(forward)).inflate(RectWidth + 1, RectHeight + 1, RectWidth + 1);
			for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, roughArea, e -> e != entity && isInRotatedRect(e.position(), eyePos, lookVec, attackRange, RectWidth, RectHeight))) {
				float damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti;
				target.hurt(entity.damageSources().mobAttack(entity), damage);
			}
			if (world instanceof ServerLevel serverLevel) {
				Vec3 _forward = lookVec.scale(attackRange);
				Vec3 _right = new Vec3(-lookVec.z, 0, lookVec.x).normalize().scale(RectWidth / 2);
				Vec3 _up = new Vec3(0, RectHeight / 2, 0);
				for (int i = 0; i < 20; i++) {
					double t = i / 20.0;
					Vec3 p1 = eyePos.add(_forward.scale(t)).add(_right).add(_up);
					Vec3 p2 = eyePos.add(_forward.scale(t)).add(_right).subtract(_up);
					Vec3 p3 = eyePos.add(_forward.scale(t)).subtract(_right).add(_up);
					Vec3 p4 = eyePos.add(_forward.scale(t)).subtract(_right).subtract(_up);
					serverLevel.sendParticles(ParticleTypes.END_ROD, p1.x, p1.y, p1.z, 1, 0, 0, 0, 0);
					serverLevel.sendParticles(ParticleTypes.END_ROD, p2.x, p2.y, p2.z, 1, 0, 0, 0, 0);
					serverLevel.sendParticles(ParticleTypes.END_ROD, p3.x, p3.y, p3.z, 1, 0, 0, 0, 0);
					serverLevel.sendParticles(ParticleTypes.END_ROD, p4.x, p4.y, p4.z, 1, 0, 0, 0, 0);
				}
			}
		}
		private static boolean isInRotatedRect(Vec3 point, Vec3 origin, Vec3 direction, double length, double width, double height) {
			Vec3 localPos = point.subtract(origin);
			double forwardDist = localPos.dot(direction);
			if (forwardDist < 0 || forwardDist > length)
				return false;
			Vec3 right = new Vec3(-direction.z, 0, direction.x).normalize();
			double sideDist = Math.abs(localPos.dot(right));
			if (sideDist > width / 2)
				return false;
			double verticalDist = localPos.y;
			return verticalDist >= -height / 2 && verticalDist <= height / 2;
		}
		*/
	public static void PerformAttack(LivingEntity entity, double RangeMulti, double RectWidth, double RectHeight, Vec3 BasicPos, float DamageMulti) {
		Level world = entity.level();
		double attackRange = 3 * RangeMulti;
		float yaw = entity.getYRot();
		Vec3 lookVec = new Vec3(-Math.sin(yaw * Math.PI / 180), 0, Math.cos(yaw * Math.PI / 180)).normalize();
		Vec3 forward = lookVec.scale(attackRange);
		AABB roughArea = new AABB(BasicPos, BasicPos.add(forward)).inflate(RectWidth + 1, RectHeight + 1, RectWidth + 1);
		for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, roughArea, e -> {
			if (e == entity)
				return false;
			AABB targetAABB = e.getBoundingBox().move(BasicPos.scale(-1)); // 转换到局部坐标系
			return isAABBInRotatedRect(targetAABB, lookVec, attackRange, RectWidth, RectHeight);
		})) {
			float damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE) * DamageMulti;
			if (entity instanceof Player player)
				target.hurt(entity.damageSources().playerAttack(player), damage);
			else
				target.hurt(entity.damageSources().mobAttack(entity), damage);
		}
		/*
		if (world instanceof ServerLevel serverLevel) {
			Vec3 _forward = lookVec.scale(attackRange);
			Vec3 _right = new Vec3(-lookVec.z, 0, lookVec.x).normalize().scale(RectWidth / 2);
			Vec3 _up = new Vec3(0, RectHeight / 2, 0);
			for (int i = 0; i < 20; i++) {
				double t = i / 20.0;
				Vec3 p1 = BasicPos.add(_forward.scale(t)).add(_right).add(_up);
				Vec3 p2 = BasicPos.add(_forward.scale(t)).add(_right).subtract(_up);
				Vec3 p3 = BasicPos.add(_forward.scale(t)).subtract(_right).add(_up);
				Vec3 p4 = BasicPos.add(_forward.scale(t)).subtract(_right).subtract(_up);
				serverLevel.sendParticles(ParticleTypes.END_ROD, p1.x, p1.y, p1.z, 1, 0, 0, 0, 0);
				serverLevel.sendParticles(ParticleTypes.END_ROD, p2.x, p2.y, p2.z, 1, 0, 0, 0, 0);
				serverLevel.sendParticles(ParticleTypes.END_ROD, p3.x, p3.y, p3.z, 1, 0, 0, 0, 0);
				serverLevel.sendParticles(ParticleTypes.END_ROD, p4.x, p4.y, p4.z, 1, 0, 0, 0, 0);
			}
		}
		*/
	}

	private static boolean isAABBInRotatedRect(AABB aabb, Vec3 direction, double length, double width, double height) {
		Vec3 right = new Vec3(-direction.z, 0, direction.x).normalize();
		Vec3 up = new Vec3(0, 1, 0);
		double[] forwardProj = getAABBProjection(aabb, direction);
		if (forwardProj[1] < 0 || forwardProj[0] > length)
			return false;
		double[] rightProj = getAABBProjection(aabb, right);
		if (rightProj[1] < -width / 2 || rightProj[0] > width / 2)
			return false;
		double[] upProj = getAABBProjection(aabb, up);
		return !(upProj[1] < -height / 2) && !(upProj[0] > height / 2);
	}

	private static double[] getAABBProjection(AABB aabb, Vec3 axis) {
		double min = (axis.x > 0 ? aabb.minX : aabb.maxX) * axis.x + (axis.y > 0 ? aabb.minY : aabb.maxY) * axis.y + (axis.z > 0 ? aabb.minZ : aabb.maxZ) * axis.z;
		double max = (axis.x > 0 ? aabb.maxX : aabb.minX) * axis.x + (axis.y > 0 ? aabb.maxY : aabb.minY) * axis.y + (axis.z > 0 ? aabb.maxZ : aabb.minZ) * axis.z;
		return new double[]{min, max};
	}
}