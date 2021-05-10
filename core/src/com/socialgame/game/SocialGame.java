package com.socialgame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.socialgame.game.HUD.HUD;
import com.socialgame.game.baseclasses.GameObject;
import com.socialgame.game.networking.GameClient;
import com.socialgame.game.networking.GameServer;
import com.socialgame.game.networking.voicechat.VoiceChatClient;
import com.socialgame.game.networking.voicechat.VoiceChatServer;
import com.socialgame.game.player.Player;
import com.socialgame.game.screens.GameScreen;
import com.socialgame.game.screens.menu.EndScreen;
import com.socialgame.game.screens.menu.ErrorScreen;
import com.socialgame.game.screens.menu.MainMenuScreen;
import com.socialgame.game.screens.menu.MenuScreen;
import com.socialgame.game.tasks.Task;
import com.socialgame.game.util.Settings;
import com.socialgame.game.util.SoundAtlas;
import com.socialgame.game.util.customisation.LinkedCustomisation;

import java.util.ArrayList;
import java.util.Random;

public class SocialGame extends Game {
	/**
	 * Main sprite batch instance used for drawing sprites on screen.
	 */
	public SpriteBatch batch;

	/**
	 * Skin used for styling in the game
	 */
	public Skin gameSkin;

	// region Game resources (sprite sheets and sound atlas)

	/**
	 * Primary sprite sheet.
	 * Currently stores all sprites from the game, might be worth splitting into more sheets if more sprites needed.
	 */
	public TextureAtlas spriteSheet;

    /**
     * Sprite sheet used for menus
     */
	public TextureAtlas menuSpriteSheet;

	/**
	 * Sprite sheet used for menus
	 */
	public TextureAtlas wearablesSpriteSheet;

	public SoundAtlas soundAtlas;

	// endregion

	/**
	 * Elapsed time since program start.
	 * Can be used for timing other game components
	 */
	public float elapsedTime;

	public Settings settings;

	public LinkedCustomisation customisation;

	private Random random;

	/*
	 * TODO: Customisation rewards
	 * Stores this client's current XP, used to unlock rewards.
	 * It might be better to move this to be stored under the settings object
	 */
	public float playerXP;

	/**
	 * Reference to this clients primary player.
	 */
    public Player mainPlayer;

    // region Stage methods

	/**
	 * Method to get the primary stage of the current screen.
	 * Currently only handles Game and Customise screens.
	 * Any other screen will return a new Stage instance.
	 * @return Primary stage of current screen, will never be null.
	 */
	public Stage getMainStage() {
        Screen curScreen = getScreen();

        if (curScreen instanceof GameScreen) {
            return ((GameScreen) curScreen).stage;
        }
        else if (curScreen instanceof MenuScreen) {
            return ((MenuScreen) curScreen).stage;
        }
        return null;
    }

	/**
	 * Method to get the UI stage of the game screen.
	 * If the current screen is not game screen, a new Stage will be returned instead.
	 * @return UI Stage of game screen, will never be null
	 */
	public Stage getUIStage() {
		Screen curScreen = getScreen();

		if (curScreen instanceof GameScreen) {
			return ((GameScreen) curScreen).uiStage;
		}
		else if (curScreen instanceof MenuScreen) {
			return ((MenuScreen) curScreen).stage;
		}
		return null;
	}

	// endregion

	// region Physics world elements

	private World physWorld;

    public World getPhysWorld() {
        return physWorld;
    }

    public void setPhysWorld(World world) {
        if (physWorld != null) physWorld.dispose();
        physWorld = world;
    }

    // endregion

    // region Networking elements

    private GameServer server;
    private VoiceChatServer voiceChatServer;
    private GameClient client;
    private VoiceChatClient voiceChatClient;

	public GameClient getClient() {
		return client;
	}

	public void setClient(GameClient client) {
		this.client = client;
	}

	public VoiceChatClient getVoiceChatClient() {
		return voiceChatClient;
	}

	public void setVoiceChatClient(VoiceChatClient voiceChatClient) {
		this.voiceChatClient = voiceChatClient;
	}

	public GameServer getServer() {
		return server;
	}

	public void setServer(GameServer server) {
		this.server = server;
	}

	public VoiceChatServer getVoiceChatServer() {
		return  voiceChatServer;
	}

	public void setVoiceChatServer(VoiceChatServer voiceChatServer) {
		this.voiceChatServer = voiceChatServer;
	}



