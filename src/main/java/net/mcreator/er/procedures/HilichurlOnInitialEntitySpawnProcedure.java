package net.mcreator.er.procedures;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;

import net.mcreator.er.init.ErModItems;

public class HilichurlOnInitialEntitySpawnProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (Math.random() <= 0.33) {
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack0 = new ItemStack(Items.CROSSBOW).copy();
				_setstack0.setCount(1);
				_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack0);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
		} else if (Math.random() <= 0.5) {
			if (entity instanceof LivingEntity _entity) {
				ItemStack _setstack1 = new ItemStack(ErModItems.WOODEN_CLUB.get()).copy();
				_setstack1.setCount(1);
				_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack1);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
		}
	}
}