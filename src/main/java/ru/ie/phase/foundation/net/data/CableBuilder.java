package ru.ie.phase.foundation.net.data;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import org.jetbrains.annotations.NotNull;
import ru.ie.phase.content.blocks.cable.Cable;
import ru.ie.phase.content.blocks.cable.CableModelLoader;
import ru.ie.phase.content.blocks.cable.CableTextureRegistry;

public class CableBuilder<P> extends BlockBuilder<Cable, P> {

    private final Factory factory;
    private String resourceId;

    private CableBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullSupplier<BlockBehaviour.Properties> initialProperties, Factory factory) {
        super(owner, parent, name, callback, factory, initialProperties);
        this.factory = factory;
    }


    public static <P> CableBuilder<P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Material material, float loss, String resourceId) {
        CableBuilder<P> builder = new CableBuilder(owner, parent, name, callback, () -> BlockBehaviour.Properties.of(material), new Factory(loss));
        builder.resourceId = resourceId;
        return builder;
    }

    @Override
    public CableBuilder<P> simpleItem() {
        item().defaultModel().register();
        return this;
    }

    @Override
    public BlockEntry<Cable> register() {
        BlockEntry<Cable> entry = super.register();

        onRegister(cable -> CableTextureRegistry.register(cable, resourceId));

        return entry;
    }

    public CableBuilder<P> model(){

        blockstate((context, provider) ->{

            BlockModelBuilder cableModel = provider.models().getBuilder("%s_cable".formatted(getName()))
                    .parent(provider.models().getExistingFile(provider.mcLoc("cube")))
                    .customLoader((blockModelBuilder, helper) -> new CustomLoaderBuilder<BlockModelBuilder>(CableModelLoader.LOADER, blockModelBuilder, helper) { })
                    .end();

            provider.simpleBlock(context.get(), cableModel);

        });

        return this;
    }

    private static class Factory implements NonNullFunction<BlockBehaviour.Properties, Cable>
    {
        float loss = 0;

        Factory(float loss){
            this.loss = loss;
        }

        @NotNull
        @Override
        public Cable apply(@NotNull BlockBehaviour.Properties properties) {
            return new Cable(properties, loss);
        }
    }

}
