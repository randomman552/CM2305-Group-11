package com.socialgame.game.tasks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.player.Player;

/**
 * Class from which all tasks are derived.
 * Task can be customised by editing the Task.table attribute after instanciating
 * Extension of Interactable class
 */
public abstract class Task extends Interactable {
    /**
     * Group which is added to the uiStage.
     * If you want to add elements outside of the primary table, add them here.
     */
    protected final Group group;
    /**
     * Table storing all task logic and elements
     * Should be edited in sub-classes
     */
    protected final Table table;

    private boolean complete = false;
    private boolean failed = false;
    /**
     * Variable storing whether onComplete and onFail have been called once before.
     */
    private boolean eventsFired = false;

    public Task(SocialGame game, float x, float y) {
        super(game, x, y, 1, 1);
        texture = game.spriteSheet.findRegion("placeholder");

        group = new Group();
        close();

        // Create base table
        Table baseTable = new Table();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        baseTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        // Create close button
        ImageButtonStyle style = new ImageButtonStyle();
        ImageButton closeBtn = new ImageButton(style);
        closeBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                close();
                return true;
            }
        });
        closeBtn.setSize(50, 50);

        // Define base table size
        float width = 1280;
        float height = 720;
        float marginX = width / 20;
        float marginY = height / 20;
        baseTable.setBounds(marginX, marginY, width - (marginX * 2), height - (marginY * 2));

        // Create central table (this is the table the sub-classes will edit)
        table = new Table();

        // Define base table layout
        baseTable.row().height(50);
        baseTable.add().width(50);
        baseTable.add().expandX();
        baseTable.add(closeBtn).width(50);

        baseTable.row().expandY();
        baseTable.add().width(50);
        baseTable.add(table).fill();
        baseTable.add().width(50);

        baseTable.row().height(50);
        baseTable.add().width(50);
        baseTable.add().expandX();
        baseTable.add().width(50);

        group.addActor(baseTable);

        // Add group to UI Stage
        game.getUIStage().addActor(group);
    }

    public Task(SocialGame game) {
        this(game, 0, 0);
    }

    /**
     * Method to be called on task completion
     */
    public void onComplete() {
        game.getHud().incrementProgress();
        if (!isFailed()) game.soundAtlas.getSound("success").play(game.settings.getAdjustedSfxVol());
        close();
    }

    /**
     * Method to be called on task failure
     */
    public void onFail() {
        game.getHud().incrementHazard();
        game.soundAtlas.getSound("fail").play(game.settings.getAdjustedSfxVol());
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

    public void setComplete(boolean val) {
        complete = val;
    }

    public boolean isFailed() { return failed; }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player && !complete){
            open();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Call on complete and on fail events ONCE after completion
        if (isComplete() && !eventsFired) {
            onComplete();
            if (isFailed()) onFail();
            game.getClient().sendTCP(Networking.taskFinished(getID(), isFailed()));
            // Prevent events from firing again
            eventsFired = true;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        game.getUIStage().getActors().removeValue(group, true);
    }
}
