package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.HUD.HUD;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.items.weapons.*;
import com.socialgame.game.map.MapBodyBuilder;
import com.socialgame.game.networking.GameClient;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerInputProcessor;
import com.socialgame.game.tasks.Task;
import com.socialgame.game.tasks.async.ClockCalibrationTask;
import com.socialgame.game.tasks.async.SimonSaysTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen implements Screen {
    protected final SocialGame game;

    /**
     * Stage object for use with Scene2d
     */
    public final Stage stage;
    /**
     * Stage to handle UI elements
     */
    public final Stage uiStage;

    private final InputMultiplexer inputProcessor;
    private final PlayerInputProcessor playerInputProcessor;

    public GameClient client;

    private final ArrayList<Task> tasks;
    private final HUD hud;

    /**
     * TODO: Think about how we want to do the map
     * libGDX has a Map and TiledMap class we could use instead of creating our own
     */
    private final TiledMap tiledMap;
    OrthogonalTiledMapRenderer renderer;
    Box2DDebugRenderer box2DDebugRenderer;

    private static final String floorLayer = "Floor";
    private static final String wallLayer = "Walls";
    private static final String simonTaskLayer = "Task Simon";
    private static final String clockTaskLayer = "Task Clock";
    private static final String walkInLayer = "WalkIn Textures";
    private static final String spawnLayer = "Player Spawns";

    private static final float unitScale = 1/64f; // 1 unit = 32 pixels
    private static int[] backgroundLayers;  //Drawn behind the player
    private static int[] taskLayer;
    private static int[] foregroundLayers;     //Drawn in-front the player


    public int getLayerIndex(String layer) {
        return tiledMap.getLayers().getIndex(layer);
    }

    public MapObjects getLayerObjects(String index) {
        return getLayer(index).getObjects();
    }

    public MapLayer getLayer(String index) {
        return tiledMap.getLayers().get(index);
    }


    public boolean taskSpawnChance() {
        double p = game.getRandom().nextDouble();
        return p <= 0.5;
    }

    public GameScreen(SocialGame game, String password) throws IOException {
        this(game, password, "localhost");
    }

    public GameScreen(SocialGame game, String password, String host) throws IOException {
        this.game = game;

        // Clear GameObject table to prevent mismatching id's between servers and clients.
        GameObject.deleteAll();
        game.mainPlayer = null;

        // Use StretchViewport so that users with bigger screens cannot see more
        this.stage = new Stage(new StretchViewport(16, 9));
        this.uiStage = new Stage(new StretchViewport(1280, 720));

        // Set debug
        this.stage.setDebugAll(game.settings.getDebug());
        this.uiStage.setDebugAll(game.settings.getDebug());
        box2DDebugRenderer = new Box2DDebugRenderer();
        box2DDebugRenderer.setDrawVelocities(true);
        box2DDebugRenderer.VELOCITY_COLOR.set(1, 0, 0, 1);

        // region Initialise map

        tiledMap = new TmxMapLoader().load(Gdx.files.internal("map/testMap.tmx").toString());

        backgroundLayers = new int[]{getLayerIndex(floorLayer), getLayerIndex(wallLayer)};  //Drawn behind the player
        taskLayer = new int[]{getLayerIndex(simonTaskLayer), getLayerIndex(clockTaskLayer) };
        foregroundLayers = new int[]{getLayerIndex(walkInLayer)};

        TiledMapTileLayer x;
        MapLayer y;

        renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        renderer.setView((OrthographicCamera) stage.getCamera());

        MapBodyBuilder.buildShapes(tiledMap, 1/unitScale, game.getPhysWorld());

        // endregion
        
        // Hud creation
        hud = new HUD(game);
        uiStage.addActor(hud);

        // Create player controller input processor
        playerInputProcessor = new PlayerInputProcessor(game);

        // Multiplex all input processors
        inputProcessor = new InputMultiplexer();
        inputProcessor.addProcessor(uiStage);
        inputProcessor.addProcessor(playerInputProcessor);
        inputProcessor.addProcessor(stage);

        // Connect to server
        client = new GameClient(game, password, host);
        game.setClient(client);

        // Create tasks (stored for later initialisation)
        tasks = new ArrayList<>();
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public HUD getHud() {
        return hud;
    }

    public void spawnPlayer(Player player) {
        MapObjects spawns = getLayerObjects(spawnLayer);
        stage.addActor(player);
        if (spawns.getCount() != 0) {
            int randSpawnIdx = game.getRandom().nextInt(spawns.getCount());
            MapObject mapObject = spawns.get(randSpawnIdx);
            if (mapObject instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) mapObject).getRectangle();
                player.setPositionAboutOrigin(rect.getX() * unitScale, rect.getY() * unitScale);
            }
        }
    }

    @Override
    public void show() {
        stage.getCamera().position.set(new float[] {0, 0, 0});

        stage.addActor(new Wrench(game, -4, 2));
        stage.addActor(new Axe(game, -2, 2));
        stage.addActor(new Sword(game, 0, 2));
        stage.addActor(new Scythe(game, 2, 2));
        stage.addActor(new Lightsword(game, 4, 2));

        // region Task generation

        // Set the max amount of tasks to spawn.
        int capSimonTask = 3;
        final int capClockTask = 3;
        int spawnCount = 0;

        // Spawns the Clock Calibration Tasks
        for (MapObject mapObject : getLayerObjects(clockTaskLayer)) {
            if (spawnCount >= capClockTask) {
                spawnCount = 0;
                break;
            }

            if (taskSpawnChance()) {
                if (mapObject instanceof EllipseMapObject) {
                    float x = ((EllipseMapObject) mapObject).getEllipse().x * unitScale;
                    float y = ((EllipseMapObject) mapObject).getEllipse().y * unitScale;
                    tasks.add(new ClockCalibrationTask(game, x + 0.5f, y + 0.5f));

                }
                spawnCount += 1;

            }
        }

        // Spawns the Simon says tasks Tasks
        for (MapObject mapObject : getLayerObjects(simonTaskLayer)) {
            if (spawnCount >= capSimonTask) {
                spawnCount = 0;
                break;
            }

            if (taskSpawnChance()) {
                if (mapObject instanceof EllipseMapObject) {
                    float x = ((EllipseMapObject) mapObject).getEllipse().x * unitScale;
                    float y = ((EllipseMapObject) mapObject).getEllipse().y * unitScale;
                    tasks.add(new SimonSaysTask(game, x + 0.5f, y + 0.5f));

                }
                spawnCount += 1;

            }
        }

        // Adds the tasks to the stage
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

        // Handle player velocity changes
        playerInputProcessor.updateVelocity((Player) game.mainPlayer, delta);

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
        renderer.render(foregroundLayers);
        uiStage.draw();

        // Draw debug if required
        if (game.settings.getDebug()) box2DDebugRenderer.render(game.getPhysWorld(), stage.getCamera().combined);
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
        game.closeClient();
    }
}
