package com.socialgame.game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;
import com.socialgame.game.baseclasses.Weapon;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.player.clothing.Hat;

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
    public static float MAX_VEL = 5f;
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
    /**
     * Position of player hand for holding items
     */
    public static final Vector2 HAND_POS = new Vector2(0.35f, 0f);

    private static final Vector2 HAT_POS = new Vector2(0.05f, 0.8f);

    public Item[] inventory;

    private float health = 100;

    private final Animation<TextureRegion> walkAnim;
    private final Animation<TextureRegion> walkAnimHold;
    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> idleAnimHold;

    private boolean isSaboteur;
    private PlayerCustomisation customisation;
    private int invSlot;

    private final Hat hat;

    public Player(SocialGame game) {
        this(game, 0);
    }

    public Player(SocialGame game, int id) {
        this(game, id, game.customisation);
    }

    public Player(SocialGame game, int id, PlayerCustomisation customisation) {
        super(game, WIDTH, HEIGHT);

        inventory = new Item[2];

        // Walk animation setup
        walkAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegions("player"));
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);
        walkAnimHold = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegions("playerHold"));
        walkAnimHold.setPlayMode(Animation.PlayMode.LOOP);

        // Idle animation setup (currently uses first from player walk animation)
        idleAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegion("player"));
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        idleAnimHold = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegion("playerHold"));
        idleAnimHold.setPlayMode(Animation.PlayMode.LOOP);

        // Clothing items and other customisation settings
        hat = new Hat(game, customisation);
        this.customisation = customisation;

        // Give unique player id
        setID(id);
    }

    @Override
    protected void setupRigidBody() {
        // Create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = game.getPhysWorld().createBody(bodyDef);
        body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();


        // Central rectangle
        PolygonShape rectShape = new PolygonShape();
        float rectX = getHeight()/6 - (getWidth()/24);
        float rectY = getHeight()/6;
        rectShape.setAsBox(rectX, rectY, new Vector2(0, -getHeight()/3), 0);
        fixtureDef.shape = rectShape;
        body.createFixture(fixtureDef);

        // Circle shapes
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(getHeight()/6);
        fixtureDef.shape = circleShape;
        float xOffset = getWidth()/4 - (getWidth()/24);
        float yOffset = -getHeight()/3;

        // Left circle
        circleShape.setPosition(new Vector2(-xOffset, yOffset));
        body.createFixture(fixtureDef);

        // Right circle
        circleShape.setPosition(new Vector2(xOffset, yOffset));
        body.createFixture(fixtureDef);

        // Dispose of unneeded resources
        circleShape.dispose();
        rectShape.dispose();
    }

    /**
     * Get the currently active key frame. Handles change between different animation states.
     * @param time Time since game start
     * @return The current frame as a {@link TextureRegion}
     */
    public TextureRegion getKeyFrame(float time) {
        if (inventory[invSlot] != null) {
            if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0)
                return walkAnimHold.getKeyFrame(time);
            return idleAnimHold.getKeyFrame(time);
        }
        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0)
            return walkAnim.getKeyFrame(time);
        return idleAnim.getKeyFrame(time);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setHealth(float val) {
        health = val;
    }

    public float getHealth() {
        return health;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        texture = getKeyFrame(game.elapsedTime);

        // If player isn't alive, make them partially transparent
        Color curCol = getColor();
        if (isAlive()) {
            curCol.a = 1;
        } else {
            curCol.a = SPEC_ALPHA;
        }
        setColor(curCol);

        // Special case for hat with index 3, needs to be drawn behind player:
        if (customisation.getHatSelection() == 3) {
            drawClothing(batch, curCol.a);
            super.draw(batch, parentAlpha);
            drawItem(batch, curCol.a);
            return;
        }
        super.draw(batch, parentAlpha);
        drawClothing(batch, curCol.a);
        drawItem(batch, curCol.a);
    }

    private void drawItem(Batch batch, float parentAlpha) {
        Item item = inventory[invSlot];
        if (item == null) return;

        item.setFlip(getFlip());
        item.setPosition(getX() + ((getFlip()) ? -(HAND_POS.x + item.getWidth()) : HAND_POS.x), getY() + HAND_POS.y);
        item.draw(batch, parentAlpha);
    }

    private void drawClothing(Batch batch, float parentAlpha) {
        hat.draw(batch, parentAlpha);
    }

    /**
     * Add the given item to the players inventory
     * Will drop currently held item if holding one
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
    }

    public int getInvSlot() {
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
    public void delete() {
        // Drop all items when deleted
        for (int i = 0; i < inventory.length; i++) {
            setInvSlot(i);
            dropItem();
        }
        super.delete();
    }

    @Override
    public void interact(GameObject caller) {
        if (caller instanceof Player) {
            Player player = ((Player) caller);
            if (player == this) return;
            if (player.isSaboteur || player.hasWeapon()) {
                game.getClient().sendTCP(Networking.playerTakeDamageUpdate(getID(), getHealth()));
                this.takeDamage(getHealth());
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Change flip state based on movement
        if (body.getLinearVelocity().x < 0) {
            setFlip(true);
            hat.setFlip(true);
        } else if (body.getLinearVelocity().x > 0) {
            setFlip(false);
            hat.setFlip(false);
        }

        // Update clothing positions
        float rootHatX = getX() + ((hat.getFlip()) ? -HAT_POS.x: HAT_POS.x);
        float hatX = rootHatX + ((hat.getFlip()) ? -hat.getOffset().x: hat.getOffset().x);

        float hatY = getY() + HAT_POS.y + hat.getOffset().y;

        hat.setPositionAboutOrigin(hatX, hatY);
    }
}
