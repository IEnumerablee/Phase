package ru.ie.phase.foundation.shapes;

import java.util.HashMap;
import java.util.Map;

public class ShapeProvidersRegistry {

    private final static Map<String, ShapeProvider> providers = new HashMap<>();

    static {
        providers.put("cable", new CableShapeProvider());

        providers.values().forEach(ShapeProvider::init);
    }

    public static ShapeProvider get(String id){
        return providers.get(id);
    }

}
