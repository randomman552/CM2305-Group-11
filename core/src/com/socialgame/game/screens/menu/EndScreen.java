package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.socialgame.game.SocialGame;

public class EndScreen extends MenuScreen {
    public boolean win;

    public EndScreen(final SocialGame game, boolean win) {
        super(game);
        this.win = win;

        stage.clear();

        TextureAtlas.AtlasRegion textureRegion;
        if (win)
            textureRegion= game.menuSpriteSheet.findRegion("win");
        else
            textureRegion= game.menuSpriteSheet.findRegion("lose");

        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth());
        background.setPosition(0,Gdx.graphics.getHeight()-background.getHeight());
        stage.addActor(background);

        // region Create buttons

        Button returnButton = new TextButton("Return",skin,"default");
        returnButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });

        Button exitButton = new TextButton("Exit",skin,"default");
        exitButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });

        Table table = new Table();
        table.setSize(Gdx.graphics.getHeight(),Gdx.graphics.getHeight()/10);
        table.setFillParent(true);
        table.center();
        table.add(returnButton).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10);
        table.add(exitButton).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10).spaceLeft(Gdx.graphics.getWidth()/5);
        table.setPosition(0,-Gdx.graphics.getHeight()/3);
        stage.addActor(table);

        // endregion
    }
}
