package com.socialgame.game.tasks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.player.Player;
import com.socialgame.game.tasks.async.Maze;

/**
 * Class from which all tasks are derived.
 * Functions as a factory, call the createTask method to create a Task
 * Extension of Interactable class
 */
public class Task extends Interactable {
    public boolean open = false;
    protected final TaskStage taskStage;
    protected boolean done = false;

    /**
     * Create a new Task instance at the given world coordinates with the given task stage
     * @param game Game instance
     * @param taskStage Stage to use for task
     * @param x X coordinate to spawn at
     * @param y Y coordinate to spawn at
     * @return A new Task object
     */
    public static Task create(SocialGame game, TaskStage taskStage, float x, float y) {
        return new Task(game, taskStage, x, y);
    }

    /**
     * Create a new Task instance with the given task stage
     * @param game Game instance
     * @param taskStage Stage to use for task
     * @return A new Task object
     */
    public static Task create(SocialGame game, TaskStage taskStage) {
        return new Task(game, taskStage);
    }

    private Task(SocialGame game, TaskStage taskStage, float x, float y) {
        super(game, x, y, 1, 1);
        this.taskStage = taskStage;
    }

    private Task(SocialGame game, TaskStage taskStage) {
        this(game, taskStage, 0, 0);
    }

    /**
     * Method to be called on task completion
     */
    public void onComplete() {
        done = true;
        taskStage.close();
    }

    /**
     * Method to be called on task failure
     */
    public void onFail() {
        done = true;
        taskStage.close();
    }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player && !done){
            taskStage.open();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (taskStage.isComplete())
            onComplete();
        if (taskStage.isFailed())
            onFail();
        taskStage.act();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        taskStage.draw();
    }
}
