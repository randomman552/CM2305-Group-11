package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.socialgame.game.player.Player;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;

public class GameScreen implements Screen {
    protected final SocialGame game;

    public GameObject focusedObj;
    public Player player;

    /**
     * Stage object for use with Scene2d
     */
    private Stage stage;

    /**
     * Box2d world used for physics simulation
     */
    private World world;

    /**
     * TODO: Think about how we want to do the map
     * libGDX has a Map and TiledMap class we could use instead of creating our own
     */
    //private Map map;

    private OrthographicCamera camera;

    public GameScreen(SocialGame game) {
        this.game = game;
        this.stage = new Stage();

        // Create our physics world with no gravity
        this.world = new World(new Vector2(0, 0), true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
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

    }

    @Override
    public void dispose() {

    }
}
