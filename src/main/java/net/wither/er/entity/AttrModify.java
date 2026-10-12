package net.wither.er.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.wither.er.mixins.RangedAttributeAccessor;

@EventBusSubscriber()
public class AttrModify {
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		if (!ModList.get().isLoaded("AttributeFix")) {
			event.enqueueWork(AttrModify::modifyAttributes);
		}
	}

	public static void modifyAttributes() {
		modifyAttributeRange(Attributes.MAX_HEALTH, 1.0, 2147483648.0);
		modifyAttributeRange(Attributes.ARMOR, 0.0, 32768.0);
		modifyAttributeRange(Attributes.ATTACK_DAMAGE, 0.0, 32768.0);
	}

	private static void modifyAttributeRange(Holder<Attribute> holder, double min, double max) {
        if (holder.value() instanceof RangedAttributeAccessor rangedAttr) {
            rangedAttr.er$setMinValue(min);
            rangedAttr.er$setMaxValue(max);
        }
	}
}