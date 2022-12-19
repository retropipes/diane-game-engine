package com.puttysoftware.diane.internal;

import com.puttysoftware.diane.ack.AvatarImageModel;
import com.puttysoftware.diane.asset.BufferedImageIcon;
import com.puttysoftware.diane.loaders.ColorReplaceRules;
import com.puttysoftware.diane.loaders.ImageCompositor;
import com.puttysoftware.diane.loaders.ImageLoader;

public class AvatarImageLoader {
    public static BufferedImageIcon load(final int familyID, final ColorReplaceRules rules) {
	final String imageExt = ".png";
	final String name = "/asset/ack/avatar/" + Integer.toHexString(familyID).toUpperCase() + imageExt;
	return rules.applyAll(ImageLoader.load(name, AvatarImageLoader.class.getResource(name)));
    }

    public static BufferedImageIcon loadFromModel(final AvatarImageModel model) {
	final String imageExt = ".png";
	final String name = "/asset/ack/avatar/" + Integer.toHexString(model.getAvatarFamilyID()).toUpperCase()
		+ imageExt;
	BufferedImageIcon image = ImageLoader.load(name, AvatarImageLoader.class.getResource(name));
	image = model.getRules().applyAll(image);
	BufferedImageIcon weaponImage = AvatarImageLoader.loadWeapon(model.getAvatarWeaponID(), model.getWeaponRules());
	BufferedImageIcon accessoryImage = AvatarImageLoader.loadAccessory(model.getAvatarAccessoryID(),
		model.getAccessoryRules());
	return ImageCompositor.composite(name, image, accessoryImage, weaponImage);
    }

    public static BufferedImageIcon loadWeapon(final int weaponID, final ColorReplaceRules rules) {
	final String imageExt = ".png";
	final String name = "/asset/ack/weapon/" + Integer.toHexString(weaponID).toUpperCase() + imageExt;
	return rules.applyAll(ImageLoader.load(name, AvatarImageLoader.class.getResource(name)));
    }

    public static BufferedImageIcon loadAccessory(final int accessoryID, final ColorReplaceRules rules) {
	final String imageExt = ".png";
	final String name = "/asset/ack/accessory/" + Integer.toHexString(accessoryID).toUpperCase() + imageExt;
	return rules.applyAll(ImageLoader.load(name, AvatarImageLoader.class.getResource(name)));
    }
}
