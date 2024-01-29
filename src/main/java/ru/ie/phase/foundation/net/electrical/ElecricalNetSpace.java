package ru.ie.phase.foundation.net.electrical;

import ru.ie.phase.foundation.net.AbstractNetSpace;
import ru.ie.phase.foundation.net.ConnectDirection;

import java.util.UUID;

public class ElecricalNetSpace extends AbstractNetSpace<CableAgent> {

    @Override
    public void connectAgent2Agent(UUID a1, UUID a2, ConnectDirection dir) {
        super.connectAgent2Agent(a1, a2, dir);
    }

    @Override
    public void connectAgent2Node(UUID agentId, UUID nodeId, ConnectDirection dir) {
        super.connectAgent2Node(agentId, nodeId, dir);
    }

    @Override
    public void removeAgent(UUID id) {
        super.removeAgent(id);
    }

    @Override
    public void removeNode(UUID id) {
        super.removeNode(id);
    }
}
