package com.socialgame.game.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;

/**
 * Class to handle player inputs on the client side.
 * Should be added to the game stage as an event listener.
 */
public class PlayerController extends InputListener {
    protected Player player;

    /**
     * Array list to store the key codes of the keys which are currently pressed down.
     * This list of keys is then used to control the player associated with this controller.
     */
    private final ArrayList<Integer> pressedKeys = new ArrayList<>();

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public boolean handle(Event e) {
        super.handle(e);

        Vector2 vel = new Vector2(0, 0);
        float accel = Player.MAX_VELOCITY;

        // Change player states depending on the keys that are pressed down
        for (int keycode: pressedKeys) {
            switch (keycode) {
                case Input.Keys.W:
                    vel.y = accel;
                    break;
                case Input.Keys.S:
                    vel.y = -accel;
                    break;
                case Input.Keys.A:
                    vel.x = -accel;
                    break;
                case Input.Keys.D:
                    vel.x = accel;
                    break;
            }
        }
        player.body.setLinearVelocity(vel);

        return false;
    }

    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        pressedKeys.add(keycode);
        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        pressedKeys.remove(Integer.valueOf(keycode));
        return false;
    }
}