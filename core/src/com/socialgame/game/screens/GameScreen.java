package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.socialgame.game.HUD.HUD;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.items.weapons.*;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerController;

public class GameScreen implements Screen {
    protected final SocialGame game;

    public GameObject focusedObj;

    /**
     * Stage object for use with Scene2d
     */
    public final Stage stage;
    /**
     * Stage to handle UI elements
     */
    private final Stage uiStage;

    /**
     * Box2d world used for physics simulation
     */
    public final World world;

    /**
     * TODO: Think about how we want to do the map
     * libGDX has a Map and TiledMap class we could use instead of creating our own
     */
    //private Map map;

    public GameScreen(SocialGame game) {
        this.game = game;

        // Use StretchViewport so that users with bigger screens cannot see more
        StretchViewport vp = new StretchViewport(16, 9);
        this.stage = new Stage(vp);
        this.uiStage = new Stage();

        uiStage.addActor(new HUD(game));

        // Create our physics world with no gravity
        this.world = new World(new Vector2(0, 0), true);
    }

    @Override
    public void show() {

        game.mainPlayer = new Player(game);
        stage.addActor(game.mainPlayer);
        stage.addListener(new PlayerController(game));
        stage.setDebugAll(true);
        stage.getCamera().position.set(new float[] {0, 0, 0});

        stage.addActor(new Wrench(game, -4, 2));
        stage.addActor(new Axe(game, -2, 2));
        stage.addActor(new Sword(game, 0, 2));
        stage.addActor(new Scythe(game, 2, 2));
        stage.addActor(new Lightsword(game, 4, 2));
        stage.addActor(new Player(game));



        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Move camera to follow main player
        stage.getCamera().position.set(game.mainPlayer.getX(), game.mainPlayer.getY(), game.mainPlayer.getZIndex());

        // Advance physics and actors
        world.step(delta, 6, 2);
        stage.act(delta);

        // Draw changes on screen
        stage.draw();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
    }
}
