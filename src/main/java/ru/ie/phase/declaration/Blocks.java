package ru.ie.phase.declaration;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import ru.ie.phase.Phase;
import ru.ie.phase.content.blocks.cable.Cable;
import ru.ie.phase.content.blocks.cable.CableModelLoader;
import ru.ie.phase.content.blocks.consumer.Consumer;
import ru.ie.phase.content.blocks.generator.Generator;

public class Blocks {

    public final static BlockEntry<Cable> CABLE = Phase.REGISTRATE.cable("cable", Material.METAL)
            .loss(0.1f)
            .defaultLoot()
            .blockstate((context, provider) ->{

                BlockModelBuilder cableModel = provider.models().getBuilder(Blocks.CABLE.getId().getPath())
                        .parent(provider.models().getExistingFile(provider.mcLoc("cube")))
                        .customLoader((blockModelBuilder, helper) -> new CustomLoaderBuilder<BlockModelBuilder>(CableModelLoader.LOADER, blockModelBuilder, helper) { })
                        .end();

                provider.simpleBlock(context.get(), cableModel);

            })
            .simpleItem()
            .register();
    public final static BlockEntry<Generator> GENERATOR = Phase.REGISTRATE.block("generator", Generator::new)
            .defaultBlockstate()
            .defaultLoot()
            .simpleItem()
            .register();
    public final static BlockEntry<Consumer> CONSUMER = Phase.REGISTRATE.block("consumer", Consumer::new)
            .defaultBlockstate()
            .defaultLoot()
            .simpleItem()
            .register();

    public static void register(){}

}