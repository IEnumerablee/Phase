package ru.ie.phase.foundation.net;

import java.util.HashMap;
import java.util.Map;

public class NetRegistry {

    private final Map<String, AbstractNetSpace> spaces = new HashMap<>();

    public void register(String id, AbstractNetSpace netSpace){
        spaces.put(id, netSpace);
    }

    public AbstractNetSpace get(String id){
        return spaces.get(id);
    }

}
