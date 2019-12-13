/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.objectmodel;

import com.puttysoftware.images.BufferedImageIcon;

public interface ObjectModel {
  int getUniqueID();

  BufferedImageIcon getImage();

  BufferedImageIcon getGameImage();

  BufferedImageIcon getEditorImage();

  BufferedImageIcon getBattleImage();

  boolean isSolid();

  boolean isDirectionallySolid(int dirX, int dirY);

  boolean isInternallyDirectionallySolid(int dirX, int dirY);

  boolean isPushable();

  boolean isDirectionallyPushable(int dirX, int dirY);

  boolean isPullable();

  boolean isDirectionallyPullable(int dirX, int dirY);

  boolean isPullableInto();

  boolean isPushableInto();

  boolean isDirectionallyPushableInto(int dirX, int dirY);

  boolean isDirectionallyPullableInto(int dirX, int dirY);

  boolean isPullableOut();

  boolean isPushableOut();

  boolean isDirectionallyPushableOut(int dirX, int dirY);

  boolean isDirectionallyPullableOut(int dirX, int dirY);

  boolean hasFriction();

  boolean isUsable();

  int getUses();

  void use();

  boolean isDestroyable();

  boolean isChainReacting();

  boolean isChainReactingHorizontally();

  boolean isChainReactingVertically();

  boolean isCarryable();

  boolean isSightBlocking();

  boolean isDirectionallySightBlocking(int inDirX, int inDirY);

  boolean isInternallyDirectionallySightBlocking(int inDirX, int inDirY);

  int getTimerTicks();

  void tickTimer();

  void resetTimer();
}