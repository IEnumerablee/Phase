package ru.ie.phase.content.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.Phase;
import ru.ie.phase.Utils;
import ru.ie.phase.foundation.net.ElectricalNetSpace;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class IndexedBlockEntity extends BlockEntity {

    protected UUID id;

    public IndexedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public final void onLoad() {
        if(this.level != null && !this.level.isClientSide) {
            if(id == null)
                createId();
            else
                registerId();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
        nbt.putUUID("netid", id);
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        id = nbt.getUUID("netid");
    }

    public final UUID getId(){
        return id;
    }

    protected abstract void createId();

    protected abstract void registerId();

}
