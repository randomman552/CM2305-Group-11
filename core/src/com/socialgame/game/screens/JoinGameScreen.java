package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.PlayerCustomisation;

public class JoinGameScreen implements Screen {

    protected final SocialGame game;
    private Stage stage;


    public JoinGameScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        Texture texture = new Texture("title.png");

        Image title = new Image(texture);

        addBackground();

        Label ipAddressLabel = new Label("IP Address:", mySkin,"big");
        TextField ipAddressText = new TextField("", mySkin);
        Label passwordLabel = new Label("Password:", mySkin,"big");
        TextField passwordText = new TextField("", mySkin);


        Button backButton = new TextButton("Back",mySkin,"default");
        backButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });

        Button joinButton = new TextButton("Join",mySkin,"default");
        joinButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }
        });

        Table table = new Table();
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)
        table.setFillParent(true);
        table.center();
        table.add(title).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/3).colspan(2);
        table.row();
        table.add(ipAddressLabel);
        table.add(ipAddressText).width(Gdx.graphics.getWidth()/5).uniform().pad(5f);
        table.row();
        table.add(passwordLabel);
        table.add(passwordText).width(Gdx.graphics.getWidth()/5).uniform().pad(5f);
        table.row();
        table.add(backButton).width(Gdx.graphics.getWidth()/6).height(Gdx.graphics.getHeight()/10).padBottom(10).padTop(10);
        table.add(joinButton).width(Gdx.graphics.getWidth()/6).height(Gdx.graphics.getHeight()/10).padBottom(10).padTop(10);
        stage.addActor(table);
    }

    public void addBackground(){
        Texture texture = new Texture(Gdx.files.internal("background.png"));
        TextureRegion textureRegion = new TextureRegion(texture);

        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth());
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
