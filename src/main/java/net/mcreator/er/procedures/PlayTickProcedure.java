package net.mcreator.er.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;

import net.mcreator.er.network.ErModVariables;
import net.mcreator.er.init.ErModBlocks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayTickProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
		}
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player && ((Level) world).getGameTime() % 20 == 1) {
			ErModVariables.PlayerVariables _vars = entity.getCapability(ErModVariables.PLAYER_VARIABLES).orElse(new ErModVariables.PlayerVariables());
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