package ru.ie.phase.content.blocks.cable;

import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static ru.ie.phase.Utils3d.v;

public class CableShapeProvider {

    private static final List<Cube> base = new ArrayList<>();

    private static Map<Direction, Map<NeighborType, List<Cube>>> shapesMap = new HashMap<>();

    private static final Cube CABLE = new Cube(v(0.2999f, 0.2999f, 0), v(0.4002f, 0.4002f, 0.5f));
    private static final Cube CONNECTOR = new Cube(v(0.25f, 0.25f, 0), v(0.5f, 0.5f, 0.35f));
    private static final Cube BASE = new Cube(v(0.3f, 0.3f, 0.3f), v(0.4f, 0.4f, 0.4f));

    static{
        initMap();
        initBase();
        createShapes();
    }

    private static void initBase(){
        base.add(BASE);
    }

    private static void createShapes()
    {

        appendConnectionConf(NeighborType.CABLE, List.of(CABLE));
        appendConnectionConf(NeighborType.NODE, List.of(CABLE, CONNECTOR));
        appendConnectionConf(NeighborType.BLOCK, new ArrayList<>());
        appendConnectionConf(NeighborType.NONE, new ArrayList<>());

    }

    private static void initMap(){
        for(Direction dir : Direction.values())
            shapesMap.put(dir, new HashMap<>());
    }

    private static void appendConnectionConf(NeighborType type, List<Cube> shape)
    {

        for(Direction dir : Direction.values()) {
            List<Cube> currentShape = copyl(shape);
            currentShape.forEach(cube -> {
                rot(dir, cube.pos,1);
                rot(dir, cube.size,0);
                cube.fix();
            });
            shapesMap.get(dir).put(type, currentShape);
        }

    }

    private static List<Cube> copyl(List<Cube> list){
        List<Cube> newList = new ArrayList<>();
        list.forEach(cube -> newList.add(cube.copy()));
        return newList;
    }

    private static void rot(Direction dir, Vector3f vec, float c)
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

    public static class Mapper<T>
    {
        private List<T> base = new ArrayList<>();

        private final BiFunction<Vector3f, Vector3f, T> mapFunction;

        private final Map<Direction, Map<NeighborType, List<T>>> mappedShapesMap = new HashMap<>();

        public Mapper(BiFunction<Vector3f, Vector3f, T> mapFunction){
            this.mapFunction = mapFunction;
            initMap();
            map();
        }

        public List<T> get(Map<Direction, NeighborType> sides)
        {
            List<T> shape = new ArrayList<>();

            shape.addAll(base);

            sides.forEach((direction, type) -> shape.addAll(mappedShapesMap.get(direction).get(type)));

            return shape;
        }

        private void map()
        {

            CableShapeProvider.base.forEach(c -> this.base.add(mapFunction.apply(c.pos, c.size)));

            shapesMap.forEach((direction, typeMap) -> typeMap.forEach((type, cubes) -> {

                List<T> shape = new ArrayList<>();

                cubes.forEach(cube -> {
                    shape.add(mapFunction.apply(cube.pos, cube.size));
                });
                mappedShapesMap.get(direction).put(type, shape);
            }));
        }

        private void initMap(){
            for(Direction dir : Direction.values())
                mappedShapesMap.put(dir, new HashMap<>());
        }

    }

    private static class Cube
    {
        Cube(Vector3f pos, Vector3f size){
            this.pos = pos;
            this.size = size;
        }

        Vector3f pos;
        Vector3f size;

        void fix(){
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

        Cube copy(){
            return new Cube(pos.copy(), size.copy());
        }
    }

}
