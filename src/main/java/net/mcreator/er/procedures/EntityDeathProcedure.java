package net.mcreator.er.procedures;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mcreator.er.init.ErModEnchantments;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.entity.EnergyOrb;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.item.data.weapon.KillAbility;
import net.wither.er.item.weapons.AbilityWeapon;
import net.wither.er.entity.outcrop.Blossom;

@Mod.EventBusSubscriber
public class EntityDeathProcedure {
    private static final ResourceLocation artifactLoot1 = new ResourceLocation("er:artifact/star1");
    private static final ResourceLocation artifactLoot2 = new ResourceLocation("er:artifact/star2");
    private static final ResourceLocation artifactLoot3 = new ResourceLocation("er:artifact/star3");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        Level level = entity.level();
        DamageSource source = event.getSource();
        Entity sourceentity = source.getEntity();

		ItemStack item_drop  = new ItemStack(ErModItems.MORA.get());
		item_drop.setCount((int) Math.min(entity.getMaxHealth() / 2, 20));
		if (level instanceof ServerLevel _level) {
			ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item_drop);
			entityToSpawn.setPickUpDelay(10);
			_level.addFreshEntity(entityToSpawn);
			Entity _entityToSpawn = ErModEntities.ENERGY_ORB.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
			if (_entityToSpawn instanceof EnergyOrb orb) {
				orb.setType(0, 6);
				orb.push(0.1 - Math.random() * 0.2, 0.3, 0.1 - Math.random() * 0.2);
			}
		}
		if (level instanceof ServerLevel _level && Math.random() < 0.01 * (1 + (sourceentity instanceof Player ? ((LivingEntity) sourceentity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.LUCK).getValue() : 0))) {
			if (Math.random() < 0.8) {
				dropArtifact(_level, entity.getOnPos(), artifactLoot1);
			} else if (Math.random() < 0.5) {
				dropArtifact(_level, entity.getOnPos(), artifactLoot2);
			} else {
				dropArtifact(_level, entity.getOnPos(), artifactLoot3);
			}
		}
        testOutCrop(entity);
		if (sourceentity == null)
			return;
		ItemStack hand_item = sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY;
		int GreedLevel = hand_item.getEnchantmentLevel(ErModEnchantments.GREED.get()) ;
		if (GreedLevel != 0 && Math.random() < GreedLevel * 0.18) {
			item_drop = new ItemStack(ErModItems.MORA.get());
			item_drop.setCount(GreedLevel);
			if (level instanceof ServerLevel _level) {
				ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, item_drop);
				entityToSpawn.setPickUpDelay(10);
				_level.addFreshEntity(entityToSpawn);
			}
		}
        if(sourceentity instanceof ErEntityInterface erEntityInterface){
            Object2IntMap<ArtifactEffect> map = erEntityInterface.er$getEffectMap();
            for(Object2IntMap.Entry<ArtifactEffect> effect : map.object2IntEntrySet()){
                if(effect.getKey() instanceof KillAbility ability){
                    ability.onKill(source, entity, effect.getIntValue());
                }
            }
        }
        if(hand_item.getItem() instanceof AbilityWeapon abilityWeapon && abilityWeapon.getAbility() instanceof KillAbility ability) {
            CompoundTag tag = hand_item.getOrCreateTag();
            int refinement = tag.contains("refinement") ? tag.getInt("refinement") : 1 ;
            ability.onKill(source, entity, refinement);
        }
	}

	private static void dropArtifact(ServerLevel level, BlockPos pos,  ResourceLocation tableKey){
		for (ItemStack itemStack : level.getServer().getLootData().getLootTable(tableKey)
				.getRandomItems(new LootParams.Builder(level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(pos))
						.withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(pos)).create(LootContextParamSets.EMPTY))) {
			ItemEntity entityToSpawn = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
			entityToSpawn.setPickUpDelay(10);
			level.addFreshEntity(entityToSpawn);
		}

	}

	public static void testOutCrop(Entity entity) {
		if (entity.level() instanceof ServerLevel serverLevel && entity.getPersistentData().contains("BlossomOwner")) {
			final Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
            Entity ent_found = serverLevel.getEntity(entity.getPersistentData().getUUID("BlossomOwner"));
			if(ent_found instanceof Blossom blossom) {
                blossom.DeclineMobLeft();
			}
		}
	}
}