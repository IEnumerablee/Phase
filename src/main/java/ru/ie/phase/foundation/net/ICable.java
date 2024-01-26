package ru.ie.phase.foundation.net;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ICable extends NetIndexed{

    void createLink(UUID id, ConnectDirection dir, LinkType linkType);

    void removeLink(UUID id);

    Map<UUID, Float> lossmap();
    List<UUID> nodes();

    List<UUID> links();

    float loss();

}