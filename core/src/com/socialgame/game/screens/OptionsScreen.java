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

public class OptionsScreen implements Screen {

    protected final SocialGame game;
    private Stage stage;


    public OptionsScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));

        Texture texture = new Texture("title.png");

        Image title = new Image(texture);

        addBackground();

        //TEMP BUTTON
        //TODO: Fix buttons and image not showing up.
        Button backButton = new TextButton("Back",mySkin,"default");
        backButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });

        Label optionsLabel = new Label("Options", mySkin, "big");
        Label setting1 = new Label("this is the VIDEO tab", mySkin, "default");
        Label setting2 = new Label("this is the audio tab", mySkin, "default");
        Button videoButton = new TextButton("Video", mySkin, "default");
        Button audioButton = new TextButton("Audio", mySkin, "default");


        Table container = new Table();
        container.defaults().expand();//.padTop(10F).padBottom(10F);
        container.setFillParent(true);
        //container.center();
        //table.add(title).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/3).colspan(2);


        Table optionsButtons = new Table();
        optionsButtons.setDebug(true);
        optionsButtons.add(videoButton);//.width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/10).colspan(2).padBottom(10).padTop(10).colspan(2);
        optionsButtons.row();
        optionsButtons.add(audioButton);


        final Table optionsVideo = new Table();
        optionsVideo.setVisible(true);
        optionsVideo.add(setting1);
        optionsVideo.add();//Can change setting here

        final Table optionsAudio = new Table();
        optionsAudio.setVisible(false);
        optionsAudio.add(setting2);


        videoButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                optionsAudio.setVisible(false);
                optionsVideo.setVisible(true);
                return true;
            }
        });
        audioButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                optionsVideo.setVisible(false);
                optionsAudio.setVisible(true);
                return true;
            }
        });




        Table optionsContainer = new Table();
        optionsContainer.setDebug(true);
        optionsContainer.add(optionsLabel);
        optionsContainer.row();
        optionsContainer.add(optionsVideo);
        optionsContainer.row();
        optionsContainer.add(optionsAudio);

        container.add(optionsButtons).width(Gdx.graphics.getWidth()/3);
        container.add(optionsContainer).width(Gdx.graphics.getWidth()/3*2);
        container.row();
        container.add(backButton).colspan(2).height(Gdx.graphics.getHeight()/12);//.width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/10).colspan(2).padBottom(10).padTop(10).colspan(2);
        stage.addActor(container);
        stage.setDebugAll(true); // turn on all debug lines (table, cell, and widget)
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
