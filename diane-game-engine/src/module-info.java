/*
 * Diane Game Engine Copyleft (C) 2019 Eric Ahnell
 * 
 * Any questions should be directed to the author via email at:
 * support@puttysoftware.com
 */
module com.puttysoftware.diane {
  requires transitive java.desktop;
  requires transitive com.puttysoftware.images;
  requires transitive com.puttysoftware.polytable;
  requires transitive com.puttysoftware.storage;
  requires transitive com.puttysoftware.xio;

  exports com.puttysoftware.diane;
  exports com.puttysoftware.diane.assets;
  exports com.puttysoftware.diane.gui;
  exports com.puttysoftware.diane.loaders;
  exports com.puttysoftware.diane.map;
  exports com.puttysoftware.diane.objectmodel;
  exports com.puttysoftware.diane.scores;
  exports com.puttysoftware.diane.utilties;
}
