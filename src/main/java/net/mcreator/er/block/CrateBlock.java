package net.mcreator.er.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;

public class CrateBlock extends Block {
	public CrateBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.1f, 0f).ignitedByLava().instrument(NoteBlockInstrument.BASS));
	}
}