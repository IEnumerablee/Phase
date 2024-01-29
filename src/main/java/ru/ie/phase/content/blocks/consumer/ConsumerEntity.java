package ru.ie.phase.content.blocks.consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.content.blocks.generic.AbstractConsumer;
import ru.ie.phase.foundation.net.electrical.VoltageLevel;

public class ConsumerEntity extends AbstractConsumer {
    public ConsumerEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        intStates(VoltageLevel.MV, 5);
    }
}
