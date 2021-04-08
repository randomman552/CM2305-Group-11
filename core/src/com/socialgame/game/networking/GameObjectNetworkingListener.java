package com.socialgame.game.networking;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.networking.updates.PositionChangeEvent;

public class GameObjectNetworkingListener implements EventListener {
    private final GameObject obj;
    public GameObjectNetworkingListener(GameObject obj) {
        this.obj = obj;
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof PositionChangeEvent) {
            PositionChangeEvent event1 = ((PositionChangeEvent) event);
            obj.setPositionAboutOrigin(event1.xPos, event1.yPos);
        }
        return false;
    }
}
