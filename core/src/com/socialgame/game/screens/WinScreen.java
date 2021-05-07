package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.screens.menu.MainMenuScreen;

import java.io.IOException;

public class WinScreen implements Screen{
    protected final SocialGame game;
    private Stage stage;


    public WinScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        addBackground();

        Button returnButton = new TextButton("Return",mySkin,"default");
        returnButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                // TODO: Add game rejoining capability.
                try {
                    game.setScreen(new GameScreen(game));
                } catch (IOException ignored) {}
                return true;
            }
        });

        Button exitButton = new TextButton("Exit",mySkin,"default");
        exitButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });

        Table table = new Table();
        table.setSize(Gdx.graphics.getHeight(),Gdx.graphics.getHeight()/10);
        table.setDebug(true); // turn on all debug lines (table, cell, and widget)
        table.setFillParent(true);
        table.center();
        table.add(returnButton).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10);
        table.add(exitButton).width(Gdx.graphics.getWidth()/5).height(Gdx.graphics.getHeight()/10).spaceLeft(Gdx.graphics.getWidth()/5);
        table.setPosition(0,-Gdx.graphics.getHeight()/3);
        stage.addActor(table);
    }

    public void addBackground(){
        Texture texture = new Texture(Gdx.files.internal("wining.png"));
        TextureRegion textureRegion = new TextureRegion(texture);

        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),texture.getHeight());
        background.setPosition(0,Gdx.graphics.getHeight()-background.getHeight());
        stage.addActor(background);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
