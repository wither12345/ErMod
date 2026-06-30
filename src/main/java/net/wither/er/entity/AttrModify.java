package net.wither.er.entity;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber()
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

	private static void modifyAttributeRange(Attribute attribute, double min, double max) {
		if (attribute instanceof RangedAttribute rangedAttr) {
			try {
				Field minValueField = RangedAttribute.class.getDeclaredField("minValue");
				minValueField.setAccessible(true);
				minValueField.setDouble(rangedAttr, min);
				Field maxValueField = RangedAttribute.class.getDeclaredField("maxValue");
				maxValueField.setAccessible(true);
				maxValueField.setDouble(rangedAttr, max);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}