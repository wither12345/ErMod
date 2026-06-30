package net.mcreator.er;

import net.minecraftforge.common.ForgeConfigSpec;

public class ERClientConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.EnumValue<HealthBarEnum> PLAYER_HEALTH;
	public static final ForgeConfigSpec.BooleanValue DAMAGE_DISPLAY;
	static {
		BUILDER.push("health_bar");
		PLAYER_HEALTH = BUILDER.comment("This will change health bar multi").defineEnum("change player health bar" , HealthBarEnum.Bar , HealthBarEnum.values());
		BUILDER.pop();
		BUILDER.push("damage");
		DAMAGE_DISPLAY = BUILDER.comment("Enable damage display").define("display damage" , true);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	public enum HealthBarEnum{
		Vanilla ,
		Bar;
	}
}