package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.socialgame.game.SocialGame;

/**
 * Screen to display the given error.
 * Used to communicate to user when a connection has failed etc.
 *
 */
public class ErrorScreen extends MenuScreen{
    public ErrorScreen(final SocialGame game, String message, final Screen returnScreen) {
        super(game);

        Image title = new Image(game.menuSpriteSheet.findRegion("title"));
        Label errorMessage = new Label(message, skin, "big");
        Button returnButton = new TextButton("Back", skin);

        returnButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(returnScreen);
                return true;
            }
        });

        Table table = new Table();
        table.add(title);
        table.row();
        table.add(errorMessage);
        table.row();
        table.add(returnButton);
        table.setFillParent(true);
        table.center();

        stage.addActor(table);
    }

    public ErrorScreen(SocialGame game, String message) {
        this(game, message, game.getScreen());
    }

    public ErrorScreen(SocialGame game) {
        this(game, "An error has occurred...");
    }
}
