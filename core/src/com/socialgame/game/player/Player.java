package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
     * Move speed of all players in units per second
     */
    public static float MAX_VEL = 2f;
    /**
     * Spectator velocity modifier
     * For example, 2 gives spectators 2 times speed of players.
     */
    public static float SPEC_VEL_MOD = 1f;

    public int ID;
    public float health = 100;

    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> idleAnim;
    private boolean movingLeft = false;

    private boolean isSaboteur;
    private PlayerCustomisation customisation;
    private Item[] inventory;
    private int curInvSlot;

    public Player(SocialGame game) {
        super(game, WIDTH, HEIGHT);

        inventory = new Item[2];

        // Walk animation setup
        walkAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegions("playerWalk"));
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);

        // Idle animation setup (currently uses first from player walk animation)
        idleAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegion("playerWalk"));
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
    }

    /**
     * Get the currently active key frame. Handles change between different animation states.
     * @param time Time since game start
     * @return The current frame as a {@link TextureRegion}
     */
    public TextureRegion getKeyFrame(float time) {
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

        // If player isn't alive, make they partially transparent
        Color batchColor = batch.getColor();
        float alpha = batchColor.a;
        if (!isAlive()) {
            batch.setColor(batchColor.r, batchColor.g, batchColor.b, 0.5f);
        }

        super.draw(batch, parentAlpha);

        // Reset alpha if we have changed it
        batch.setColor(batchColor.r, batchColor.g, batchColor.b, alpha);
    }

    /**
     * Add the given item to the players inventory
     * @param item The item to add
     */
    public void pickupItem(Item item) {
        if (inventory[curInvSlot] != null)
            dropItem();

        inventory[curInvSlot] = item;
        item.setVisible(false);
    }

    /**
     * Drop the item in the players selected inventory slot
     */
    public void dropItem() {
        Item item = inventory[curInvSlot];

        if (item == null)
            return;

        inventory[curInvSlot] = null;
        item.setVisible(true);
        item.setPosition(getX(), getY());
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

    public void setCurInvSlot(int val) {
        curInvSlot = val;
    }
}
