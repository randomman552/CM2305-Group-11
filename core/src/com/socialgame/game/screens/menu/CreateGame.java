package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.socialgame.game.SocialGame;
import com.socialgame.game.networking.GameServer;
import com.socialgame.game.screens.GameScreen;

import java.io.IOException;

public class CreateGame extends MenuScreen {
    public CreateGame(final SocialGame game) {
        super(game);
        Image title = new Image(game.menuSpriteSheet.findRegion("title"));

        Label passwordLabel = new Label("Password:", skin,"big");
        final TextField passwordText = new TextField("", skin);

        // Back button
        Button backButton = new TextButton("Back",skin,"default");
        backButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new Main(game));
                return true;
            }
        });

        // Create button
        Button createButton = new TextButton("Create",skin,"default");
        createButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                try {
                    game.setServer(new GameServer());
                    game.setScreen(new GameScreen(game, "localhost"));
                } catch (IOException e) {
                    String message = "Server failed to start.\nIs another server already running?";
                    e.printStackTrace();
                    game.setScreen(new Error(game, message));
                }
                return true;
            }
        });

        Table table = new Table();
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)
        table.setFillParent(true);
        table.center();
        table.add(title).width(Gdx.graphics.getWidth()/3f).height(Gdx.graphics.getHeight()/3f).colspan(2);
        table.row();
        table.add(passwordLabel);
        table.add(passwordText).width(Gdx.graphics.getWidth()/5f).uniform().pad(5f);
        table.row();
        table.add(backButton).width(Gdx.graphics.getWidth()/6f).height(Gdx.graphics.getHeight()/10f).padBottom(10).padTop(10);
        table.add(createButton).width(Gdx.graphics.getWidth()/6f).height(Gdx.graphics.getHeight()/10f).padBottom(10).padTop(10);
        stage.addActor(table);
    }
}
