package net.wither.er.block.entity;

import net.mcreator.er.init.ErModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LinkMechanismBaseEntity extends BlockEntity {
    public double cosToPlayer = -1;
    public LinkMechanismBaseEntity(BlockPos pos, BlockState state) {
        super(ErModBlockEntities.LINK_MECHANISM_ENTITY.get(), pos, state);
    }
}
