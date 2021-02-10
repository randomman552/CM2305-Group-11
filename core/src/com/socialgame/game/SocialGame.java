package com.socialgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.screens.GameScreen;

public class SocialGame extends Game {
	/**
	 * Main sprite batch instance used for drawing sprites on screen.
	 */
	public SpriteBatch batch;

	/**
	 * Primary font used for text in game
	 */
	public BitmapFont font;

	/**
	 * Primary sprite sheet.
	 * Currently stores all sprites from the game, might be worth splitting into more sheets if more sprites needed.
	 */
	public TextureAtlas spriteSheet;

	/**
	 * Elapsed time since program start.
	 * Can be used for timing other game components
	 */
	public float elapsedTime;

	public Settings settings;

	/**
	 * TODO: Customisation rewards
	 * Stores this client's current XP, used to unlock rewards.
	 * It might be better to move this to be stored under the settings object
	 */
	public float playerXP;

	/**
	 * Physics world used by all game objects physics
	 */
	public World world;

	/**
	 * Reference to this clients primary player.
	 */
    public GameObject mainPlayer;

    @Override
	public void create () {
		// Initialise Box2D engine
		Box2D.init();
		// Create new Box2D physics world with no gravity
		world = new World(new Vector2(0, 0), true);

		batch = new SpriteBatch();
		font = new BitmapFont();
		spriteSheet = new TextureAtlas(Gdx.files.internal("sprites.atlas"));
		elapsedTime = 0;

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		elapsedTime += Gdx.graphics.getDeltaTime();
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		spriteSheet.dispose();
		font.dispose();
		world.dispose();
	}
}
