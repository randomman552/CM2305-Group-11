package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;

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

        final Image playerDisplayImg = new Image(texture);

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





        // Colour Buttons
        /* Colour IDs from 1 to 12
        205,66,41,255
        221,115,39,255
        241,234,87,255
        110,165,22,255
        41,205,162,255
        41,175,205,255
        188,141,232,255
        131,67,156,255
        153,89,54,255
        79,117,19,255
        119,119,119,255
        13,102,133,255`
         */


        //TODO: Change the colour of a button when pressing.

        // Base.png
        // tick.png
        final Texture baseColourButtonTexture = new Texture("Base.png");
        final TextureRegionDrawable baseColourButtonDraw = new TextureRegionDrawable(baseColourButtonTexture);

        final ImageButton c1Button = new ImageButton(baseColourButtonDraw);
        c1Button.getImage().setColor(new Color(205,66,41,255));
        c1Button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                c1Button.getImage().setColor(205,66,41,255);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                c1Button.getImage().setColor(205,66,41,255);
            }
        });

        final ImageButton c2Button = new ImageButton(baseColourButtonDraw);
        c2Button.getImage().setColor(255, 0, 255, 255);
        c2Button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                c2Button.getImage().setColor(0, 255, 255, 255);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                c2Button.getImage().setColor(255, 0, 255, 255);
            }
        });

        //TODO: CHANGE LABELS TO CHECKBOX BUTTONS, THEN COLOUR THE BUTTONS ACCOURDING TO THE COLOURS, THEN RESIZE THEM TO FIT IN THE BOX

        //TEMP: Labels for the colours
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


        //TEMP: Labels for the playerInfo
        Label playerName = new Label("NAMEEEEEEEEEEE",mySkin, "big");
        Label playerLvl = new Label("Lv.8",mySkin,"big");
        Label playerLvlCurrentBar = new Label("8",mySkin,"big");
        Label playerLvlBar = new Label("##############----",mySkin,"big");
        Label playerLvlNextBar = new Label("9",mySkin,"big");

        //Buttons for the itemMenu table, currently they do not do anything.
        Button hatButton = new TextButton("HAT",mySkin,"default");
        Button topButton = new TextButton("TOP",mySkin,"default");



        Texture itemImageTexture = new Texture("exampleItem.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(itemImageTexture));
        ImageButton itemButton = new ImageButton(drawable);


        //Save button, to be used to confirm a users choice of customisation
        Button saveButton = new TextButton("Save",mySkin,"default");
        saveButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

                return true;
            }
        });

        //Places the user back to the main menu.
        Button exitButton = new TextButton("Exit",mySkin,"default");
        exitButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });


        //Creates a table that covers the entire screen, and allows nested tables
        Table container = new Table();
        container.defaults().padTop(10F).padBottom(10F);
        container.setFillParent(true);

        // Creates the table for the player to choose colours, TODO: replace labels with buttons
        Table clrPicker = new Table();
        clrPicker.setDebug(false);
        clrPicker.add(c1Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c2Button).height(Gdx.graphics.getHeight()/14).pad(2f);
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

        // Creates the table for the players info
        // TODO: Replace labels with player data when added to the game.
        // TODO: Replace level bar with actual bar that represents the progress to next level

        Table playerInfoContainer = new Table();

        Table playerInfo = new Table();
        playerInfo.defaults().pad(5F);///////
        playerInfo.add(playerName).colspan(2).left();
        playerInfo.add(playerLvl);
        playerInfo.row();
        playerInfo.add(playerLvlCurrentBar);
        playerInfo.add(playerLvlBar);
        playerInfo.add(playerLvlNextBar);

        // TODO: Once items are implemented add preview of picked items for players
        Table playerDisplay = new Table();
        playerDisplay.add(playerDisplayImg);

        // Combines the player display and info a single table
        playerInfoContainer.add(playerInfo).height(Gdx.graphics.getHeight()/14*3);/////////////////
        playerInfoContainer.row();
        playerInfoContainer.add(playerDisplay).height(Gdx.graphics.getHeight()/14*11);////////////////


        // Creates the table to house the item menu side
        Table playerItemMenuContainer = new Table();

        // Creates the table that houses the items for players to pick.
        // TODO: Make buttons intractable
        // TODO: Combine with player preview to see items picked.
        Table playerItems = new Table();
        playerItems.defaults().pad(10F).width(Gdx.graphics.getWidth()/8).height(Gdx.graphics.getWidth()/8);/////////
        playerItems.add(itemButton);
        playerItems.add(itemButton);
        playerItems.add(itemButton);
        playerItems.row();
        playerItems.add(itemButton);
        playerItems.add(itemButton);
        playerItems.add(itemButton);

        // Table that houses the buttons for nav and items for selection.
        // TODO: Once items added make it so the two buttons swap items from Hats to Tops
        Table playerItemMenu = new Table();
        playerItemMenu.defaults();
        playerItemMenu.add(hatButton).padRight(Gdx.graphics.getWidth()/11);////////////
        playerItemMenu.add(topButton);
        playerItemMenu.row().padTop(Gdx.graphics.getWidth()/40);///////////////
        playerItemMenu.add(playerItems).colspan(2);

        Table navButtons = new Table();
        navButtons.defaults();
        navButtons.add(saveButton).padRight(Gdx.graphics.getWidth()/11);
        navButtons.add(exitButton);


        // Combines the tables with the nav buttons for the game.
        playerItemMenuContainer.defaults();
        playerItemMenuContainer.add(playerItemMenu).height(Gdx.graphics.getHeight()/10*8).width(Gdx.graphics.getHeight()/10*8);//////
        playerItemMenuContainer.row();
        playerItemMenuContainer.add(navButtons).height(Gdx.graphics.getHeight()/10*2).padBottom(Gdx.graphics.getHeight()/10).width(Gdx.graphics.getHeight()/10*8);///////////

        //Puts all the tables together in one table.
        container.add(clrPicker).width(Gdx.graphics.getWidth()/18).expandY();
        container.add(playerInfoContainer).width(Gdx.graphics.getWidth()/18*8).expandY();/////
        container.add(playerItemMenuContainer).width(Gdx.graphics.getWidth()/18*9).expandY();////

        stage.addActor(container);
        stage.setDebugAll(false);

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
