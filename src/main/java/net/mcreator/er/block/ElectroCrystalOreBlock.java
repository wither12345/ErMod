package net.mcreator.er.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class ElectroCrystalOreBlock extends Block {
	public ElectroCrystalOreBlock() {
		super(BlockBehaviour.Properties.of().strength(3f).lightLevel(blockstate -> 3).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM));
	}
}