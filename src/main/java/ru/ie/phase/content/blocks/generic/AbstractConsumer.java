package ru.ie.phase.content.blocks.generic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import ru.ie.phase.Phase;
import ru.ie.phase.foundation.net.electrical.NetConsumer;
import ru.ie.phase.foundation.net.electrical.VoltageLevel;

public abstract class AbstractConsumer extends IndexedBlockEntity implements NetConsumer {

    private float amperage;
    private VoltageLevel voltage;
    private float realVoltage = 0;

    private float effectivity = 0;

    public AbstractConsumer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected final void intStates(VoltageLevel voltage, float amperage){
        this.amperage = amperage;
        this.voltage = voltage;
    }

    @Override
    public void updateVoltage(float voltage) {
        realVoltage = voltage;
        effectivity = 100.0f / this.voltage.getVoltage() * voltage;
        Phase.LOGGER.debug("upd c v - %s e - %s  ID: %s".formatted(realVoltage, effectivity, id));
    }

    @Override
    public float getAmperage() {
        return amperage;
    }

    @Override
    public float getVoltage() {
        return voltage.getVoltage();
    }

    protected float realVoltage(){
        return realVoltage;
    }

    protected final float effectivity(){
        return effectivity;
    }

    protected final void updateAmperage(float amperage){
        this.amperage = amperage;
        ElectricalNetSpace.updatePowerStatement(id);
    }

    protected final float amperage(){
        return amperage;
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
