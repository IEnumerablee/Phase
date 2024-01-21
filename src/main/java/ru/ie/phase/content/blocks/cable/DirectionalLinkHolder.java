package ru.ie.phase.content.blocks.cable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.UUID;

public class DirectionalLinkHolder {

    private HashMap<Direction, LinkEntry> links;

    public DirectionalLinkHolder(){
        initLinks();
    }

    public void changeLink(Direction dir, LinkType linkType, @Nullable UUID id){
        LinkEntry entry = links.get(dir);
        entry.linkType = linkType;
        entry.id = id;
    }

    public void disconnect(Direction dir){
        changeLink(dir, LinkType.NONE, null);
    }

    public LinkType getLinkType(Direction dir){
        return links.get(dir).linkType;
    }

    public UUID[] getLinks(LinkType linkType){
        return (UUID[]) links.values().stream()
                .filter(linkEntry -> linkEntry.linkType == linkType)
                .map(linkEntry -> linkEntry.id)
                .toArray();
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

    public enum Direction
    {
        NORTH,
        SOUTH,
        WEST,
        EAST,
        UP,
        DOWN
    }

    public enum LinkType
    {
        NODE,
        CABLE,
        NONE
    }
}
