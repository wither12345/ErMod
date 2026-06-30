package net.mcreator.er;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ERClientConfig {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
	public static final ModConfigSpec.EnumValue<HealthBarEnum> PLAYER_HEALTH;
	public static final ModConfigSpec.BooleanValue DAMAGE_DISPLAY;
	static {
		BUILDER.push("health_bar");
		PLAYER_HEALTH = BUILDER.comment("This will change health bar multi").defineEnum("change player health bar" , HealthBarEnum.Bar , HealthBarEnum.values());
		BUILDER.pop();
		BUILDER.push("damage");
		DAMAGE_DISPLAY = BUILDER.comment("Enable damage display").define("change player health bar" , true);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	public enum HealthBarEnum{
		Vanilla ,
		Bar;
	}
}