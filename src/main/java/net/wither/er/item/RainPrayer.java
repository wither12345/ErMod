package net.wither.er.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.wither.er.entity.samachurl.RainCloud;
import org.jetbrains.annotations.NotNull;

public class RainPrayer extends Item {
    public RainPrayer() {
        super(new Properties().stacksTo(1).durability(32));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack, @NotNull LivingEntity entity) {
        return 200;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if(!level.isClientSide()) {
            Vec3 start = player.getEyePosition();
            Vec3 end = start.add(player.getViewVector(1f).scale(5));
            Vec3 pos = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getLocation();
            RainCloud cloud = new RainCloud(level, pos.add(0, 4, 0), player, 0.05f);
            level.addFreshEntity(cloud);
        }
        itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void onStopUsing(@NotNull ItemStack stack, @NotNull LivingEntity entity, int count) {
        if(entity instanceof Player player)
            player.getCooldowns().addCooldown(stack.getItem(), 400);
        super.onStopUsing(stack, entity, count);
    }
}
