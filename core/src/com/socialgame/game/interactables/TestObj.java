package com.socialgame.game.interactables;

import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Item;

public class TestObj extends Item {
    public TestObj(SocialGame game) {
        super(game);

        texture = game.spriteSheet.findRegion("square");
        setBounds(0, 0, 1, 1);
    }
}
