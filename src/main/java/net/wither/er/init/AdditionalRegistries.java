package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.wither.er.elements.Element;
import net.wither.er.entity.outcrop.AttributeGiver;
import net.wither.er.entity.outcrop.EntityModifier;
import net.wither.er.entity.outcrop.ItemGiver;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.shield.ShieldRegistry;

import java.util.function.Supplier;

import static net.mcreator.er.ErMod.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdditionalRegistries {
    public static final ResourceKey<Registry<EntityModifier.Builder>> ENTITY_MODIFIER = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "entity_modifier"));
    public static final DeferredRegister<EntityModifier.Builder> MODIFIERS = DeferredRegister.create(ENTITY_MODIFIER, ErMod.MODID);
    public static final Supplier<EntityModifier.Builder> ATTRIBUTE = MODIFIERS.register("attribute", () -> AttributeGiver::read);
    public static final Supplier<EntityModifier.Builder> MAIN_HAND = MODIFIERS.register("main_hand", () -> (j -> ItemGiver.read(j, EquipmentSlot.MAINHAND)));
    public static final Supplier<EntityModifier.Builder> BOOTS = MODIFIERS.register("boots", () -> (j -> ItemGiver.read(j, EquipmentSlot.FEET)));
    public static final Supplier<EntityModifier.Builder> LEGGINGS = MODIFIERS.register("leggings", () -> (j -> ItemGiver.read(j, EquipmentSlot.LEGS)));
    public static final Supplier<EntityModifier.Builder> CHESTPLATE = MODIFIERS.register("chestplate", () -> (j -> ItemGiver.read(j, EquipmentSlot.CHEST)));
    public static final Supplier<EntityModifier.Builder> HELMET = MODIFIERS.register("helmet", () -> (j -> ItemGiver.read(j, EquipmentSlot.HEAD)));

    public static final ResourceKey<Registry<ArtifactEffect>> ARTIFACT_EFFECT = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "artifact_effect"));
    public static final DeferredRegister<ArtifactEffect> ARTIFACT_EFFECTS = DeferredRegister.create(ARTIFACT_EFFECT, ErMod.MODID);


    public static IForgeRegistry<EntityModifier.Builder> ENTITY_MODIFIERS_REGISTRY;
    public static IForgeRegistry<ArtifactEffect> ARTIFACT_REGISTRY ;
    public static IForgeRegistry<Element> ELEMENT_REGISTRY ;

    public static Supplier<IForgeRegistry<EntityModifier.Builder>> ENTITY_MODIFIERS_SUPP;
    public static Supplier<IForgeRegistry<ArtifactEffect>> ARTIFACT_SUPP ;
    static {
        ENTITY_MODIFIERS_SUPP = MODIFIERS.makeRegistry(
                () -> new RegistryBuilder<EntityModifier.Builder>()
                        .setName(ENTITY_MODIFIER.location())
        );
        ARTIFACT_SUPP = ARTIFACT_EFFECTS.makeRegistry(
                () -> new RegistryBuilder<ArtifactEffect>()
                        .setName(ARTIFACT_EFFECT.location())
        );
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ShieldRegistry.SHIELD_REGISTRY = ShieldRegistry.SHIELD_SUPP.get();
        ENTITY_MODIFIERS_REGISTRY = ENTITY_MODIFIERS_SUPP.get();
        ARTIFACT_REGISTRY = ARTIFACT_SUPP.get();
        ELEMENT_REGISTRY = ElementRegistry.ELEMENT_SUPP.get();
    }
}
