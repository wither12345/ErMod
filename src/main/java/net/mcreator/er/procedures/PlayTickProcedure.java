package net.mcreator.er.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.er.network.ErModVariables;
import net.mcreator.er.init.ErModBlocks;

import javax.annotation.Nullable;

@EventBusSubscriber
public class PlayTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player && ((Level) world).getGameTime() % 20 == 1) {
			ErModVariables.PlayerVariables _vars = entity.getData(ErModVariables.PLAYER_VARIABLES);
			_vars.statue_health = Math.min(_vars.statue_health + 10, 500);
			if (entity instanceof Player living && (living.getHealth() < living.getMaxHealth() || living.getFoodData().getFoodLevel() < 20)) {
				for (int i = -3; i <= 3; i++)
					for (int j = -2; j <= 2; j++)
						for (int k = -3; k <= 3; k++)
							if ((world.getBlockState(BlockPos.containing(x + i, y + j, z + k))).getBlock() == ErModBlocks.STATUEOF_THE_SEVEN_CORE.get()) {
								float heal_amount = (float) Math.min(living.getMaxHealth() - living.getHealth(), _vars.statue_health);
								living.heal(heal_amount);
								_vars.statue_health -= heal_amount;
								_vars.markSyncDirty();
								living.getFoodData().setFoodLevel(20);
								living.getFoodData().setSaturation(20);
								break;
							}
			}
		}
	}
}