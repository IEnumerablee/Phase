package ru.ie.phase.content.blocks.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.Phase;
import ru.ie.phase.Utils;
import ru.ie.phase.content.blocks.generic.IndexedBlockEntity;
import ru.ie.phase.declaration.BlockEntities;
import ru.ie.phase.foundation.net.ElectricalNetSpace;
import ru.ie.phase.foundation.net.ICable;

import java.io.IOException;
import java.util.*;

public class CableEntity extends IndexedBlockEntity implements ICable{

    private float loss;
    private Map<UUID, Float> lossmap = new HashMap<>();
    private List<UUID> links = new ArrayList<>();
    private List<UUID> nodes = new ArrayList<>();

    public CableEntity(BlockEntityType<CableEntity> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public static CableEntity create(BlockPos pos, BlockState state, float loss){
        CableEntity entity = new CableEntity(BlockEntities.CABLE_ENTITY.get(), pos, state);
        entity.loss = loss;
        return entity;
    }

    @Override
    public void createLink(UUID id){
        if(!links.contains(id))
            links.add(id);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt)
    {
        super.saveAdditional(nbt);
        nbt.putFloat("loss", loss);

        byte[] netdata;

        try {

            netdata = Utils.writeToByteArray(lossmap, links, nodes);
            nbt.putByteArray("netdata", netdata);

        } catch (IOException e) {
            BlockPos pos = getBlockPos();
            Phase.LOGGER.error("Failed to save netdata for block %s %s %s)".formatted(pos.getX(), pos.getY(), pos.getZ()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public void load(CompoundTag nbt)
    {
        super.load(nbt);
        loss = nbt.getFloat("loss");

        byte[] byteNetdata = nbt.getByteArray("netdata");

        try {
            Object[] netdata = Utils.readByteArray(byteNetdata);

            lossmap = (Map<UUID, Float>) netdata[0];
            links = (List<UUID>) netdata[1];
            nodes = (List<UUID>) netdata[2];

        } catch (IOException | ClassNotFoundException | IndexOutOfBoundsException | ClassCastException e) {
            BlockPos pos = getBlockPos();
            Phase.LOGGER.error("Failed to load netdata for block %s %s %s)".formatted(pos.getX(), pos.getY(), pos.getZ()));
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<UUID, Float> lossmap() {
        return lossmap;
    }

    @Override
    public List<UUID> nodes() {
        return nodes;
    }

    @Override
    public List<UUID> links() {
        return links;
    }

    @Override
    public float loss() {
        return loss;
    }

    @Override
    public boolean isStandalone() {
        return links.isEmpty();
    }

    @Override
    protected void createId() {
        id = ElectricalNetSpace.createCable(this);
    }

}
