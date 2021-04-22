package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.socialgame.game.HUD.HUD;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.items.weapons.*;
import com.socialgame.game.networking.GameClient;
import com.socialgame.game.player.PlayerController;
import com.socialgame.game.tasks.Task;
import com.socialgame.game.tasks.async.ClockCalibrationTask;
import com.socialgame.game.tasks.async.SimonSaysTask;

import java.io.IOException;
import java.util.ArrayList;

import java.awt.*;

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
    public final Stage uiStage;

    private final InputMultiplexer inputProcessor;

    public GameClient client;

    private final ArrayList<Task> tasks;
    private final HUD hud;

    /**
     * TODO: Think about how we want to do the map
     * libGDX has a Map and TiledMap class we could use instead of creating our own
     */
    private TiledMap tiledMap;
    OrthogonalTiledMapRenderer renderer;
    ShapeRenderer sr;
    float unitScale = 1/64f; // 1 unit = 32 pixels
    int[] backgroundLayers = { 0, 1 };  //Drawn behind the player
    int[] taskLayer = { 2 };
    int[] foregroundLayers = { 3 };     //Drawn in-front the player


    public void mapCreate() {
        tiledMap = new TmxMapLoader().load("./map/testMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        renderer.setView((OrthographicCamera) stage.getCamera());

    }

    /*
    public void mapRender() {
        renderer.setView((OrthographicCamera) stage.getCamera());
        renderer.render(layer);
    }
    */

    public GameScreen(SocialGame game) throws IOException {
        this(game, "localhost");
    }

    public GameScreen(SocialGame game, String host) throws IOException {
        this.game = game;

        // Use StretchViewport so that users with bigger screens cannot see more
        StretchViewport vp = new StretchViewport(16, 9);
        this.stage = new Stage(vp);
        this.uiStage = new Stage();
        mapCreate();
        
        // Hud creation
        hud = new HUD(game);
        uiStage.addActor(hud);

        // Multiplex stage and uiStage input handlers (so both can be interacted with)
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(uiStage);
        inputProcessor.addProcessor(stage);

        // Set debug
        stage.setDebugAll(true);
        uiStage.setDebugAll(true);

        // Connect to server
        client = new GameClient(game, host);
        game.setClient(client);
        try {
            client = new GameClient(game, host);
            game.setClient(client);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialise tasks array
        tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public HUD getHud() {
        return hud;
    }

    @Override
    public void show() {
        stage.addListener(new PlayerController(game));
        stage.getCamera().position.set(new float[] {0, 0, 0});

        stage.addActor(new Wrench(game, -4, 2));
        stage.addActor(new Axe(game, -2, 2));
        stage.addActor(new Sword(game, 0, 2));
        stage.addActor(new Scythe(game, 2, 2));
        stage.addActor(new Lightsword(game, 4, 2));

        // region Task generation

        tasks.add(new ClockCalibrationTask(game, 0, -2));
        tasks.add(new SimonSaysTask(game, -2, -2));

        for (Task task: tasks) {
            stage.addActor(task);
        }

        // endregion

        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Move camera to follow main player
        if (game.mainPlayer != null) {
            stage.getCamera().position.set(game.mainPlayer.getX(), game.mainPlayer.getY(), game.mainPlayer.getZIndex());
        }

        // Advance physics and actors
        game.getPhysWorld().step(delta, 6, 2);
        stage.act(delta);
        uiStage.act(delta);

        // Draw map
        renderer.setView((OrthographicCamera) stage.getCamera());
        renderer.render(backgroundLayers);

        // Draw changes on screen
        stage.draw();
        uiStage.draw();

        //Map - Draws in-front of player
        //render objects
        //FIXME - Cannot pass the ((Ortho) stage.getCamera).combined

        /*sr.setProjectionMatrix(((OrthographicCamera) stage.getCamera()).combined);
        for(MapObject object : tiledMap.getLayers().get("Tasks").getObjects()) {
            if(object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.rect(rect.x, rect.y, rect.width, rect.height);
                sr.end();
            } else if(object instanceof CircleMapObject) {
                Circle circle = ((CircleMapObject) object).getCircle();
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.circle(circle.x, circle.y, circle.radius);
                sr.end();
            }
        }*/
        //Walk-in textures.
        renderer.render(foregroundLayers);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        uiStage.getViewport().update(width, height);
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
