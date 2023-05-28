/*  Diane Game Engine
Copyleft (C) 2019-present Eric Ahnell
Any questions should be directed to the author via email at: support@puttysoftware.com */
package com.puttysoftware.diane.ack;

import java.awt.Color;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;
import com.puttysoftware.diane.asset.image.ColorReplaceRules;
import com.puttysoftware.diane.internal.AvatarColors;
import com.puttysoftware.diane.internal.AvatarImageLoader;

public final class AvatarImageModel {
    private static String colorToHex3Bytes(final Color value) {
	return String.format("%1$06X", value.getRGB() << 8 >>> 8);
    }

    private static String intToHexByte(final int value) {
	return String.format("%1$02X", value);
    }

    private static String intToHexNybble(final int value) {
	return String.format("%1$01X", value);
    }

    // Fields
    private final int familyID;
    private final int weaponID;
    private final int accessoryID;
    private final int miscID;
    private final Color hairColor;
    private final Color skinColor;
    private final Color torsoColor;
    private final Color legsColor;
    private final Color feetColor;
    private final Color eyesColor;
    private final Color weaponColor1;
    private final Color weaponColor2;
    private final Color accessoryColor1;
    private final Color accessoryColor2;
    private final ColorReplaceRules rules;
    private final ColorReplaceRules weaponRules;
    private final ColorReplaceRules accessoryRules;

    // Constructors
    public AvatarImageModel(final int family, final int weapon, final int accessory, final int misc, final Color hair,
	    final Color skin, final Color torso, final Color legs, final Color feet, final Color eyes,
	    final Color weapon1, final Color weapon2, final Color accessory1, final Color accessory2) {
	this.familyID = family;
	this.weaponID = weapon;
	this.accessoryID = accessory;
	this.miscID = misc;
	this.hairColor = hair;
	this.skinColor = skin;
	this.torsoColor = torso;
	this.legsColor = legs;
	this.feetColor = feet;
	this.eyesColor = eyes;
	this.weaponColor1 = weapon1;
	this.weaponColor2 = weapon2;
	this.accessoryColor1 = accessory1;
	this.accessoryColor2 = accessory2;
	this.rules = new ColorReplaceRules();
	this.weaponRules = new ColorReplaceRules();
	this.accessoryRules = new ColorReplaceRules();
	this.addRules();
    }

    private void addRules() {
	this.rules.add(AvatarColors.hairBase, this.hairColor);
	this.rules.add(AvatarColors.skinBase, this.skinColor);
	this.rules.add(AvatarColors.bodyBase, this.torsoColor);
	this.rules.add(AvatarColors.pantsBase, this.legsColor);
	this.rules.add(AvatarColors.shoesBase, this.feetColor);
	this.rules.add(AvatarColors.eyesBase, this.eyesColor);
	this.weaponRules.add(AvatarColors.weapon1Base, this.weaponColor1);
	this.weaponRules.add(AvatarColors.weapon2Base, this.weaponColor2);
	this.accessoryRules.add(AvatarColors.accessory1Base, this.accessoryColor1);
	this.accessoryRules.add(AvatarColors.accessory2Base, this.accessoryColor2);
    }

    public BufferedImageIcon generateAvatarImage() {
	return AvatarImageLoader.loadFromModel(this);
    }

    public ColorReplaceRules getAccessoryRules() {
	return this.accessoryRules;
    }

    public Color getAvatarAccessoryColor1() {
	return this.accessoryColor1;
    }

    public Color getAvatarAccessoryColor2() {
	return this.accessoryColor2;
    }

    public int getAvatarAccessoryID() {
	return this.accessoryID;
    }

    public Color getAvatarEyesColor() {
	return this.eyesColor;
    }

    public int getAvatarFamilyID() {
	return this.familyID;
    }

    public Color getAvatarFeetColor() {
	return this.feetColor;
    }

    public Color getAvatarHairColor() {
	return this.hairColor;
    }

    public String getAvatarImageID() {
	final var builder = new StringBuilder();
	builder.append(AvatarImageModel.intToHexNybble(this.familyID));
	builder.append(AvatarImageModel.intToHexNybble(this.accessoryID * 4 + this.weaponID));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.hairColor));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.skinColor));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.torsoColor));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.legsColor));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.feetColor));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.eyesColor));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.weaponColor1));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.weaponColor2));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.accessoryColor1));
	builder.append(AvatarImageModel.colorToHex3Bytes(this.accessoryColor2));
	builder.append(AvatarImageModel.intToHexByte(this.miscID));
	return builder.toString();
    }

    public Color getAvatarLegsColor() {
	return this.legsColor;
    }

    public Color getAvatarSkinColor() {
	return this.skinColor;
    }

    public Color getAvatarTorsoColor() {
	return this.torsoColor;
    }

    public Color getAvatarWeaponColor1() {
	return this.weaponColor1;
    }

    public Color getAvatarWeaponColor2() {
	return this.weaponColor2;
    }

    public int getAvatarWeaponID() {
	return this.weaponID;
    }

    public ColorReplaceRules getRules() {
	return this.rules;
    }

    public ColorReplaceRules getWeaponRules() {
	return this.weaponRules;
    }
}
