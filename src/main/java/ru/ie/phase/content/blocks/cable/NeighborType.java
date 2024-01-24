package ru.ie.phase.content.blocks.cable;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import ru.ie.phase.foundation.net.ICable;
import ru.ie.phase.foundation.net.NetNode;

import java.util.Locale;
import java.util.function.BiPredicate;

public enum NeighborType implements StringRepresentable {

    NONE((block, blockEntity) -> blockEntity == null && (block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR)),
    CABLE((block, blockEntity) -> blockEntity instanceof ICable),
    NODE((block, blockEntity) -> blockEntity instanceof NetNode),
    BLOCK((block, blockEntity) -> block != Blocks.AIR && block != Blocks.CAVE_AIR && block != Blocks.VOID_AIR);

    private final BiPredicate<Block, BlockEntity> checker;

    NeighborType(BiPredicate<Block, BlockEntity> checker){
        this.checker = checker;
    }

    public boolean check(Block block, BlockEntity blockEntity){
        return checker.test(block, blockEntity);
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }
}
