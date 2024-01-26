package ru.ie.phase.content.blocks.cable;

import com.mojang.math.Vector3f;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.ie.phase.content.blocks.generic.AbstractNodeBlock;
import ru.ie.phase.foundation.shapes.ShapeMapper;
import ru.ie.phase.foundation.shapes.ShapeProvider;
import ru.ie.phase.foundation.shapes.NeighborType;
import ru.ie.phase.foundation.net.ElectricalNetSpace;
import ru.ie.phase.foundation.net.ICable;
import ru.ie.phase.foundation.net.NetIndexed;
import ru.ie.phase.foundation.net.NetNode;
import ru.ie.phase.foundation.shapes.ShapeProvidersRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cable extends AbstractNodeBlock implements EntityBlock {

    private final float loss;

    private ShapeMapper<VoxelShape> shapeMapper;

    public static final EnumProperty<NeighborType> NORTH = EnumProperty.create("north", NeighborType.class);
    public static final EnumProperty<NeighborType> SOUTH = EnumProperty.create("south", NeighborType.class);
    public static final EnumProperty<NeighborType> WEST = EnumProperty.create("west", NeighborType.class);
    public static final EnumProperty<NeighborType> EAST = EnumProperty.create("east", NeighborType.class);
    public static final EnumProperty<NeighborType> UP = EnumProperty.create("up", NeighborType.class);
    public static final EnumProperty<NeighborType> DOWN = EnumProperty.create("down", NeighborType.class);

    public Cable(Properties properties, float loss){
        super(properties);
        this.loss = loss;
        initMapper();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
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

    @Nonnull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return createShape(state);
    }

    @Nonnull
    @Override
    public BlockState updateShape(@NotNull BlockState state, @Nonnull Direction direction, @Nonnull BlockState neighbourState,
                                           @Nonnull LevelAccessor level, @Nonnull BlockPos current, @Nonnull BlockPos offset) {
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

    private VoxelShape combine(List<VoxelShape> shapes){
        VoxelShape mainShape = null;

        for(VoxelShape shape : shapes){
            if(mainShape == null){
                mainShape = shape;
                continue;
            }

            mainShape = Shapes.join(mainShape, shape, BooleanOp.OR);
        }

        return mainShape;
    }

    private VoxelShape createShape(BlockState state){
        Map<Direction, NeighborType> sidesMap = new HashMap<>();

        sidesMap.put(Direction.NORTH, state.getValue(Cable.NORTH));
        sidesMap.put(Direction.SOUTH, state.getValue(Cable.SOUTH));
        sidesMap.put(Direction.WEST, state.getValue(Cable.WEST));
        sidesMap.put(Direction.EAST, state.getValue(Cable.EAST));
        sidesMap.put(Direction.UP, state.getValue(Cable.UP));
        sidesMap.put(Direction.DOWN, state.getValue(Cable.DOWN));

        List<VoxelShape> shapes = new ArrayList<>(shapeMapper.get(sidesMap));
        return combine(shapes);
    }

    private void initMapper(){
        shapeMapper = new ShapeMapper<>(ShapeProvidersRegistry.get("cable"), (cube, context) ->{
                Vector3f pos = cube.pos();
                Vector3f size = cube.size();
                return Shapes.box(pos.x(), pos.y(), pos.z(), pos.x() + size.x(), pos.y() + size.y(), pos.z() + size.z());
        });
    }
}
