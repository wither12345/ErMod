package net.wither.er.init;


import net.mcreator.er.ErMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber
public class ErAttributeRegister {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, ErMod.MODID);
    public static final DeferredHolder<Attribute, Attribute> ANEMO_RES = REGISTRY.register("anemo_res", () -> new RangedPercentAttribute("attribute.er.anemo_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> CRYO_RES = REGISTRY.register("cryo_res", () -> new RangedPercentAttribute("attribute.er.cryo_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> DENDRO_RES = REGISTRY.register("dendro_res", () -> new RangedPercentAttribute("attribute.er.dendro_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ELECTRO_RES = REGISTRY.register("electro_res", () -> new RangedPercentAttribute("attribute.er.electro_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> GEO_RES = REGISTRY.register("geo_res", () -> new RangedAttribute("attribute.er.geo_res", 0, -512, 100).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> HYDRO_RES = REGISTRY.register("hydro_res", () -> new RangedPercentAttribute("attribute.er.hydro_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> PYRO_RES = REGISTRY.register("pyro_res", () -> new RangedPercentAttribute("attribute.er.pyro_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> PHYSICAL_RES = REGISTRY.register("physical_res", () -> new RangedPercentAttribute("attribute.er.physical_res", 0, -512, 100, true).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> STAMINA_COST = REGISTRY.register("stamina_cost", () -> new RangedAttribute("attribute.er.stamina_cost", 1, 0, 16).setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE));


    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, STAMINA_COST);
    }

    public static class RangedPercentAttribute extends RangedAttribute {
        private final boolean scaled;
        public RangedPercentAttribute(String name, double defaultValue, double min, double max, boolean scaled) {
            super(name, defaultValue, min, max);
            this.scaled = scaled;
        }

        @Override
        public @NotNull MutableComponent toValueComponent(AttributeModifier.@Nullable Operation op, double value, @NotNull TooltipFlag flag) {
            return Component.translatable("neoforge.value.percent", FORMAT.format(value * (scaled ? 1f : 100F)));
        }
    }
}
