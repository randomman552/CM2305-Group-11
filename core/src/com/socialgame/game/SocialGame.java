package com.socialgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.socialgame.game.baseclasses.GameObject;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.socialgame.game.screens.GameScreen;
import com.socialgame.game.screens.MainMenuScreen;

public class SocialGame extends Game {
	/**
	 * Main sprite batch instance used for drawing sprites on screen.
	 */
	public SpriteBatch batch;

	/**
	 * Skin used for styling in the game
	 */
	static public Skin gameSkin;

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
	 * Reference to this clients primary player.
	 */
    public GameObject mainPlayer;

    @Override
	public void create () {
		// Locates skin
		gameSkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
		// Initialise Box2D engine
		Box2D.init();

		batch = new SpriteBatch();
		font = new BitmapFont();
		spriteSheet = new TextureAtlas(Gdx.files.internal("game.atlas"));
		elapsedTime = 0;

		this.setScreen(new MainMenuScreen(this));
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
		gameSkin.dispose();
	}
}
