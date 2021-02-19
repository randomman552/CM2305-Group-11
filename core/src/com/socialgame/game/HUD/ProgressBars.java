package com.socialgame.game.HUD;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.w3c.dom.Text;

public class ProgressBars extends Actor{


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    private void progressBar(){

        //Below we are creating a pixmap, the background and then overlaying a drawable knob over it, this will leave
        //a drawn trail behind it, so that it appears to be a progress bar
        //This progress bar will be accessed when a task is completed and the value will be changed to represent the
        //progress of the players.
        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888); // creation of the pixmap for the bar
        pixmap.setColor(Color.GRAY); // The background for the main progress bar will we grey, THIS CAN BE CHANGED
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle progressBarStyle = new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        progressBarStyle.background = drawable;
        pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        progressBarStyle.knob = drawable;
        Pixmap pixmap1 = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap1.setColor(Color.GREEN);
        pixmap1.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap1)));
        pixmap1.dispose();
        progressBarStyle.knobBefore = drawable;
        Stage stage;
        stage = new Stage();
        //To change the value of the bar, access the PlayersProgress value and reset it using the setValue() method.
        //1.0 is full and 0 is empty
        ProgressBar PlayersProgress = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
        PlayersProgress.setValue(1.0f);
        PlayersProgress.setAnimateDuration(0.25f);
        PlayersProgress.setBounds(10, 10, 100, 20);
        stage.addActor(PlayersProgress);
        stage.draw();
        stage.act();


    }

    private void hazardBar(){

        Pixmap pixmap = new Pixmap(10, 5, Pixmap.Format.RGBA8888); // creation of the pixmap for the bar
        // The hazard bar is half of the size of the progress bar, so that it can still be seen if it overlaps it.
        pixmap.setColor(Color.WHITE); // The background for the main hazard bar will we WHITE, THIS CAN BE CHANGED
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle progressBarStyle = new com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle();
        progressBarStyle.background = drawable;
        pixmap = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();
        progressBarStyle.knob = drawable;
        Pixmap pixmap1 = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixmap1.setColor(Color.RED);
        pixmap1.fill();
        drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap1)));
        pixmap1.dispose();
        progressBarStyle.knobBefore = drawable;
        Stage stage;
        stage = new Stage();
        //To change the value of the bar, access the HazardProgress value and reset it using the setValue() method.
        //1.0 is full and 0 is empty
        ProgressBar HazardProgress = new ProgressBar(0.0f, 1.0f, 0.01f, false, progressBarStyle);
        HazardProgress.setValue(1.0f);
        HazardProgress.setAnimateDuration(0.25f);
        HazardProgress.setBounds(10, 10, 100, 20);
        stage.addActor(HazardProgress);
        stage.draw();
        stage.act();

        //Many of these terms say progressbar, and that is because the progress and hazard bar are part of the
        //same feature

    }
}
