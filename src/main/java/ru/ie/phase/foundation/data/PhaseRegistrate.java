package ru.ie.phase.foundation.data;

import com.tterrag.registrate.AbstractRegistrate;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;

public class PhaseRegistrate extends AbstractRegistrate<PhaseRegistrate> {

    protected PhaseRegistrate(String modid) {
        super(modid);
    }

    public static PhaseRegistrate create(String modid){
        return new PhaseRegistrate(modid);
    }

    @NotNull
    public PhaseRegistrate registerEventListeners(@NotNull IEventBus bus) {
        return super.registerEventListeners(bus);
    }

    public CableBuilder<PhaseRegistrate> cable(String name, String resourceId, Material material, float loss) {
        return this.cable(this.self(), name, resourceId, material, loss);
    }

    public <P> CableBuilder<P> cable(P parent, String name, String resourceId, Material material, float loss) {
        return (CableBuilder<P>)this.entry(name, (callback) -> CableBuilder.create(this, parent, name, callback, material, loss, resourceId));
    }
}
