package ru.ie.phase.foundation.net;

import java.util.UUID;

public interface NetGenerator extends NetNode{

    void updatePowerStatement();

    void removeConsumer(UUID consumerId);

    void updateConsumer(UUID consumerId, float power);

    float getRealVoltage();

}
