package com.puttysoftware.diane.ack;

import java.awt.Color;
import java.util.regex.Pattern;

import javax.swing.JColorChooser;

import com.puttysoftware.diane.asset.BufferedImageIcon;
import com.puttysoftware.diane.gui.dialog.CommonDialogs;
import com.puttysoftware.diane.internal.AvatarColors;
import com.puttysoftware.diane.internal.AvatarImageLoader;
import com.puttysoftware.diane.loaders.ColorReplaceRules;
import com.puttysoftware.diane.randomrange.RandomRange;

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
    private static int currentFamilyID = randomFamilyID;
    private static Color currentSkinColor = randomSkinColor;
    private static Color currentHairColor = randomHairColor;
    private static Color currentTorsoColor = randomTorsoColor;
    private static Color currentLegsColor = randomLegsColor;
    private static Color currentEyesColor = randomEyesColor;
    private static Color currentFeetColor = randomFeetColor;
    private static Color currentWeaponColor1 = randomWeaponColor1;
    private static Color currentWeaponColor2 = randomWeaponColor2;
    private static Color currentAccessoryColor1 = randomAccessoryColor1;
    private static Color currentAccessoryColor2 = randomAccessoryColor2;
    private static int currentWeaponID = randomWeaponID;
    private static int currentAccessoryID = randomAccessoryID;
    private static int miscBits = 0;
    private static final ColorReplaceRules rules = new ColorReplaceRules();
    private static final ColorReplaceRules weaponRules = new ColorReplaceRules();
    private static final ColorReplaceRules accessoryRules = new ColorReplaceRules();
    private static final Pattern avatarIDRegex = Pattern.compile("^[0-9A-Fa-f]{64}$");

    // Constructors
    private AvatarConstructionKit() {
	// Do nothing
    }

    // Methods
    public static AvatarImageModel constructFromAvatarID(String avatarID) {
	// Sanity check
	if (avatarID == null || !avatarIDRegex.matcher(avatarID).matches()) {
	    throw new IllegalArgumentException();
	}
	// Decode avatar ID
	currentFamilyID = Integer.parseInt(avatarID.substring(0, 1), 16);
	int accessoryWeapon = Integer.parseInt(avatarID.substring(1, 2), 16);
	currentAccessoryID = accessoryWeapon / 4;
	currentWeaponID = accessoryWeapon % 4;
	currentHairColor = new Color(Integer.parseInt(avatarID.substring(2, 8), 16), false);
	currentSkinColor = new Color(Integer.parseInt(avatarID.substring(8, 14), 16), false);
	currentTorsoColor = new Color(Integer.parseInt(avatarID.substring(14, 20), 16), false);
	currentLegsColor = new Color(Integer.parseInt(avatarID.substring(20, 26), 16), false);
	currentFeetColor = new Color(Integer.parseInt(avatarID.substring(26, 32), 16), false);
	currentEyesColor = new Color(Integer.parseInt(avatarID.substring(32, 38), 16), false);
	currentWeaponColor1 = new Color(Integer.parseInt(avatarID.substring(38, 44), 16), false);
	currentWeaponColor2 = new Color(Integer.parseInt(avatarID.substring(44, 50), 16), false);
	currentAccessoryColor1 = new Color(Integer.parseInt(avatarID.substring(50, 56), 16), false);
	currentAccessoryColor2 = new Color(Integer.parseInt(avatarID.substring(56, 62), 16), false);
	miscBits = Integer.parseInt(avatarID.substring(62, 64), 16);
	// Populate rules
	rules.add(AvatarColors.hairBase, currentHairColor);
	rules.add(AvatarColors.skinBase, currentSkinColor);
	rules.add(AvatarColors.bodyBase, currentTorsoColor);
	rules.add(AvatarColors.pantsBase, currentLegsColor);
	rules.add(AvatarColors.shoesBase, currentFeetColor);
	rules.add(AvatarColors.eyesBase, currentEyesColor);
	weaponRules.add(AvatarColors.weapon1Base, currentWeaponColor1);
	weaponRules.add(AvatarColors.weapon2Base, currentWeaponColor2);
	accessoryRules.add(AvatarColors.accessory1Base, currentAccessoryColor1);
	accessoryRules.add(AvatarColors.accessory2Base, currentAccessoryColor2);
	// Construct avatar
	return new AvatarImageModel(currentFamilyID, currentWeaponID, currentAccessoryID, miscBits, currentHairColor,
		currentSkinColor, currentTorsoColor, currentLegsColor, currentFeetColor, currentEyesColor,
		currentWeaponColor1, currentWeaponColor2, currentAccessoryColor1, currentAccessoryColor2);
    }

    public static AvatarImageModel constructAvatar() {
	// Populate rules
	rules.add(AvatarColors.hairBase, currentHairColor);
	rules.add(AvatarColors.skinBase, currentSkinColor);
	rules.add(AvatarColors.bodyBase, currentTorsoColor);
	rules.add(AvatarColors.pantsBase, currentLegsColor);
	rules.add(AvatarColors.shoesBase, currentFeetColor);
	rules.add(AvatarColors.eyesBase, currentEyesColor);
	weaponRules.add(AvatarColors.weapon1Base, currentWeaponColor1);
	weaponRules.add(AvatarColors.weapon2Base, currentWeaponColor2);
	accessoryRules.add(AvatarColors.accessory1Base, currentAccessoryColor1);
	accessoryRules.add(AvatarColors.accessory2Base, currentAccessoryColor2);
	// Construct avatar
	currentFamilyID = AvatarConstructionKit.pickAvatarFamily();
	if (currentFamilyID == CommonDialogs.CANCEL) {
	    return null;
	}
	currentSkinColor = AvatarConstructionKit.pickAvatarSkinColor();
	if (currentSkinColor == null) {
	    return null;
	}
	currentHairColor = AvatarConstructionKit.pickAvatarHairColor();
	if (currentHairColor == null) {
	    return null;
	}
	currentEyesColor = AvatarConstructionKit.pickAvatarEyesColor();
	if (currentEyesColor == null) {
	    return null;
	}
	currentTorsoColor = AvatarConstructionKit.pickAvatarTorsoColor();
	if (currentTorsoColor == null) {
	    return null;
	}
	currentLegsColor = AvatarConstructionKit.pickAvatarLegsColor();
	if (currentLegsColor == null) {
	    return null;
	}
	currentFeetColor = AvatarConstructionKit.pickAvatarFeetColor();
	if (currentFeetColor == null) {
	    return null;
	}
	currentWeaponID = AvatarConstructionKit.pickAvatarWeapon();
	if (currentWeaponID == CommonDialogs.CANCEL) {
	    return null;
	}
	currentWeaponColor1 = AvatarConstructionKit.pickAvatarWeaponColor1();
	if (currentWeaponColor1 == null) {
	    return null;
	}
	currentWeaponColor2 = AvatarConstructionKit.pickAvatarWeaponColor2();
	if (currentWeaponColor2 == null) {
	    return null;
	}
	currentAccessoryID = AvatarConstructionKit.pickAvatarAccessory();
	if (currentAccessoryID == CommonDialogs.CANCEL) {
	    return null;
	}
	currentAccessoryColor1 = AvatarConstructionKit.pickAvatarAccessoryColor1();
	if (currentAccessoryColor1 == null) {
	    return null;
	}
	currentAccessoryColor2 = AvatarConstructionKit.pickAvatarAccessoryColor2();
	if (currentAccessoryColor2 == null) {
	    return null;
	}
	return new AvatarImageModel(currentFamilyID, currentWeaponID, currentAccessoryID, miscBits, currentHairColor,
		currentSkinColor, currentTorsoColor, currentLegsColor, currentFeetColor, currentEyesColor,
		currentWeaponColor1, currentWeaponColor2, currentAccessoryColor1, currentAccessoryColor2);
    }

    private static int pickAvatarFamily() {
	final String labelText = AckStrings.load(0);
	final String title = AckStrings.load(1);
	final BufferedImageIcon[] input = new BufferedImageIcon[] { AvatarImageLoader.load(0, rules),
		AvatarImageLoader.load(1, rules), AvatarImageLoader.load(2, rules), AvatarImageLoader.load(3, rules),
		AvatarImageLoader.load(4, rules), AvatarImageLoader.load(5, rules), AvatarImageLoader.load(6, rules),
		AvatarImageLoader.load(7, rules), AvatarImageLoader.load(8, rules), AvatarImageLoader.load(9, rules),
		AvatarImageLoader.load(10, rules), AvatarImageLoader.load(11, rules), AvatarImageLoader.load(12, rules),
		AvatarImageLoader.load(13, rules), AvatarImageLoader.load(14, rules),
		AvatarImageLoader.load(15, rules) };
	return CommonDialogs.showImageListDialog(labelText, title, input, currentFamilyID);
    }

    private static int pickAvatarWeapon() {
	final String labelText = AckStrings.load(2);
	final String title = AckStrings.load(3);
	final BufferedImageIcon[] input = new BufferedImageIcon[] { AvatarImageLoader.loadWeapon(0, weaponRules),
		AvatarImageLoader.loadWeapon(1, weaponRules), AvatarImageLoader.loadWeapon(2, weaponRules),
		AvatarImageLoader.loadWeapon(3, weaponRules) };
	final String[] descriptions = new String[] { AckStrings.load(16), AckStrings.load(17), AckStrings.load(18),
		AckStrings.load(19) };
	return CommonDialogs.showImageListWithDescDialog(labelText, title, input, 0, descriptions[0], descriptions);
    }

    private static int pickAvatarAccessory() {
	final String labelText = AckStrings.load(4);
	final String title = AckStrings.load(5);
	final BufferedImageIcon[] input = new BufferedImageIcon[] { AvatarImageLoader.loadAccessory(0, accessoryRules),
		AvatarImageLoader.loadAccessory(1, accessoryRules), AvatarImageLoader.loadAccessory(2, accessoryRules),
		AvatarImageLoader.loadAccessory(3, accessoryRules) };
	final String[] descriptions = new String[] { AckStrings.load(20), AckStrings.load(21), AckStrings.load(22),
		AckStrings.load(23) };
	return CommonDialogs.showImageListWithDescDialog(labelText, title, input, 0, descriptions[0], descriptions);
    }

    private static Color pickAvatarSkinColor() {
	final String title = AckStrings.load(6);
	return JColorChooser.showDialog(null, title, currentSkinColor, false);
    }

    private static Color pickAvatarHairColor() {
	final String title = AckStrings.load(7);
	return JColorChooser.showDialog(null, title, currentHairColor, false);
    }

    private static Color pickAvatarEyesColor() {
	final String title = AckStrings.load(8);
	return JColorChooser.showDialog(null, title, currentEyesColor, false);
    }

    private static Color pickAvatarTorsoColor() {
	final String title = AckStrings.load(9);
	return JColorChooser.showDialog(null, title, currentTorsoColor, false);
    }

    private static Color pickAvatarLegsColor() {
	final String title = AckStrings.load(10);
	return JColorChooser.showDialog(null, title, currentLegsColor, false);
    }

    private static Color pickAvatarFeetColor() {
	final String title = AckStrings.load(11);
	return JColorChooser.showDialog(null, title, currentFeetColor, false);
    }

    private static Color pickAvatarWeaponColor1() {
	final String title = AckStrings.load(12);
	return JColorChooser.showDialog(null, title, currentWeaponColor1, false);
    }

    private static Color pickAvatarWeaponColor2() {
	final String title = AckStrings.load(13);
	return JColorChooser.showDialog(null, title, currentWeaponColor2, false);
    }

    private static Color pickAvatarAccessoryColor1() {
	final String title = AckStrings.load(14);
	return JColorChooser.showDialog(null, title, currentAccessoryColor1, false);
    }

    private static Color pickAvatarAccessoryColor2() {
	final String title = AckStrings.load(15);
	return JColorChooser.showDialog(null, title, currentAccessoryColor2, false);
    }
}
