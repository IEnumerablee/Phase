package ru.ie.phase.content.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import ru.ie.phase.foundation.net.ConnectDirection;
import ru.ie.phase.foundation.net.NetIndexed;

public abstract class AbstractNetBlock extends Block {

    public AbstractNetBlock(Properties properties) {
        super(properties);
    }



    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState state2, boolean p_60519_)
    {
        if(!level.isClientSide() && state2.isAir()) {
            NetIndexed me = (NetIndexed) level.getBlockEntity(pos);
            remove(me);
        }
        super.onRemove(state, level, pos, state2, p_60519_);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos thisPos, @NotNull Block block, @NotNull BlockPos neighborPos, boolean p_60514_)
    {
        super.neighborChanged(state, level, thisPos, block, neighborPos, p_60514_);

        if(!level.isClientSide()) {
            BlockEntity me = level.getBlockEntity(thisPos);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);

            if(me instanceof NetIndexed && neighbor instanceof NetIndexed) {
              
                ConnectDirection dir = ConnectDirection.getDir(thisPos, neighborPos);

                connect((NetIndexed) neighbor, (NetIndexed) me, dir);
            }
        }
    }

    protected abstract void connect(NetIndexed neighbor, NetIndexed me, ConnectDirection dir);

    protected abstract void remove(NetIndexed me);
}
