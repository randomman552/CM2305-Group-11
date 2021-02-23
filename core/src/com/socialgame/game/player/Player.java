package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;
import com.socialgame.game.baseclasses.Weapon;

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
    /**
     * Spectator alpha modifier
     * When in spectator mode the player is drawn with this alpha value
     */
    public static float SPEC_ALPHA = 0.25f;

    public int ID;
    private float health = 100;

    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> idleAnim;
    private boolean movingLeft = false;

    private boolean isSaboteur;
    private PlayerCustomisation customisation;
    private Item[] inventory;
    private int invSlot;

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

        // If player isn't alive, make them partially transparent
        Color curCol = getColor();
        if (isAlive()) {
            setColor(curCol.r, curCol.g, curCol.b, 1f);
        } else {
            setColor(curCol.r, curCol.g, curCol.b, SPEC_ALPHA);
        }

        super.draw(batch, parentAlpha);
    }

    /**
     * Add the given item to the players inventory
     * @param item The item to add
     */
    public void pickupItem(Item item) {
        if (inventory[invSlot] != null)
            dropItem();

        inventory[invSlot] = item;
        item.setVisible(false);
    }

    /**
     * Drop the item in the players selected inventory slot
     */
    public void dropItem() {
        Item item = inventory[invSlot];

        if (item == null)
            return;

        inventory[invSlot] = null;
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

    public int getInvSLot() {
        return invSlot;
    }

    public void setInvSlot(int val) {
        // % operator does not work as we want for negative numbers
        // Instead we take take the modulo of the absolute value, and subtract that from inventory length.
        // This means the invSlot int will always be in range for inventory
        invSlot = (val < 0) ? inventory.length - (-val % inventory.length): val % inventory.length;
    }

    public boolean hasWeapon() {
        for (Item item: inventory)
            if (item instanceof Weapon)
                return true;
        return false;
    }

    public void takeDamage(float amount) {
        health -= amount;
    }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player) {
            Player player = ((Player) caller);
            if (player.isSaboteur || player.hasWeapon()) {
                this.takeDamage(this.health);
            }
        }
    }
}
