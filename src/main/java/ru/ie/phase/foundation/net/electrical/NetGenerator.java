package ru.ie.phase.foundation.net.electrical;

import ru.ie.phase.foundation.net.NetNode;

import java.util.UUID;

public interface NetGenerator extends NetNode {

    void updatePowerStatement();

    void removeConsumer(UUID consumerId);

    void updateConsumer(UUID consumerId, float power);

    void flushConsumers();

    float getNominalVoltage();

}
