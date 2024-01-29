package ru.ie.phase.foundation.net;

import ru.ie.phase.utils.HashRegistry;
import ru.ie.phase.utils.Utils;

import java.util.*;

public abstract class AbstractNetSpace<T extends Agent> {

    private final HashRegistry<NetNode> nodes = new HashRegistry<>();
    private final HashRegistry<Agent> agents= new HashRegistry<>();

    private final Map<UUID, UUID> nodeJoints = new HashMap<>();

    public final UUID createAgent(T agent){
        Utils.checkElement(agent);
        return agents.create(agent);
    }

    public final void addAgent(UUID id, T agent){
        Utils.checkElement(agent);
        agents.put(id, agent);
    }

    public final UUID createNode(NetNode node){
        Utils.checkElement(node);
        return nodes.create(node);
    }

    public final void addNode(UUID id, NetNode node){
        Utils.checkElement(node);
        nodes.put(id, node);
    }

    public void connectAgent2Agent(UUID a1, UUID a2, ConnectDirection dir)
    {
        Agent agent1 = agents.get(a1);
        Agent agent2 = agents.get(a2);

        agent1.createLink(a2, dir, LinkType.AGENT);
        agent2.createLink(a1, dir, LinkType.AGENT);
    }

    public void connectAgent2Node(UUID agentId, UUID nodeId, ConnectDirection dir)
    {
        if(nodeJoints.containsKey(nodeId)) return;

        agents.get(agentId).createLink(nodeId, dir, LinkType.NODE);
        nodeJoints.put(nodeId, agentId);
    }

    public void removeAgent(UUID id)
    {
        Agent agent = agents.get(id);
        agent.links().forEach(uuid -> agents.get(uuid).removeLink(id));
        agents.remove(id);
    }

    public void removeNode(UUID id)
    {
        Agent agent = agents.get(nodeJoints.get(id));
        agent.removeLink(id);
        nodes.remove(id);
    }
}