	/**
	 * Stop server if it is running
	 */
	synchronized public void closeServer() {
		if (getServer() != null) {
			getServer().stop();
			getVoiceChatServer().stop();
			setServer(null);
			setVoiceChatServer(null);
		}

	}

	/**
	 * Stop client if it is running
	 */
	synchronized public void closeClient() {
		if (getClient() != null) {
			getClient().stop();
			getVoiceChatClient().stop();
			setClient(null);
			setVoiceChatClient(null);
		}
	}

	//endregion

	// region Error message handling

	volatile private String errorMessage;
	volatile private Screen errorNextScreen;

	/**
	 * Display an error message to the user, then return them to the given screen.
	 * @param message The message to display.
	 * @param nextScreen The screen to return to after display
	 */
	synchronized public void showErrorMessage(String message, Screen nextScreen) {
		this.errorMessage = message;
		this.errorNextScreen = nextScreen;
	}

	/**
	 * Display an error message to the user, then return them to the main menu.
	 * @param message The message to display.
	 */
	synchronized public void showErrorMessage(String message) {
		this.errorMessage = message;
	}

	/**
	 * Display an error message to the user, then return them to the main menu.
	 */
	synchronized public void showErrorMessage() {
		showErrorMessage("An error has occurred");
	}

	// endregion

	// region End screen display

	volatile private boolean displayEnd;
	volatile private boolean endWin;

	synchronized public void showEndScreen(boolean win) {
		// End all connections before showing end screen
		closeServer();
		closeClient();

		displayEnd = true;
		endWin = win;
	}

	// endregion

	// region Sound playing functions

	/**
	 * Utility function to get scalar for sound volume based on distance from game.mainPlayer;
	 * @param origin The source the sound is coming from
	 * @return Float sound scalar
	 */
	private float getDistanceVolumeScalar(GameObject origin) {
		if (mainPlayer == null)
			return 1;

		float dist = mainPlayer.calcDistance(origin);
		float falloffStart = Player.HEARING_FALLOFF_START;
		float falloffEnd = Player.HEARING_FALLOFF_END;

		if (dist <= falloffStart) {
			return 1;
		} else if (dist <= falloffEnd) {
			return 1 -((dist - falloffStart) / (falloffEnd - falloffStart));
		} else {
			return 0;
		}
	}

	public float getSoundVolumeScalar(GameObject origin) {
		return settings.getAdjustedSfxVol() * getDistanceVolumeScalar(origin);
	}

	public float getSoundVolumeScalar() {
		return settings.getAdjustedSfxVol();
	}

	// region String based playSound methods

	/**
	 * Play the sound with the given key in {@link SoundAtlas game.soundAtlas}
	 * @param soundKey The key to search for in the atlas.
	 * @return The ID of the played sound.
	 */
	public long playSound(String soundKey) {
		return playSound(soundAtlas.getSound(soundKey));
	}

	/**
	 * Play the sound with the given key in {@link SoundAtlas game.soundAtlas}
	 * @param soundKey The key to search for in the atlas.
	 * @param pitch Desired pitch (0.5 to 2).
	 * @param pan Desired pan (-1 to 1).
	 * @return The ID of the played sound.
	 */
	public long playSound(String soundKey, float pitch, float pan) {
		return playSound(soundAtlas.getSound(soundKey), pitch, pan);
	}

	/**
	 * Play the sound with the given key in {@link SoundAtlas game.soundAtlas}
	 * @param soundKey The key to search for in the atlas.
	 * @param origin {@link GameObject Object} the sound originates from.
	 * @return The ID of the played sound.
	 */
	public long playSound(String soundKey, GameObject origin) {
		return playSound(soundAtlas.getSound(soundKey), origin);
	}

	/**
	 * Play the sound with the given key in {@link SoundAtlas game.soundAtlas}
	 * @param soundKey The key to search for in the atlas.
	 * @param origin {@link GameObject Object} the sound originates from.
	 * @param pitch Desired pitch (0.5 to 2).
	 * @param pan Desired pan (-1 to 1).
	 * @return The ID of the played sound.
	 */
	public long playSound(String soundKey, GameObject origin, float pitch, float pan) {
		return playSound(soundAtlas.getSound(soundKey), origin, pitch, pan);
	}

	// endregion

	// region Sound based playSound methods

	/**
	 * Helper function to play a sound with volume scaled by distance to game.mainPlayer
	 * @param sound The sound to play
	 * @param origin The origin of the sound (GameObject)
	 * @return The id of the played sound
	 */
	public long playSound(Sound sound, GameObject origin) {
		return sound.play(getSoundVolumeScalar(origin));
	}

