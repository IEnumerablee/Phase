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

    public final static BlockEntry<Cable> COPPER_CABLE = Phase.REGISTRATE.cable("copper_cable", "copper", Material.METAL,0.05f)
            .model()
            .simpleItem()
            .defaultLoot()
            .register();

    public final static BlockEntry<Cable> IRON_CABLE = Phase.REGISTRATE.cable("iron_cable", "iron", Material.METAL,0.1f)
            .model()
            .simpleItem()
            .defaultLoot()
            .register();

    public final static BlockEntry<Cable> GOLD_CABLE = Phase.REGISTRATE.cable("gold_cable", "gold", Material.METAL,0.01f)
            .model()
            .simpleItem()
            .defaultLoot()
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