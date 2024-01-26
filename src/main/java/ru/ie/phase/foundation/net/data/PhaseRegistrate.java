package ru.ie.phase.foundation.net.data;

import com.tterrag.registrate.AbstractRegistrate;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;

public class PhaseRegistrate extends AbstractRegistrate<PhaseRegistrate> {

    protected PhaseRegistrate(String modid) {
        super(modid);
    }

    public static PhaseRegistrate create(String modid){
        return new PhaseRegistrate(modid);
    }

    public PhaseRegistrate registerEventListeners(IEventBus bus) {
        return super.registerEventListeners(bus);
    }

    public CableBuilder<PhaseRegistrate> cable(String name, String resourceId, Material material, float loss) {
        return this.cable(this.self(), name, resourceId, material, loss);
    }

    public <P> CableBuilder<P> cable(P parent, String name, String resourceId, Material material, float loss) {
        return (CableBuilder<P>)this.entry(name, (callback) -> CableBuilder.create(this, parent, name, callback, material, loss, resourceId));
    }
}
