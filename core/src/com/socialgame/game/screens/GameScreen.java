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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.HUD.HUD;
import com.socialgame.game.SocialGame;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.baseclasses.Item;
import com.socialgame.game.map.MapBodyBuilder;
import com.socialgame.game.networking.GameClient;
import com.socialgame.game.networking.Networking;
import com.socialgame.game.networking.voicechat.VoiceChatClient;
import com.socialgame.game.networking.voicechat.VoiceClient;
import com.socialgame.game.player.Player;
import com.socialgame.game.player.PlayerInputProcessor;
import com.socialgame.game.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
    public static VoiceChatClient voiceChatClient;
    public static VoiceClient voiceClient;

    private final ArrayList<Task> tasks;
    private final ArrayList<Item> items;
    private final HUD hud;

    /**
     * TODO: Think about how we want to do the map
     * libGDX has a Map and TiledMap class we could use instead of creating our own
     */
    private final TiledMap tiledMap;
    OrthogonalTiledMapRenderer renderer;
    Box2DDebugRenderer box2DDebugRenderer;

    private final ArrayList<Array<Body>> builtBodies;
    private final ArrayList<MapObject> taskObjects;
    private final ArrayList<MapObject> weaponObjects;
    private final ImageButton startButton;

    private static final String floorLayer = "Floor";
    private static final String wallLayer = "Walls";
    private static final String tasksLayer = "Tasks";
    private static final String weaponLayer = "Weapons";
    private static final String walkInLayer = "WalkIn Textures";
    private static final String spawnLayer = "Player Spawns";
    private static final String startLayer = "Start";

    private static final float unitScale = 1/64f; // 1 unit = 32 pixels
    private static int[] backgroundLayers;  //Drawn behind the player
    private static int[] foregroundLayers;     //Drawn in-front the player
    private static int[] startLayers;

    private boolean startGameFlag = false;
    private Networking.TaskInfo[] taskInfos = new Networking.TaskInfo[0];
    private Networking.ItemInfo[] itemInfos = new Networking.ItemInfo[0];

    public GameScreen(SocialGame game) throws IOException {
        this(game, "");
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

        tiledMap = new TmxMapLoader().load(Gdx.files.internal("map/gameMap.tmx").toString());

        backgroundLayers = new int[]{getLayerIndex(floorLayer), getLayerIndex(wallLayer)};  //Drawn behind the player
        foregroundLayers = new int[]{getLayerIndex(walkInLayer)};
        startLayers = new int[]{getLayerIndex(startLayer)};


        renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
        renderer.setView((OrthographicCamera) stage.getCamera());

        builtBodies = new ArrayList<>();
        builtBodies.add(MapBodyBuilder.buildShapes(tiledMap, getLayerIndex("Static Body"),1/unitScale, game.getPhysWorld()));
        builtBodies.add(MapBodyBuilder.buildShapes(tiledMap, getLayerIndex(startLayer),1/unitScale, game.getPhysWorld()));

        // endregion

        //initialise ArrayList for accessing objects in task, and weapon generation later on

        taskObjects = new ArrayList<>();
        for (MapObject mapObject: getLayerObjects(tasksLayer)) {
            taskObjects.add(mapObject);
        }

        weaponObjects = new ArrayList<>();
        for (MapObject mapObject: getLayerObjects(weaponLayer)) {
            weaponObjects.add(mapObject);
        }

        System.out.println(taskObjects);

        //Start button for lobby
        Drawable texture = new TextureRegionDrawable(game.spriteSheet.findRegion("start"));
        startButton = new ImageButton(texture);

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

        // Connect to voice chat server
        voiceChatClient = new VoiceChatClient(host);

        game.setVoiceChatClient(voiceChatClient);
        voiceClient = new VoiceClient(voiceChatClient.getKryo());

        voiceClient.addReceiver(voiceChatClient);


        // Create tasks (stored for later initialisation)
        tasks = new ArrayList<>();
        items = new ArrayList<>();
    }


    public int getLayerIndex(String layer) {
        return tiledMap.getLayers().getIndex(layer);
    }

    public MapObjects getLayerObjects(String index) {
        return getLayer(index).getObjects();
    }

    public MapLayer getLayer(String index) {
        return tiledMap.getLayers().get(index);
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

    synchronized public void setStartGameFlag(boolean flag, Networking.PlayerInfo[] playerInfos, Networking.TaskInfo[] taskInfos, Networking.ItemInfo[] itemInfos) {
        this.startGameFlag = flag;
        this.taskInfos = taskInfos;
        this.itemInfos = itemInfos;
    }

    synchronized public void setStartGameFlag(boolean flag) {
        this.startGameFlag = flag;
    }

    private void spawnTasks(Networking.TaskInfo[] taskInfos) {
        Collections.shuffle(taskObjects, game.getRandom());
        // Picks a random MapObject from the array to use for a task location
        for (int i = 0; i < taskInfos.length && i < taskObjects.size(); i++) {
            MapObject mapObject = taskObjects.get(i);
            if (mapObject instanceof EllipseMapObject) {
                float x = ((EllipseMapObject) mapObject).getEllipse().x * unitScale;
                float y = ((EllipseMapObject) mapObject).getEllipse().y * unitScale;
                tasks.add(Task.create(taskInfos[i].type, game, x + 0.5f, y + 0.5f));
            }
        }

        // Adds the tasks to the stage
        for (Task task: tasks) {
            stage.addActor(task);
        }
    }

    public void spawnWeapons(Networking.ItemInfo[] itemInfos) {
        Collections.shuffle(weaponObjects, game.getRandom());
        // Picks a random MapObject from the array to use for a task location
        for (int i = 0; i < itemInfos.length && i < weaponObjects.size(); i++) {
            MapObject mapObject = weaponObjects.get(i);
            if (mapObject instanceof RectangleMapObject) {
                float x = ((RectangleMapObject) mapObject).getRectangle().x * unitScale;
                float y = ((RectangleMapObject) mapObject).getRectangle().y * unitScale;
                items.add(Item.create(i, game, x + 0.5f, y + 0.5f));
            }
        }

        for (Item item: items) {
            stage.addActor(item);
        }
    }

    public void releasePlayers() {
        for (Body body: builtBodies.get(1)) {
            body.setActive(false);
        }
        startButton.setVisible(false);
    }

    private void startGame() {
        spawnTasks(taskInfos);
        spawnWeapons(itemInfos);
        releasePlayers();

        setStartGameFlag(false);
    }

    @Override
    public void show() {
        stage.getCamera().position.set(new float[] {0, 0, 0});

        // region Start game button

        for (final MapObject mapObject : getLayerObjects(startLayer)) {
            if (mapObject instanceof EllipseMapObject) {
                float x = ((EllipseMapObject) mapObject).getEllipse().x * unitScale;
                float y = ((EllipseMapObject) mapObject).getEllipse().y * unitScale;
                startButton.setPosition(x, y);
                startButton.setSize(1f,1f);
                startButton.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (game.getServer() != null) {
                            game.getClient().sendTCP(Networking.initialiseGame());
                        }
                        return true;
                    }
                });
                stage.addActor(startButton);
            }
        }

        // endregion

        Gdx.input.setInputProcessor(inputProcessor);

        // Start voice chat thread for sending voice
        Thread voiceChatSenderThread = new Thread(() -> {
            while (true) {
                if (playerInputProcessor.sendVoice && game.mainPlayer.isAlive())
                    voiceClient.sendVoice(voiceChatClient, Gdx.graphics.getDeltaTime());
            }
        });
        voiceChatSenderThread.start();
    }

    @Override
    public void render(float delta) {
        // Check if we need to generate map
        if (startGameFlag) {
            startGame();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Handle player velocity changes
        playerInputProcessor.updateVelocity((Player) game.mainPlayer, delta);

        // Move camera to follow main player
        if (game.mainPlayer != null) {
            stage.getCamera().position.set(game.mainPlayer.getX(), game.mainPlayer.getY(), game.mainPlayer.getZIndex());
        }

        // Advance physics and actors
        synchronized (game.getPhysWorld()) {
            game.getPhysWorld().step(delta, 6, 2);
        }
        stage.act(delta);
        uiStage.act(delta);

        // Draw map
        renderer.setView((OrthographicCamera) stage.getCamera());
        renderer.render(backgroundLayers);

        // Draw changes on screen
        stage.draw();
        renderer.render(foregroundLayers);
        renderer.render(startLayers);
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
