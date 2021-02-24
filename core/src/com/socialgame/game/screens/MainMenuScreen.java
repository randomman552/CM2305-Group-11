package com.socialgame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.socialgame.game.SocialGame;
import com.socialgame.game.player.PlayerCustomisation;

public class MainMenuScreen implements Screen {

    protected final SocialGame game;
    private Stage stage;
    public PlayerCustomisation playerCustomisation;
    private OrthographicCamera camera;

    public MainMenuScreen(final SocialGame game) {
        this.game = game;
        this.stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        Skin mySkin;
        mySkin = new Skin(Gdx.files.internal("skin/comic-ui.json"));
        Texture texture = new Texture("title.png");

        Image title = new Image(texture);
        addBackground();

        Button joinGameButton = new TextButton("Join Game",mySkin,"default");
        joinGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new JoinGameScreen(game));
                return true;
            }
        });
        Button createGameButton = new TextButton("Create Game",mySkin,"default");
        createGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new CreateGameScreen(game));
                return true;
            }
        });
        Button customiseButton = new TextButton("Customise",mySkin,"default");
        customiseButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new CustomiseScreen(game));
                return true;
            }
        });
        Button optionsButton = new TextButton("Options",mySkin,"default");
        optionsButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new OptionsScreen(game));
                return true;
            }
        });
        Button quitGameButton = new TextButton("Quit",mySkin,"default");
        quitGameButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                Gdx.app.exit();
                return true;
            }
        });

        /**
         *  Button testing, to be used to launch the game in its current temp state
         *  TODO: Fix setScreen not working
         */

        Button testingButton = new TextButton("testing",mySkin,"default");
        testingButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                return true;
            }
        });

        //	Button scaling done on the cell, for some reason it overrides .setSize() for buttons.
        // TODO: Change it from pixels to a fraction to improve scaling.



        Table table = new Table();
        table.setDebug(false); // turn on all debug lines (table, cell, and widget)
        table.setFillParent(true);
        table.center();
        table.add(title).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/3).colspan(2);
        table.row();
        table.add(joinGameButton).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/10).colspan(2).padBottom(10).padTop(10);
        table.row();
        table.add(createGameButton).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/10).colspan(2).padBottom(10).padTop(10);
        table.row();
        table.add(customiseButton).width(Gdx.graphics.getWidth()/3).height(Gdx.graphics.getHeight()/10).colspan(2).padBottom(10).padTop(10);
        table.row();
        table.add(optionsButton).width(Gdx.graphics.getWidth()/6).height(Gdx.graphics.getHeight()/10).padBottom(10).padTop(10).padRight(5);
        table.add(quitGameButton).width(Gdx.graphics.getWidth()/6).height(Gdx.graphics.getHeight()/10).padBottom(10).padTop(10);
        table.row();
        table.add(testing).colspan(2);
        stage.addActor(table);


    }


    public void addBackground(){
        Texture texture = new Texture(Gdx.files.internal("background.png"));
        TextureRegion textureRegion = new TextureRegion(texture);

        textureRegion.setRegion(0,0,texture.getWidth(),texture.getHeight());
        Image background = new Image(textureRegion);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth());
        background.setPosition(0,Gdx.graphics.getHeight()-background.getHeight());
        stage.addActor(background);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
