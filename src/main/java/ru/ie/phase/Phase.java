package ru.ie.phase;

import com.mojang.logging.LogUtils;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import ru.ie.phase.content.blocks.cable.CableModelLoader;
import ru.ie.phase.declaration.BlockEntities;
import ru.ie.phase.declaration.Blocks;
import ru.ie.phase.foundation.data.PhaseRegistrate;

@Mod("phase")
@Mod.EventBusSubscriber(modid = Phase.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        Phase.LOGGER.debug("ml e");
        ModelLoaderRegistry.registerLoader(CableModelLoader.LOADER, new CableModelLoader());
    }

}
