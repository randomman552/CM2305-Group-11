package com.socialgame.game.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Interactable;
import com.socialgame.game.baseclasses.Item;
import com.socialgame.game.baseclasses.Weapon;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.player.clothing.Hat;
import com.socialgame.game.util.customisation.Customisation;


public class Player extends Interactable {
    /**
     * Height of all players in units
     */
    public static float HEIGHT = 1.5f;
    /**
     * Width of all players in units
     */
    public static float WIDTH = 1f;

    // region Listen radius variables

    /**
     * Distance at which sound volume begins to drop off.
     */
    public static float HEARING_FALLOFF_START = 3f;
    /**
     * Distance at which sound volume falls to 0;
     */
    public static float HEARING_FALLOFF_END = 10f;

    // endregion

    // region Velocity variables
    /**
     * Move speed of all players in units per second
     */
    public static float MAX_VEL = 5f;
    /**
     * Spectator velocity modifier
     * For example, 2 gives spectators 2 times speed of players.
     */
    public static float SPEC_VEL_MOD = 1.5f;
    /**
     * Time required until player reaches MAX_VEL.
     * Used in calculation of ACCELERATION.
     */
    public static float ACCELERATION_TIME = 0.1f;
    /**
     * Rate at which the player accelerations (in units per second per second).
     */
    public static float ACCELERATION = (MAX_VEL * 10) / ACCELERATION_TIME;
    /**
     * Threshold for the velocity at which the player is considered to be moving
     */
    public final static float MOVE_THRESHOLD = 0.1f;
    // endregion

    // region Drawing variables
    /**
     * Spectator alpha modifier
     * When in spectator mode the player is drawn with this alpha value
     */
    public static float SPEC_ALPHA = 0.25f;
    /**
     * Position of player hand for holding items
     */
    public static final Vector2 HAND_POS = new Vector2(0.35f, 0f);
    /**
     * Position of player hat
     */
    private static final Vector2 HAT_POS = new Vector2(0.05f, 0.8f);
    // endregion

    /**
     * Box2D category mask for all players to prevent them from colliding.
     */
    private static final int CATEGORY_MASK = -1;

    public Item[] inventory;

    private float health = 100;

    private final Animation<TextureRegion> walkAnim;
    private final Animation<TextureRegion> walkAnimHold;
    private final Animation<TextureRegion> idleAnim;
    private final Animation<TextureRegion> idleAnimHold;

    private final Sound walkSound;
    private long walkSoundID;

    private boolean isSaboteur;
    private final Customisation customisation;
    private int invSlot;

    private final Hat hat;

    public Player(SocialGame game) {
        this(game, 0);
    }

    public Player(SocialGame game, int id) {
        this(game, id, game.customisation);
    }

    public Player(SocialGame game, int id, Customisation customisation) {
        super(game, WIDTH, HEIGHT);

        inventory = new Item[2];

        // Walk animation setup
        walkAnim = new Animation<TextureRegion>(0.25f, game.spriteSheet.findRegions("player"));
        walkAnim.setPlayMode(Animation.PlayMode.LOOP);
        walkAnimHold = new Animation<TextureRegion>(0.25f, game.spriteSheet.findRegions("playerHold"));
        walkAnimHold.setPlayMode(Animation.PlayMode.LOOP);

        // Idle animation setup (currently uses first from player walk animation)
        idleAnim = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegion("player"));
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        idleAnimHold = new Animation<TextureRegion>(0.5f, game.spriteSheet.findRegion("playerHold"));
        idleAnimHold.setPlayMode(Animation.PlayMode.LOOP);

        // Clothing items and other customisation settings
        hat = new Hat(game, customisation);
        this.customisation = customisation;

        // Walking sound setup
        walkSound = game.soundAtlas.getSound("footsteps");
        walkSoundID = -1;

        // Give unique player id
        setID(id);
    }

    @Override
    protected void setupRigidBody() {
        // Create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1/ACCELERATION_TIME;
        body = game.getPhysWorld().createBody(bodyDef);
        body.setUserData(this);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.groupIndex = CATEGORY_MASK;


        // Central rectangle
        PolygonShape rectShape = new PolygonShape();
        float rectX = getHeight()/6 - (getWidth()/24);
        float rectY = getHeight()/6.25f;
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
        Vector2 vel = body.getLinearVelocity();
        boolean isMoving = Math.abs(vel.x) >= MOVE_THRESHOLD || Math.abs(vel.y) >= MOVE_THRESHOLD;

        if (inventory[invSlot] != null) {
            if (isMoving)
                return walkAnimHold.getKeyFrame(time);
            return idleAnimHold.getKeyFrame(time);
        }

        if (isMoving)
            return walkAnim.getKeyFrame(time);
        return idleAnim.getKeyFrame(time);
    }

    public boolean isAlive() {
        return health > 0;
    }


    public boolean getIsSaboteur(){
        return isSaboteur;
    }

    public void setIsSaboteur(boolean bool) {
        isSaboteur = bool;
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

        if(!(!isAlive() && game.mainPlayer.isAlive())){
            super.draw(batch, parentAlpha);
            drawClothing(batch, curCol.a);
            drawItem(batch, curCol.a);
        }
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
        game.playSound("death", this);
    }

    @Override
    public void delete() {
        // Drop all items when deleted
        for (int i = 0; i < inventory.length; i++) {
            setInvSlot(i);
            dropItem();
        }
        walkSound.stop(walkSoundID);
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
        if (body.getLinearVelocity().x < -MOVE_THRESHOLD) {
            setFlip(true);
            hat.setFlip(true);
        } else if (body.getLinearVelocity().x > MOVE_THRESHOLD) {
            setFlip(false);
            hat.setFlip(false);
        }

        // Update clothing positions
        float rootHatX = getX() + ((hat.getFlip()) ? -HAT_POS.x: HAT_POS.x);
        float hatX = rootHatX + ((hat.getFlip()) ? -hat.getOffset().x: hat.getOffset().x);
        float hatY = getY() + HAT_POS.y + hat.getOffset().y;
        hat.setPositionAboutOrigin(hatX, hatY);

        // Play footstep sound if moving
        Vector2 vel = body.getLinearVelocity();
        if (Math.abs(vel.x) + Math.abs(vel.y) > MOVE_THRESHOLD) {
            if (walkSoundID == -1) {
                walkSoundID = game.playSound(walkSound, this);
                walkSound.setLooping(walkSoundID, true);
            } else {
                walkSound.setVolume(walkSoundID, game.getSoundVolumeScalar(this));
            }
        } else {
            walkSound.stop(walkSoundID);
            walkSoundID = -1;
        }
    }
}
