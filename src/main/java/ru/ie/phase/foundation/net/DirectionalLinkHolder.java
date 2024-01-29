package ru.ie.phase.foundation.net;

import net.minecraft.core.Direction;
import org.antlr.v4.codegen.model.SrcOp;
import ru.ie.phase.Phase;
import ru.ie.phase.utils.Utils;

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

    public void addInterface(NetNode sideInterface, ConnectDirection... directions){
        Utils.checkElement(sideInterface);
        for(ConnectDirection direction : directions)
            links.get(direction).sideInterface = sideInterface;
    }

    public void changeLink(ConnectDirection dir, LinkType linkType, @Nullable UUID id)
    {
        Phase.LOGGER.debug("d - %s lt - %s ID: %s".formatted(dir, linkType, id));
        LinkEntry entry = links.get(dir);
        entry.linkType = linkType;
    }

    public void disconnect(UUID id)
    {
        LinkEntry entry = links.values().stream()
                .filter(linkEntry -> linkEntry.linkId != null)
                .filter(linkEntry -> linkEntry.linkId.equals(id))
                .findFirst()
                .orElseThrow();
        entry.linkType = LinkType.NONE;
        entry.linkId = null;
    }

    public ConnectDirection getDirection(UUID id)
    {
        for(ConnectDirection dir : ConnectDirection.values()){
            LinkEntry entry = links.get(dir);
            if(entry.linkId != null && entry.linkId.equals(id)) return dir;
        }
        throw new IllegalArgumentException("id not found");
    }

    public List<UUID> getLinks(LinkType linkType)
    {
        return links.values().stream()
                .filter(linkEntry -> linkEntry.linkType == linkType)
                .map(linkEntry -> linkEntry.linkId)
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

        @Nullable UUID linkId;
        @Nullable NetNode sideInterface;
        LinkType linkType;

    }
}
