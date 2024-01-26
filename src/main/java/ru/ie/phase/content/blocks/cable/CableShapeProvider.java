package ru.ie.phase.content.blocks.cable;

import ru.ie.phase.foundation.shapes.NeighborType;
import ru.ie.phase.foundation.shapes.ShapeProvider;

import java.util.ArrayList;
import java.util.List;

import static ru.ie.phase.Utils3d.v;

public class CableShapeProvider extends ShapeProvider {

    private final Cube CABLE = new Cube(v(0.3f, 0.3f, 0), v(0.4f, 0.4f, 0.5f), "cable");
    private final Cube BASE = new Cube(v(0.3f, 0.3f, 0.3f), v(0.4f, 0.4f, 0.4f), "base");
    private final Cube CONNECTOR = new Cube(v(0.25f, 0.25f, 0), v(0.5f, 0.5f, 0.35f), "connector");
    private final Cube CABLE_HIDER = new Cube(v(0.295f, 0.295f, 0.295f), v(0.41f, 0.41f, 0.4f), "hider");

    @Override
    protected List<Cube> getBase() {
        return List.of(BASE);
    }

    @Override
    protected void createShapes() {

        appendConnectionConf(NeighborType.CABLE, List.of(CABLE, CABLE_HIDER));
        appendConnectionConf(NeighborType.NODE, List.of(CABLE, CONNECTOR));
        appendConnectionConf(NeighborType.BLOCK, new ArrayList<>());
        appendConnectionConf(NeighborType.NONE, new ArrayList<>());

    }
}
