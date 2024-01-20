package ru.ie.phase.content.blocks.generic;

import ru.ie.phase.foundation.net.ElectricalNetSpace;
import ru.ie.phase.foundation.net.ICable;
import ru.ie.phase.foundation.net.NetIndexed;

public class NodeBlock extends AbstractNetBlock{
    public NodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected final void connect(NetIndexed neighbor, NetIndexed me) {
        if(neighbor instanceof ICable)
            ElectricalNetSpace.connectCable2Node(neighbor.getId(), me.getId());
    }

    @Override
    protected final void remove(NetIndexed me) {
        ElectricalNetSpace.removeNode(me.getId());
    }
}
