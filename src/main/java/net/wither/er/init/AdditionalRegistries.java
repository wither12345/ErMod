package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.wither.er.item.artifact_effect.ArtifactEffect;
import net.wither.er.elements.Element;
import net.wither.er.outcrop.EntityModifier;
import net.wither.er.shield.ShieldRegistry;

import java.util.function.Supplier;

import static net.mcreator.er.ErMod.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdditionalRegistries {
    public static final ResourceKey<Registry<EntityModifier>> ENTITY_MODIFIER = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "entity_modifier"));
    public static final DeferredRegister<EntityModifier> MODIFIERS = DeferredRegister.create(ENTITY_MODIFIER, ErMod.MODID);

    public static final ResourceKey<Registry<ArtifactEffect>> ARTIFACT_EFFECT = ResourceKey.createRegistryKey(new ResourceLocation(MODID, "artifact_effect"));
    public static final DeferredRegister<ArtifactEffect> ARTIFACT_EFFECTS = DeferredRegister.create(ARTIFACT_EFFECT, ErMod.MODID);


    public static IForgeRegistry<EntityModifier> ENTITY_MODIFIERS_REGISTRY;
    public static IForgeRegistry<ArtifactEffect> ARTIFACT_REGISTRY ;
    public static IForgeRegistry<Element> ELEMENT_REGISTRY ;

    public static Supplier<IForgeRegistry<EntityModifier>> ENTITY_MODIFIERS_SUPP;
    public static Supplier<IForgeRegistry<ArtifactEffect>> ARTIFACT_SUPP ;
    static {
        ENTITY_MODIFIERS_SUPP = MODIFIERS.makeRegistry(
                () -> new RegistryBuilder<EntityModifier>()
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
