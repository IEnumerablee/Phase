package ru.ie.phase.content.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.foundation.net.NetIndexed;

public abstract class AbstractNodeBlock extends Block {

    public AbstractNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean p_60519_)
    {
        if(!level.isClientSide()) {
            NetIndexed me = (NetIndexed) level.getBlockEntity(pos);
            remove(me);
        }
        super.onRemove(state, level, pos, state2, p_60519_);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos thisPpos, Block block, BlockPos neighborPos, boolean p_60514_)
    {
        super.neighborChanged(state, level, thisPpos, block, neighborPos, p_60514_);
        if(!level.isClientSide()) {
            NetIndexed me = (NetIndexed) level.getBlockEntity(thisPpos);
            NetIndexed neighbor = (NetIndexed) level.getBlockEntity(neighborPos);

            connect(neighbor, me);
        }
        super.neighborChanged(state, level, thisPpos, block, neighborPos, p_60514_);
    }

    protected abstract void connect(NetIndexed neighbor, NetIndexed me);

    protected abstract void remove(NetIndexed me);
}
