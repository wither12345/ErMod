package net.wither.er.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.wither.er.world.inventory.FortunaEnhanceMenu;
import org.jetbrains.annotations.NotNull;

public class FortunaEnhancer extends Block {
    public FortunaEnhancer() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(1.5F).sound(SoundType.WOOD));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos,
            @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.isClientSide)
            return InteractionResult.SUCCESS;
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(getMenuProvider());
        }
        return InteractionResult.SUCCESS;
    }

    protected MenuProvider getMenuProvider() {
        return new SimpleMenuProvider(
                (id, inventory, p) -> new FortunaEnhanceMenu(id, inventory),
                Component.literal("FortunaEnhanceMenu"));
    }
}