package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.socialgame.game.HUD.HUD;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerController;

public class GameScreen implements Screen {
    protected final SocialGame game;

    public GameObject focusedObj;
    public Player player;

    /**
     * Stage object for use with Scene2d
     */
    private final Stage stage;
    /**
     * Stage to handle UI elements
     */
    private final Stage uiStage;

    /**
     * Box2d world used for physics simulation
     */
    private final World world;

    /**
     * TODO: Think about how we want to do the map
     * libGDX has a Map and TiledMap class we could use instead of creating our own
     */
    //private Map map;

    public GameScreen(SocialGame game) {
        this.game = game;
        this.stage = new Stage();
        this.uiStage = new Stage();

        // Create our physics world with no gravity
        this.world = game.world;

        player = new Player(game);
        stage.addActor(player);
        stage.addListener(new PlayerController(player));

        uiStage.addActor(new HUD(game));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Advance physics and actors
        world.step(delta, 6, 2);
        stage.act(delta);

        // Draw changes on screen
        stage.draw();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
    }
}
