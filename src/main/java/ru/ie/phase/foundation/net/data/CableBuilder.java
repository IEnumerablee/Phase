package ru.ie.phase.foundation.net.data;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import ru.ie.phase.content.blocks.cable.Cable;

public class CableBuilder<P> extends BlockBuilder<Cable, P> {

    private final Factory factory;

    private CableBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullSupplier<BlockBehaviour.Properties> initialProperties, Factory factory) {
        super(owner, parent, name, callback, factory, initialProperties);
        this.factory = factory;
    }


    public static <P> CableBuilder<P> create(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, Material material) {
        return new CableBuilder<>(owner, parent, name, callback, () -> BlockBehaviour.Properties.of(material), new Factory());
    }

    public CableBuilder<P> loss(float loss){
        factory.loss = loss;
        return this;
    }

    private static class Factory implements NonNullFunction<BlockBehaviour.Properties, Cable>
    {
        float loss = 0;

        @NotNull
        @Override
        public Cable apply(@NotNull BlockBehaviour.Properties properties) {
            return new Cable(properties, loss);
        }
    }

}
