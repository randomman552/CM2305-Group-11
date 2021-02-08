package com.socialgame.game.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;

public class Player extends Interactable {
    /**
     * Height of all players in units
     */
    public static float HEIGHT = 1.5f;
    /**
     * Width of all players in units
     */
    public static float WIDTH = 1f;
    /**
     * Move speed of all players in Box2D units per second (WARN: TAKE INTO ACCOUNT PHYS_SCALE)
     */
    public static float MAX_VELOCITY = 2f;

    public int ID;
    public float health;

    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> idleAnim;
    private boolean movingLeft = false;

    private boolean isSaboteur;
    private PlayerCustomisation customisation;
    private Item[] inventory;
    private int curInvSlot;

    public Player(SocialGame game) {
        super(game);

        inventory = new Item[2];

        // Walk animation setup
        walkAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegions("playerWalk"));
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        // Idle animation setup (currently uses first from player walk animation)
        idleAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegion("playerWalk"));
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);

        setupRigidBody();

        // Set position and size constants
        setSize(WIDTH, HEIGHT);
        setOrigin(WIDTH / 2f, HEIGHT / 2f);
        setPosition(0, 0);
    }

    /**
     * Get the currently active key frame. Handles change between different animation states.
     * @param time Time since game start
     * @return The current frame as a {@link TextureRegion}
     */
    protected TextureRegion getKeyFrame(float time) {
        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0)
            return walkAnim.getKeyFrame(time);
        return idleAnim.getKeyFrame(time);
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        texture = getKeyFrame(game.elapsedTime);

        // Change movingLeft to flip sprite
        // We don't change the state of movingLeft when we are stationary,
        // this preserves the flipped state and prevents the player from flipping to default when stopped.
        if (body.getLinearVelocity().x < 0)
            movingLeft = true;
        else if (body.getLinearVelocity().x > 0)
            movingLeft = false;

        // Flip texture if moving left and it is not already flipped
        // Or flip it back to default if we are not moving left and it is already flipped
        if (movingLeft && !texture.isFlipX() || !movingLeft && texture.isFlipX())
            texture.flip(true, false);

        super.draw(batch, parentAlpha);
    }

    /**
     * Add the given item to the players inventory
     * @param item The item to add
     */
    public void pickupItem(Item item) {

    }

    /**
     * Drop the item in the players selected inventory slot
     */
    public void dropItem() {

    }

    /**
     * Get a copy of this players inventory
     * @return Copy of the players inventory
     */
    public Item[] getInventory() {
        return inventory.clone();
    }

    public int getCurInvSlot() {
        return curInvSlot;
    }
}
