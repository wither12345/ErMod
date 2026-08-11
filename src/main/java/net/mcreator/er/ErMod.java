package net.mcreator.er;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.wither.er.outcrop.EntityModifierRegistry;
import net.wither.er.network.ErItemVariables;
import net.wither.er.network.ErCombatVariables;
import net.wither.er.loottables.RegisterLootModifier;
import net.wither.er.loottables.RegisterLootFunction;
import net.wither.er.item.artifact_effect.ArtifactEffectRegistry;
import net.wither.er.init.*;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

import net.minecraft.server.TickTask;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import net.mcreator.er.network.ErModVariables;
import net.mcreator.er.init.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

import it.unimi.dsi.fastutil.ints.IntObjectPair;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;

@Mod("er")
public class ErMod {
	public static final Logger LOGGER = LogManager.getLogger(ErMod.class);
	public static final String MODID = "er";

	public ErMod(IEventBus modEventBus) {
		// Start of user code block mod constructor
		// End of user code block mod constructor
		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(this::registerNetworking);
		ErModBlocks.REGISTRY.register(modEventBus);
		ErModBlockEntities.REGISTRY.register(modEventBus);
		ErModItems.REGISTRY.register(modEventBus);
		ErModEntities.REGISTRY.register(modEventBus);
		ErModTabs.REGISTRY.register(modEventBus);
		ErModVariables.ATTACHMENT_TYPES.register(modEventBus);
		ErModFeatures.REGISTRY.register(modEventBus);
		ErModPotions.REGISTRY.register(modEventBus);
		ErModMobEffects.REGISTRY.register(modEventBus);
		ErModParticleTypes.REGISTRY.register(modEventBus);
		ErModAttributes.REGISTRY.register(modEventBus);
		// Start of user code block mod init
		RegisterLootModifier.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
		RegisterLootFunction.LOOT_FUNCTION_TYPES.register(modEventBus);
		ShieldRegistry.SHIELDS.register(modEventBus);
		EntityModifierRegistry.MODIFIERS.register(modEventBus);
		ElementRegistry.ELEMENTS.register(modEventBus);
		ErCombatVariables.ATTACHMENT_TYPES.register(modEventBus);
		ErItemVariables.ATTACHMENT_TYPES.register(modEventBus);
		ElementalAttributesRegister.EFFECT_REGISTRY.register(modEventBus);
		ElementalAttributesRegister.POT_REGISTRY.register(modEventBus);
		DataComponentsRegister.REGISTRAR.register(modEventBus);
		ErMenus.REGISTRY.register(modEventBus);
		ArtifactEffectRegistry.ARTIFACT_EFFECTS.register(modEventBus);
		WeaponAbilityRegister.WEAPON_ABILITIES.register(modEventBus);
		EffectRegister.REGISTRY.register(modEventBus);
		ExtraTabs.REGISTRY.register(modEventBus);
		RecipeSerializerRegister.RECIPE_SERIALIZERS.register(modEventBus);
		ErAttributeRegister.REGISTRY.register(modEventBus);
		ArmorMaterialsRegister.REGISTRY.register(modEventBus);
		AdvancementTriggerRegister.TRIGGER_TYPES.register(modEventBus);
		// End of user code block mod init
	}

	// Start of user code block mod methods
	// End of user code block mod methods
	private static boolean networkingRegistered = false;
	private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();

	private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
	}

	public static <T extends CustomPacketPayload> void addNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
		if (networkingRegistered)
			throw new IllegalStateException("Cannot register new network messages after networking has been registered");
		MESSAGES.put(id, new NetworkMessage<>(reader, handler));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerNetworking(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MODID);
		MESSAGES.forEach((id, networkMessage) -> registrar.playBidirectional(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler()));
		networkingRegistered = true;
	}

	private static final Queue<IntObjectPair<Runnable>> workToBeScheduled = new ConcurrentLinkedQueue<>();
	private static final PriorityQueue<TickTask> workQueue = new PriorityQueue<>(Comparator.comparingInt(TickTask::getTick));

	public static void queueServerWork(int delay, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workToBeScheduled.add(new IntObjectImmutablePair<>(delay, action));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		int currentTick = event.getServer().getTickCount();
		IntObjectPair<Runnable> work;
		while ((work = workToBeScheduled.poll()) != null) {
			workQueue.add(new TickTask(currentTick + work.leftInt(), work.right()));
		}
		while (!workQueue.isEmpty() && currentTick >= workQueue.peek().getTick()) {
			workQueue.poll().run();
		}
	}
}