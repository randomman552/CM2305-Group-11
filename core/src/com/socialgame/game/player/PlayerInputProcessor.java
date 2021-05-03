package com.socialgame.game.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.socialgame.game.SocialGame;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.screens.menu.MainMenuScreen;

import java.util.ArrayList;

/**
 * Class to handle player inputs on the client side.
 * Should be added to the game stage as an event listener.
 */
public class PlayerInputProcessor implements InputProcessor {
    protected SocialGame game;

    /**
     * Array list to store the key codes of the keys which are currently pressed down.
     * This list of keys is then used to control the player associated with this controller.
     */
    private final ArrayList<Integer> pressedKeys = new ArrayList<>();

    public PlayerInputProcessor(SocialGame game) {
        this.game = game;
    }

    /**
     * Method to update players velocity, should be called once per frame.
     * @param delta The time elapsed between this frame and the last.
     */
    public void updateVelocity(Player player, float delta) {
        Vector2 vel = player.body.getLinearVelocity();
        float maxVel = (player.isAlive())? Player.MAX_VEL : Player.MAX_VEL * Player.SPEC_VEL_MOD;
        float rawAcceleration = Player.ACCELERATION;
        float baseAcceleration = (player.body.getMass() * rawAcceleration * delta);
        // Force to counteract the linear damping force added by Box2D.
        float counteractForce = player.body.getLinearDamping() * maxVel;

        // region X axis

        float xForce = 0;
        if (pressedKeys.contains(Input.Keys.D)) {
            float scalar = (maxVel - vel.x) / maxVel;
            xForce = baseAcceleration * scalar + counteractForce;
        }
        else if (pressedKeys.contains(Input.Keys.A)) {
            float scalar = (-maxVel - vel.x) / -maxVel;
            xForce = -baseAcceleration * scalar - counteractForce;
        }

        // endregion

        // region Y axis

        float yForce = 0;
        if (pressedKeys.contains(Input.Keys.W)) {
            float scalar = (maxVel - vel.y) / maxVel;
            yForce = baseAcceleration * scalar + counteractForce;
        }
        else if (pressedKeys.contains(Input.Keys.S)) {
            float scalar = (-maxVel - vel.y) / -maxVel;
            yForce = -baseAcceleration * scalar - counteractForce;
        }

        // endregion

        // Apply final force
        if (yForce != 0 || xForce != 0) {
            player.body.applyForceToCenter(xForce, yForce, true);
        }

        vel = player.body.getLinearVelocity();
        if (game.getClient() != null && player.body.isAwake()) {
            game.getClient().sendTCP(Networking.velocityUpdate(player.getID(), vel.x, vel.y));
            game.getClient().sendTCP(Networking.positionUpdate(player.getID(), player.getX(), player.getY()));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        // Handle any required immediate actions which arent movement here
        Player player = (Player) game.mainPlayer;
        switch (keycode) {
            case Input.Keys.Q:
                player.dropItem();
                game.getClient().sendTCP(Networking.dropItemUpdate(player.getID()));
                return true;
            case Input.Keys.NUM_1:
                player.setInvSlot(0);
                game.getClient().sendTCP(Networking.switchItemUpdate(player.getID(), player.getInvSlot()));
                return true;
            case Input.Keys.NUM_2:
                player.setInvSlot(1);
                game.getClient().sendTCP(Networking.switchItemUpdate(player.getID(), player.getInvSlot()));
                return true;
            case Input.Keys.ESCAPE:
                game.setScreen(new MainMenuScreen(game));
                game.closeServer();
                game.closeClient();
                return true;
        }

        pressedKeys.add(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(Integer.valueOf(keycode));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Player player = ((Player) game.mainPlayer);
        // amountY is positive when scrolling down, and we want to go DOWN a slot. So we must negate amountY
        player.setInvSlot(player.getInvSlot() + (int)-amountY);
        game.getClient().sendUDP(Networking.switchItemUpdate(player.getID(), player.getInvSlot()));
        return false;
    }
}
