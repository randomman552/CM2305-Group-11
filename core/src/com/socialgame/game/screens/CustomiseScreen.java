package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerCustomisation;
import com.socialgame.game.player.clothing.Hat;

import java.util.ArrayList;

public class CustomiseScreen implements Screen {
    /**
     * Input listener to be placed on all color selection buttons
     * This is defined as it's own class to avoid repeating code with anonymous classes.
     */
    private static class ColorButtonInputListener extends InputListener {
        private final PlayerCustomisation customisation;
        private final ArrayList<Actor> actors;
        private final int colorIdx;

        public ColorButtonInputListener(PlayerCustomisation customisation, ArrayList<Actor> actors, int colorIdx) {
            this.customisation = customisation;
            this.actors = actors;
            this.colorIdx = colorIdx;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
            customisation.setColorSelection(colorIdx);
            for (Actor actor: actors){
                actor.setColor(customisation.getColor());
            }
            return true;
        }
    }

    /**
     * Input listener to be placed on all hat selection buttons
     * This is defined as it's own class to avoid repeating code with anonymous classes.
     */
    private static class HatButtonInputListener extends InputListener {
        private final PlayerCustomisation customisation;
        private final int hatIdx;

        public HatButtonInputListener(PlayerCustomisation customisation, int hatIdx) {
            this.customisation = customisation;
            this.hatIdx = hatIdx;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            customisation.setHatSelection(hatIdx);
            return true;
        }
    }

    protected final SocialGame game;
    protected final PlayerCustomisation customisation;
    public Stage stage;

    public CustomiseScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        stage.setDebugAll(true);
        this.customisation = this.game.customisation;

        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        addBackground();

        // Array list of all HUD elements to change the color of when selecting a new color
        // All items of clothing should be added to this array
        ArrayList<Actor> toChangeColor = new ArrayList<>();

        // region Hat and top selectors creation

        // region Hat buttons

        // Create hat selection buttons and add to table
        final Table hatShow = new Table();
        Hat[] hats = new Hat[6];
        for (int i = 0; i < hats.length; i++) {
            // Create hat and set it's type
            hats[i] = new Hat(game);
            hats[i].setHatType(i);
            hats[i].addListener(new HatButtonInputListener(customisation, i));
            hats[i].setColor(customisation.getColor());
            toChangeColor.add(hats[i]);

            // Add to table, break into rows if we have reached 3rd element
            if (i % 3 == 0)
                hatShow.row();
            hatShow.add(hats[i]).size(150);
        }
        hatShow.setVisible(true);

        //endregion

        // region Top buttons

        // TODO: Tops

