package ru.ie.phase.foundation.net;

import java.util.UUID;

public interface NetGenerator extends NetNode{

    void updateConsumerStatement(UUID consumerId, float power);

    void removeConsumer(UUID consumerId);

}
