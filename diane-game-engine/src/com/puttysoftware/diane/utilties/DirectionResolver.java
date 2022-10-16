/*  DungeonDiver7: A Dungeon-Diving RPG
 Copyright (C) 2021-present Eric Ahnell

 Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.diane.utilties;

public class DirectionResolver {
    public static final int COUNT = 8;

    public static Directions resolve(final int dX, final int dY) {
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Directions.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Directions.NORTH;
	} else if (dirX == 0 && dirY == 1) {
	    return Directions.SOUTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Directions.WEST;
	} else if (dirX == 1 && dirY == 0) {
	    return Directions.EAST;
	} else if (dirX == 1 && dirY == 1) {
	    return Directions.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Directions.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Directions.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Directions.NORTHEAST;
	} else {
	    return Directions.INVALID;
	}
    }

    public static Directions resolveHV(final int dX, final int dY) {
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Directions.NONE;
	} else if ((dirX == 0 && dirY == -1) || (dirX == 0 && dirY == 1)) {
	    return Directions.VERTICAL;
	} else if (dirX == -1 && dirY == 0) {
	    return Directions.HORIZONTAL;
	} else if (dirX == 1 && dirY == 0) {
	    return Directions.HORIZONTAL;
	} else if (dirX == 1 && dirY == 1) {
	    return Directions.SOUTHEAST;
	} else if (dirX == -1 && dirY == 1) {
	    return Directions.SOUTHWEST;
	} else if (dirX == -1 && dirY == -1) {
	    return Directions.NORTHWEST;
	} else if (dirX == 1 && dirY == -1) {
	    return Directions.NORTHEAST;
	} else {
	    return Directions.INVALID;
	}
    }

    public static Directions resolveInvert(final int dX, final int dY) {
	final var dirX = (int) Math.signum(dX);
	final var dirY = (int) Math.signum(dY);
	if (dirX == 0 && dirY == 0) {
	    return Directions.NONE;
	} else if (dirX == 0 && dirY == -1) {
	    return Directions.SOUTH;
	} else if (dirX == 0 && dirY == 1) {
	    return Directions.NORTH;
	} else if (dirX == -1 && dirY == 0) {
	    return Directions.EAST;
	} else if (dirX == 1 && dirY == 0) {
	    return Directions.WEST;
	} else if (dirX == 1 && dirY == 1) {
	    return Directions.NORTHWEST;
	} else if (dirX == -1 && dirY == 1) {
	    return Directions.NORTHEAST;
	} else if (dirX == -1 && dirY == -1) {
	    return Directions.SOUTHEAST;
	} else if (dirX == 1 && dirY == -1) {
	    return Directions.SOUTHWEST;
	} else {
	    return Directions.INVALID;
	}
    }

    public static int[] unresolve(final Directions dir) {
	var res = new int[2];
	if (dir == Directions.NONE) {
	    res[0] = 0;
	    res[1] = 0;
	} else if (dir == Directions.NORTH) {
	    res[0] = 0;
	    res[1] = -1;
	} else if (dir == Directions.SOUTH) {
	    res[0] = 0;
	    res[1] = 1;
	} else if (dir == Directions.WEST) {
	    res[0] = -1;
	    res[1] = 0;
	} else if (dir == Directions.EAST) {
	    res[0] = 1;
	    res[1] = 0;
	} else if (dir == Directions.SOUTHEAST) {
	    res[0] = 1;
	    res[1] = 1;
	} else if (dir == Directions.SOUTHWEST) {
	    res[0] = -1;
	    res[1] = 1;
	} else if (dir == Directions.NORTHWEST) {
	    res[0] = -1;
	    res[1] = -1;
	} else if (dir == Directions.NORTHEAST) {
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
