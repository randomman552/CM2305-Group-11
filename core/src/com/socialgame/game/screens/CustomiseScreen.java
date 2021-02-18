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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.PlayerCustomisation;

public class CustomiseScreen implements Screen {

    protected final SocialGame game;
    private Stage stage;


    public CustomiseScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        Texture texture = new Texture("playerCustomisePlaceholder.png");

        Image playerDisplayImg = new Image(texture);

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

        Table container = new Table();
        container.defaults().padTop(10F).padBottom(10F);
        container.setFillParent(true);

        Label colour1 = new Label("colour",mySkin, "default");
        Label colour2 = new Label("colour",mySkin, "default");
        Label colour3 = new Label("colour",mySkin, "default");
        Label colour4 = new Label("colour",mySkin, "default");
        Label colour5 = new Label("colour",mySkin, "default");
        Label colour6 = new Label("colour",mySkin, "default");
        Label colour7 = new Label("colour",mySkin, "default");
        Label colour8 = new Label("colour",mySkin, "default");
        Label colour9 = new Label("colour",mySkin, "default");
        Label colour10 = new Label("colour",mySkin, "default");
        Label colour11 = new Label("colour",mySkin, "default");
        Label colour12 = new Label("colour",mySkin, "default");

        Label playerName = new Label("NAMEEEEEEEEEEE",mySkin, "big");
        Label playerLvl = new Label("Lv.8",mySkin,"big");
        Label playerLvlCurrentBar = new Label("8",mySkin,"big");
        Label playerLvlBar = new Label("##############----",mySkin,"big");
        Label playerLvlNextBar = new Label("9",mySkin,"big");
        Label label3 = new Label("playerItem Table",mySkin,"default");

        Table clrPicker = new Table();
            clrPicker.setDebug(true);
            clrPicker.add(colour1).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour2).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour3).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour4).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour5).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour6).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour7).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour8).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour9).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour10).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour11).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();
            clrPicker.add(colour12).height(Gdx.graphics.getHeight()/14).pad(2f);
            clrPicker.row();

        Table playerInfoContainer = new Table();

            Table playerInfo = new Table();
            playerInfo.defaults().pad(5F);
            playerInfo.add(playerName).colspan(2).left();
            playerInfo.add(playerLvl);
            playerInfo.row();
            playerInfo.add(playerLvlCurrentBar);
            playerInfo.add(playerLvlBar);
            playerInfo.add(playerLvlNextBar);

            Table playerDisplay = new Table();
            playerDisplay.add(playerDisplayImg);

        playerInfoContainer.add(playerInfo).height(Gdx.graphics.getHeight()/14*3);;
        playerInfoContainer.row();
        playerInfoContainer.add(playerDisplay).height(Gdx.graphics.getHeight()/14*11);


        Table playerItems = new Table();
        playerItems.add();


        container.add(clrPicker).width(Gdx.graphics.getWidth()/18).expandY();
        container.add(playerInfoContainer).width(Gdx.graphics.getWidth()/18*8).expandY();
        container.add(playerItems).width(Gdx.graphics.getWidth()/18*9).expandY();

        stage.addActor(container);
        stage.setDebugAll(true);

    }

    // Container

    // Colour picker

    // Player info and level

    // Item selections
        // Hat
        // Top

    // Save and Exit buttons


    public void addBackground() {
        Texture texture = new Texture(Gdx.files.internal("background.png"));
        TextureRegion textureRegion = new TextureRegion(texture);

        textureRegion.setRegion(0, 0, texture.getWidth(), texture.getHeight());
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        background.setPosition(0, Gdx.graphics.getHeight() - background.getHeight());
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
