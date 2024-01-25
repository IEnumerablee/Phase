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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static ru.ie.phase.Utils3d.createQuad;
import static ru.ie.phase.Utils3d.v;

public class CableDynamicModel implements IDynamicBakedModel {

    private final ModelState modelState;
    private final Function<Material, TextureAtlasSprite> spriteGetter;
    private final ItemOverrides overrides;
    private final ItemTransforms itemTransforms;

    public CableDynamicModel(ModelState modelState, Function<Material, TextureAtlasSprite> spriteGetter,
                             ItemOverrides overrides, ItemTransforms itemTransforms) {
        this.modelState = modelState;
        this.spriteGetter = spriteGetter;
        this.overrides = overrides;
        this.itemTransforms = itemTransforms;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull Random random, @NotNull IModelData iModelData) {
        List<BakedQuad> quads = new ArrayList<>();

        Material material = ForgeHooksClient.getBlockMaterial(Blocks.STONE.getRegistryName());
        TextureAtlasSprite textureSide = spriteGetter.apply(material);


        quads.add(createQuad(v(-1, -1, -1), v(1, -1, -1), v(1, 1, -1), v(1, 1, 1), modelState.getRotation(), textureSide));
        

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
}
