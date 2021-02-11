package com.socialgame.game.baseclasses;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.socialgame.game.SocialGame;
import com.socialgame.game.screens.GameScreen;

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
            ((GameScreen) game.getScreen()).stage.setKeyboardFocus(parent);
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            ((GameScreen) game.getScreen()).stage.setKeyboardFocus(null);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            if (keycode == Input.Keys.E) {
                parent.interact(game.mainPlayer);
                ((GameScreen) game.getScreen()).stage.setKeyboardFocus(null);
            }
            return false;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            parent.interact(game.mainPlayer);
            ((GameScreen) game.getScreen()).stage.setKeyboardFocus(null);
            return false;
        }
    }

    public Interactable(SocialGame game) {
        super(game);
        addListener(new InputListener(game, this));
    }

    /**
     * @param caller The GameObject which called interact on this Interactable
     */
    public void interact(GameObject caller) {
    }
}
