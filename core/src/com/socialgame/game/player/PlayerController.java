package com.socialgame.game.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.socialgame.game.SocialGame;

import java.util.ArrayList;

/**
 * Class to handle player inputs on the client side.
 * Should be added to the game stage as an event listener.
 */
public class PlayerController extends InputListener {
    protected SocialGame game;

    /**
     * Array list to store the key codes of the keys which are currently pressed down.
     * This list of keys is then used to control the player associated with this controller.
     */
    private final ArrayList<Integer> pressedKeys = new ArrayList<>();

    public PlayerController(SocialGame game) {
        this.game = game;
    }

    @Override
    public boolean handle(Event e) {
        super.handle(e);

        Player player = (Player) game.mainPlayer;
        Vector2 vel = new Vector2(0, 0);

        // Acceleration is proportional to player X scale
        float accel = (player.isAlive()) ? Player.MAX_VEL: Player.MAX_VEL * Player.SPEC_VEL_MOD;

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
        // Handle any required immediate actions which arent movement here
        Player player = (Player) game.mainPlayer;
        switch (keycode) {
            case Input.Keys.Q:
                player.dropItem();
                break;
            case Input.Keys.NUM_1:
                player.setInvSlot(0);
                break;
            case Input.Keys.NUM_2:
                player.setInvSlot(1);
                break;
        }

        pressedKeys.add(keycode);
        return false;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        pressedKeys.remove(Integer.valueOf(keycode));
        return false;
    }

    @Override
    public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
        Player player = ((Player) game.mainPlayer);
        // amountY is positive when scrolling down, and we want to go DOWN a slot. So we must negate amountY
        player.setInvSlot(player.getInvSlot() + (int)-amountY);
        return false;
    }
}
