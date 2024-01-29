package ru.ie.phase.foundation.net;

import java.util.List;
import java.util.UUID;

public interface Agent extends NetIndexed {

    void createLink(UUID id, ConnectDirection dir, LinkType linkType);

    void removeLink(UUID id);

    List<UUID> nodes();

    List<UUID> links();

}