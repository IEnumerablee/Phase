package ru.ie.phase.content.blocks.cable;

import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CableTextureRegistry {

    private static final Map<Block, String> map = new HashMap<>();

    public static void register(Cable cable, String id){
        map.put(cable, id);
    }

    public static String getId(Block block){
        return map.get(block);
    }

    public static Collection<String> getAll(){
        return map.values();
    }

}
