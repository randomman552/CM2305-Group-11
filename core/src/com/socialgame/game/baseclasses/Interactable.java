package com.socialgame.game.baseclasses;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.socialgame.game.SocialGame;

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
            super.enter(event, x, y, pointer, fromActor);
        }

        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            super.exit(event, x, y, pointer, toActor);
        }

        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            parent.interact(game.mainPlayer);
            return false;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            parent.interact(game.mainPlayer);
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
