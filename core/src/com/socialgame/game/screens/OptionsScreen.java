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
        Label setting2 = new Label("this is the AUDIO tab", mySkin, "default");
        Label masVolLabel = new Label("Master Volume:", mySkin, "default");
        Label sfxLabel = new Label("SFX Volume:", mySkin, "default");
        Label musicLabel = new Label("Music Volume:", mySkin, "default");
        Label voiceLabel = new Label("VOICE", mySkin, "default");
        Label micLabel = new Label("Microphone:", mySkin, "default");
        Label micVolLabel = new Label("Microphone Volume:", mySkin, "default");
        Label resLabel = new Label("Resolution:", mySkin, "default");
        Button videoButton = new TextButton("Video", mySkin, "default");
        Button audioButton = new TextButton("Audio", mySkin, "default");
        Slider masVol = new Slider(0,1, 1, false, mySkin);
        Slider SFX = new Slider(0,1, 1, false, mySkin);
        Slider musVol = new Slider(0,1, 1, false, mySkin);
        Slider micVol = new Slider(0,1, 1, false, mySkin);
        SelectBox<String> resSettings = new SelectBox<String>(mySkin);
        resSettings.setItems("1280x720", "1920x1080", "2560x1440");
        SelectBox<String> micSettings = new SelectBox<String>(mySkin);
        micSettings.setItems("Press to Talk", "On", "Off");


        // Global container
        Table globalTable = new Table();
        stage.addActor(globalTable);
        globalTable.setFillParent(true);

        //region Buttons container

        Table navOptionsButtons = new Table();
        navOptionsButtons.add(optionsLabel);
        navOptionsButtons.row();
        navOptionsButtons.add(videoButton).height(50);
        navOptionsButtons.row();
        navOptionsButtons.add(audioButton).height(50);
        navOptionsButtons.row();
        navOptionsButtons.add(backButton).height(50);

        globalTable.add(navOptionsButtons).width(1/5f * Gdx.graphics.getWidth());

        //endregion

        // Settings container
        final Table videoOptions = new Table();
        videoOptions.setFillParent(true);
        videoOptions.add(setting1);
        videoOptions.row();
        videoOptions.add(resLabel);
        videoOptions.row();
        videoOptions.add(resSettings);


        final Table audioOptions = new Table();
        audioOptions.setFillParent(true);
        audioOptions.add(setting2);
        audioOptions.row();
        audioOptions.add(masVolLabel);
        audioOptions.row();
        audioOptions.add(masVol);
        audioOptions.row();
        audioOptions.add(sfxLabel);
        audioOptions.row();
        audioOptions.add(SFX);
        audioOptions.row();
        audioOptions.add(musicLabel);
        audioOptions.row();
        audioOptions.add(musVol);
        audioOptions.row();
        audioOptions.add(voiceLabel);
        audioOptions.row();
        audioOptions.add(micLabel);
        audioOptions.row();
        audioOptions.add(micSettings);
        audioOptions.row();
        audioOptions.add(micVolLabel);
        audioOptions.row();
        audioOptions.add(micVol);



        // Settings widget group for RHS of global table
        WidgetGroup settingsGroup = new WidgetGroup();
        settingsGroup.addActor(videoOptions);
        settingsGroup.addActor(audioOptions);
        globalTable.add(settingsGroup).expandX();

        // Set default state
        videoOptions.setVisible(true);
        audioOptions.setVisible(false);


        videoButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                videoOptions.setVisible(true);
                audioOptions.setVisible(false);
                return true;
            }
        });
        audioButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                videoOptions.setVisible(false);
                audioOptions.setVisible(true);
                return true;
            }
        });

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
