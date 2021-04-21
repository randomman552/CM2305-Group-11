package com.socialgame.game.HUD;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;

public class HUD extends Group {
    protected final SocialGame game;

    private final ProgressBar progressBar;
    private final ProgressBar hazardBar;
    private final ImageButton interactButton;
    private final ImageButton mapButton;
    private final ImageButton dropButton;

    public HUD(SocialGame game) {
        this.game = game;

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
        com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle progressBarStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        progressBarStyle.background = background;
        progressBarStyle.knob = progressForeground;
        progressBarStyle.knobBefore = progressForeground;

        // Create progress bar
        progressBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
        progressBar.setValue(0);
        progressBar.setAnimateDuration(0.25f);
        progressBar.setBounds(10, 10, 100, 30);

        // Set up hazard bar
        com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle hazardBarStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        hazardBarStyle.knob = hazardForeground;
        hazardBarStyle.knobBefore = hazardForeground;

        // Create hazard bar
        hazardBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, hazardBarStyle);
        hazardBar.setValue(0f);
        hazardBar.setAnimateDuration(0.25f);
        hazardBar.setBounds(10, 10, 100, 10);




        TextureRegionDrawable interactButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("killButton"));

        interactButton = new ImageButton(interactButtonDrawable);

        interactButton.setPosition(2f + 1100f, 10f);
        interactButton.setSize(100f, 100f);
        interactButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        TextureRegionDrawable mapButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("killButton"));

        mapButton = new ImageButton(mapButtonDrawable);

        mapButton.setPosition(2f + 1100f, 600f);
        mapButton.setSize(100f, 100f);
        mapButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        TextureRegionDrawable dropButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("killButton"));

        dropButton = new ImageButton(dropButtonDrawable);

        dropButton.setPosition(2f + 900f, 10f);
        dropButton.setSize(100f, 100f);
        dropButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        //Add actors to this HUD group
        addActor(progressBar);
        addActor(hazardBar);
        addActor(interactButton);
        addActor(mapButton);
        addActor(dropButton);
    }

    public void incrementProgress() {
        float step = 1f / game.getTasks().size();
        float curStep = progressBar.getValue() * game.getTasks().size();
        progressBar.setValue(curStep + step);
    }

    public void incrementHazard() {
        float step = 1f / game.getTasks().size();
        float curStep = progressBar.getValue() * game.getTasks().size();
        hazardBar.setValue(curStep + step);
    }
}
