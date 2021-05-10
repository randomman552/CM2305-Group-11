package com.socialgame.game.HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;
import com.socialgame.game.networking.GameServer;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;
import java.util.concurrent.TimeoutException;


public class HUD extends Group {
    protected final SocialGame game;

    private final ProgressBar progressBar;
    private final ProgressBar hazardBar;
    private final ImageButton interactButton;
    private final ImageButton mapButton;
    private final Table mapTable;
    private final ImageButton dropButton;
    private final TextChat chat;


    public HUD(final SocialGame game) {
        this.game = game;

        // region Progress bars

        // Create Pixmap objects to define colours for progress and hazard bar
        Pixmap progressBackgroundPixmap = new Pixmap(1, 30, Pixmap.Format.RGBA8888);
        progressBackgroundPixmap.setColor(Color.GRAY);
        progressBackgroundPixmap.fill();

        Pixmap progressForegroundPixmap = new Pixmap(1, 30, Pixmap.Format.RGBA8888);
        progressForegroundPixmap.setColor(Color.GREEN);
        progressForegroundPixmap.fill();

        Pixmap hazardForegroundPixmap = new Pixmap(1, 10, Pixmap.Format.RGBA8888);
        hazardForegroundPixmap.setColor(Color.RED);
        hazardForegroundPixmap.fill();

        // Create texture regions for progress bars to use
        TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(
                new Texture(progressBackgroundPixmap)));
        TextureRegionDrawable progressForeground = new TextureRegionDrawable(new TextureRegion(
                new Texture(progressForegroundPixmap)));
        TextureRegionDrawable hazardForeground = new TextureRegionDrawable(new TextureRegion(
                new Texture(hazardForegroundPixmap)));

        // Set up progress bar
        ProgressBar.ProgressBarStyle progressBarStyle =new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        progressBarStyle.background = background;
        progressBarStyle.knob = progressForeground;
        progressBarStyle.knobBefore = progressForeground;

        // Create progress bar
        progressBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
        progressBar.setValue(0);
        progressBar.setAnimateDuration(0.25f);
        progressBar.setBounds(10, 680, 100, 30);

        // Set up hazard bar
        ProgressBar.ProgressBarStyle hazardBarStyle = new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        hazardBarStyle.knob = hazardForeground;
        hazardBarStyle.knobBefore = hazardForeground;

        // Create hazard bar
        hazardBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, hazardBarStyle);
        hazardBar.setValue(0f);
        hazardBar.setAnimateDuration(0.25f);
        hazardBar.setBounds(10, 700, 100, 10);

        // endregion

        // region Interact button

        TextureRegionDrawable interactButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("use"));
        interactButton = new ImageButton(interactButtonDrawable);

        interactButton.setPosition(2f + 1100f, 10f);
        interactButton.setSize(100f, 100f);
        interactButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // FIXME: 04/05/2021 Horribly inefficient
                Player player = ((Player) game.mainPlayer);
                ArrayList<GameObject> toIgnore = new ArrayList<>();
                toIgnore.add(player);

                // Add all players and their items to ignored list
                for (int i = 0; i < GameServer.MAX_PLAYERS; i++) {
                    Player ignoredPlayer = ((Player) GameObject.objects.get(i));
                    if (ignoredPlayer != null) {
                        toIgnore.addAll(Arrays.asList(ignoredPlayer.inventory));
                    }
                }
                
                // Iterate over all objects, and find the closest one
                Interactable closest = null;
                float closestDist = Float.MAX_VALUE;
                for (GameObject object: GameObject.objects.values()) {
                    if (object instanceof Interactable && !toIgnore.contains(object)) {
                        float dist = (float) Math.sqrt(Math.pow(object.getX() - player.getX(), 2) + Math.pow(object.getY() - player.getY(), 2));
                        if (dist < closestDist) {
                            closest = ((Interactable) object);
                            closestDist = dist;
                        }
                    }
                }

                if (closest != null && closestDist < 2f){
                    closest.interact(player);
                }

                // Update on serverside if required
                if (closest instanceof Item) {
                    game.getClient().sendTCP(Networking.pickupItemUpdate(game.mainPlayer.getID(), closest.getID()));
                }
                return true;
            }
        });

        // endregion

        // region Map button

        TextureRegionDrawable mapButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("mapSprite"));
        mapButton = new ImageButton(mapButtonDrawable);
        mapButton.setPosition(2f + 1100f, 600f);
        mapButton.setSize(100f, 100f);
        mapButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                toggleMap();
                return true;
            }
        });

        // region Map Table

        // Create close button

        TextureRegionDrawable closeButton = new TextureRegionDrawable(game.spriteSheet.findRegion("close button"));
        //ImageButton.ImageButtonStyle style = new ImageButton(closeButton);
        ImageButton mapCloseButton = new ImageButton(closeButton);
        mapCloseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hideMap();
                return true;
            }
        });

        mapTable = new Table();
        hideMap();

        // Define table size
        float width = 1280;
        float height = 720;
        float marginX = width / 20;
        float marginY = height / 20;
        mapTable.setBounds(marginX, marginY, width - (marginX * 2), height - (marginY * 2));

        // Get image to display
        Image mapImage = new Image(game.spriteSheet.findRegion("map"));
        //Turns map slightly transparent
        mapImage.setColor(1,1,1,.75f);

        // Define base table layout
        mapTable.row().height(50);
        mapTable.add().width(50);
        mapTable.add().expandX();
        mapTable.add(mapCloseButton).width(50);

        mapTable.row().expandY();
        mapTable.add().width(50);
        mapTable.add(mapImage);
        mapTable.add().width(50);

        mapTable.row().height(50);
        mapTable.add().width(50);
        mapTable.add().expandX();
        mapTable.add().width(50);

        // endregion

        // endregion

        // region Drop button
        TextureRegionDrawable dropButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("drop"));

        dropButton = new ImageButton(dropButtonDrawable);

        dropButton.setPosition(2f + 900f, 10f);
        dropButton.setSize(100f, 100f);
        dropButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((Player) game.mainPlayer).dropItem();
                return true;
            }
        });

        // endregion

        // region Text chat component

        chat = new TextChat(game);
        chat.setPosition(10, 10);

        // endregion

        // region Add actors to group

        addActor(progressBar);
        addActor(hazardBar);
        addActor(interactButton);
        addActor(mapButton);
        addActor(dropButton);
        addActor(mapTable);
        addActor(chat);

        // endregion
    }

    public void incrementProgress() {
        float step = 1f / game.getTasks().size();
        float curStep = progressBar.getValue() * game.getTasks().size();
        progressBar.setValue(curStep + step);
    }

    public void incrementHazard() {
        //TODO make end statement when bar is full
        float step = 1f / game.getTasks().size();
        float curStep = hazardBar.getValue() * game.getTasks().size();
        hazardBar.setValue(curStep + step);
    }


    public void setHazardBar(){
        //TODO call this when a game starts

        Thread thread = new Thread(){



            @Override
            public void run(){

                while (true){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    float curValue = hazardBar.getValue();
                    hazardBar.setValue(curValue + 0.05f);
                }
            }

        };
        thread.start();
    }


    public void toggleMap() {
        mapTable.setVisible(!mapTable.isVisible());
    }

    public void showMap() {
        mapTable.setVisible(true);
    }

    public void hideMap() {
        mapTable.setVisible(false);
    }


    public void receiveMessage(String sender, String message) {
        chat.receiveMessage(sender, message);
    }

    public TextChat getChat() {
        return chat;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        synchronized (this) {
            super.draw(batch, parentAlpha);
        }
    }
}
