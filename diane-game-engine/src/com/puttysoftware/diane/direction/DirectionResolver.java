/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.direction;

public class DirectionResolver {
    public static final int COUNT = 8;

    public static Direction resolve(final int dX, final int dY) {
        final var dirX = (int) Math.signum(dX);
        final var dirY = (int) Math.signum(dY);
        if (dirX == 0 && dirY == 0) {
            return Direction.NONE;
        } else if (dirX == 0 && dirY == -1) {
            return Direction.NORTH;
        } else if (dirX == 0 && dirY == 1) {
            return Direction.SOUTH;
        } else if (dirX == -1 && dirY == 0) {
            return Direction.WEST;
        } else if (dirX == 1 && dirY == 0) {
            return Direction.EAST;
        } else if (dirX == 1 && dirY == 1) {
            return Direction.SOUTH_EAST;
        } else if (dirX == -1 && dirY == 1) {
            return Direction.SOUTH_WEST;
        } else if (dirX == -1 && dirY == -1) {
            return Direction.NORTH_WEST;
        } else if (dirX == 1 && dirY == -1) {
            return Direction.NORTH_EAST;
        } else {
            return Direction.NONE;
        }
    }

    public static Direction resolveInvert(final int dX, final int dY) {
        final var dirX = (int) Math.signum(dX);
        final var dirY = (int) Math.signum(dY);
        if (dirX == 0 && dirY == 0) {
            return Direction.NONE;
        } else if (dirX == 0 && dirY == -1) {
            return Direction.SOUTH;
        } else if (dirX == 0 && dirY == 1) {
            return Direction.NORTH;
        } else if (dirX == -1 && dirY == 0) {
            return Direction.EAST;
        } else if (dirX == 1 && dirY == 0) {
            return Direction.WEST;
        } else if (dirX == 1 && dirY == 1) {
            return Direction.NORTH_WEST;
        } else if (dirX == -1 && dirY == 1) {
            return Direction.NORTH_EAST;
        } else if (dirX == -1 && dirY == -1) {
            return Direction.SOUTH_EAST;
        } else if (dirX == 1 && dirY == -1) {
            return Direction.SOUTH_WEST;
        } else {
            return Direction.NONE;
        }
    }

    public static int[] unresolve(final Direction dir) {
        var res = new int[2];
        if (dir == Direction.NONE) {
            res[0] = 0;
            res[1] = 0;
        } else if (dir == Direction.NORTH) {
            res[0] = 0;
            res[1] = -1;
        } else if (dir == Direction.SOUTH) {
            res[0] = 0;
            res[1] = 1;
        } else if (dir == Direction.WEST) {
            res[0] = -1;
            res[1] = 0;
        } else if (dir == Direction.EAST) {
            res[0] = 1;
            res[1] = 0;
        } else if (dir == Direction.SOUTH_EAST) {
            res[0] = 1;
            res[1] = 1;
        } else if (dir == Direction.SOUTH_WEST) {
            res[0] = -1;
            res[1] = 1;
        } else if (dir == Direction.NORTH_WEST) {
            res[0] = -1;
            res[1] = -1;
        } else if (dir == Direction.NORTH_EAST) {
            res[0] = 1;
            res[1] = -1;
        } else {
            res = null;
        }
        return res;
    }

    private DirectionResolver() {
        // Do nothing
    }
}
