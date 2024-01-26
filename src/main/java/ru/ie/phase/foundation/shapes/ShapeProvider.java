package ru.ie.phase.foundation.shapes;

import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class ShapeProvider {

    private List<Cube> base;

    private final Map<Direction, Map<NeighborType, List<Cube>>> shapesMap = new HashMap<>();

    void init(){
        initMap();
        base = getBase();
        createShapes();
    }

    List<Cube> base(){
        return base;
    }

    void forEachShapes(BiConsumer<Direction, Map<NeighborType, List<Cube>>> consumer){
        shapesMap.forEach(consumer);
    }

    protected abstract List<Cube> getBase();

    protected abstract void createShapes();

    protected final void appendConnectionConf(NeighborType type, List<Cube> shape)
    {

        for(Direction dir : Direction.values()) {
            List<Cube> currentShape = copyl(shape);
            currentShape.forEach(cube -> {
                rot(dir, cube.pos(),1);
                rot(dir, cube.size(),0);
                cube.fix();
            });
            shapesMap.get(dir).put(type, currentShape);
        }

    }

    private void initMap(){
        for(Direction dir : Direction.values())
            shapesMap.put(dir, new HashMap<>());
    }

    private List<Cube> copyl(List<Cube> list){
        List<Cube> newList = new ArrayList<>();
        list.forEach(cube -> newList.add(cube.copy()));
        return newList;
    }

    private void rot(Direction dir, Vector3f vec, float c)
    {
        switch(dir){
            case UP -> {
                float tmpy = vec.y();
                vec.setY(c - vec.z());
                vec.setZ(tmpy);
            }
            case DOWN -> {
                float tmpy = vec.y();
                vec.setY(vec.z());
                vec.setZ(tmpy);
            }
            case EAST -> {
                float tmpx = vec.x();
                vec.setX(c - vec.z());
                vec.setZ(tmpx);
            }
            case WEST -> {
                float tmpx = vec.x();
                vec.setX(vec.z());
                vec.setZ(tmpx);
            }
            case SOUTH -> vec.setZ(c - vec.z());
        }
    }

    public final static class Cube
    {
        private final Vector3f pos;
        private final Vector3f size;

        public Cube(Vector3f pos, Vector3f size){
            this.pos = pos;
            this.size = size;
        }

        public Vector3f pos(){
            return pos;
        }

        public Vector3f size(){
            return size;
        }

        private void fix(){
            if(size.x() < 0){
                pos.setX(pos.x() + size.x());
                size.setX(-size.x());
            }
            if(size.z() < 0){
                pos.setZ(pos.z() + size.z());
                size.setZ(-size.z());
            }
            if(size.y() < 0){
                pos.setY(pos.y() + size.y());
                size.setY(-size.y());
            }
        }

        private Cube copy(){
            return new Cube(pos.copy(), size.copy());
        }
    }

}
