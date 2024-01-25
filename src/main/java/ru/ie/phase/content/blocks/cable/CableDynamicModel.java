package ru.ie.phase.content.blocks.cable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ie.phase.Phase;

import java.util.*;
import java.util.function.Function;

import static ru.ie.phase.Utils3d.*;

public class CableDynamicModel implements IDynamicBakedModel {

    private final ModelState modelState;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final ItemOverrides overrides;
    private final ItemTransforms itemTransforms;

    private CableShapeProvider.Mapper<List<BakedQuad>> shapeMapper;

    public CableDynamicModel(ModelState modelState, Function<Material, TextureAtlasSprite> spriteGetter,
                             ItemOverrides overrides, ItemTransforms itemTransforms) {
        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.overrides = overrides;
        this.itemTransforms = itemTransforms;
        initMapper();
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull Random random, @NotNull IModelData iModelData) {

        Map<Direction, NeighborType> sidesMap = new HashMap<>();
        List<BakedQuad> quads = new ArrayList<>();

        if(blockState != null) {
            sidesMap.put(Direction.NORTH, blockState.getValue(Cable.NORTH));
            sidesMap.put(Direction.SOUTH, blockState.getValue(Cable.SOUTH));
            sidesMap.put(Direction.WEST, blockState.getValue(Cable.WEST));
            sidesMap.put(Direction.EAST, blockState.getValue(Cable.EAST));
            sidesMap.put(Direction.UP, blockState.getValue(Cable.UP));
            sidesMap.put(Direction.DOWN, blockState.getValue(Cable.DOWN));
        }

        shapeMapper.get(sidesMap).forEach(quads::addAll);

        return quads;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        Material material = ForgeHooksClient.getBlockMaterial(Blocks.STONE.getRegistryName());
        return spriteGetter.apply(material);
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    private void initMapper(){

        Material material = ForgeHooksClient.getBlockMaterial(Blocks.STONE.getRegistryName());
        TextureAtlasSprite textureSide = spriteGetter.apply(material);

        shapeMapper = new CableShapeProvider.Mapper<>((pos, size) ->
                createCube(pos, size, modelState.getRotation(), textureSide)
        );
    }

}
