package com.socialgame.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.socialgame.game.SocialGame;

public class Options extends MenuScreen {
    public Options(final SocialGame game) {
        super(game);
        Image title = new Image(game.menuSpriteSheet.findRegion("title"));

        //TEMP BUTTON
        //TODO: Fix buttons and image not showing up.
        Button backButton = new TextButton("Back",skin,"default");
        backButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.setScreen(new Main(game));
                return true;
            }
        });

        Label optionsLabel = new Label("Options", skin, "big");
        Label setting1 = new Label("this is the VIDEO tab", skin, "default");
        Label setting2 = new Label("this is the AUDIO tab", skin, "default");
        Label masVolLabel = new Label("Master Volume:", skin, "default");
        Label sfxLabel = new Label("SFX Volume:", skin, "default");
        Label musicLabel = new Label("Music Volume:", skin, "default");
        Label voiceLabel = new Label("VOICE", skin, "default");
        Label micLabel = new Label("Microphone:", skin, "default");
        Label micVolLabel = new Label("Microphone Volume:", skin, "default");
        Label resLabel = new Label("Resolution:", skin, "default");
        Button videoButton = new TextButton("Video", skin, "default");
        Button audioButton = new TextButton("Audio", skin, "default");

        final Slider masVol = new Slider(0,1, 0.05f, false, skin);
        masVol.setValue(game.settings.getMasterVol());

        final Slider SFX = new Slider(0,1, 0.05f, false, skin);
        SFX.setValue(game.settings.getSFXVol());

        final Slider musVol = new Slider(0,1, 0.05f, false, skin);
        musVol.setValue(game.settings.getMusicVol());

        final Slider micVol = new Slider(0,1, 0.05f, false, skin);
        micVol.setValue(game.settings.getMicVol());

        final SelectBox<String> resSettings = new SelectBox<String>(skin);
        resSettings.setItems("1280x720", "1920x1080", "2560x1440");
        resSettings.setSelected(game.settings.getResolution());

        final SelectBox<String> micSettings = new SelectBox<String>(skin);
        micSettings.setItems("Press to Talk", "On", "Off");
        micSettings.setSelected(game.settings.getMic());

        // region Debug mode checkbox

        Label debugLabel = new Label("Draw debug: ", skin, "default");
        Drawable checked = new TextureRegionDrawable(game.menuSpriteSheet.findRegion("checkbox_tick"));
        Drawable unchecked = new TextureRegionDrawable(game.menuSpriteSheet.findRegion("checkbox_base"));
        final ImageButton debugCheckBox = new ImageButton(unchecked, checked, checked);
        debugCheckBox.setChecked(game.settings.getDebug());

        // endregion

        Table debugGroup = new Table();
        debugGroup.add(debugLabel);
        debugGroup.add(debugCheckBox).size(20);


        // Global container
        Table globalTable = new Table();
        stage.addActor(globalTable);
        globalTable.setFillParent(true);
        globalTable.add().width(Gdx.graphics.getWidth()/4f);

        //region Navigation container

        Table navOptionsButtons = new Table();
        navOptionsButtons.add(optionsLabel);
        navOptionsButtons.row();
        navOptionsButtons.add(videoButton).height(50);
        navOptionsButtons.row();
        navOptionsButtons.add(audioButton).height(50);
        navOptionsButtons.row();
        navOptionsButtons.add(backButton).height(50);

        globalTable.add(navOptionsButtons).width(1/5f * Gdx.graphics.getWidth());

        //endregion

        // region Video settings container

        final Table videoOptions = new Table();
        videoOptions.setFillParent(true);
        videoOptions.add(setting1);
        videoOptions.row();
        videoOptions.add(resLabel);
        videoOptions.row();
        videoOptions.add(resSettings);
        videoOptions.row();
        videoOptions.add(debugGroup);

        // endregion

        // region Audio settings container

        final Table audioOptions = new Table();
        audioOptions.setFillParent(true);
        audioOptions.add(setting2);
        audioOptions.row();
        audioOptions.add(masVolLabel);
        audioOptions.row();
        audioOptions.add(masVol);
        audioOptions.row();
        audioOptions.add(sfxLabel);
        audioOptions.row();
        audioOptions.add(SFX);
        audioOptions.row();
        audioOptions.add(musicLabel);
        audioOptions.row();
        audioOptions.add(musVol);
        audioOptions.row();
        audioOptions.add(voiceLabel);
        audioOptions.row();
        audioOptions.add(micLabel);
        audioOptions.row();
        audioOptions.add(micSettings);
        audioOptions.row();
        audioOptions.add(micVolLabel);
        audioOptions.row();
        audioOptions.add(micVol);

        // endregion

        // Settings widget group for RHS of global table
        WidgetGroup settingsGroup = new WidgetGroup();
        settingsGroup.addActor(videoOptions);
        settingsGroup.addActor(audioOptions);
        globalTable.add(settingsGroup).expand();

        // Save button for settings.
        Button saveButton = new TextButton("Save", skin, "default");
        saveButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                game.settings.setResolution(resSettings.getSelected());
                game.settings.setDebug(debugCheckBox.isChecked());
                game.settings.setMasterVol(masVol.getValue());
                game.settings.setSFXVol(SFX.getValue());
                game.settings.setMusicVol(musVol.getValue());
                game.settings.setMic(micSettings.getSelected());
                game.settings.setMicVol(micVol.getValue());
                game.settings.save();

                stage.setDebugAll(game.settings.getDebug());
                return true;
            }
        });
        globalTable.add().width(Gdx.graphics.getWidth()/4f);
        globalTable.row();
        globalTable.add(saveButton).colspan(4).padBottom(150f);

        // Set default state
        videoOptions.setVisible(true);
        audioOptions.setVisible(false);


        videoButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                videoOptions.setVisible(true);
                audioOptions.setVisible(false);
                return true;
            }
        });
        audioButton.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) { /* touchDown = hovering over button */
                videoOptions.setVisible(false);
                audioOptions.setVisible(true);
                return true;
            }
        });
    }
}
