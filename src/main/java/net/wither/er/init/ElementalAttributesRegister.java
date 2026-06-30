package net.wither.er.init;

import net.mcreator.er.ErMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.wither.er.effect.ElementalDMGEffect;
import net.wither.er.effect.ElementalRESEffect;
import net.wither.er.elements.Element;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class ElementalAttributesRegister {
    public static final DeferredRegister<MobEffect> EFFECT_REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, ErMod.MODID);
    public static final DeferredRegister<Potion> POT_REGISTRY = DeferredRegister.create(Registries.POTION, ErMod.MODID);

    public static final Map<Element.Category, RegistryObject<MobEffect>> DMG_ATTR = new EnumMap<>(Element.Category.class);
    public static final Map<Element.Category, RegistryObject<MobEffect>> RES_ATTR = new EnumMap<>(Element.Category.class);

    public static final Map<Element.Category, RegistryObject<Potion>> DMG_POT = new EnumMap<>(Element.Category.class);
    public static final Map<Element.Category, RegistryObject<Potion>> RES_POT = new EnumMap<>(Element.Category.class);

    static {
        for (Element.Category category : Element.Category.values()) {
            registerDamageEffect(category);
            registerResistanceEffect(category);
        }
    }

    private static void registerDamageEffect(Element.Category category) {
        RegistryObject<MobEffect> effectHolder = EFFECT_REGISTRY.register(
                category.toString().toLowerCase(Locale.ROOT) + "_dmg_bonus",
                () -> new ElementalDMGEffect(category)
        );
        DMG_ATTR.put(category, effectHolder);
        final RegistryObject<Potion> potHolder = POT_REGISTRY.register(category.toString().toLowerCase(Locale.ROOT) + "_dmg_bonus",
                () -> new Potion(new MobEffectInstance(effectHolder.get(), 2400, 0, false, true)));
        DMG_POT.put(category, potHolder) ;
    }

    private static void registerResistanceEffect(Element.Category category) {
        RegistryObject<MobEffect> effectHolder = EFFECT_REGISTRY.register(
                category.toString().toLowerCase(Locale.ROOT) + "_res",
                () -> new ElementalRESEffect(category)
        );
        RES_ATTR.put(category, effectHolder);
        final RegistryObject<Potion> potHolder = POT_REGISTRY.register(category.toString().toLowerCase(Locale.ROOT) + "_res",
                () -> new Potion(new MobEffectInstance(effectHolder.get(), 2400, 0, false, true)));
        RES_POT.put(category, potHolder) ;
    }


}
