package com.socialgame.game.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.Disposable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.player.Player;
import com.socialgame.game.screens.GameScreen;

/**
 * Class from which all tasks are derived.
 * Functions as a factory, call the createTask method to create a Task
 * Extension of Interactable class
 */
public abstract class Task extends Interactable implements Disposable {
    /**
     * Actor extension to act as task background (common to all tasks)
     */
    private static class BGActor extends Actor implements Disposable {
        private final Texture texture;
        public BGActor() {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.GRAY);
            pixmap.fill();
            texture = new Texture(pixmap);
            pixmap.dispose();

            this.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.draw(this.texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }

        @Override
        public void dispose() {
            this.texture.dispose();
        }
    }

    protected final Group group;

    private boolean complete = false;
    private boolean failed = false;

    public Task(SocialGame game, float x, float y) {
        super(game, x, y, 1, 1);
        texture = game.spriteSheet.findRegion("placeholder");

        group = new Group();
        close();

        // Add common elements (needed for all tasks)
        ImageButtonStyle style = new ImageButtonStyle();
        ImageButton closeBtn = new ImageButton(style);
        closeBtn.setBounds(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() - 30, 30, 30);

        // Add InputListener to close task when close button is pressed
        closeBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });

        group.addActor(new BGActor());
        group.addActor(closeBtn);

        // Add group to UI Stage, TODO: Add method to game that does this to reduce the cross dependencies
        ((GameScreen) game.getScreen()).uiStage.addActor(group);
    }

    public Task(SocialGame game) {
        this(game, 0, 0);
    }

    /**
     * Method to be called on task completion
     */
    public void onComplete() {
        complete = true;
        close();
    }

    /**
     * Method to be called on task failure
     */
    public void onFail() {
        complete = true;
        failed = true;
        close();
    }

    public void open() {
        group.setVisible(true);
    }

    public void close() {
        group.setVisible(false);
    }

    public boolean isOpen() {
        return group.isVisible();
    }

    public boolean isComplete() { return complete; }

    public boolean isFailed() { return failed; }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player && !complete){
            open();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isFailed())
            onFail();
        else if (isComplete())
            onComplete();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {
        ((GameScreen) game.getScreen()).uiStage.getActors().removeValue(group, true);
    }
}
