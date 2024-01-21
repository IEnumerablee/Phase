package ru.ie.phase.content.blocks.cable;

import net.minecraft.core.BlockPos;

public enum Direction {
    NORTH {
        @Override
        public Direction invert() {
            return SOUTH;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getZ() > p2.getZ();
        }
    },
    SOUTH {
        @Override
        public Direction invert() {
            return NORTH;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getZ() < p2.getZ();
        }
    },
    WEST {
        @Override
        public Direction invert() {
            return EAST;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getX() > p2.getX();
        }
    },
    EAST {
        @Override
        public Direction invert() {
            return WEST;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getX() < p2.getX();
        }
    },
    UP {
        @Override
        public Direction invert() {
            return DOWN;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getY() < p2.getY();
        }
    },
    DOWN {
        @Override
        public Direction invert() {
            return UP;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getY() > p2.getY();
        }
    };

    public abstract Direction invert();

    public abstract boolean check(BlockPos p1, BlockPos p2);

    public static Direction getDir(BlockPos p1, BlockPos p2){
        for(Direction d : Direction.values())
            if(d.check(p1, p2)) return d;
        throw new IllegalArgumentException("Positions cannot be the equal");
    }

}
