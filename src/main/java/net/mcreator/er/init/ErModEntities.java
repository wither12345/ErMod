/*
*    MCreator note: This file will be REGENERATED on each build.
*/
package net.mcreator.er.init;

import net.wither.er.entity.slimes.*;
import net.wither.er.entity.*;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.mcreator.er.entity.*;
import net.mcreator.er.ErMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ErModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ErMod.MODID);
	public static final RegistryObject<EntityType<PyroCrystallizeEntity>> PYRO_CRYSTALLIZE = register("pyro_crystallize", EntityType.Builder.<PyroCrystallizeEntity>of(PyroCrystallizeEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PyroCrystallizeEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<CryoCrystallizeEntity>> CRYO_CRYSTALLIZE = register("cryo_crystallize", EntityType.Builder.<CryoCrystallizeEntity>of(CryoCrystallizeEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CryoCrystallizeEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<ElectroCrystallizeEntity>> ELECTRO_CRYSTALLIZE = register("electro_crystallize", EntityType.Builder.<ElectroCrystallizeEntity>of(ElectroCrystallizeEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ElectroCrystallizeEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<HydroCrystallizeEntity>> HYDRO_CRYSTALLIZE = register("hydro_crystallize", EntityType.Builder.<HydroCrystallizeEntity>of(HydroCrystallizeEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(HydroCrystallizeEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<MistFlowerEntity>> MIST_FLOWER = register("mist_flower",
			EntityType.Builder.<MistFlowerEntity>of(MistFlowerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MistFlowerEntity::new).sized(0.6f, 0.9f));
	public static final RegistryObject<EntityType<FlamingFlowerEntity>> FLAMING_FLOWER = register("flaming_flower",
			EntityType.Builder.<FlamingFlowerEntity>of(FlamingFlowerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FlamingFlowerEntity::new).sized(0.6f, 0.9f));
	public static final RegistryObject<EntityType<AnemoCrystalflyEntity>> ANEMO_CRYSTALFLY = register("anemo_crystalfly", EntityType.Builder.<AnemoCrystalflyEntity>of(AnemoCrystalflyEntity::new, MobCategory.AMBIENT)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(AnemoCrystalflyEntity::new).sized(0.5f, 0.3f));
	public static final RegistryObject<EntityType<TartagliaEntity>> TARTAGLIA = register("tartaglia",
			EntityType.Builder.<TartagliaEntity>of(TartagliaEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(TartagliaEntity::new).sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<HilichurlEntity>> HILICHURL = register("hilichurl",
			EntityType.Builder.<HilichurlEntity>of(HilichurlEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(HilichurlEntity::new).sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<ElectroCicinEntity>> ELECTRO_CICIN = register("electro_cicin",
			EntityType.Builder.<ElectroCicinEntity>of(ElectroCicinEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ElectroCicinEntity::new).sized(0.5f, 0.8f));
	public static final RegistryObject<EntityType<ElementalProjectileEntity>> ELEMENTAL_PROJECTILE = register("elemental_projectile", EntityType.Builder.<ElementalProjectileEntity>of(ElementalProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(ElementalProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<FatuiElectroCicinMageEntity>> FATUI_ELECTRO_CICIN_MAGE = register("fatui_electro_cicin_mage", EntityType.Builder.<FatuiElectroCicinMageEntity>of(FatuiElectroCicinMageEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FatuiElectroCicinMageEntity::new).sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<TravelerTornadoEntity>> TRAVELER_TORNADO = register("traveler_tornado", EntityType.Builder.<TravelerTornadoEntity>of(TravelerTornadoEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(TravelerTornadoEntity::new).fireImmune().sized(1.5f, 2f));
	public static final RegistryObject<EntityType<BlossomOfWealthEntity>> BLOSSOM_OF_WEALTH = register("blossom_of_wealth", EntityType.Builder.<BlossomOfWealthEntity>of(BlossomOfWealthEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BlossomOfWealthEntity::new).fireImmune().sized(1.4f, 2f));
	public static final RegistryObject<EntityType<TrounceBlossomEntity>> TROUNCE_BLOSSOM = register("trounce_blossom", EntityType.Builder.<TrounceBlossomEntity>of(TrounceBlossomEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(TrounceBlossomEntity::new).sized(1.4f, 2f));
	public static final RegistryObject<EntityType<BlossomOfRevelationEntity>> BLOSSOM_OF_REVELATION = register("blossom_of_revelation", EntityType.Builder.<BlossomOfRevelationEntity>of(BlossomOfRevelationEntity::new, MobCategory.CREATURE)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BlossomOfRevelationEntity::new).fireImmune().sized(1.4f, 2f));
	public static final RegistryObject<EntityType<ButterflyEntity>> BUTTERFLY = register("butterfly",
			EntityType.Builder.<ButterflyEntity>of(ButterflyEntity::new, MobCategory.AMBIENT).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ButterflyEntity::new).sized(0.5f, 0.3f));
	// Start of user code block custom entities
	public static final RegistryObject<EntityType<BloomEntityEntity>> BLOOM_ENTITY = register("dendro_core",
			EntityType.Builder.of(BloomEntityEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.4f, 0.4f));
	public static final RegistryObject<EntityType<ArcEntity>> ARC = register("arc", EntityType.Builder.of(ArcEntity::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));
	public static final RegistryObject<EntityType<Hyperbloom>> HYPERBLOOM = register("hyperbloom",
			EntityType.Builder.of(Hyperbloom::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));
	public static final RegistryObject<EntityType<EnergyOrb>> ENERGY_ORB = register("energy_orb",
			EntityType.Builder.of(EnergyOrb::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));
	public static final RegistryObject<EntityType<ElectroSlime>> ELECTRO_SLIME = register("electro_slime",
			EntityType.Builder.of(ElectroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<GeoSlime>> GEO_SLIME = register("geo_slime",
			EntityType.Builder.of(GeoSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<PyroSlime>> PYRO_SLIME = register("pyro_slime",
			EntityType.Builder.of(PyroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<HydroSlime>> HYDRO_SLIME = register("hydro_slime",
			EntityType.Builder.of(HydroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<CryoSlime>> CRYO_SLIME = register("cryo_slime",
			EntityType.Builder.of(CryoSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<DendroSlime>> DENDRO_SLIME = register("dendro_slime",
			EntityType.Builder.of(DendroSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<AnemoSlime>> ANEMO_SLIME = register("anemo_slime",
			EntityType.Builder.of(AnemoSlime::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.8f, 0.6f));
	public static final RegistryObject<EntityType<LunarChargedCloud>> LUNAR_CLOUD = register("lunar_cloud",
			EntityType.Builder.of(LunarChargedCloud::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).sized(2, 1));
	public static final RegistryObject<EntityType<LunarCrystallize>> LUNAR_CRYSTALLIZE = register("lunar_crystallize",
			EntityType.Builder.of(LunarCrystallize::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).sized(1, 1));
	public static final RegistryObject<EntityType<LunarCrystallizeProjectile>> LUNAR_CRYSTALLIZE_PROJECTILE = register("lunar_crystallize_projectile",
			EntityType.Builder.of(LunarCrystallizeProjectile::new, MobCategory.MISC).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).sized(0.2f, 0.2f));

	// End of user code block custom entities
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PyroCrystallizeEntity.init();
			CryoCrystallizeEntity.init();
			ElectroCrystallizeEntity.init();
			HydroCrystallizeEntity.init();
			MistFlowerEntity.init();
			FlamingFlowerEntity.init();
			AnemoCrystalflyEntity.init();
			TartagliaEntity.init();
			HilichurlEntity.init();
			ElectroCicinEntity.init();
			FatuiElectroCicinMageEntity.init();
			TravelerTornadoEntity.init();
			BlossomOfWealthEntity.init();
			TrounceBlossomEntity.init();
			BlossomOfRevelationEntity.init();
			ButterflyEntity.init();
		});
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