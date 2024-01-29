package ru.ie.phase.utils;

import java.util.HashMap;
import java.util.UUID;

public class HashRegistry<T> extends HashMap<UUID, T>{

    public final UUID create(T val)
    {
        UUID uid = UUID.randomUUID();
        put(uid, val);
        return uid;
    }
}
