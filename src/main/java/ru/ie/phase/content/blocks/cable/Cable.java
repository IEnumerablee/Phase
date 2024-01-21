package ru.ie.phase.content.blocks.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ru.ie.phase.content.blocks.generic.AbstractNodeBlock;
import ru.ie.phase.foundation.net.ElectricalNetSpace;
import ru.ie.phase.foundation.net.ICable;
import ru.ie.phase.foundation.net.NetIndexed;
import ru.ie.phase.foundation.net.NetNode;

public class Cable extends AbstractNodeBlock implements EntityBlock {

    private final float loss;

    public Cable(Properties properties, float loss){
        super(properties);
        this.loss = loss;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return CableEntity.create(pos, state, loss);
    }

    @Override
    protected void connect(NetIndexed neighbor, NetIndexed me) {
        if(neighbor instanceof ICable)
            ElectricalNetSpace.connectCable2Cable(me.getId(), neighbor.getId());
        else if(neighbor instanceof NetNode)
            ElectricalNetSpace.connectCable2Node(me.getId(), neighbor.getId());
    }

    @Override
    protected void remove(NetIndexed me) {
        ElectricalNetSpace.removeCable(me.getId());
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        InteractionResult result = super.use(p_60503_, level, pos, player, p_60507_, p_60508_);
        if(level.isClientSide) return  result;

        ICable cable = (ICable) level.getBlockEntity(pos);

        String id = cable.getId().toString();

        player.sendMessage(Component.nullToEmpty("CABLE - %s".formatted(id)), player.getUUID());
        player.sendMessage(Component.nullToEmpty("Links: %s".formatted(cable.links().size())), player.getUUID());
        player.sendMessage(Component.nullToEmpty("Nodes: %s".formatted(cable.nodes().size())), player.getUUID());

        player.sendMessage(Component.nullToEmpty("Lossmap:"), player.getUUID());
        cable.lossmap().forEach((uuid, aFloat) ->
                player.sendMessage(Component.nullToEmpty("    %s - %s".formatted(aFloat, uuid)), player.getUUID())
        );

        return result;
    }
}
