package ru.ie.phase.content.blocks.cable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ie.phase.Phase;
import ru.ie.phase.foundation.shapes.ShapeMapper;
import ru.ie.phase.foundation.shapes.ShapeProvider;
import ru.ie.phase.foundation.shapes.NeighborType;
import ru.ie.phase.foundation.shapes.ShapeProvidersRegistry;

import java.util.*;
import java.util.function.Function;

import static ru.ie.phase.Utils3d.*;

public class CableDynamicModel implements IDynamicBakedModel {

    private final ModelState modelState;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final ItemOverrides overrides;
    private final ItemTransforms itemTransforms;

    private ShapeMapper<List<BakedQuad>> shapeMapper;

    private TextureAtlasSprite SIDE;
    private TextureAtlasSprite END;
    private TextureAtlasSprite BASE;
    private TextureAtlasSprite CONNECTOR;

    private boolean isInit = false;

    public CableDynamicModel(ModelState modelState, Function<Material, TextureAtlasSprite> spriteGetter,
                             ItemOverrides overrides, ItemTransforms itemTransforms) {
        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.overrides = overrides;
        this.itemTransforms = itemTransforms;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull Random random, @NotNull IModelData iModelData)
    {
        if(!isInit){
            if(blockState != null) initTextures(blockState.getBlock());
            initMapper();
            isInit = true;
        }

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

    private TextureAtlasSprite getTexture(String path) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(Phase.MODID, path));
    }

    private void initTextures(Block block){

        String id = CableTextureRegistry.getId(block);

        END = getTexture("block/cable/%s/end".formatted(id));
        SIDE = getTexture("block/cable/%s/side".formatted(id));
        BASE = getTexture("block/cable/base");
        CONNECTOR = getTexture("block/cable/connector");
    }

    private void initMapper(){

        shapeMapper = new ShapeMapper<>(ShapeProvidersRegistry.get("cable"), (cube, context) -> {

            if(context.isBase()) {
                return createCube(cube.pos(), cube.size(), modelState.getRotation(), BASE, BASE, false);
            }else {

                if(context.neighborType() == NeighborType.NODE)
                    return createCube(cube.pos(), cube.size(), modelState.getRotation(), CONNECTOR, CONNECTOR, false);

                boolean swapUv = false;
                if (context.direction() == Direction.NORTH || context.direction() == Direction.SOUTH)
                    swapUv = true;
                return createCube(cube.pos(), cube.size(), modelState.getRotation(), SIDE, END, swapUv);
            }
        });
    }

}
