/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.wither.er.entity.slimes.*;
import net.wither.er.entity.*;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;

import net.mcreator.er.entity.*;
import net.mcreator.er.ErMod;

@EventBusSubscriber
public class ErModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, ErMod.MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<PyroCrystallizeEntity>> PYRO_CRYSTALLIZE = register("pyro_crystallize",
			EntityType.Builder.<PyroCrystallizeEntity>of(PyroCrystallizeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<CryoCrystallizeEntity>> CRYO_CRYSTALLIZE = register("cryo_crystallize",
			EntityType.Builder.<CryoCrystallizeEntity>of(CryoCrystallizeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<ElectroCrystallizeEntity>> ELECTRO_CRYSTALLIZE = register("electro_crystallize",
			EntityType.Builder.<ElectroCrystallizeEntity>of(ElectroCrystallizeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<HydroCrystallizeEntity>> HYDRO_CRYSTALLIZE = register("hydro_crystallize",
			EntityType.Builder.<HydroCrystallizeEntity>of(HydroCrystallizeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<MistFlowerEntity>> MIST_FLOWER = register("mist_flower",
			EntityType.Builder.<MistFlowerEntity>of(MistFlowerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.6f, 0.9f));
	public static final DeferredHolder<EntityType<?>, EntityType<FlamingFlowerEntity>> FLAMING_FLOWER = register("flaming_flower",
			EntityType.Builder.<FlamingFlowerEntity>of(FlamingFlowerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.6f, 0.9f));
	public static final DeferredHolder<EntityType<?>, EntityType<AnemoCrystalflyEntity>> ANEMO_CRYSTALFLY = register("anemo_crystalfly",
			EntityType.Builder.<AnemoCrystalflyEntity>of(AnemoCrystalflyEntity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.5f, 0.3f));
	public static final DeferredHolder<EntityType<?>, EntityType<TartagliaEntity>> TARTAGLIA = register("tartaglia",
			EntityType.Builder.<TartagliaEntity>of(TartagliaEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).ridingOffset(-0.6f).sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<HilichurlEntity>> HILICHURL = register("hilichurl",
			EntityType.Builder.<HilichurlEntity>of(HilichurlEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).ridingOffset(-0.6f).sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<ElectroCicinEntity>> ELECTRO_CICIN = register("electro_cicin",
			EntityType.Builder.<ElectroCicinEntity>of(ElectroCicinEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.5f, 0.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<ElementalProjectileEntity>> ELEMENTAL_PROJECTILE = register("elemental_projectile",
			EntityType.Builder.<ElementalProjectileEntity>of(ElementalProjectileEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final DeferredHolder<EntityType<?>, EntityType<FatuiElectroCicinMageEntity>> FATUI_ELECTRO_CICIN_MAGE = register("fatui_electro_cicin_mage",
			EntityType.Builder.<FatuiElectroCicinMageEntity>of(FatuiElectroCicinMageEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).ridingOffset(-0.6f).sized(0.6f, 1.8f));
	public static final DeferredHolder<EntityType<?>, EntityType<TravelerTornadoEntity>> TRAVELER_TORNADO = register("traveler_tornado",
			EntityType.Builder.<TravelerTornadoEntity>of(TravelerTornadoEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(1.5f, 2f));
	public static final DeferredHolder<EntityType<?>, EntityType<BlossomOfWealthEntity>> BLOSSOM_OF_WEALTH = register("blossom_of_wealth",
			EntityType.Builder.<BlossomOfWealthEntity>of(BlossomOfWealthEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(1.4f, 2f));
	public static final DeferredHolder<EntityType<?>, EntityType<TrounceBlossomEntity>> TROUNCE_BLOSSOM = register("trounce_blossom",
			EntityType.Builder.<TrounceBlossomEntity>of(TrounceBlossomEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(1.4f, 2f));
	public static final DeferredHolder<EntityType<?>, EntityType<BlossomOfRevelationEntity>> BLOSSOM_OF_REVELATION = register("blossom_of_revelation",
			EntityType.Builder.<BlossomOfRevelationEntity>of(BlossomOfRevelationEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).fireImmune().sized(1.4f, 2f));
	public static final DeferredHolder<EntityType<?>, EntityType<ButterflyEntity>> BUTTERFLY = register("butterfly",
			EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.5f, 0.3f));
	// Start of user code block custom entities
	public static final DeferredHolder<EntityType<?>, EntityType<BloomEntityEntity>> BLOOM_ENTITY = register("dendro_core",
			EntityType.Builder.of(BloomEntityEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.4f, 0.4f));
	public static final DeferredHolder<EntityType<?>, EntityType<ArcEntity>> ARC = register("arc",
			EntityType.Builder.of(ArcEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<Hyperbloom>> HYPERBLOOM = register("hyperbloom",
			EntityType.Builder.of(Hyperbloom::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<EnergyOrb>> ENERGY_ORB = register("energy_orb",
			EntityType.Builder.of(EnergyOrb::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));
	public static final DeferredHolder<EntityType<?>, EntityType<ElectroSlime>> ELECTRO_SLIME = register("electro_slime",
			EntityType.Builder.of(ElectroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<GeoSlime>> GEO_SLIME = register("geo_slime",
			EntityType.Builder.of(GeoSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<PyroSlime>> PYRO_SLIME = register("pyro_slime",
			EntityType.Builder.of(PyroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<HydroSlime>> HYDRO_SLIME = register("hydro_slime",
			EntityType.Builder.of(HydroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<CryoSlime>> CRYO_SLIME = register("cryo_slime",
			EntityType.Builder.of(CryoSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<DendroSlime>> DENDRO_SLIME = register("dendro_slime",
			EntityType.Builder.of(DendroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<AnemoSlime>> ANEMO_SLIME = register("anemo_slime",
			EntityType.Builder.of(AnemoSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final DeferredHolder<EntityType<?>, EntityType<LunarChargedCloud>> LUNAR_CLOUD = register("lunar_cloud",
			EntityType.Builder.of(LunarChargedCloud::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).sized(2, 1));
	public static final DeferredHolder<EntityType<?>, EntityType<LunarCrystallize>> LUNAR_CRYSTALLIZE = register("lunar_crystallize",
			EntityType.Builder.of(LunarCrystallize::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).sized(1, 1));
	public static final DeferredHolder<EntityType<?>, EntityType<LunarCrystallizeProjectile>> LUNAR_CRYSTALLIZE_PROJECTILE = register("lunar_crystallize_projectile",
			EntityType.Builder.of(LunarCrystallizeProjectile::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));

	// End of user code block custom entities
	private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(RegisterSpawnPlacementsEvent event) {
		PyroCrystallizeEntity.init(event);
		CryoCrystallizeEntity.init(event);
		ElectroCrystallizeEntity.init(event);
		HydroCrystallizeEntity.init(event);
		MistFlowerEntity.init(event);
		FlamingFlowerEntity.init(event);
		AnemoCrystalflyEntity.init(event);
		TartagliaEntity.init(event);
		HilichurlEntity.init(event);
		ElectroCicinEntity.init(event);
		FatuiElectroCicinMageEntity.init(event);
		TravelerTornadoEntity.init(event);
		BlossomOfWealthEntity.init(event);
		TrounceBlossomEntity.init(event);
		BlossomOfRevelationEntity.init(event);
		ButterflyEntity.init(event);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(PYRO_CRYSTALLIZE.get(), PyroCrystallizeEntity.createAttributes().build());
		event.put(CRYO_CRYSTALLIZE.get(), CryoCrystallizeEntity.createAttributes().build());
		event.put(ELECTRO_CRYSTALLIZE.get(), ElectroCrystallizeEntity.createAttributes().build());
		event.put(HYDRO_CRYSTALLIZE.get(), HydroCrystallizeEntity.createAttributes().build());
		event.put(MIST_FLOWER.get(), MistFlowerEntity.createAttributes().build());
		event.put(FLAMING_FLOWER.get(), FlamingFlowerEntity.createAttributes().build());
		event.put(ANEMO_CRYSTALFLY.get(), AnemoCrystalflyEntity.createAttributes().build());
		event.put(TARTAGLIA.get(), TartagliaEntity.createAttributes().build());
		event.put(HILICHURL.get(), HilichurlEntity.createAttributes().build());
		event.put(ELECTRO_CICIN.get(), ElectroCicinEntity.createAttributes().build());
		event.put(FATUI_ELECTRO_CICIN_MAGE.get(), FatuiElectroCicinMageEntity.createAttributes().build());
		event.put(TRAVELER_TORNADO.get(), TravelerTornadoEntity.createAttributes().build());
		event.put(BLOSSOM_OF_WEALTH.get(), BlossomOfWealthEntity.createAttributes().build());
		event.put(TROUNCE_BLOSSOM.get(), TrounceBlossomEntity.createAttributes().build());
		event.put(BLOSSOM_OF_REVELATION.get(), BlossomOfRevelationEntity.createAttributes().build());
		event.put(BUTTERFLY.get(), ButterflyEntity.createAttributes().build());
	}
}