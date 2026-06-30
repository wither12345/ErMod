package net.mcreator.er.procedures;

import net.mcreator.er.EntityHurtEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.wither.er.elements.AuraContainerInterface;
import net.wither.er.elements.Element;
import net.wither.er.init.ElementRegistry;
import net.wither.er.elements.ElementSource;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.shield.ShieldStack;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.core.registries.Registries.ENTITY_TYPE;

@Mod.EventBusSubscriber
public class EntityTickProcedure {
	public static final TagKey<EntityType<?>> pyro = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:permanent_pyro"));
	public static final TagKey<EntityType<?>> electro = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:permanent_electro"));
	public static final TagKey<EntityType<?>> hydro = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:permanent_hydro"));
	public static final TagKey<EntityType<?>> cryo = TagKey.create(ENTITY_TYPE, new ResourceLocation("er:permanent_cryo"));
	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingTickEvent event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity.level() instanceof ServerLevel && entity instanceof ErEntityInterface enti) {
			List<ShieldStack> shields = enti.getShieldStacks();
			boolean changed = false;
			Iterator<ShieldStack> iterator = shields.iterator();
			while (iterator.hasNext()) {
				ShieldStack shield = iterator.next();
				if (!shield.getShield().tick(shield, entity)) {
					shield.getShield().end(entity);
					changed = true;
					iterator.remove();
				}
			}
			if (changed)
				enti.syncShield();
		}
		if(!world.isClientSide() && entity instanceof AuraContainerInterface auraContainerInterface){
			auraContainerInterface.getAuraContainer().tick(world,x,y,z,EntityHurtEvent.getEntityLevel(entity));
			if (entity.getType().is(hydro) && auraContainerInterface.getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty()) {
				auraContainerInterface.getAuraContainer().addAura(new ElementSource(ElementRegistry.HYDRO.get(), null, 2, true, true));
			}
			else if(entity.getType().is(pyro) && auraContainerInterface.getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty()){
				auraContainerInterface.getAuraContainer().addAura(new ElementSource(ElementRegistry.PYRO.get(), null, 2, true, true));
			}
			else if(entity.getType().is(electro) && auraContainerInterface.getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty()){
				auraContainerInterface.getAuraContainer().addAura(new ElementSource(ElementRegistry.ELECTRO.get(), null, 2, true, true));
			}
			else if(entity.getType().is(cryo) && auraContainerInterface.getAuraContainer().getAura().get(Element.Category.HYDRO.getId()).isEmpty()){
				auraContainerInterface.getAuraContainer().addAura(new ElementSource(ElementRegistry.CRYO.get(), null, 2, true, true));
			}
			else if(entity instanceof LivingEntity && entity.isInWaterRainOrBubble())
				auraContainerInterface.getAuraContainer().addAura(new ElementSource(ElementRegistry.HYDRO.get(), null, 2, true, true));
		}
		if (entity.getPersistentData().getInt("Electro_Charged_Cd") > 0) {
			entity.getPersistentData().putInt("Electro_Charged_Cd", entity.getPersistentData().getInt("Electro_Charged_Cd") - 1);
		}
	}
}