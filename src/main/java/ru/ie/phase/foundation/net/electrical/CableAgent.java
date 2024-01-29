package ru.ie.phase.foundation.net.electrical;

import ru.ie.phase.foundation.net.Agent;

import java.util.Map;
import java.util.UUID;

public interface CableAgent extends Agent {

    Map<UUID, Float> lossmap();

    float loss();

}
