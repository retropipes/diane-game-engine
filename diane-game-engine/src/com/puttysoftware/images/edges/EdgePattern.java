package com.puttysoftware.images.edges;

import java.awt.Color;

public class EdgePattern {
    // Fields
    private final EdgePosition position;
    private final EdgeDirection direction;
    private final EdgeType type;
    private final Color color;
    private final boolean inverted;

    // Constructor
    public EdgePattern(final EdgePosition newPos, final EdgeDirection newDir,
            final EdgeType newType, final Color newColor,
            final boolean invert) {
        this.direction = newDir;
        this.position = newPos;
        this.type = newType;
        this.color = newColor;
        this.inverted = invert;
    }

    public EdgePosition getPosition() {
        return this.position;
    }

    public EdgeDirection getDirection() {
        return this.direction;
    }

    public EdgeType getType() {
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isInverted() {
        return this.inverted;
    }
}
