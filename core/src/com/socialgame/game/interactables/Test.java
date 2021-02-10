package com.socialgame.game.interactables;

import com.badlogic.gdx.Gdx;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;

public class Test extends Interactable {
    public Test(SocialGame game) {
        super(game);

        texture = game.spriteSheet.findRegion("square");

        setSize(1, 1);
    }

    @Override
    public void interact(GameObject caller) {
        Gdx.app.exit();
    }
}
