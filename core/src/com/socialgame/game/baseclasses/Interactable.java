package com.socialgame.game.baseclasses;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.socialgame.game.SocialGame;
import com.socialgame.game.networking.Networking;

/**
 * Base class from which all interactable game objects are derived
 * Extension of GameObject
 */
public abstract class Interactable extends GameObject {
    private static class InputListener extends com.badlogic.gdx.scenes.scene2d.InputListener {
        private final Interactable parent;
        private final SocialGame game;

        public InputListener(SocialGame game, Interactable parent) {
            this.parent = parent;
            this.game = game;
        }

        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            game.getMainStage().setKeyboardFocus(parent);
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            game.getMainStage().setKeyboardFocus(null);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.E) {
                parent.interact(game.mainPlayer);
                game.getMainStage().setKeyboardFocus(null);
                if (parent instanceof Item) {
                    game.getClient().sendTCP(Networking.pickupItemUpdate(game.mainPlayer.getId(), parent.getId()));
                }
            }
            return false;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            parent.interact(game.mainPlayer);
            game.getMainStage().setKeyboardFocus(null);
            return false;
        }
    }

    public Interactable(SocialGame game, float x, float y, float width, float height) {
        super(game, width, height, x, y);
        addListener(new InputListener(game, this));
    }

    public Interactable(SocialGame game, float width, float height){
        this(game, 0, 0, width, height);
    }

    public Interactable(SocialGame game) {
        this(game, 0, 0, 0, 0);
    }

    /**
     * Method to be called when player interacts with a GameObject, should be overridden to get desired behavior.
     * @param caller The GameObject which called interact on this Interactable
     */
    public void interact(GameObject caller) {
    }
}