        Texture itemImageTexture = new Texture("exampleItem.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(itemImageTexture));
        ImageButton itemButton = new ImageButton(drawable);

        // Top table placeholder
        final Table topShow = new Table();
        topShow.setVisible(false);
        topShow.add(itemButton);
        topShow.add(itemButton);
        topShow.add(itemButton);
        topShow.row();
        topShow.add(itemButton);
        topShow.add(itemButton);
        topShow.add(itemButton);

        //endregion

        WidgetGroup hatAndTop = new WidgetGroup();
        hatAndTop.setSize(450, 300);
        hatAndTop.addActor(hatShow);
        hatAndTop.addActor(topShow);

        // endregion

        // region Color buttons

        final Texture clrBtnTextureBase = new Texture("Base.png");      // Base.png
        final Texture clrBtnTextureTick = new Texture("tick.png");      // Tick.png
        final TextureRegionDrawable clrBtnDrawBase = new TextureRegionDrawable(clrBtnTextureBase);
        final TextureRegionDrawable clrBtnDrawTick = new TextureRegionDrawable(clrBtnTextureTick);

        // Create color selection buttons and add them to table
        Table colorPicker = new Table();
        ButtonGroup<ImageButton> colorButtonGroup = new ButtonGroup<>();
        for (int i = 0; i < 12; i++) {
            // Create button, set its color and add listeners
            ImageButton button = new ImageButton(clrBtnDrawBase, null,  clrBtnDrawTick);
            button.getImage().setColor(customisation.getColor(i));
            colorButtonGroup.add(button);
            button.addListener(new ColorButtonInputListener(customisation, toChangeColor, i));
            if (i == customisation.getColorSelection()) button.setChecked(true);

            // Add button to color picker table
            colorPicker.add(button).height(Gdx.graphics.getHeight()/14f).pad(2f);
            colorPicker.row();
        }
        colorButtonGroup.setMaxCheckCount(1);
        colorButtonGroup.setMinCheckCount(1);
        colorButtonGroup.setUncheckLast(true);

        // endregion

        //FIXME Player Info:  [DELAYED UNTIL REST OF GAME COMPLETE]
        // - Replace labels with player data when added to the game.
        // - Replace level bar with actual bar that represents the progress to next level.
        // - Draw tops on the player (need confirmation on whether or not they are to be implemented).
        // - Implement all items into the game, and make all items wearable.

        // region Player info

        // region Player information table
        Table playerInfoContainer = new Table();

        //TEMP: Labels for the playerInfo
        Label playerName = new Label("NAMEEEEEEEEEEE",mySkin, "big");
        Label playerLvl = new Label("Lv.8",mySkin,"big");
        Label playerLvlCurrentBar = new Label("8",mySkin,"big");
        Label playerLvlBar = new Label("##############----",mySkin,"big");
        Label playerLvlNextBar = new Label("9",mySkin,"big");

        //Creates a table that covers the entire screen, and allows nested tables
        Table container = new Table();
        container.defaults().padTop(10F).padBottom(10F);
        container.setFillParent(true);

        Table playerInfo = new Table();
        playerInfo.defaults().pad(5F);///////
        playerInfo.add(playerName).colspan(2).left();
        playerInfo.add(playerLvl);
        playerInfo.row();
        playerInfo.add(playerLvlCurrentBar);
        playerInfo.add(playerLvlBar);
        playerInfo.add(playerLvlNextBar);

        // endregion

        // Preview of players current equipment
        Player playerPreview = new Player(game, customisation);

        // Combines the player display and info a single table
        playerInfoContainer.add(playerInfo).height(Gdx.graphics.getHeight()/14f*3);
        playerInfoContainer.row();
        playerInfoContainer.add(playerPreview).size(350, 525);

        // Creates the table to house the item menu side
        Table playerItemMenuContainer = new Table();

        // endregion

        // region Mode switch creation

        Button hatButton = new TextButton("HAT",mySkin,"default");
        Button topButton = new TextButton("TOP",mySkin,"default");
        hatButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                hatShow.setVisible(true);
                topShow.setVisible(false);
                return true;
            }
        });
        topButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                hatShow.setVisible(false);
                topShow.setVisible(true);
                return true;
            }
        });

        Table modeSwitches = new Table();
        modeSwitches.defaults();
        modeSwitches.add(hatButton);
        modeSwitches.add().expandX();
        modeSwitches.add(topButton);

        //endregion

        // region Navigation buttons

        // Save button
        final Button saveButton = new TextButton("Save",mySkin,"default");
        saveButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

                return true;
            }
        });

        // Exit button
        Button exitButton = new TextButton("Exit",mySkin,"default");
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        });


        Table navButtons = new Table();
        navButtons.add(saveButton);
        navButtons.add().expandX();
        navButtons.add(exitButton);

        //endregion

        // region Final table assembly

        // Combine mode switches with clothing selection and navigation buttons
        playerItemMenuContainer.add(modeSwitches).pad(Gdx.graphics.getHeight()/10f).fillX();
        playerItemMenuContainer.row();
        playerItemMenuContainer.add(hatAndTop).expand();
        playerItemMenuContainer.row();
        playerItemMenuContainer.add(navButtons).pad(Gdx.graphics.getHeight()/10f).fillX();

        //Puts all the tables together in one table.
        container.add(colorPicker).width(Gdx.graphics.getWidth()/18f).fillY();
        container.add(playerInfoContainer).width(Gdx.graphics.getWidth()/18f*8).expandY().top();
        container.add(playerItemMenuContainer).width(Gdx.graphics.getWidth()/18f*9).fillY();

        stage.addActor(container);

        // endregion
    }


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
