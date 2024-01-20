package ru.ie.phase.declaration;

import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.material.Material;
import ru.ie.phase.Phase;
import ru.ie.phase.content.blocks.cable.Cable;
import ru.ie.phase.content.blocks.consumer.Consumer;
import ru.ie.phase.content.blocks.generator.Generator;

public class Blocks {

    public final static BlockEntry<Cable> CABLE = Phase.REGISTRATE.cable("cable", Material.METAL)
            .loss(0.1f)
            .simpleItem()
            .register();
    public final static BlockEntry<Generator> GENERATOR = Phase.REGISTRATE.block("generator", Generator::new)
            .simpleItem()
            .register();
    public final static BlockEntry<Consumer> CONSUMER = Phase.REGISTRATE.block("consumer", Consumer::new)
            .simpleItem()
            .register();

    public static void register(){}

}