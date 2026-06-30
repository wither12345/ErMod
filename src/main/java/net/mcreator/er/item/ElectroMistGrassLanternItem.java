
package net.mcreator.er.item;

import net.mcreator.er.entity.ElectroCicinEntity;
import net.mcreator.er.init.ErModEntities;
import net.mcreator.er.init.ErModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class ElectroMistGrassLanternItem extends Item {
	public ElectroMistGrassLanternItem() {
		super(new Item.Properties().durability(64).rarity(Rarity.COMMON));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		if (world instanceof ServerLevel level) {
			if (true) {
				entity.getCooldowns().addCooldown(ErModItems.ELECTRO_MIST_GRASS_LANTERN.get(), 200);
				ItemStack stack = ar.getObject();
				stack.hurtAndBreak(1, entity, null);
			}
			ElectroCicinEntity cicin = ErModEntities.ELECTRO_CICIN.get().spawn(level, BlockPos.containing(entity.getX() + Mth.nextDouble(RandomSource.create(), -1, 1), entity.getY() + 2, entity.getZ() + Mth.nextDouble(RandomSource.create(), -1, 1)),
					MobSpawnType.MOB_SUMMONED);
			cicin.setOwnerUUID(entity.getUUID());
		}
		return ar;
	}
}
