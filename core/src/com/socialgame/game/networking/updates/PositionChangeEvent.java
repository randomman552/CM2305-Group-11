package com.socialgame.game.networking.updates;

import com.badlogic.gdx.scenes.scene2d.Event;

public class PositionChangeEvent extends Event {
    public final float xPos;
    public final float yPos;
    public final float targetID;

    /**
     * Constructor with no arguments required by Kryonet
     */
    public PositionChangeEvent() {
        this(0, 0, 0);
    }

    public PositionChangeEvent(float xPos, float yPos, int targetID) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.targetID = targetID;
    }
}
