package ru.ie.phase.content.blocks.cable;

import ru.ie.phase.Phase;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DirectionalLinkHolder implements Serializable {

    private final HashMap<ConnectDirection, LinkEntry> links = new HashMap<>();

    public DirectionalLinkHolder(){
        initLinks();
    }

    public void changeLink(ConnectDirection dir, LinkType linkType, @Nullable UUID id)
    {
        Phase.LOGGER.debug("d - %s lt - %s ID: %s".formatted(dir, linkType, id));
        LinkEntry entry = links.get(dir);
        entry.linkType = linkType;
        entry.id = id;
    }

    public void disconnect(UUID id)
    {
        LinkEntry entry = links.values().stream()
                .filter(linkEntry -> linkEntry.id != null)
                .filter(linkEntry -> linkEntry.id.equals(id))
                .findFirst()
                .orElseThrow();
        entry.linkType = LinkType.NONE;
        entry.id = null;
    }

    public LinkType getLinkType(UUID id){
        return links.values().stream()
                .filter(linkEntry -> linkEntry.id.equals(id))
                .findFirst()
                .orElseThrow().linkType;
    }

    public List<UUID> getLinks(LinkType linkType)
    {
        return links.values().stream()
                .filter(linkEntry -> linkEntry.linkType == linkType)
                .map(linkEntry -> linkEntry.id)
                .toList();
    }

    private void initLinks(){
        for(ConnectDirection direction : ConnectDirection.values())
            links.put(direction, new LinkEntry());
    }

    private static class LinkEntry implements Serializable
    {
        LinkEntry(){
            linkType = LinkType.NONE;
        }

        @Nullable UUID id;
        LinkType linkType;
    }
}
