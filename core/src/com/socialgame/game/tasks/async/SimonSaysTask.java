package com.socialgame.game.tasks.async;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.tasks.Task;

/**
 * Simon says task based on the task with the same name from the game "Keep Talking and Nobody Explodes"
 * https://ktane.fandom.com/wiki/Simon_Says
 */
public class SimonSaysTask extends Task {
    public SimonSaysTask(SocialGame game, float x, float y) {
        super(game, x, y);

        // Define colours for use with buttons
        Color c1 = new Color(1, 0, 0, 1);
        Color c2 = new Color(0, 1, 0, 1);
        Color c3 = new Color(0, 0, 1, 1);
        Color c4 = new Color(1, 1, 0, 1);

        // Pixmap (reused for setting all button colours)
        Pixmap pixmap = new Pixmap(150, 150, Pixmap.Format.RGBA8888);

        //region Create red button
        pixmap.setColor(c1);
        pixmap.fill();
        TextureRegionDrawable rBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c1.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable rBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton rBtn = new ImageButton(rBtnUp, rBtnDown, rBtnDown);
        //endregion

        //region Create green button
        pixmap.setColor(c2);
        pixmap.fill();
        TextureRegionDrawable gBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c2.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable gBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton gBtn = new ImageButton(gBtnUp, gBtnDown, gBtnDown);
        //endregion

        //region Create blue button
        pixmap.setColor(c3);
        pixmap.fill();
        TextureRegionDrawable bBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c3.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable bBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton bBtn = new ImageButton(bBtnUp, bBtnDown, bBtnDown);
        //endregion

        //region Create yellow button
        pixmap.setColor(c4);
        pixmap.fill();
        TextureRegionDrawable yBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c4.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable yBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton yBtn = new ImageButton(yBtnUp, yBtnDown, yBtnDown);
        //endregion

        //region Create inner table
        Table innerTable = new Table();
        innerTable.add(rBtn).size(150);
        innerTable.add(gBtn).size(150);
        innerTable.row();
        innerTable.add(bBtn).size(150);
        innerTable.add(yBtn).size(150);
        //endregion

        // Add to main table
        table.row().fill();
        table.add(innerTable);

        // Dispose of no longer required objects to prevent memory leaks
        pixmap.dispose();
    }
}
