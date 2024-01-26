package ru.ie.phase.foundation.net;

import net.minecraft.core.BlockPos;

public enum ConnectDirection {
    NORTH {
        @Override
        public ConnectDirection invert() {
            return SOUTH;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getZ() > p2.getZ();
        }
    },
    SOUTH {
        @Override
        public ConnectDirection invert() {
            return NORTH;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getZ() < p2.getZ();
        }
    },
    WEST {
        @Override
        public ConnectDirection invert() {
            return EAST;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getX() > p2.getX();
        }
    },
    EAST {
        @Override
        public ConnectDirection invert() {
            return WEST;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getX() < p2.getX();
        }
    },
    UP {
        @Override
        public ConnectDirection invert() {
            return DOWN;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getY() < p2.getY();
        }
    },
    DOWN {
        @Override
        public ConnectDirection invert() {
            return UP;
        }

        @Override
        public boolean check(BlockPos p1, BlockPos p2) {
            return p1.getY() > p2.getY();
        }
    };

    public abstract ConnectDirection invert();

    public abstract boolean check(BlockPos p1, BlockPos p2);

    public static ConnectDirection getDir(BlockPos p1, BlockPos p2){
        for(ConnectDirection d : ConnectDirection.values())
            if(d.check(p1, p2)) return d;
        throw new IllegalArgumentException("Positions cannot be the equal");
    }

}