	/**
	 * Helper function to play a sound with volume scaled by distance to game.mainPlayer
	 * @param sound The sound to play
	 * @param origin The origin of the sound (GameObject)
	 * @param pitch Pitch of the sound (between 0.5 and 2)
	 * @param pan Pan of the sound (0 middle, -1 full left, 1 full right)
	 * @return The id of the played sound
	 */
	public long playSound(Sound sound, GameObject origin, float pitch, float pan) {
		return sound.play(getSoundVolumeScalar(origin), pitch, pan);
	}

	/**
	 * Helper function to play a sound.
	 * @param sound The sound to play.
	 * @return ID of played sound.
	 */
	public long playSound(Sound sound) {
		return sound.play(getSoundVolumeScalar());
	}

	/**
	 * Helper function to play a sound.
	 * @param sound The sound to play.
	 * @param pitch The pitch to play at.
	 * @param pan The pan of the sound.
	 * @return ID of played sound.
	 */
	public long playSound(Sound sound, float pitch, float pan) {
		return sound.play(getSoundVolumeScalar(), pitch, pan);
	}

	// endregion

	// endregion

	/**
	 * Method to get the current list of tasks from the game screen.
	 * If not present will return an empty ArrayList.
	 * @return Current list of tasks, will never be null.
	 */
	public ArrayList<Task> getTasks() {
		Screen screen = getScreen();
		if (screen instanceof GameScreen) {
			return ((GameScreen) screen).getTasks();
		}
		return new ArrayList<>();
	}

	public void setMainPlayer(Player player) {
		if (getScreen() instanceof GameScreen) {
			mainPlayer = player;
			GameScreen screen = ((GameScreen) getScreen());
			screen.spawnPlayer(player);
		}
	}

	public Player getMainPlayer() {
		return ((Player) mainPlayer);
	}

	public Random getRandom() {
		return random;
	}

	/**
	 * Method to get the HUD of the game screen.
	 * If the current screen is not a game screen, a new HUD instance will be returned.
	 * @return Current HUD, will never be null.
	 */
	public HUD getHud() {
		Screen screen = getScreen();
		if (screen instanceof GameScreen) {
			return ((GameScreen) screen).getHud();
		}
		return null;
	}
	
	/**
	 * Helper function to generate fonts using the FreeType extension
	 * @param path Path to the .ttf file
	 * @param parameter FreeTypeFontParameter object defining parameters (such as font size etc)
	 * @return Generated BitmapFont
	 */
	public BitmapFont generateFont(FileHandle path, FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(path);
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		return font;
	}


    @Override
	public void create () {
		// Locates skin
		gameSkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
		// Initialise Box2D engine
		Box2D.init();

		physWorld = new World(new Vector2(0, 0), true);

		settings = new Settings();
		customisation = new LinkedCustomisation();

		batch = new SpriteBatch();
		spriteSheet = new TextureAtlas(Gdx.files.internal("game.atlas"));
		menuSpriteSheet = new TextureAtlas(Gdx.files.internal("menu.atlas"));
		wearablesSpriteSheet = new TextureAtlas(Gdx.files.internal("wearables.atlas"));
		soundAtlas = new SoundAtlas();
		soundAtlas.setMusicVolumes(settings.getAdjustedMusicVol());
		elapsedTime = 0;

		random = new Random();

		this.setScreen(new MainMenuScreen(this));
		soundAtlas.getSound("death").play(settings.getAdjustedSfxVol());
		soundAtlas.getMusic("ambiance").setLooping(true);
		soundAtlas.getMusic("ambiance").play();
	}

	@Override
	public void render () {
		elapsedTime += Gdx.graphics.getDeltaTime();

		// Check if we need to display an error message.
		if (errorMessage != null) {
			if (errorNextScreen != null)
				setScreen(new ErrorScreen(this, errorMessage, errorNextScreen));
			else
				setScreen(new ErrorScreen(this, errorMessage, new MainMenuScreen(this)));
			errorMessage = null;
			errorNextScreen = null;
		}

		// Check if we need to display an end screen
		if (displayEnd) {
			setScreen(new EndScreen(this, endWin));
			displayEnd = false;
		}

		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		spriteSheet.dispose();
		menuSpriteSheet.dispose();
		wearablesSpriteSheet.dispose();
		soundAtlas.dispose();
		gameSkin.dispose();
	}
}
