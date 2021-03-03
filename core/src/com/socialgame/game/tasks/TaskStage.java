package com.socialgame.game.tasks;

import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class TaskStage extends Stage {
    protected boolean complete = false;
    protected boolean failed = false;
    protected boolean isOpen = false;


    public boolean isComplete() {
        return complete;
    }

    public boolean isFailed() {
        return failed;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }

    @Override
    public void act(float delta) {
        if (isOpen) super.act(delta);
    }

    @Override
    public void draw() {
        if (isOpen) super.draw();
    }
}
