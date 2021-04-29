package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.socialgame.game.SocialGame;

public class Options extends MenuScreen {
    public Options(final SocialGame game) {
        super(game);
        Image title = new Image(game.menuSpriteSheet.findRegion("title"));

        //TEMP BUTTON
        //TODO: Fix buttons and image not showing up.
        Button backButton = new TextButton("Back",skin,"default");
        backButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new Main(game));
                return true;
            }
        });

        Table table = new Table();
        table.setDebug(true); // turn on all debug lines (table, cell, and widget)
        table.setFillParent(true);
        table.center();
        table.add(title).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/3f).colspan(2);
        table.row();
        table.add(backButton).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/10f).colspan(2).padBottom(10).padTop(10);
        stage.addActor(table);
    }
}
