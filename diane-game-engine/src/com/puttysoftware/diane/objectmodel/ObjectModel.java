/*  Diane Game Engine
Copyleft (C) 2019 Eric Ahnell

Any questions should be directed to the author via email at: support@puttysoftware.com
 */
package com.puttysoftware.diane.objectmodel;

import com.puttysoftware.diane.asset.image.BufferedImageIcon;

public interface ObjectModel {
    BufferedImageIcon getBattleImage();

    BufferedImageIcon getEditorImage();

    BufferedImageIcon getGameImage();

    BufferedImageIcon getImage();

    int getTimerTicks();

    int getUniqueID();

    int getUses();

    boolean hasFriction();

    boolean isCarryable();

    boolean isChainReacting();

    boolean isChainReactingHorizontally();

    boolean isChainReactingVertically();

    boolean isDestroyable();

    boolean isDirectionallyPullable(int dirX, int dirY);

    boolean isDirectionallyPullableInto(int dirX, int dirY);

    boolean isDirectionallyPullableOut(int dirX, int dirY);

    boolean isDirectionallyPushable(int dirX, int dirY);

    boolean isDirectionallyPushableInto(int dirX, int dirY);

    boolean isDirectionallyPushableOut(int dirX, int dirY);

    boolean isDirectionallySightBlocking(int inDirX, int inDirY);

    boolean isDirectionallySolid(int dirX, int dirY);

    boolean isInternallyDirectionallySightBlocking(int inDirX, int inDirY);

    boolean isInternallyDirectionallySolid(int dirX, int dirY);

    boolean isPullable();

    boolean isPullableInto();

    boolean isPullableOut();

    boolean isPushable();

    boolean isPushableInto();

    boolean isPushableOut();

    boolean isSightBlocking();

    boolean isSolid();

    boolean isUsable();

    void resetTimer();

    void tickTimer();

    void use();
}