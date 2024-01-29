package ru.ie.phase.content.blocks.generator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.content.blocks.generic.AbstractGenerator;
import ru.ie.phase.foundation.net.electrical.VoltageLevel;

public class GeneratorEntity extends AbstractGenerator {

    public GeneratorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        intStates(VoltageLevel.MV, 10);
    }



}
