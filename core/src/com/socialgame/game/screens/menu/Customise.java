package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerCustomisation;
import com.socialgame.game.player.clothing.Hat;

import java.util.ArrayList;

public class Customise extends BaseMenuScreen {
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

        public int getHatIdx() {
            return hatIdx;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            customisation.setHatSelection(hatIdx);
            return true;
        }
    }

    protected final PlayerCustomisation customisation;
    private final Hat hatPreview;

    public Customise(final SocialGame game) {
        super(game);
        this.customisation = this.game.customisation;

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





        WidgetGroup hatAndTop = new WidgetGroup();
        hatAndTop.setSize(450, 300);
        hatAndTop.addActor(hatShow);

        // endregion

        // region Color buttons

        final TextureAtlas.AtlasRegion clrBtnTextureBase = game.menuSpriteSheet.findRegion("checkbox_base");
        final TextureAtlas.AtlasRegion clrBtnTextureTick = game.menuSpriteSheet.findRegion("checkbox_tick");
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
        Label playerName = new Label("NAMEEEEEEEEEEE",skin, "big");
        Label playerLvl = new Label("Lv.8",skin,"big");
        Label playerLvlCurrentBar = new Label("8",skin,"big");
        Label playerLvlBar = new Label("##############----",skin,"big");
        Label playerLvlNextBar = new Label("9",skin,"big");

        //Creates a table that covers the entire screen, and allows nested tables
        Table container = new Table();
        container.defaults().padTop(10F).padBottom(10F);
        container.setFillParent(true);

        Table playerInfo = new Table();
        playerInfo.defaults().pad(5F);
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

        // Create hat preview (free floating from table)
        hatPreview = new Hat(game);
        hatPreview.setPosition(Gdx.graphics.getWidth()/28f*5,Gdx.graphics.getHeight()/18f*11);
        hatPreview.setSize(Gdx.graphics.getWidth()/6f,Gdx.graphics.getWidth()/6f);
        toChangeColor.add(hatPreview);

        // Creates the table to house the item menu side
        Table playerItemMenuContainer = new Table();

        // endregion

        // region Mode switch creation

        Button hatButton = new TextButton("HAT",skin,"default");
        Button topButton = new TextButton("TOP",skin,"default");
        hatButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                hatShow.setVisible(true);
                return true;
            }
        });

        //Disabled due to focusing on other aspects of the game. Only hats available atm.
        /*topButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { *//* touchDown = hovering over button *//*
                hatShow.setVisible(false);
                topShow.setVisible(true);
                return true;
            }
        });*/

        Table modeSwitches = new Table();
        modeSwitches.defaults();
        modeSwitches.add(hatButton);
        modeSwitches.add().expandX();
        modeSwitches.add(topButton);

        //endregion

        // region Navigation buttons

        // Exit button
        Button exitButton = new TextButton("Exit",skin,"default");
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                customisation.save();
                game.setScreen(new Main(game));
                return true;
            }
        });


        Table navButtons = new Table();
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
        stage.addActor(hatPreview);

        // endregion
    }
}
