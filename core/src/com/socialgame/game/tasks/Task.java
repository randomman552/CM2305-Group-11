package com.socialgame.game.tasks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.player.Player;
import com.socialgame.game.screens.GameScreen;

/**
 * Class from which all tasks are derived.
 * Task can be customised by editing the Task.table attribute after instanciating
 * Extension of Interactable class
 */
public abstract class Task extends Interactable implements Disposable {
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
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
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
