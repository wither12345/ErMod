package net.wither.er.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.wither.er.entity.ErEntityInterface;
import net.wither.er.shield.ErShield;
import net.wither.er.shield.ShieldStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class WhopperflowerFruit extends Item {
    private final Supplier<ErShield> shield ;
    public WhopperflowerFruit(Supplier<ErShield> shield) {
        super(new Properties());
        this.shield = shield;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemStack) {
        return 60;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemStack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof Player player)
            player.getCooldowns().addCooldown(itemStack.getItem(), 10);

        return super.finishUsingItem(itemStack, level, entity);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack itemStack, int t) {
        if (level instanceof ServerLevel &&
                entity instanceof Player player &&
                entity instanceof ErEntityInterface erEntityInterface
                && !erEntityInterface.er$getShields().contains(shield.get())
        ) {
            player.getCooldowns().addCooldown(itemStack.getItem(), 40);
            player.stopUsingItem();
        }
    }

    @Override
    public void onStopUsing(@NotNull ItemStack stack, @NotNull LivingEntity entity, int t) {
        super.onStopUsing(stack, entity, t);
        if(entity.level() instanceof ServerLevel && t > 0){
            ErEntityInterface erEntityInterface = (ErEntityInterface) entity;
            erEntityInterface.er$removeShield(shield.get());
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(level, player, hand);
        if(level instanceof ServerLevel) {
            ErEntityInterface erEntityInterface = (ErEntityInterface) player;
            erEntityInterface.er$addShield(new ShieldStack(shield.get(), 5, 60));
        }
        player.startUsingItem(hand);
        return ar;
    }
}
