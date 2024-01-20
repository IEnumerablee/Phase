package ru.ie.phase.content.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.Phase;
import ru.ie.phase.foundation.net.ElectricalNetSpace;
import ru.ie.phase.foundation.net.NetGenerator;
import ru.ie.phase.foundation.net.VoltageLevel;

import java.util.*;

public abstract class AbstractGenerator extends IndexedBlockEntity implements NetGenerator {

    protected float amperage;
    private float drop = 0;
    private VoltageLevel voltage;
    private float usedPower = 0;

    private int updateCounter = 0;

    protected final Map<UUID, Float> consumerStatements = new HashMap<>();

    public AbstractGenerator(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected final void intStates(VoltageLevel voltage, float amperage){
        this.amperage = amperage;
        this.voltage = voltage;
    }

    @Override
    public void updatePowerStatement()
    {
        drop = 0;
        usedPower = 0;

        for(float p : consumerStatements.values()) usedPower += p;

        if(usedPower > voltage.getVoltage() * amperage)
            drop = (usedPower - voltage.getVoltage() * amperage) / amperage;

        consumerStatements.keySet().forEach(uuid -> ElectricalNetSpace.updateConsumerVoltage(uuid));
    }

    @Override
    public void removeConsumer(UUID consumerId) {
        consumerStatements.remove(consumerId);
        updatePowerStatement();
    }

    @Override
    public void updateConsumer(UUID consumerId, float power) {
        consumerStatements.put(consumerId, power);
        updatePowerStatement();
    }

    @Override
    public float getAmperage() {
        return amperage;
    }

    @Override
    public float getVoltage() {
        return voltage.getVoltage() - drop;
    }

    protected final VoltageLevel voltage(){
        return voltage;
    }

    protected final float amperage(){
        return amperage;
    }

    protected final float drop(){
        return drop;
    }

    protected final float usedPower(){
        return usedPower;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putFloat("drop", drop);
        nbt.putFloat("usedpower", usedPower);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        drop = nbt.getFloat("drop");
        usedPower = nbt.getFloat("usedpower");
    }

    @Override
    protected void createId()
    {
        id = ElectricalNetSpace.createNode(this);
    }

    @Override
    protected void registerId() {
        ElectricalNetSpace.addNode(id, this);
    }
}