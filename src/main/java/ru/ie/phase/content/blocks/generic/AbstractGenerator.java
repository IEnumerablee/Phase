package ru.ie.phase.content.blocks.generic;

import net.minecraft.core.BlockPos;
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
    public void updateConsumerStatement(UUID nodeId, float power)
    {
        consumerStatements.put(nodeId, power);

        float absolutePower = 0;
        for(float p : consumerStatements.values()){
            Phase.LOGGER.debug("%s".formatted(p));
            absolutePower += p;
        }

        usedPower = absolutePower;

        Phase.LOGGER.debug("upd g p - %s".formatted(usedPower));
        Phase.LOGGER.debug("uc - %s".formatted(updateCounter));
        if(absolutePower < voltage.getVoltage() * amperage || updateCounter != 0) return;

        drop = (absolutePower - voltage.getVoltage() * amperage) / amperage;

        refreshStatements();

        Phase.LOGGER.debug("drop g p - %s".formatted(drop));
    }

    @Override
    public void removeConsumer(UUID consumerId) {
        consumerStatements.remove(consumerId);

        drop = 0;
        if(consumerStatements.isEmpty())
            usedPower = 0;
        else
            refreshStatements();
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
    protected void createId()
    {
        id = ElectricalNetSpace.createNode(this);
    }

    private void refreshStatements(){
        Set<UUID> consumers = new HashSet<>(consumerStatements.keySet());
        consumerStatements.clear();
        updateCounter = consumers.size();

        for(UUID consumer: consumers){
            updateCounter--;
            ElectricalNetSpace.updatePowerStatement(consumer);
        }
    }





}