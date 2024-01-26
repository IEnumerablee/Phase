package ru.ie.phase.declaration;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import ru.ie.phase.Phase;
import ru.ie.phase.content.blocks.cable.CableEntity;
import ru.ie.phase.content.blocks.consumer.ConsumerEntity;
import ru.ie.phase.content.blocks.generator.GeneratorEntity;

public class BlockEntities {

    public final static BlockEntityEntry<CableEntity> CABLE_ENTITY = Phase.REGISTRATE.blockEntity("cable_entity", CableEntity::new)
            .register();
    public final static BlockEntityEntry<GeneratorEntity> GENERATOR_ENTITY = Phase.REGISTRATE.blockEntity("generator_entity", GeneratorEntity::new)
            .validBlock(Blocks.GENERATOR)
            .register();

    public final static BlockEntityEntry<ConsumerEntity> CINSUMER_ENTITY = Phase.REGISTRATE.blockEntity("consumer_entity", ConsumerEntity::new)
            .validBlock(Blocks.CONSUMER)
            .register();

    public static void register(){}

}
