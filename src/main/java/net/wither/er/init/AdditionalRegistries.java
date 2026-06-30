package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.elements.Element;
import net.wither.er.item.data.weapon.WeaponAbility;
import net.wither.er.outcrop.EntityModifier;
import net.wither.er.shield.ErShield;

@EventBusSubscriber()
public class AdditionalRegistries {
    public static final ResourceKey<Registry<ErShield>> SHIELD = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "shields"));
    public static final ResourceKey<Registry<Element>> ELEMENT = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "elements"));
    public static final ResourceKey<Registry<ArtifactEffect>> ARTIFACT_EFFECT = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "artifact_effect"));
    public static final ResourceKey<Registry<EntityModifier>> ENTITY_MODIFIER = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "entity_modifier"));
    public static final ResourceKey<Registry<WeaponAbility>> WEAPON_ABILITY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ErMod.MODID, "weapon_ability"));
    public static final Registry<ErShield> SHIELD_REGISTRY = new RegistryBuilder<>(SHIELD).sync(true).create();
    public static final Registry<Element> ELEMENT_REGISTRY = new RegistryBuilder<>(ELEMENT).sync(true).create();
    public static final Registry<ArtifactEffect> ARTIFACT_REGISTRY = new RegistryBuilder<>(ARTIFACT_EFFECT).sync(true).defaultKey(ResourceLocation.parse("er:empty")).create();
    public static final Registry<EntityModifier> ENTITY_MODIFIER_REGISTRY = new RegistryBuilder<>(ENTITY_MODIFIER).sync(false).create();
    public static final Registry<WeaponAbility> WEAPON_ABILITY_REGISTRY = new RegistryBuilder<>(WEAPON_ABILITY).sync(true).create();

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(AdditionalRegistries.SHIELD_REGISTRY);
        event.register(AdditionalRegistries.ELEMENT_REGISTRY);
        event.register(AdditionalRegistries.ARTIFACT_REGISTRY);
        event.register(AdditionalRegistries.ENTITY_MODIFIER_REGISTRY);
        event.register(AdditionalRegistries.WEAPON_ABILITY_REGISTRY);
    }
}
