package com.socialgame.game.HUD;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.socialgame.game.SocialGame;
import org.graalvm.compiler.phases.common.NodeCounterPhase;
import org.w3c.dom.Text;

import javax.swing.event.ChangeEvent;

public class HUD extends Group {
    protected SocialGame game;


    public ProgressBar progressBar;
    public ProgressBar hazardBar;
    private ImageButton killButton;

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
        progressBar.setValue(0.6f);
        progressBar.setAnimateDuration(0.25f);
        progressBar.setBounds(10, 10, 100, 30);

        // Set up hazard bar
        com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle hazardBarStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        hazardBarStyle.knob = hazardForeground;
        hazardBarStyle.knobBefore = hazardForeground;

        // Create hazard bar
        hazardBar = new ProgressBar(0.0f, 1.0f, 0.01f, false, hazardBarStyle);
        hazardBar.setValue(0.2f);
        hazardBar.setAnimateDuration(0.25f);
        hazardBar.setBounds(10, 10, 100, 10);

        //Add actors to this HUD group
        addActor(progressBar);
        addActor(hazardBar);


        TextureRegionDrawable killButtonDrawable = new TextureRegionDrawable(game.spriteSheet.findRegion("killButton"));

        killButton = new ImageButton(killButtonDrawable);

        killButton.setPosition(2f +100f,
                2f - 50f);
        killButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });

        addActor(killButton);





    }

}
