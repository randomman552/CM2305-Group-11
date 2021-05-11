package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.socialgame.game.SocialGame;
import com.socialgame.game.screens.GameScreen;

import java.io.IOException;

public class MainMenuScreen extends MenuScreen {
    public MainMenuScreen(final SocialGame game) {
        super(game);

        Image title = new Image(game.menuSpriteSheet.findRegion("title"));

        // region Button creation and listeners

        Button joinGameButton = new TextButton("Join Game",skin,"default");
        joinGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new JoinGameScreen(game));
                return true;
            }
        });
        Button createGameButton = new TextButton("Create Game",skin,"default");
        createGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new CreateGameScreen(game));
                return true;
            }
        });
        Button customiseButton = new TextButton("Customise",skin,"default");
        customiseButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new CustomiseScreen(game));
                return true;
            }
        });
        Button optionsButton = new TextButton("Options",skin,"default");
        optionsButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });
        Button quitGameButton = new TextButton("Quit",skin,"default");
        quitGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                Gdx.app.exit();
                return true;
            }
        });

        Button testingButton = new TextButton("testing",skin,"default");
        testingButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new EndScreen(game, false));
                return true;
            }
        });

        // endregion

        // region Create display table

        Table table = new Table();
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)
        table.setFillParent(true);
        table.center();
        table.add(title).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/3f).colspan(2);
        table.row();
        table.add(joinGameButton).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/10f).colspan(2).padBottom(10).padTop(10);
        table.row();
        table.add(createGameButton).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/10f).colspan(2).padBottom(10).padTop(10);
        table.row();
        table.add(customiseButton).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/10f).colspan(2).padBottom(10).padTop(10);
        table.row();
        table.add(optionsButton).width(Gdx.graphics.getWidth()/6f).height(Gdx.graphics.getHeight()/10f).padBottom(10).padTop(10).padRight(5);
        table.add(quitGameButton).width(Gdx.graphics.getWidth()/6f).height(Gdx.graphics.getHeight()/10f).padBottom(10).padTop(10);
        table.row();
        table.add(testingButton).colspan(2);
        stage.addActor(table);

        // endregion
    }
}
