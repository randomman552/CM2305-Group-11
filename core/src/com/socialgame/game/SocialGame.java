package com.socialgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
	 * Current stores all sprites from the game, might be worth splitting into more sheets if more sprites needed.
	 */
	public TextureAtlas spriteSheet;

	/**
	 * Elapsed time since program start.
	 * Can be used for timing other game components
	 */
	public float elapsedTime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		spriteSheet = new TextureAtlas();
		elapsedTime = 0;
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
	}
}
