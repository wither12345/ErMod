package net.mcreator.er;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ERConfig {
	public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
	public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Integer> MAX_REFINEMENT;
	public static final ModConfigSpec.ConfigValue<Double> COMMON_DMG_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> UNCOMMON_DMG_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> RARE_DMG_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> EPIC_DMG_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> LEGENDARY_DMG_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> MYTHIC_DMG_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> COMMON_HP_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> UNCOMMON_HP_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> RARE_HP_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> EPIC_HP_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> LEGENDARY_HP_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> MYTHIC_HP_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> COMMON_OTHER_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> UNCOMMON_OTHER_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> RARE_OTHER_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> EPIC_OTHER_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> LEGENDARY_OTHER_SCALING;
	public static final ModConfigSpec.ConfigValue<Double> MYTHIC_OTHER_SCALING;
	public static final ModConfigSpec.ConfigValue<List<? extends String>> FLOWER_OF_LIFE_MAIN_ATTR;
	public static final ModConfigSpec.ConfigValue<List<? extends String>> PLUME_OF_DEATH_ATTR;
	public static final ModConfigSpec.ConfigValue<List<? extends String>> SANDS_OF_EON_ATTR;
	public static final ModConfigSpec.ConfigValue<List<? extends String>> GOBLET_OF_EONOTHEM_ATTR;
	public static final ModConfigSpec.ConfigValue<List<? extends String>> CIRCLET_OF_LOGOS_ATTR;
	public static final ModConfigSpec.ConfigValue<List<? extends String>> MINOR_ATTR;

	private static final List<String> flower_of_life_main = List.of("minecraft:generic.max_health,14,0") ;

	public static final ModConfigSpec.ConfigValue<Boolean> ARMOR_RULE_MODIFY;
	/*
	 * dmg : 1:7
	 * hp  : 2:9
	*/
	static {
        BUILDER.push("weapon");
        MAX_REFINEMENT = BUILDER.comment("Weapon's max refinement").define("max_refinement", 5);
        BUILDER.pop();
		BUILDER.push("artifacts");
		BUILDER.push("damage");
		COMMON_DMG_SCALING = BUILDER.comment("This control Artifact(1 Star)'s damage attributes scaling").define("scaling_dmg_1", 0.17);
		UNCOMMON_DMG_SCALING = BUILDER.comment("This control Artifact(2 Star)'s damage attributes scaling").define("scaling_dmg_2", 0.36);
		RARE_DMG_SCALING = BUILDER.comment("This control Artifact(3 Star)'s damage attributes scaling").define("scaling_dmg_3", 0.6);
		EPIC_DMG_SCALING = BUILDER.comment("This control Artifact(4 Star)'s damage attributes scaling").define("scaling_dmg_4", 0.87);
		LEGENDARY_DMG_SCALING = BUILDER.comment("This control Artifact(5 Star)'s damage attributes scaling").define("scaling_dmg_5", 1.0);
		MYTHIC_DMG_SCALING = BUILDER.comment("This control Artifact(6 Star)'s damage attributes scaling").define("scaling_dmg_6", 1.2);
		BUILDER.pop();
		BUILDER.push("max_health");
		COMMON_HP_SCALING = BUILDER.comment("This control Artifact(1 Star)'s max health attributes scaling").define("scaling_hp_1", 0.17);
		UNCOMMON_HP_SCALING = BUILDER.comment("This control Artifact(2 Star)'s max health attributes scaling").define("scaling_hp_2", 0.34);
		RARE_HP_SCALING = BUILDER.comment("This control Artifact(3 Star)'s max health attributes scaling").define("scaling_hp_3", 0.57);
		EPIC_HP_SCALING = BUILDER.comment("This control Artifact(4 Star)'s max health attributes scaling").define("scaling_hp_4", 0.86);
		LEGENDARY_HP_SCALING = BUILDER.comment("This control Artifact(5 Star)'s max health attributes scaling").define("scaling_hp_5", 1.0);
		MYTHIC_HP_SCALING = BUILDER.comment("This control Artifact(6 Star)'s max health attributes scaling").define("scaling_hp_6", 1.2);
		BUILDER.pop();
		BUILDER.push("others");
		COMMON_OTHER_SCALING = BUILDER.comment("This control Artifact(1 Star)'s other attributes scaling").define("scaling_other_1", 0.45);
		UNCOMMON_OTHER_SCALING = BUILDER.comment("This control Artifact(2 Star)'s other attributes scaling").define("scaling_other_2", 0.61);
		RARE_OTHER_SCALING = BUILDER.comment("This control Artifact(3 Star)'s other attributes scaling").define("scaling_other_3", 0.75);
		EPIC_OTHER_SCALING = BUILDER.comment("This control Artifact(4 Star)'s other attributes scaling").define("scaling_other_4", 0.9);
		LEGENDARY_OTHER_SCALING = BUILDER.comment("This control Artifact(5 Star)'s other attributes scaling").define("scaling_other_5", 1.0);
		MYTHIC_OTHER_SCALING = BUILDER.comment("This control Artifact(6 Star)'s other attributes scaling").define("scaling_other_6", 1.15);
		BUILDER.pop();
		BUILDER.push("attribute");
		FLOWER_OF_LIFE_MAIN_ATTR = BUILDER.comment("This control Flower of Life's main attribute").defineList("flower_main", flower_of_life_main, obj -> obj instanceof String);
		PLUME_OF_DEATH_ATTR = BUILDER.comment("This control Plume of Death's main attribute").defineList("plume_main", List.of("minecraft:generic.attack_damage,7,0"), ele -> ele instanceof String);
		SANDS_OF_EON_ATTR = BUILDER.comment("This control Sands of Eon's main attribute").defineList("sands_main",
				List.of(
					"minecraft:generic.max_health,0.07,1",
					"minecraft:generic.attack_damage,0.07,1",
					"minecraft:generic.armor,0.087,1",
					"er:elemental_mastery,28,0",
					"er:energy_recharge,0.078,1"
				),
				ele -> ele instanceof String);
		GOBLET_OF_EONOTHEM_ATTR = BUILDER.comment("This control Goblet of Eonothem's main attribute")
				.defineList("goblet_main", List.of(
					"minecraft:generic.max_health,0.07,1",
					"minecraft:generic.attack_damage,0.07,1",
					"minecraft:generic.armor,0.087,1",
					"er:elemental_mastery,28,0",
					"er:anemo_dmg_bonus,0.07,1",
					"er:cryo_dmg_bonus,0.07,1",
					"er:dendro_dmg_bonus,0.07,1",
					"er:electro_dmg_bonus,0.07,1",
					"er:geo_dmg_bonus,0.07,1",
					"er:hydro_dmg_bonus,0.07,1",
					"er:pyro_dmg_bonus,0.07,1",
					"er:physical_dmg_bonus,0.087,1"
				), ele -> ele instanceof String);
		CIRCLET_OF_LOGOS_ATTR = BUILDER.comment("This control Circlet of Logos' main attribute").defineList("circlet_main",
				List.of(
					"minecraft:generic.max_health,0.07,1",
					"minecraft:generic.attack_damage,0.07,1",
					"minecraft:generic.armor,0.087,1",
					"er:elemental_mastery,28,0",
					"er:crit_damage,0.093,0",
					"er:crit_rate,0.047,0"
				), ele -> ele instanceof String);
		MINOR_ATTR = BUILDER.comment("This control Artifacts' minor attribute").defineList("mino_attrr",
				List.of(
					"minecraft:generic.max_health,6,0",
					"minecraft:generic.max_health,0.0583,1",
					"minecraft:generic.attack_damage,3,0",
					"minecraft:generic.attack_damage,0.0583,1",
					"minecraft:generic.armor,23.15,0",
					"minecraft:generic.armor,0.0729,1",
					"er:elemental_mastery,23.31,0",
					"er:energy_recharge,0.0648,1",
					"er:crit_damage,0.0777,0",
					"er:crit_rate,0.0389,0"
				), ele -> ele instanceof String);
		
		/*
		FLOWER_OF_LIFE_MAIN_ATTR = BUILDER.comment("This control Flower of Life's main attribute").define("flower_of_life_main", "minecraft:generic.max_health,14,0");
		PLUME_OF_DEATH_ATTR = BUILDER.comment("This control Plume of Death's main attribute").define("plume_of_death_main", "minecraft:generic.attack_damage,3,0");
		SANDS_OF_EON_ATTR = BUILDER.comment("This control Sands of Eon's main attribute").define("sands_of_eon_main",
				"minecraft:generic.max_health,0.07,1;minecraft:generic.attack_damage,0.07,1;minecraft:generic.armor,0.087,1;er:elemental_mastery,28,0;er:energy_recharge,7.8,0");
		GOBLET_OF_EONOTHEM_ATTR = BUILDER.comment("This control Goblet of Eonothem's main attribute").define("goblet_of_eonothem_main",
				"minecraft:generic.max_health,0.07,1; minecraft:generic.attack_damage,0.07,1; minecraft:generic.armor,0.087,1; er:elemental_mastery,28,0; er:elemental_mastery,28,0; er:anemo_dmg_bonus,0.07,1; er:cryo_dmg_bonus,0.07,1; er:dendro_dmg_bonus,0.07,1; er:electro_dmg_bonus,0.07,1; er:geo_dmg_bonus,0.07,1; er:hydro_dmg_bonus,0.07,1; er:pyro_dmg_bonus,0.07,1; er:physical_dmg_bonus,8.7,1");
		CIRCLET_OF_LOGOS_ATTR = BUILDER.comment("This control Circlet of Logos' main attribute").define("circlet_of_logos_main",
				"minecraft:generic.max_health,0.07,1; minecraft:generic.attack_damage,0.07,1; minecraft:generic.armor,0.087,1;er:elemental_mastery,28,0; er:elemental_mastery,28,0; er:crit_damage,0.093,1;");
				*/
		BUILDER.pop();
		BUILDER.pop();
		BUILDER.push("combat");
		ARMOR_RULE_MODIFY = BUILDER.comment("This will change armor calculation rules").define("armor rule modify", true);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}