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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerCustomisation;

import java.util.ArrayList;

public class CustomiseScreen implements Screen {
    private static class ColorButtonInputListener extends InputListener {
        private final PlayerCustomisation customisation;
        private final ArrayList<Image> images;
        private final int colorIdx;

        public ColorButtonInputListener(PlayerCustomisation customisation, ArrayList<Image> images, int colorIdx) {
            this.customisation = customisation;
            this.images = images;
            this.colorIdx = colorIdx;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
            customisation.setColorSelection(colorIdx);
            for (Image image: images){
                image.setColor(customisation.getColor());
            }
            return true;
        }
    }

    protected final SocialGame game;
    protected final PlayerCustomisation customisation;
    public Stage stage;

    public CustomiseScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.customisation = this.game.customisation;

        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        addBackground();


        ////////////////////////////// ITEM BUTTONS //////////////////////////////


        //FIXME Change the colours of all the items in a neat and efficient way
        // -Change the colour of the sprite sheet
        // -Set up the items differently, call an array and loop to change the colour




        final TextureRegionDrawable hat1ColourButtonDraw = new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat1", 0));
        final ImageButton hat1Button = new ImageButton(hat1ColourButtonDraw);
        hat1Button.getImage().setColor(customisation.getColor());
        hat1Button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

            }
        });

        final TextureRegionDrawable hat2ColourButtonDraw = new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat2", 1));
        final ImageButton hat2Button = new ImageButton(hat2ColourButtonDraw);
        hat2Button.getImage().setColor(customisation.getColor());
        hat2Button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

            }
        });

        final Image hat2_top = new Image(game.wearablesSpriteSheet.findRegion("hat2", 0));
        WidgetGroup hat2 = new WidgetGroup();
        hat2.addActor(hat2Button);
        hat2.addActor(hat2_top);
        hat2Button.setSize(Gdx.graphics.getHeight()/5,Gdx.graphics.getHeight()/5);
        hat2_top.setSize(Gdx.graphics.getHeight()/5,Gdx.graphics.getHeight()/5);

        final TextureRegionDrawable hat3ColourButtonDraw = new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat3", 1));
        final ImageButton hat3Button = new ImageButton(hat3ColourButtonDraw);
        hat3Button.getImage().setColor(customisation.getColor());
        hat3Button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

            }
        });


        final Image hat3_top = new Image(game.wearablesSpriteSheet.findRegion("hat3", 0));
        WidgetGroup hat3 = new WidgetGroup();
        hat3.addActor(hat3Button);
        hat3.addActor(hat3_top);
        hat3Button.setSize(Gdx.graphics.getHeight()/5,Gdx.graphics.getHeight()/5);
        hat3_top.setSize(Gdx.graphics.getHeight()/5,Gdx.graphics.getHeight()/5);

        final TextureRegionDrawable hat4ColourButtonDraw = new TextureRegionDrawable(game.wearablesSpriteSheet.findRegion("hat4", 0));
        final ImageButton hat4Button = new ImageButton(hat4ColourButtonDraw);
        hat4Button.getImage().setColor(customisation.getColor());
        hat4Button.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                return true;
            }


            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */

            }
        });
        hat4Button.setSize(Gdx.graphics.getHeight()/6,Gdx.graphics.getHeight()/6);

        // Add ALL Clothing item buttons/interactables to an list for later use
        ArrayList<Image> clothingItems = new ArrayList<>();
        clothingItems.add(hat1Button.getImage());
        clothingItems.add(hat2Button.getImage());
        clothingItems.add(hat3Button.getImage());
        clothingItems.add(hat4Button.getImage());


        ///////////////////////////// COLOUR BUTTONS /////////////////////////////
        // Colour buttons images
        final Texture clrBtnTextureBase = new Texture("Base.png");// Base.png
        final Texture clrBtnTextureTick = new Texture("tick.png");   // tick.png
        final TextureRegionDrawable clrBtnDrawBase = new TextureRegionDrawable(clrBtnTextureBase);
        final TextureRegionDrawable clrBtnDrawTick = new TextureRegionDrawable(clrBtnTextureTick);

        // Buttons
        //TODO: Clean buttons code up, poss make a button class.
        final ImageButton c0Button = new ImageButton(clrBtnDrawBase, null,  clrBtnDrawTick);
        c0Button.getImage().setColor(customisation.getColor(0));

        final ImageButton c1Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c1Button.getImage().setColor(customisation.getColor(1));

        final ImageButton c2Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c2Button.getImage().setColor(customisation.getColor(2));

        final ImageButton c3Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c3Button.getImage().setColor(customisation.getColor(3));

        final ImageButton c4Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c4Button.getImage().setColor(customisation.getColor(4));

        final ImageButton c5Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c5Button.getImage().setColor(customisation.getColor(5));

        final ImageButton c6Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c6Button.getImage().setColor(customisation.getColor(6));

        final ImageButton c7Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c7Button.getImage().setColor(customisation.getColor(7));

        final ImageButton c8Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c8Button.getImage().setColor(customisation.getColor(8));

        final ImageButton c9Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c9Button.getImage().setColor(customisation.getColor(9));

        final ImageButton c10Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c10Button.getImage().setColor(customisation.getColor(10));

        final ImageButton c11Button = new ImageButton(clrBtnDrawBase,null,clrBtnDrawTick);
        c11Button.getImage().setColor(customisation.getColor(11));

        // Creates button group
        final ButtonGroup<ImageButton> colourButtons = new ButtonGroup<>(c0Button,c1Button,c2Button,c3Button,c4Button,c5Button,c6Button,c7Button,c8Button,c9Button,c10Button,c11Button);
        colourButtons.setMaxCheckCount(1);
        colourButtons.setMinCheckCount(1);
        colourButtons.setUncheckLast(true);
        System.out.println(customisation.getColor());

        // Add button input listeners
        Array<ImageButton> buttons = colourButtons.getButtons();
        for (int i = 0; i < buttons.size; i++) {
            ImageButton button = buttons.get(i);
            button.addListener(new ColorButtonInputListener(customisation, clothingItems, i));
            if (i == customisation.getColorSelection()) button.setChecked(true);
        }

        ///////////////////////////// PlayerInfo /////////////////////////////
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


        //Creates a table that covers the entire screen, and allows nested tables
        Table container = new Table();
        container.defaults().padTop(10F).padBottom(10F);
        container.setFillParent(true);

        // Creates the table for the player to choose colours
        Table clrPicker = new Table();
        clrPicker.setDebug(false);
        clrPicker.add(c0Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c1Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c2Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c3Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c4Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c5Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c6Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c7Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c8Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c9Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c10Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();
        clrPicker.add(c11Button).height(Gdx.graphics.getHeight()/14).pad(2f);
        clrPicker.row();

        // Creates the table for the players info
        //FIXME Player Info:  [DELAYED UNTIL REST OF GAME COMPLETE]
        // - Replace labels with player data when added to the game.
        // - Replace level bar with actual bar that represents the progress to next level.

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
        playerDisplay.add();

        // Combines the player display and info a single table
        playerInfoContainer.add(playerInfo).height(Gdx.graphics.getHeight()/14*3);/////////////////
        playerInfoContainer.row();
        playerInfoContainer.add(playerDisplay).height(Gdx.graphics.getHeight()/14*11);////////////////


        // Creates the table to house the item menu side
        Table playerItemMenuContainer = new Table();

        // Creates the table that houses the items for players to pick.
        //FIXME Item customisation and preview:
        // - Draw the player where the preview is.
        // - Remove the old preview.
        // - Draw hats on the player (start with bowtie).
        // - Draw tops on the player (need confirmation on whether or not they are to be implemented).
        // - Get player drawn in game to display customisation (poss give each player an idea, to enable to customisation to stay separate).
        // - Implement all items into the game, and make all items wearable.



        final Table hatShow = new Table();
        hatShow.defaults().pad(10F).width(Gdx.graphics.getWidth()/8).height(Gdx.graphics.getWidth()/8);
        hatShow.setVisible(true);
        hatShow.add(hat1Button);
        hatShow.add(hat2);
        hatShow.add(hat3);
        hatShow.row();
        hatShow.add(hat4Button);

        final Table topShow = new Table();
        topShow.defaults().pad(10F).width(Gdx.graphics.getWidth()/8).height(Gdx.graphics.getWidth()/8);
        topShow.setVisible(false);
        topShow.add(itemButton);
        topShow.add(itemButton);
        topShow.add(itemButton);
        topShow.row();
        topShow.add(itemButton);
        topShow.add(itemButton);
        topShow.add(itemButton);

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


        WidgetGroup hatAndTop = new WidgetGroup();
        hatAndTop.addActor(hatShow);
        hatAndTop.addActor(topShow);


        // Table that houses the buttons for nav and items for selection.
        Table playerItemMenu = new Table();
        playerItemMenu.defaults();
        playerItemMenu.add(hatButton).padRight(Gdx.graphics.getWidth()/11);
        playerItemMenu.add(topButton);
        playerItemMenu.row().padTop(Gdx.graphics.getWidth()/40);
        playerItemMenu.add(hatAndTop).colspan(2).padTop(Gdx.graphics.getHeight()/3).padBottom(Gdx.graphics.getHeight()/5);

        ///////////////////////////// NAV BUTTONS /////////////////////////////
        //Save button, to be used to confirm a users choice of customisation
        final Button saveButton = new TextButton("Save",mySkin,"default");
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

        Table navButtons = new Table();
        navButtons.defaults();
        navButtons.add(saveButton).padRight(Gdx.graphics.getWidth()/11);
        navButtons.add(exitButton);


        // Combines the tables with the nav buttons for the game.
        playerItemMenuContainer.defaults();
        playerItemMenuContainer.add(playerItemMenu).height(Gdx.graphics.getHeight()/10*8).width(Gdx.graphics.getHeight()/10*8);
        playerItemMenuContainer.row();
        playerItemMenuContainer.add(navButtons).height(Gdx.graphics.getHeight()/10*2).padBottom(Gdx.graphics.getHeight()/10).width(Gdx.graphics.getHeight()/10*8);

        //Puts all the tables together in one table.
        container.add(clrPicker).width(Gdx.graphics.getWidth()/18).expandY();
        container.add(playerInfoContainer).width(Gdx.graphics.getWidth()/18*8).expandY();
        container.add(playerItemMenuContainer).width(Gdx.graphics.getWidth()/18*9).expandY();

        stage.addActor(container);
        stage.setDebugAll(false);

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
        Player player = new Player(game);
        stage.addActor(player);
        player.setSize(1/5f * Gdx.graphics.getWidth(), 1/2f * Gdx.graphics.getHeight());
        player.setPosition(200, 150);//FIXME: Change position after merge, scaling will be fixed then.
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
