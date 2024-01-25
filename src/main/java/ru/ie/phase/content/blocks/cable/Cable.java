package ru.ie.phase.content.blocks.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ie.phase.content.blocks.generic.AbstractNodeBlock;
import ru.ie.phase.foundation.net.ElectricalNetSpace;
import ru.ie.phase.foundation.net.ICable;
import ru.ie.phase.foundation.net.NetIndexed;
import ru.ie.phase.foundation.net.NetNode;

import javax.annotation.Nonnull;

public class Cable extends AbstractNodeBlock implements EntityBlock {

    private final float loss;

    public static final EnumProperty<NeighborType> NORTH = EnumProperty.create("north", NeighborType.class);
    public static final EnumProperty<NeighborType> SOUTH = EnumProperty.create("south", NeighborType.class);
    public static final EnumProperty<NeighborType> WEST = EnumProperty.create("west", NeighborType.class);
    public static final EnumProperty<NeighborType> EAST = EnumProperty.create("east", NeighborType.class);
    public static final EnumProperty<NeighborType> UP = EnumProperty.create("up", NeighborType.class);
    public static final EnumProperty<NeighborType> DOWN = EnumProperty.create("down", NeighborType.class);

    private boolean isChangingState = false;


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
    public InteractionResult use(BlockState p_60503_, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        InteractionResult result = super.use(p_60503_, level, pos, player, p_60507_, p_60508_);
        if(level.isClientSide) return  result;

        ICable cable = (ICable) level.getBlockEntity(pos);

        BlockState state = level.getBlockState(pos);

        String id = cable.getId().toString();

        player.sendMessage(Component.nullToEmpty("CABLE - %s".formatted(id)), player.getUUID());
        player.sendMessage(Component.nullToEmpty("Links: %s".formatted(cable.links().size())), player.getUUID());
        player.sendMessage(Component.nullToEmpty("Nodes: %s".formatted(cable.nodes().size())), player.getUUID());

        player.sendMessage(Component.nullToEmpty("Lossmap:"), player.getUUID());
        cable.lossmap().forEach((uuid, aFloat) ->
                player.sendMessage(Component.nullToEmpty("    %s - %s".formatted(aFloat, uuid)), player.getUUID())
        );

        player.sendMessage(Component.nullToEmpty(state.toString()), player.getUUID());

        return result;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @Nonnull Direction direction, @Nonnull BlockState neighbourState,
                                           @Nonnull LevelAccessor level, @Nonnull BlockPos current, @Nonnull BlockPos offset) {
        isChangingState = true;
        return createState(level, current, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return createState(level, pos, defaultBlockState());
    }

    @Override
    protected void connect(NetIndexed neighbor, NetIndexed me, ConnectDirection dir) {
        if(neighbor instanceof ICable)
            ElectricalNetSpace.connectCable2Cable(me.getId(), neighbor.getId(), dir);
        else if(neighbor instanceof NetNode)
            ElectricalNetSpace.connectCable2Node(me.getId(), neighbor.getId(), dir);
    }

    @Override
    protected void remove(NetIndexed me) {
            ElectricalNetSpace.removeCable(me.getId());
    }

    private NeighborType getNeighborType(LevelAccessor level, BlockPos thisPos, Direction dir) {
        BlockPos neighborPos = thisPos.relative(dir);
        BlockState state = level.getBlockState(neighborPos);
        Block block = state.getBlock();
        return getNeighborType(block, level.getBlockEntity(neighborPos));
    }

    private NeighborType getNeighborType(Block block, BlockEntity blockEntity){
        return NeighborType.getType(block, blockEntity);
    }

    private BlockState createState(LevelAccessor level, BlockPos pos, BlockState state) {
        NeighborType north = getNeighborType(level, pos, Direction.NORTH);
        NeighborType south = getNeighborType(level, pos, Direction.SOUTH);
        NeighborType west = getNeighborType(level, pos, Direction.WEST);
        NeighborType east = getNeighborType(level, pos, Direction.EAST);
        NeighborType up = getNeighborType(level, pos, Direction.UP);
        NeighborType down = getNeighborType(level, pos, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }

}
