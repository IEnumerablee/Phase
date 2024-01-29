package ru.ie.phase.foundation.shapes;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.ie.phase.foundation.net.NetNode;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiPredicate;

public enum NeighborType implements StringRepresentable {

    NONE((block, blockEntity) -> blockEntity == null && (block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR)),
    CABLE((block, blockEntity) -> blockEntity instanceof ICable),
    NODE((block, blockEntity) -> blockEntity instanceof NetNode);

    private final BiPredicate<Block, BlockEntity> checker;

    NeighborType(BiPredicate<Block, BlockEntity> checker){
        this.checker = checker;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public static NeighborType getType(Block block, BlockEntity entity){
        return Arrays.stream(values())
                .filter(neighborType -> neighborType.checker.test(block, entity))
                .findFirst()
                .orElse(NONE);
    }

}
