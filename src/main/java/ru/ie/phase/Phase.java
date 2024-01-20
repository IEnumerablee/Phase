package ru.ie.phase;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import ru.ie.phase.declaration.BlockEntities;
import ru.ie.phase.declaration.Blocks;
import ru.ie.phase.foundation.net.data.PhaseRegistrate;

@Mod("phase")
public class Phase {

    public static final String MODID = "phase";

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final PhaseRegistrate REGISTRATE = PhaseRegistrate.create(MODID);

    public Phase() {
        init();
    }

    private void init()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        Blocks.register();
        BlockEntities.register();
    }

}
