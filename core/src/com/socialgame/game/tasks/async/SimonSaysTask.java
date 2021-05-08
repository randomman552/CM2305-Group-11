package com.socialgame.game.tasks.async;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Simon says task based on the task with the same name from the game "Keep Talking and Nobody Explodes"
 * https://ktane.fandom.com/wiki/Simon_Says
 */
public class SimonSaysTask extends Task {
    private static class ButtonInputListener extends InputListener {
        private final SimonSaysTask task;
        private final int btnNum;

        public ButtonInputListener(SimonSaysTask task, int btnNum) {
            this.task = task;
            this.btnNum = btnNum;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            task.playBeep(btnNum);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            ((ImageButton) event.getListenerActor()).setChecked(false);
            task.checkInput(btnNum);
        }
    }

    /**
     * Length of the randomly generated sequence
     */
    private static final int SEQUENCE_LENGTH = 6;

    // Sequence storage variables
    private final ArrayList<Integer> desiredSequence;
    private final ArrayList<Button> buttons;
    private int curSequenceIdx = 0;

    // Sequence playing variables
    private boolean playingSequence = false;
    private float lastPlayedTime = 0;
    private final float timePerTile = 1;
    private int curPlayedIdx = 0;

    public SimonSaysTask(SocialGame game, float x, float y) {
        super(game, x, y);
        lastPlayedTime = game.elapsedTime;

        //region Button colours
        Color c1 = new Color(1, 0, 0, 1);
        Color c2 = new Color(0, 1, 0, 1);
        Color c3 = new Color(0, 0, 1, 1);
        Color c4 = new Color(1, 1, 0, 1);
        //endregion

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
        rBtn.addListener(new ButtonInputListener(this, 0));
        //endregion

        //region Create green button
        pixmap.setColor(c2);
        pixmap.fill();
        TextureRegionDrawable gBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c2.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable gBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton gBtn = new ImageButton(gBtnUp, gBtnDown, gBtnDown);
        gBtn.addListener(new ButtonInputListener(this, 1));
        //endregion

        //region Create blue button
        pixmap.setColor(c3);
        pixmap.fill();
        TextureRegionDrawable bBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c3.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable bBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton bBtn = new ImageButton(bBtnUp, bBtnDown, bBtnDown);
        bBtn.addListener(new ButtonInputListener(this, 2));
        //endregion

        //region Create yellow button
        pixmap.setColor(c4);
        pixmap.fill();
        TextureRegionDrawable yBtnUp = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.setColor(c4.mul(0.5f));
        pixmap.fill();
        TextureRegionDrawable yBtnDown = new TextureRegionDrawable(new Texture(pixmap));

        ImageButton yBtn = new ImageButton(yBtnUp, yBtnDown, yBtnDown);
        yBtn.addListener(new ButtonInputListener(this, 3));
        //endregion

        //region Create inner table
        Table innerTable = new Table();
        innerTable.add(rBtn).size(150);
        innerTable.add(gBtn).size(150);
        innerTable.row();
        innerTable.add(bBtn).size(150);
        innerTable.add(yBtn).size(150);

        // Store buttons in array for later use
        buttons = new ArrayList<Button>(Arrays.asList(rBtn, gBtn, bBtn, yBtn));
        //endregion

        // Add to main table
        table.row().fill();
        table.add(innerTable);

        // Dispose pixmap to prevent memory leaks
        pixmap.dispose();

        //region Generate random sequence
        desiredSequence = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < SEQUENCE_LENGTH; i++) {
            desiredSequence.add(random.nextInt(buttons.size()));
        }

        //endregion
    }

    /**
     * Method to be called to begin the sequence playing.
     */
    private void playSequence() {
        playingSequence = true;
        lastPlayedTime = game.elapsedTime - timePerTile;
        curSequenceIdx = 0;
        for (Button button: buttons) {
            button.setTouchable(Touchable.disabled);
        }
    }

    private void handlePlaySequence() {
        if (lastPlayedTime + timePerTile <= game.elapsedTime) {
            int curButtonIdx = desiredSequence.get(Math.min(curPlayedIdx, desiredSequence.size() - 1));
            int prevButtonIdx = desiredSequence.get(Math.max(curPlayedIdx - 1, 0));
            Button curButton = buttons.get(curButtonIdx);
            Button prevButton = buttons.get(prevButtonIdx);

            prevButton.setChecked(false);
            curButton.setChecked(true);

            lastPlayedTime = game.elapsedTime;
            curPlayedIdx++;

            if (curPlayedIdx > desiredSequence.size()) {
                playingSequence = false;
                curPlayedIdx = 0;
                for (Button button: buttons) {
                    button.setTouchable(Touchable.enabled);
                    button.setChecked(false);
                }
            } else if (isOpen()) {
                playBeep(curButtonIdx);
            }
        }
    }

    private void checkInput(int val) {
        if (desiredSequence.get(curSequenceIdx) != val) {
            curSequenceIdx = 0;
            playSequence();
            return;
        }

        // Check if we have reached the end of the desired sequence.
        curSequenceIdx++;
        if (curSequenceIdx >= desiredSequence.size()) {
            setComplete(true);
        }
    }

    private void playBeep(int btnNum) {
        float pitch = (btnNum + 1) / 2f;
        game.playSound("beep", pitch, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (playingSequence) handlePlaySequence();
    }

    @Override
    public void open() {
        playSequence();
        super.open();
    }
}
