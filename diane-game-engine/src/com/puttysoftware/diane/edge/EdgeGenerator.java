/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.edge;

import java.awt.Color;

import com.puttysoftware.diane.asset.BufferedImageIcon;

public class EdgeGenerator {
    // Constants
    private static final String[] NAME_SUFFIXES = { "_south", //$NON-NLS-1$
	    "_east", "_southeast", "_northeast", "_southwest", "_northwest", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	    "_north", "_west", "_southeast_inverted", "_northeast_inverted", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    "_southwest_inverted", "_northwest_inverted", }; //$NON-NLS-1$ //$NON-NLS-2$
    private static final String[] FRIENDLY_NAME_SUFFIXES = { "South", //$NON-NLS-1$
	    "East", "Southeast", "Northeast", "Southwest", "Northwest", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	    "North", "West", "Southeast Inverted", "Northeast Inverted", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	    "Southwest Inverted", "Northwest Inverted", }; //$NON-NLS-1$ //$NON-NLS-2$

    public static String[] generateAllEdgedImageFriendlyNameSuffixes() {
	return EdgeGenerator.FRIENDLY_NAME_SUFFIXES;
    }

    public static String[] generateAllEdgedImageNameSuffixes() {
	return EdgeGenerator.NAME_SUFFIXES;
    }

