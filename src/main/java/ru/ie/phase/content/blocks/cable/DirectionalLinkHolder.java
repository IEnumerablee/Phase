package ru.ie.phase.content.blocks.cable;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DirectionalLinkHolder implements Serializable {

    private HashMap<Direction, LinkEntry> links;

    public DirectionalLinkHolder(){
        initLinks();
    }

    public void changeLink(Direction dir, LinkType linkType, @Nullable UUID id){
        LinkEntry entry = links.get(dir);
        entry.linkType = linkType;
        entry.id = id;
    }

    public void disconnect(UUID id){
        LinkEntry entry =links.values().stream()
                .filter(linkEntry -> linkEntry.id == id)
                .findFirst()
                .get();
        entry.linkType = LinkType.NONE;
        entry.id = null;
    }

    public LinkType getLinkType(Direction dir){
        return links.get(dir).linkType;
    }

    public List<UUID> getLinks(LinkType linkType){
        return links.values().stream()
                .filter(linkEntry -> linkEntry.linkType == linkType)
                .map(linkEntry -> linkEntry.id)
                .toList();
    }

    private void initLinks(){
        for(Direction direction : Direction.values())
            links.put(direction, new LinkEntry());
    }

    private static class LinkEntry
    {
        LinkEntry(){
            linkType = LinkType.NONE;
        }

        @Nullable UUID id;
        LinkType linkType;
    }
}
