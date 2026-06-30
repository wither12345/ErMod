package net.mcreator.er.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class WhiteIronOreBlock extends Block {
	public WhiteIronOreBlock() {
		super(BlockBehaviour.Properties.of().strength(5f, 3f).lightLevel(blockstate -> 3).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM));
	}
}