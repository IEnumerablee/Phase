package ru.ie.phase.foundation.net;

import java.util.List;
import java.util.UUID;

public interface NetNode extends NetIndexed{

    void addAgent(UUID id);

    void removeAgent(UUID id);

    List<UUID> agents();

}
