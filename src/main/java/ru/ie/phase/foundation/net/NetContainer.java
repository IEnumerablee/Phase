package ru.ie.phase.foundation.net;

import ru.ie.phase.foundation.net.ConnectDirection;
import ru.ie.phase.foundation.net.NetNode;

public interface NetContainer {
    NetNode getInterface(ConnectDirection direction);

}
