package ru.ie.phase.content.blocks.cable;

public enum Direction {
    NORTH {
        @Override
        public Direction invert() {
            return WEST;
        }
    },
    SOUTH {
        @Override
        public Direction invert() {
            return EAST;
        }
    },
    WEST {
        @Override
        public Direction invert() {
            return NORTH;
        }
    },
    EAST {
        @Override
        public Direction invert() {
            return SOUTH;
        }
    },
    UP {
        @Override
        public Direction invert() {
            return DOWN;
        }
    },
    DOWN {
        @Override
        public Direction invert() {
            return UP;
        }
    };

    public abstract Direction invert();

}
