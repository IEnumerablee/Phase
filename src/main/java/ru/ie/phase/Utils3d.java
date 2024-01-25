package ru.ie.phase;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.ArrayList;
import java.util.List;

public class Utils3d {

    private static void putVertex(BakedQuadBuilder builder, Vector3f normal, Vector4f vector, float u, float v, TextureAtlasSprite sprite)
    {

        var elements = builder.getVertexFormat().getElements().asList();
        for (int j = 0 ; j < elements.size() ; j++) {
            var e = elements.get(j);
            switch (e.getUsage()) {
                case POSITION -> builder.put(j, vector.x(), vector.y(), vector.z(), 1.0f);
                case COLOR    -> builder.put(j, 1.0f, 1.0f, 1.0f, 1.0f);
                case UV       -> putVertexUV(builder, u, v, sprite, j, e);
                case NORMAL   -> builder.put(j, normal.x(), normal.y(), normal.z());
                default       -> builder.put(j);
            }
        }
    }

    private static void putVertexUV(BakedQuadBuilder builder, float u, float v, TextureAtlasSprite sprite, int j, VertexFormatElement e)
    {
        switch (e.getIndex()) {
            case 0  -> builder.put(j, sprite.getU(u), sprite.getV(v));
            case 2  -> builder.put(j, (short) 0, (short) 0);
            default -> builder.put(j);
        }
    }

    public static BakedQuad createQuad(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, Transformation rotation, TextureAtlasSprite sprite)
    {
        Vector3f normal = v3.copy();
        normal.sub(v2);
        Vector3f temp = v1.copy();
        temp.sub(v2);
        normal.cross(temp);
        normal.normalize();

        int tw = sprite.getWidth();
        int th = sprite.getHeight();

        rotation = rotation.blockCenterToCorner();
        rotation.transformNormal(normal);

        Vector4f vv1 = new Vector4f(v1); rotation.transformPosition(vv1);
        Vector4f vv2 = new Vector4f(v2); rotation.transformPosition(vv2);
        Vector4f vv3 = new Vector4f(v3); rotation.transformPosition(vv3);
        Vector4f vv4 = new Vector4f(v4); rotation.transformPosition(vv4);

        var builder = new BakedQuadBuilder(sprite);
        builder.setQuadOrientation(Direction.getNearest(normal.x(), normal.y(), normal.z()));
        putVertex(builder, normal, vv1, 0, 0, sprite);
        putVertex(builder, normal, vv2, 0, th, sprite);
        putVertex(builder, normal, vv3, tw, th, sprite);
        putVertex(builder, normal, vv4, tw, 0, sprite);
        return builder.build();
    }

    public static List<BakedQuad> createCube(Vector3f position, Vector3f size,
                                             Transformation rotation, TextureAtlasSprite sprite)
    {
        return createCube(position, size.y(), size.x(), size.z(), rotation, sprite);
    }

    public static List<BakedQuad> createCube(Vector3f position, float height, float weight, float length,
                                             Transformation rotation, TextureAtlasSprite sprite)
    {
        List<BakedQuad> quads = new ArrayList<>();

        quads.add(createQuad(
                v(position.x(), position.y() + height, position.z()),
                v(position.x() + weight, position.y() + height, position.z()),
                v(position.x() + weight, position.y(), position.z()),
                v(position.x(), position.y(), position.z()),
                rotation, sprite));

        quads.add(createQuad(
                v(position.x(), position.y(), position.z() + length),
                v(position.x() + weight, position.y(), position.z() + length),
                v(position.x() + weight, position.y() + height, position.z() + length),
                v(position.x(), position.y() + height, position.z() + length),
                rotation, sprite));

        quads.add(createQuad(
                v(position.x(), position.y(), position.z()),
                v(position.x() + weight, position.y(), position.z()),
                v(position.x() + weight, position.y() , position.z() + length),
                v(position.x(), position.y(), position.z() + length),
                rotation, sprite));

        quads.add(createQuad(
                v(position.x(), position.y() + height, position.z() + length),
                v(position.x() + weight, position.y() + height, position.z() + length),
                v(position.x() + weight, position.y() + height, position.z()),
                v(position.x(), position.y() + height, position.z()),
                rotation, sprite));

        quads.add(createQuad(
                v(position.x(), position.y(), position.z()),
                v(position.x(), position.y(), position.z() + length),
                v(position.x(), position.y() + height, position.z() + length),
                v(position.x(), position.y() + height, position.z()),
                rotation, sprite));

        quads.add(createQuad(
                v(position.x() + weight, position.y() + height, position.z()),
                v(position.x() + weight, position.y() + height, position.z() + length),
                v(position.x() + weight, position.y(), position.z() + length),
                v(position.x() + weight, position.y(), position.z()),
                rotation.inverse(), sprite));

        return quads;
    }

    public static Vector3f v(float x, float y, float z) {
        return new Vector3f(x, y, z);
    }

}
