package com.socialgame.game.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;

public class Player extends Interactable {
    public int ID;
    public float health;

    private Animation<TextureRegion> walkAnim;
    private Animation<TextureRegion> idleAnim;
    private boolean isSaboteur;
    private PlayerCustomisation customisation;
    private Item[] inventory;
    private int curInvSlot;

    public Player(SocialGame game) {
        super(game);
        inventory = new Item[2];
    }

    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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
