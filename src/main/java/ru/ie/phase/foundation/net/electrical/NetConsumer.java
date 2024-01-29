package ru.ie.phase.foundation.net.electrical;

import ru.ie.phase.foundation.net.NetNode;

public interface NetConsumer extends NetNode {

    void updateVoltage(float voltage);

}