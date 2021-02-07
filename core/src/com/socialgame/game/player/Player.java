package com.socialgame.game.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;

public class Player extends Interactable {
    public static float HEIGHT = 75;
    public static float WIDTH = 50;
    /**
     * Move speed of all players in units per second
     */
    public static float MAX_VELOCITY = 75;

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

        // Setup animations
        walkAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegions("playerWalk"));
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        // Set texture to prevent null reference exception
        texture = getKeyFrame(game.elapsedTime);

        // Set position and size constants
        setPosition(0, 0);
        setSize(WIDTH, HEIGHT);
        // Set origin of the player to be in the middle middle
        // This is used as an offset for the underlying rigid body
        setOrigin(getWidth() / 2f, getHeight() / 2f);

        //Setup Box2D rigid body TODO: Handle collisions with other objects, this just handles velocity ATM
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() + getOriginX(), getY() + getOriginY());

        // Create our rigid body
        body = game.world.createBody(bodyDef);
        body.setUserData(this);
    }

    /**
     * Get the currently active key frame. Handles change between different animation states.
     * @param time Time since game start
     * @return The current frame as a {@link TextureRegion}
     */
    protected TextureRegion getKeyFrame(float time) {
        //TODO: Add idle player anims
        return walkAnim.getKeyFrame(time);
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

    @Override
    public void act(float delta) {
        // Update position based on movement of the rigid body
        this.setX(body.getPosition().x - getOriginX());
        this.setY(body.getPosition().y - getOriginY());
        super.act(delta);
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
