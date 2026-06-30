package net.mcreator.er.item;

import net.mcreator.er.ERConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import net.wither.er.artifact_effect.ArtifactEffect;
import net.wither.er.entity.ArtifactSlot;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.init.DataComponentsRegister;
import net.wither.er.item.data.artifactdata.ArtifactData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Artifact extends Item {
	private final ArtifactSlot slot;
	private final Supplier<ArtifactEffect> effect;

	public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
		protected @NotNull ItemStack execute(@NotNull BlockSource source, @NotNull ItemStack itemStack) {
			return Artifact.dispenseArtifact(source, itemStack) ? itemStack : super.execute(source, itemStack);
		}
	};


	public Artifact(ArtifactSlot slot, Supplier<ArtifactEffect> effect) {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
		this.slot = slot;
		this.effect = effect;
		DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
	}

	public static boolean dispenseArtifact(BlockSource source, ItemStack itemStack) {
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		List<LivingEntity> list = source.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS.and(EntitySelector.LIVING_ENTITY_STILL_ALIVE));
		if (list.isEmpty())
			return false;
		LivingEntity livingentity = list.get(0);
		ArtifactData data = DataComponentsRegister.ARTIFACT.getData(itemStack);
		if (data == null)
			return false;
		ArtifactSlot slot = data.slot() ;
		ItemStack itemStack1 = itemStack.split(1);
		if (livingentity instanceof ErEntityInterface entityInterface) {
			if(entityInterface.getArtifact(slot) == ItemStack.EMPTY){
				entityInterface.setArtifact(slot, itemStack1);
				entityInterface.er$updateArtifact();
				return true ;
			}
		}
		return false;
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

    public ArtifactSlot getSlot() {
        return slot;
    }

    public Supplier<ArtifactEffect> getEffect() {
        return effect;
    }

	public static boolean isSlot(ItemStack stack, ArtifactSlot slot){
		Optional<ArtifactData> data = stack.getCapability(ArtifactData.ARTIFACT_DATA).resolve();
        return data.filter(artifactData -> artifactData.slot() == slot).isPresent();
    }
}