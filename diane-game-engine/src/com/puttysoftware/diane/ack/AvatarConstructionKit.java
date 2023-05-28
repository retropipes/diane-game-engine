/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.ack;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.swing.JColorChooser;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.asset.image.ColorReplaceRules;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.internal.AvatarColors;
import com.puttysoftware.diane.internal.AvatarImageLoader;
import com.puttysoftware.diane.random.RandomRange;

public class AvatarConstructionKit {
    // Fields
    private static final int randomFamilyID = RandomRange.generate(0, 15);
    private static final Color randomSkinColor = new Color(RandomRange.generate(0, 255), RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255));
    private static final Color randomHairColor = new Color(RandomRange.generate(0, 255), RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255));
    private static final Color randomTorsoColor = new Color(RandomRange.generate(0, 255), RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255));
    private static final Color randomLegsColor = new Color(RandomRange.generate(0, 255), RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255));
    private static final Color randomEyesColor = new Color(RandomRange.generate(0, 255), RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255));
    private static final Color randomFeetColor = new Color(RandomRange.generate(0, 255), RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255));
    private static final Color randomWeaponColor1 = new Color(RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255), RandomRange.generate(0, 255));
    private static final Color randomWeaponColor2 = new Color(RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255), RandomRange.generate(0, 255));
    private static final Color randomAccessoryColor1 = new Color(RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255), RandomRange.generate(0, 255));
    private static final Color randomAccessoryColor2 = new Color(RandomRange.generate(0, 255),
	    RandomRange.generate(0, 255), RandomRange.generate(0, 255));
    private static final int randomWeaponID = RandomRange.generate(0, 3);
    private static final int randomAccessoryID = RandomRange.generate(0, 3);
    private static int currentFamilyID = AvatarConstructionKit.randomFamilyID;
    private static Color currentSkinColor = AvatarConstructionKit.randomSkinColor;
    private static Color currentHairColor = AvatarConstructionKit.randomHairColor;
    private static Color currentTorsoColor = AvatarConstructionKit.randomTorsoColor;
    private static Color currentLegsColor = AvatarConstructionKit.randomLegsColor;
    private static Color currentEyesColor = AvatarConstructionKit.randomEyesColor;
    private static Color currentFeetColor = AvatarConstructionKit.randomFeetColor;
    private static Color currentWeaponColor1 = AvatarConstructionKit.randomWeaponColor1;
    private static Color currentWeaponColor2 = AvatarConstructionKit.randomWeaponColor2;
    private static Color currentAccessoryColor1 = AvatarConstructionKit.randomAccessoryColor1;
    private static Color currentAccessoryColor2 = AvatarConstructionKit.randomAccessoryColor2;
    private static int currentWeaponID = AvatarConstructionKit.randomWeaponID;
    private static int currentAccessoryID = AvatarConstructionKit.randomAccessoryID;
    private static int miscBits = 0;
    private static final ColorReplaceRules rules = new ColorReplaceRules();
    private static final ColorReplaceRules weaponRules = new ColorReplaceRules();
    private static final ColorReplaceRules accessoryRules = new ColorReplaceRules();
    private static final Pattern avatarIDRegex = Pattern.compile("^[0-9A-Fa-f]{64}$");

    public static AvatarImageModel constructAvatar() {
	// Populate rules
	AvatarConstructionKit.rules.add(AvatarColors.hairBase, AvatarConstructionKit.currentHairColor);
	AvatarConstructionKit.rules.add(AvatarColors.skinBase, AvatarConstructionKit.currentSkinColor);
	AvatarConstructionKit.rules.add(AvatarColors.bodyBase, AvatarConstructionKit.currentTorsoColor);
	AvatarConstructionKit.rules.add(AvatarColors.pantsBase, AvatarConstructionKit.currentLegsColor);
	AvatarConstructionKit.rules.add(AvatarColors.shoesBase, AvatarConstructionKit.currentFeetColor);
	AvatarConstructionKit.rules.add(AvatarColors.eyesBase, AvatarConstructionKit.currentEyesColor);
	AvatarConstructionKit.weaponRules.add(AvatarColors.weapon1Base, AvatarConstructionKit.currentWeaponColor1);
	AvatarConstructionKit.weaponRules.add(AvatarColors.weapon2Base, AvatarConstructionKit.currentWeaponColor2);
	AvatarConstructionKit.accessoryRules.add(AvatarColors.accessory1Base,
		AvatarConstructionKit.currentAccessoryColor1);
	AvatarConstructionKit.accessoryRules.add(AvatarColors.accessory2Base,
		AvatarConstructionKit.currentAccessoryColor2);
	// Construct avatar
	AvatarConstructionKit.currentFamilyID = AvatarConstructionKit.pickAvatarFamily();
	if (AvatarConstructionKit.currentFamilyID == CommonDialogs.CANCEL) {
	    return null;
	}
	AvatarConstructionKit.currentSkinColor = AvatarConstructionKit.pickAvatarSkinColor();
	if (AvatarConstructionKit.currentSkinColor == null) {
	    return null;
	}
	AvatarConstructionKit.currentHairColor = AvatarConstructionKit.pickAvatarHairColor();
	if (AvatarConstructionKit.currentHairColor == null) {
	    return null;
	}
	AvatarConstructionKit.currentEyesColor = AvatarConstructionKit.pickAvatarEyesColor();
	if (AvatarConstructionKit.currentEyesColor == null) {
	    return null;
	}
	AvatarConstructionKit.currentTorsoColor = AvatarConstructionKit.pickAvatarTorsoColor();
	if (AvatarConstructionKit.currentTorsoColor == null) {
	    return null;
	}
	AvatarConstructionKit.currentLegsColor = AvatarConstructionKit.pickAvatarLegsColor();
	if (AvatarConstructionKit.currentLegsColor == null) {
	    return null;
	}
	AvatarConstructionKit.currentFeetColor = AvatarConstructionKit.pickAvatarFeetColor();
	if (AvatarConstructionKit.currentFeetColor == null) {
	    return null;
	}
	AvatarConstructionKit.currentWeaponID = AvatarConstructionKit.pickAvatarWeapon();
	if (AvatarConstructionKit.currentWeaponID == CommonDialogs.CANCEL) {
	    return null;
	}
	AvatarConstructionKit.currentWeaponColor1 = AvatarConstructionKit.pickAvatarWeaponColor1();
	if (AvatarConstructionKit.currentWeaponColor1 == null) {
	    return null;
	}
	AvatarConstructionKit.currentWeaponColor2 = AvatarConstructionKit.pickAvatarWeaponColor2();
	if (AvatarConstructionKit.currentWeaponColor2 == null) {
	    return null;
	}
	AvatarConstructionKit.currentAccessoryID = AvatarConstructionKit.pickAvatarAccessory();
	if (AvatarConstructionKit.currentAccessoryID == CommonDialogs.CANCEL) {
	    return null;
	}
	AvatarConstructionKit.currentAccessoryColor1 = AvatarConstructionKit.pickAvatarAccessoryColor1();
	if (AvatarConstructionKit.currentAccessoryColor1 == null) {
	    return null;
	}
	AvatarConstructionKit.currentAccessoryColor2 = AvatarConstructionKit.pickAvatarAccessoryColor2();
	if (AvatarConstructionKit.currentAccessoryColor2 == null) {
	    return null;
	}
	return new AvatarImageModel(AvatarConstructionKit.currentFamilyID, AvatarConstructionKit.currentWeaponID,
		AvatarConstructionKit.currentAccessoryID, AvatarConstructionKit.miscBits,
		AvatarConstructionKit.currentHairColor, AvatarConstructionKit.currentSkinColor,
		AvatarConstructionKit.currentTorsoColor, AvatarConstructionKit.currentLegsColor,
		AvatarConstructionKit.currentFeetColor, AvatarConstructionKit.currentEyesColor,
		AvatarConstructionKit.currentWeaponColor1, AvatarConstructionKit.currentWeaponColor2,
		AvatarConstructionKit.currentAccessoryColor1, AvatarConstructionKit.currentAccessoryColor2);
    }

    public static AvatarImageModel constructFromAvatarID(final String avatarID) {
	// Sanity check
	if (avatarID == null || !AvatarConstructionKit.avatarIDRegex.matcher(avatarID).matches()) {
	    throw new IllegalArgumentException();
	}
	// Decode avatar ID
	AvatarConstructionKit.currentFamilyID = Integer.parseInt(avatarID.substring(0, 1), 16);
	final var accessoryWeapon = Integer.parseInt(avatarID.substring(1, 2), 16);
	AvatarConstructionKit.currentAccessoryID = accessoryWeapon / 4;
	AvatarConstructionKit.currentWeaponID = accessoryWeapon % 4;
	AvatarConstructionKit.currentHairColor = new Color(Integer.parseInt(avatarID.substring(2, 8), 16), false);
	AvatarConstructionKit.currentSkinColor = new Color(Integer.parseInt(avatarID.substring(8, 14), 16), false);
	AvatarConstructionKit.currentTorsoColor = new Color(Integer.parseInt(avatarID.substring(14, 20), 16), false);
	AvatarConstructionKit.currentLegsColor = new Color(Integer.parseInt(avatarID.substring(20, 26), 16), false);
	AvatarConstructionKit.currentFeetColor = new Color(Integer.parseInt(avatarID.substring(26, 32), 16), false);
	AvatarConstructionKit.currentEyesColor = new Color(Integer.parseInt(avatarID.substring(32, 38), 16), false);
	AvatarConstructionKit.currentWeaponColor1 = new Color(Integer.parseInt(avatarID.substring(38, 44), 16), false);
	AvatarConstructionKit.currentWeaponColor2 = new Color(Integer.parseInt(avatarID.substring(44, 50), 16), false);
	AvatarConstructionKit.currentAccessoryColor1 = new Color(Integer.parseInt(avatarID.substring(50, 56), 16),
		false);
	AvatarConstructionKit.currentAccessoryColor2 = new Color(Integer.parseInt(avatarID.substring(56, 62), 16),
		false);
	AvatarConstructionKit.miscBits = Integer.parseInt(avatarID.substring(62, 64), 16);
	// Populate rules
	AvatarConstructionKit.rules.add(AvatarColors.hairBase, AvatarConstructionKit.currentHairColor);
	AvatarConstructionKit.rules.add(AvatarColors.skinBase, AvatarConstructionKit.currentSkinColor);
	AvatarConstructionKit.rules.add(AvatarColors.bodyBase, AvatarConstructionKit.currentTorsoColor);
	AvatarConstructionKit.rules.add(AvatarColors.pantsBase, AvatarConstructionKit.currentLegsColor);
	AvatarConstructionKit.rules.add(AvatarColors.shoesBase, AvatarConstructionKit.currentFeetColor);
	AvatarConstructionKit.rules.add(AvatarColors.eyesBase, AvatarConstructionKit.currentEyesColor);
	AvatarConstructionKit.weaponRules.add(AvatarColors.weapon1Base, AvatarConstructionKit.currentWeaponColor1);
	AvatarConstructionKit.weaponRules.add(AvatarColors.weapon2Base, AvatarConstructionKit.currentWeaponColor2);
	AvatarConstructionKit.accessoryRules.add(AvatarColors.accessory1Base,
		AvatarConstructionKit.currentAccessoryColor1);
	AvatarConstructionKit.accessoryRules.add(AvatarColors.accessory2Base,
		AvatarConstructionKit.currentAccessoryColor2);
	// Construct avatar
	return new AvatarImageModel(AvatarConstructionKit.currentFamilyID, AvatarConstructionKit.currentWeaponID,
		AvatarConstructionKit.currentAccessoryID, AvatarConstructionKit.miscBits,
		AvatarConstructionKit.currentHairColor, AvatarConstructionKit.currentSkinColor,
		AvatarConstructionKit.currentTorsoColor, AvatarConstructionKit.currentLegsColor,
		AvatarConstructionKit.currentFeetColor, AvatarConstructionKit.currentEyesColor,
		AvatarConstructionKit.currentWeaponColor1, AvatarConstructionKit.currentWeaponColor2,
		AvatarConstructionKit.currentAccessoryColor1, AvatarConstructionKit.currentAccessoryColor2);
    }

    private static int pickAvatarAccessory() {
	final var labelText = AckStrings.load(4);
	final var title = AckStrings.load(5);
	final BufferedImageIcon[] input = { AvatarImageLoader.loadAccessory(0, AvatarConstructionKit.accessoryRules),
		AvatarImageLoader.loadAccessory(1, AvatarConstructionKit.accessoryRules),
		AvatarImageLoader.loadAccessory(2, AvatarConstructionKit.accessoryRules),
		AvatarImageLoader.loadAccessory(3, AvatarConstructionKit.accessoryRules) };
	final String[] descriptions = { AckStrings.load(20), AckStrings.load(21), AckStrings.load(22),
		AckStrings.load(23) };
	return CommonDialogs.showImageListWithDescDialog(labelText, title, input, 0, descriptions[0], descriptions);
    }

    private static Color pickAvatarAccessoryColor1() {
	final var title = AckStrings.load(14);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentAccessoryColor1, false);
    }

    private static Color pickAvatarAccessoryColor2() {
	final var title = AckStrings.load(15);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentAccessoryColor2, false);
    }

    private static Color pickAvatarEyesColor() {
	final var title = AckStrings.load(8);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentEyesColor, false);
    }

    private static int pickAvatarFamily() {
	final var labelText = AckStrings.load(0);
	final var title = AckStrings.load(1);
	final BufferedImageIcon[] input = { AvatarImageLoader.load(0, AvatarConstructionKit.rules),
		AvatarImageLoader.load(1, AvatarConstructionKit.rules),
		AvatarImageLoader.load(2, AvatarConstructionKit.rules),
		AvatarImageLoader.load(3, AvatarConstructionKit.rules),
		AvatarImageLoader.load(4, AvatarConstructionKit.rules),
		AvatarImageLoader.load(5, AvatarConstructionKit.rules),
		AvatarImageLoader.load(6, AvatarConstructionKit.rules),
		AvatarImageLoader.load(7, AvatarConstructionKit.rules),
		AvatarImageLoader.load(8, AvatarConstructionKit.rules),
		AvatarImageLoader.load(9, AvatarConstructionKit.rules),
		AvatarImageLoader.load(10, AvatarConstructionKit.rules),
		AvatarImageLoader.load(11, AvatarConstructionKit.rules),
		AvatarImageLoader.load(12, AvatarConstructionKit.rules),
		AvatarImageLoader.load(13, AvatarConstructionKit.rules),
		AvatarImageLoader.load(14, AvatarConstructionKit.rules),
		AvatarImageLoader.load(15, AvatarConstructionKit.rules) };
	return CommonDialogs.showImageListDialog(labelText, title, input, AvatarConstructionKit.currentFamilyID);
    }

    private static Color pickAvatarFeetColor() {
	final var title = AckStrings.load(11);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentFeetColor, false);
    }

    private static Color pickAvatarHairColor() {
	final var title = AckStrings.load(7);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentHairColor, false);
    }

    private static Color pickAvatarLegsColor() {
	final var title = AckStrings.load(10);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentLegsColor, false);
    }

    private static Color pickAvatarSkinColor() {
	final var title = AckStrings.load(6);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentSkinColor, false);
    }

    private static Color pickAvatarTorsoColor() {
	final var title = AckStrings.load(9);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentTorsoColor, false);
    }

    private static int pickAvatarWeapon() {
	final var labelText = AckStrings.load(2);
	final var title = AckStrings.load(3);
	final BufferedImageIcon[] input = { AvatarImageLoader.loadWeapon(0, AvatarConstructionKit.weaponRules),
		AvatarImageLoader.loadWeapon(1, AvatarConstructionKit.weaponRules),
		AvatarImageLoader.loadWeapon(2, AvatarConstructionKit.weaponRules),
		AvatarImageLoader.loadWeapon(3, AvatarConstructionKit.weaponRules) };
	final String[] descriptions = { AckStrings.load(16), AckStrings.load(17), AckStrings.load(18),
		AckStrings.load(19) };
	return CommonDialogs.showImageListWithDescDialog(labelText, title, input, 0, descriptions[0], descriptions);
    }

    private static Color pickAvatarWeaponColor1() {
	final var title = AckStrings.load(12);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentWeaponColor1, false);
    }

    private static Color pickAvatarWeaponColor2() {
	final var title = AckStrings.load(13);
	return JColorChooser.showDialog(null, title, AvatarConstructionKit.currentWeaponColor2, false);
    }

    // Constructors
    private AvatarConstructionKit() {
	// Do nothing
    }
}
