package net.mcreator.er.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;

import net.minecraft.world.item.component.CustomData;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;

import net.mcreator.er.ERConfig;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import net.wither.er.item.data.artifactdata.ArtifactLevel;
import net.wither.er.item.data.artifactdata.MainAffix;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Artifact extends Item {
	public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
		protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack itemStack) {
			return Artifact.dispenseArtifact(source, itemStack) ? itemStack : super.execute(source, itemStack);
		}
	};


	public Artifact(ArtifactSlot slot, Holder<ArtifactEffect> effect) {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).component(DataComponentsRegister.ARTIFACT,new ArtifactData(slot,new MainAffix(Attributes.MAX_HEALTH,0,false), new ArrayList<>(),effect ,new ArtifactLevel(0,0,0),1)));
		DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
	}

	public static boolean dispenseArtifact(BlockSource source, ItemStack itemStack) {
		BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
		List<LivingEntity> list = source.level().getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE));
		if (list.isEmpty())
			return false;
		LivingEntity livingentity = list.getFirst();
		ArtifactData data = itemStack.getComponents().get(DataComponentsRegister.ARTIFACT.get());
		if (data == null)
			return false;
		ArtifactSlot slot = data.slot() ;
		ItemStack itemStack1 = itemStack.split(1);
		if (livingentity instanceof ErEntityInterface entityInterface) {
			if(entityInterface.er$getArtifact(slot) == ItemStack.EMPTY){
				entityInterface.setArtifact(slot, itemStack1);
				entityInterface.updateArtifact();
				return true ;
			}
		}
		return false;
	}

	public static String getUuid(ItemStack itemstack, int sort) {
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("er:flower_of_life")))) {
			if (sort == 4)
				return "er.flower.main";
			else
				return "er.flower.sort." + new java.text.DecimalFormat("#").format(sort);
		}
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("er:plume_of_death")))) {
			if (sort == 4)
				return "er.plume.main";
			else
				return "er.plume.sort." + new java.text.DecimalFormat("#").format(sort);
		}
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("er:sands_of_eon")))) {
			if (sort == 4)
				return "er.sands.main";
			else
				return "er.sands.sort." + new java.text.DecimalFormat("#").format(sort);
		}
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("er:goblet_of_eonothem")))) {
			if (sort == 4)
				return "er.goblet.main";
			else
				return "er.goblet.sort." + new java.text.DecimalFormat("#").format(sort);
		}
		if (itemstack.is(ItemTags.create(ResourceLocation.parse("er:circlet_of_logos.")))) {
			if (sort == 4)
				return "er.circlet.main";
			else
				return "er.circlet.sort." + new java.text.DecimalFormat("#").format(sort);
		}
		return "er.error.nothing";
	}

	public static double final_attribute(ItemStack itemstack, double sort) {
		String attr_name = "";
		boolean mult = false;
		double original_count = 0;
		double rarity = 0;
		if (itemstack.getItem() instanceof Artifact) {
			if (sort == 0) {
				attr_name = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("main_attribute_name");
				original_count = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("main_attribute_count");
				rarity = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("rarity");
				mult = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean("main_attribute_multiplied");
				original_count *= itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("level") * 0.285 + 1;
			} else {
				attr_name = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString(("minor_attribute_name_" + new java.text.DecimalFormat("#").format(sort - 1)));
				original_count = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble(("minor_attribute_count_" + new java.text.DecimalFormat("#").format(sort - 1)));
				rarity = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble("rarity");
				mult = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getBoolean(("minor_attribute_multiplied_" + new java.text.DecimalFormat("#").format(sort - 1)));
				original_count *= itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getDouble(("minor_count_multiplier_" + new java.text.DecimalFormat("#").format(sort - 1)));
			}
			if (!mult && (attr_name).equals("generic.attack_damage")) {
				if (rarity >= 5) {
					return ERConfig.MYTHIC_DMG_SCALING.get() * original_count;
				} else if (rarity >= 4) {
					return ERConfig.LEGENDARY_DMG_SCALING.get() * original_count;
				} else if (rarity >= 3) {
					return ERConfig.EPIC_DMG_SCALING.get() * original_count;
				} else if (rarity >= 2) {
					return ERConfig.RARE_DMG_SCALING.get() * original_count;
				} else if (rarity >= 1) {
					return ERConfig.UNCOMMON_DMG_SCALING.get() * original_count;
				} else {
					return ERConfig.COMMON_DMG_SCALING.get() * original_count;
				}
			} else if (!mult && (attr_name).equals("generic.max_health")) {
				if (rarity >= 5) {
					return ERConfig.MYTHIC_HP_SCALING.get() * original_count;
				} else if (rarity >= 4) {
					return ERConfig.LEGENDARY_HP_SCALING.get() * original_count;
				} else if (rarity >= 3) {
					return ERConfig.EPIC_HP_SCALING.get() * original_count;
				} else if (rarity >= 2) {
					return ERConfig.RARE_HP_SCALING.get() * original_count;
				} else if (rarity >= 1) {
					return ERConfig.UNCOMMON_HP_SCALING.get() * original_count;
				} else {
					return ERConfig.COMMON_HP_SCALING.get() * original_count;
				}
			} else {
				if (rarity >= 5) {
					return ERConfig.MYTHIC_OTHER_SCALING.get() * original_count;
				} else if (rarity >= 4) {
					return ERConfig.LEGENDARY_OTHER_SCALING.get() * original_count;
				} else if (rarity >= 3) {
					return ERConfig.EPIC_OTHER_SCALING.get() * original_count;
				} else if (rarity >= 2) {
					return ERConfig.RARE_OTHER_SCALING.get() * original_count;
				} else if (rarity >= 1) {
					return ERConfig.UNCOMMON_OTHER_SCALING.get() * original_count;
				} else {
					return ERConfig.COMMON_OTHER_SCALING.get() * original_count;
				}
			}
		}
		return 0;
	}

	public static double max_level(ItemStack itemstack) {
		int rarity = itemstack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("rarity");
		if (rarity <= 1)
			return 4;
		if (rarity <= 2)
			return 12;
		if (rarity <= 3)
			return 16;
		return 20;
	}

	public static double getScaling(int level, int rarity, int attr){
		if(attr == 1){
			return (level * 0.285 + 1) * switch (rarity){
				case 1 -> ERConfig.COMMON_DMG_SCALING.get();
				case 2 -> ERConfig.UNCOMMON_DMG_SCALING.get();
				case 3 -> ERConfig.RARE_DMG_SCALING.get();
				case 4 -> ERConfig.EPIC_DMG_SCALING.get() ;
				case 5 -> ERConfig.LEGENDARY_DMG_SCALING.get() ;
				default -> linear(ERConfig.LEGENDARY_DMG_SCALING.get(), ERConfig.MYTHIC_DMG_SCALING.get(), rarity);
			} ;
		}
		if(attr == 2){
			return (level * 0.285 + 1) * switch (rarity){
				case 1 -> ERConfig.COMMON_HP_SCALING.get();
				case 2 -> ERConfig.UNCOMMON_HP_SCALING.get();
				case 3 -> ERConfig.RARE_HP_SCALING.get();
				case 4 -> ERConfig.EPIC_HP_SCALING.get() ;
				case 5 -> ERConfig.LEGENDARY_HP_SCALING.get() ;
				default -> linear(ERConfig.LEGENDARY_HP_SCALING.get(), ERConfig.MYTHIC_HP_SCALING.get(), rarity);
			} ;
		}
		return (level * 0.285 + 1) * switch (rarity){
			case 1 -> ERConfig.COMMON_OTHER_SCALING.get();
			case 2 -> ERConfig.UNCOMMON_OTHER_SCALING.get();
			case 3 -> ERConfig.RARE_OTHER_SCALING.get();
			case 4 -> ERConfig.EPIC_OTHER_SCALING.get() ;
			case 5 -> ERConfig.LEGENDARY_OTHER_SCALING.get() ;
			default -> linear(ERConfig.LEGENDARY_OTHER_SCALING.get(), ERConfig.MYTHIC_OTHER_SCALING.get(), rarity);
		} ;
	}

	private static double linear(double a, double b, int rarity){
		return a + (b - a) * (rarity - 5) ;
	}
}