    public static BufferedImageIcon[] generateAllEdgedImages(final BufferedImageIcon img1, final BufferedImageIcon img2,
	    final Color edgeColor) {
	final EdgePattern[] validPatterns = {
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.HORIZONTAL, EdgeType.LINE, edgeColor, false),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.VERTICAL, EdgeType.LINE, edgeColor, false),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.NORTHWEST, EdgeType.CORNER, edgeColor, false),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.NORTHEAST, EdgeType.CORNER, edgeColor, false),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.SOUTHWEST, EdgeType.CORNER, edgeColor, false),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.SOUTHEAST, EdgeType.CORNER, edgeColor, false),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.HORIZONTAL, EdgeType.LINE, edgeColor, true),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.VERTICAL, EdgeType.LINE, edgeColor, true),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.NORTHWEST, EdgeType.CORNER, edgeColor, true),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.NORTHEAST, EdgeType.CORNER, edgeColor, true),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.SOUTHWEST, EdgeType.CORNER, edgeColor, true),
		new EdgePattern(EdgePosition.MIDDLE, EdgeDirection.SOUTHEAST, EdgeType.CORNER, edgeColor, true) };
	final var results = new BufferedImageIcon[validPatterns.length];
	for (var z = 0; z < results.length; z++) {
	    results[z] = EdgeGenerator.generateEdgedImage(img1, img2, validPatterns[z]);
	}
	return results;
    }

    public static BufferedImageIcon generateEdgedImage(final BufferedImageIcon img1, final BufferedImageIcon img2,
	    final EdgePattern pattern) {
	if (img1 == null || img2 == null) {
	    throw new IllegalArgumentException("Input images must not be null!"); //$NON-NLS-1$
	}
	if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()
		|| img1.getWidth() != img2.getHeight() || img1.getHeight() != img2.getWidth()) {
	    throw new IllegalArgumentException("Input images must be the same size and square!"); //$NON-NLS-1$
	}
	final var result = new BufferedImageIcon(img1);
	final var dist = img1.getHeight();
	final var transWidth = 1 - dist % 2 + 1;
	final var transPoint1 = (int) (dist * pattern.getPosition().getOffset()) - transWidth;
	final var invTransPoint1 = (int) (dist * pattern.getPosition().getOffset()) + transWidth;
	final var type = pattern.getType();
	final var direction = pattern.getDirection();
	for (var x = 0; x < dist; x++) {
	    for (var y = 0; y < dist; y++) {
		switch (type) {
		case LINE:
		    switch (direction) {
		    case HORIZONTAL:
			if (pattern.isInverted()) {
			    if (y <= invTransPoint1) {
				if (y <= invTransPoint1 - transWidth) {
				    result.setRGB(x, y, img2.getRGB(x, y));
				} else {
				    result.setRGB(x, y, pattern.getColor().getRGB());
				}
			    }
			} else if (y >= transPoint1) {
			    if (y >= transPoint1 + transWidth) {
				result.setRGB(x, y, img2.getRGB(x, y));
			    } else {
				result.setRGB(x, y, pattern.getColor().getRGB());
			    }
			}
			break;
		    case VERTICAL:
			if (pattern.isInverted()) {
			    if (x <= invTransPoint1) {
				if (x <= invTransPoint1 - transWidth) {
				    result.setRGB(x, y, img2.getRGB(x, y));
				} else {
				    result.setRGB(x, y, pattern.getColor().getRGB());
				}
			    }
			} else if (x >= transPoint1) {
			    if (x >= transPoint1 + transWidth) {
				result.setRGB(x, y, img2.getRGB(x, y));
			    } else {
				result.setRGB(x, y, pattern.getColor().getRGB());
			    }
			}
			break;
		    default:
			throw new IllegalArgumentException(
				"Pattern is invalid: Direction must be HORIZONTAL or VERTICAL for lines!"); //$NON-NLS-1$
		    }
		    break;
		case CORNER:
		    switch (direction) {
		    case NORTHWEST:
			if (pattern.isInverted()) {
			    if (x <= invTransPoint1 || y <= invTransPoint1) {
				if (x <= invTransPoint1 - transWidth || y <= invTransPoint1 - transWidth) {
				    result.setRGB(x, y, img2.getRGB(x, y));
				} else {
				    result.setRGB(x, y, pattern.getColor().getRGB());
				}
			    }
			} else if (x >= transPoint1 && y >= transPoint1) {
			    if (x >= transPoint1 + transWidth && y >= transPoint1 + transWidth) {
				result.setRGB(x, y, img2.getRGB(x, y));
			    } else {
				result.setRGB(x, y, pattern.getColor().getRGB());
			    }
			}
			break;
		    case NORTHEAST:
			if (pattern.isInverted()) {
			    if (x <= invTransPoint1 || y >= transPoint1) {
				if (x <= invTransPoint1 - transWidth || y >= transPoint1 + transWidth) {
				    result.setRGB(x, y, img2.getRGB(x, y));
				} else {
				    result.setRGB(x, y, pattern.getColor().getRGB());
				}
			    }
			} else if (x >= transPoint1 && y <= invTransPoint1) {
			    if (x >= transPoint1 + transWidth && y <= invTransPoint1 - transWidth) {
				result.setRGB(x, y, img2.getRGB(x, y));
			    } else {
				result.setRGB(x, y, pattern.getColor().getRGB());
			    }
			}
			break;
		    case SOUTHWEST:
			if (pattern.isInverted()) {
			    if (x >= transPoint1 || y <= invTransPoint1) {
				if (x >= transPoint1 + transWidth || y <= invTransPoint1 - transWidth) {
				    result.setRGB(x, y, img2.getRGB(x, y));
				} else {
				    result.setRGB(x, y, pattern.getColor().getRGB());
				}
			    }
			} else if (x <= invTransPoint1 && y >= transPoint1) {
			    if (x <= invTransPoint1 - transWidth && y >= transPoint1 + transWidth) {
				result.setRGB(x, y, img2.getRGB(x, y));
			    } else {
				result.setRGB(x, y, pattern.getColor().getRGB());
			    }
			}
			break;
		    case SOUTHEAST:
			if (pattern.isInverted()) {
			    if (x >= transPoint1 || y >= transPoint1) {
				if (x >= transPoint1 + transWidth || y >= transPoint1 + transWidth) {
				    result.setRGB(x, y, img2.getRGB(x, y));
				} else {
				    result.setRGB(x, y, pattern.getColor().getRGB());
				}
			    }
			} else if (x <= invTransPoint1 && y <= invTransPoint1) {
			    if (x <= invTransPoint1 - transWidth && y <= invTransPoint1 - transWidth) {
				result.setRGB(x, y, img2.getRGB(x, y));
			    } else {
				result.setRGB(x, y, pattern.getColor().getRGB());
			    }
			}
			break;
		    default:
			throw new IllegalArgumentException(
				"Pattern is invalid: Direction must be NORTHWEST, NORTHEAST, SOUTHWEST, or SOUTHEAST for corners!"); //$NON-NLS-1$
		    }
		    break;
		default:
		    throw new IllegalArgumentException(
			    "Pattern is invalid: Type must be HORIZONTAL, VERTICAL, or CORNER!"); //$NON-NLS-1$
		}
	    }
	}
	return result;
    }

    // Constructor
    private EdgeGenerator() {
	// Do nothing
    }
}
