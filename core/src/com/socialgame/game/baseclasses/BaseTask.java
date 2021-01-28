package com.socialgame.game.baseclasses;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.socialgame.game.SocialGame;

/**
 * Class from which all tasks are deerived.
 * Extension of Interactable class
 */
public class BaseTask extends Interactable {
    public boolean isComplete = false;

    public BaseTask(SocialGame game) {
        super(game);
    }

    /**
     * Method to be called on task completion
     */
    public void onComplete() {}

    /**
     * Method to be called on task failure
     */
    public void onFail() {}

    public void interact(GameObject caller) {}

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // TODO: Code to draw task screen here
    }
}
