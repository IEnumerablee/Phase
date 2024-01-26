package ru.ie.phase.content.blocks.cable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import ru.ie.phase.Phase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class CableModelLoader implements IModelLoader<CableModelLoader.GeneratorModelGeometry> {

    public static final ResourceLocation LOADER = new ResourceLocation(Phase.MODID, "cableloader");

    @Override
    public GeneratorModelGeometry read(JsonDeserializationContext jsonDeserializationContext, JsonObject jsonObject) {
        return new GeneratorModelGeometry();
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    public static class GeneratorModelGeometry implements IModelGeometry<GeneratorModelGeometry>
    {

        @Override
        public BakedModel bake(IModelConfiguration owner, ModelBakery modelBakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides itemOverrides, ResourceLocation resourceLocation) {
            return new CableDynamicModel(modelState, spriteGetter, itemOverrides, owner.getCameraTransforms());
        }

        @Override
        public Collection<Material> getTextures(IModelConfiguration iModelConfiguration, Function<ResourceLocation, UnbakedModel> function, Set<Pair<String, String>> set) {

            List<Material> materials = new ArrayList<>();

            CableTextureRegistry.getAll().forEach(id -> {
                materials.add(t(new ResourceLocation(Phase.MODID, "block/cable/%s/end".formatted(id))));
                materials.add(t(new ResourceLocation(Phase.MODID, "block/cable/%s/side".formatted(id))));
            });

            materials.add(t(new ResourceLocation(Phase.MODID, "block/cable/connector")));
            materials.add(t(new ResourceLocation(Phase.MODID, "block/cable/base")));

            return materials;
        }

        private Material t(ResourceLocation location){
            return ForgeHooksClient.getBlockMaterial(location);
        }

    }
}