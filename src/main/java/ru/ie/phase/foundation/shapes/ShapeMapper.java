package ru.ie.phase.foundation.shapes;

import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ShapeMapper<T> {

    private final ShapeProvider provider;

    private final List<T> base = new ArrayList<>();

    private final BiFunction<ShapeProvider.Cube, ShapeContext, T> mapFunction;

    private final Map<Direction, Map<NeighborType, List<T>>> mappedShapesMap = new HashMap<>();

    public ShapeMapper(ShapeProvider provider, BiFunction<ShapeProvider.Cube, ShapeContext, T> mapFunction){
        this.mapFunction = mapFunction;
        this.provider = provider;
        initMap();
        map();
    }

    public final List<T> get(Map<Direction, NeighborType> sides)
    {
        List<T> shape = new ArrayList<>(base);

        sides.forEach((direction, type) -> shape.addAll(mappedShapesMap.get(direction).get(type)));

        return shape;
    }

    private void map()
    {
        provider.base().forEach(c -> this.base.add(mapFunction.apply(c, new ShapeContext())));

        provider.forEachShapes((direction, typeMap) -> typeMap.forEach((type, cubes) -> {

            List<T> shape = new ArrayList<>();

            cubes.forEach(cube -> {
                shape.add(mapFunction.apply(cube, new ShapeContext(type, direction)));
            });
            mappedShapesMap.get(direction).put(type, shape);
        }));
    }

    private void initMap(){
        for(Direction dir : Direction.values())
            mappedShapesMap.put(dir, new HashMap<>());
    }

    public final static class ShapeContext
    {
        @Nullable
        private final NeighborType neighborType;

        @Nullable
        private final Direction direction;

        private final boolean isBase;

        private ShapeContext(@Nonnull NeighborType neighborType, @Nonnull Direction direction){
            this.direction = direction;
            this.neighborType = neighborType;
            isBase = false;
        }

        public ShapeContext(){
            direction = null;
            neighborType = null;
            isBase = true;
        }

        @Nullable
        public NeighborType neighborType() {
            return neighborType;
        }

        @Nullable
        public Direction direction() {
            return direction;
        }

        public boolean isBase(){
            return isBase;
        }

    }

}
