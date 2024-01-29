package ru.ie.phase.content.blocks.consumer;

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
import ru.ie.phase.content.blocks.generic.NodeBlock;
import ru.ie.phase.declaration.BlockEntities;
import ru.ie.phase.foundation.net.electrical.NetConsumer;

public class Consumer extends NodeBlock implements EntityBlock {

    public Consumer(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ConsumerEntity(BlockEntities.CINSUMER_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        InteractionResult result = super.use(p_60503_, level, pos, player, p_60507_, p_60508_);
        if(level.isClientSide) return  result;

        NetConsumer consumer = (NetConsumer) level.getBlockEntity(pos);

        String id = consumer.getId().toString();
        float voltage = consumer.getVoltage();
        float amperage = consumer.getAmperage();

        player.sendMessage(Component.nullToEmpty("CONSUMER - %s".formatted(id)), player.getUUID());
        player.sendMessage(Component.nullToEmpty("voltage: %s".formatted(voltage)), player.getUUID());
        player.sendMessage(Component.nullToEmpty("amperage: %s".formatted(amperage)), player.getUUID());
        player.sendMessage(Component.nullToEmpty("power: %s".formatted(voltage * amperage)), player.getUUID());

        return result;
    }

}
