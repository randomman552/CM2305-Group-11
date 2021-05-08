package com.socialgame.game.tasks.async;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.socialgame.game.SocialGame;
import com.socialgame.game.tasks.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClockCalibrationTask extends Task {
    /**
     * Clock widget displayed in the center of this task screen.
     * The player enters their response into this widget.
     */
    private static class ClockWidget extends Table {
        private final Label hourLabel;
        private final Label minuteLabel;

        public ClockWidget(SocialGame game, int hour, int minute) {
            LabelStyle style = new LabelStyle();
            FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
            fontParameter.size = 140;

            style.font = game.generateFont(Gdx.files.internal("fonts/time.ttf"), fontParameter);

            hourLabel = new Label("00", style);
            hourLabel.setAlignment(Align.center);
            Label dividerLabel = new Label(":", style);
            minuteLabel = new Label("00", style);
            minuteLabel.setAlignment(Align.center);

            row().fill();
            add(hourLabel).width(140);
            add(dividerLabel).center();
            add(minuteLabel).width(140);

            setHour(hour);
            setMinute(minute);
        }

        public void setHour(int val) {
            val = (val < 0) ? (24 + val) % 24 : val % 24;
            hourLabel.setText(String.format("%02d", val));
        }

        public void setMinute(int val) {
            val = (val < 0) ? (60 + val) % 60 : val % 60;
            minuteLabel.setText(String.format("%02d", val));
        }

        public int getHour() {
            return Integer.parseInt(hourLabel.getText().toString());
        }

        public int getMinute() {
            return Integer.parseInt(minuteLabel.getText().toString());
        }
    }

    /**
     * Left hand table widget for this task.
     * Shows a random time and timezone for the player to perform the offset from.
     * For example, this window could display GMT+0, and show the time 04:05.
     * Simplifies the ClockCalibrationTask's constructor significantly.
     * WARN:
     *  titleFont and normalFont need to BOTH be initialised before constructing this.
     */
    private class CurrentTimeWidget extends Table {
        public CurrentTimeWidget(SocialGame game, int hour, int minute) {
            // Create style for title and time labels
            LabelStyle titleStyle = new LabelStyle();
            LabelStyle timeStyle = new LabelStyle();
            titleStyle.font = titleFont;
            timeStyle.font = normalFont;

            // Generate label text
            String hourText = String.format("%02d", hour);
            String minuteText = String.format("%02d", minute);

            // Create labels
            Label titleLabel = new Label("GMT+0", titleStyle);
            Label curTimeLabel = new Label(hourText + ":" + minuteText, timeStyle);
            titleLabel.setAlignment(Align.center);
            curTimeLabel.setAlignment(Align.center);

            // Add labels to table
            row().expandY();
            add(titleLabel).fill();
            row().height(50);
            add(curTimeLabel).fill();
        }
    }

    /**
     * Right hand table widget for this task.
     * WARN:
     *  titleFont and normalFont need to BOTH be initialised before constructing this.
     */
    private class TimeZoneListWidget extends Table {
        public TimeZoneListWidget(SocialGame game, Map<String, Integer> cityOffsets) {
            // Create font parameters for title and time fonts
            FreeTypeFontParameter titleFontParam = new FreeTypeFontParameter();
            FreeTypeFontParameter timeFontParam = new FreeTypeFontParameter();
            titleFontParam.size = 70;
            timeFontParam.size = 40;

            // Create style for title and time labels
            LabelStyle titleStyle = new LabelStyle();
            LabelStyle timeStyle = new LabelStyle();
            titleStyle.font = titleFont;
            timeStyle.font = normalFont;

            Label titleLabel = new Label("Zones", titleStyle);
            titleLabel.setAlignment(Align.center);

            row().expandY();
            add(titleLabel).expandX();
            for (Map.Entry entry: cityOffsets.entrySet()) {
                String text = entry.getKey() + ":" + entry.getValue();
                Label cityLabel = new Label(text, timeStyle);

                row().height(50);
                add(cityLabel);
            }
        }
    }

    /**
     * Interactable clock widget, main element of this task.
     */
    private final ClockWidget clockWidget;

    protected final BitmapFont titleFont;
    protected final BitmapFont normalFont;

    private final int tgtHour;
    private final int tgtMinute;

    public ClockCalibrationTask(final SocialGame game, float x, float y) {
        super(game, x, y);

        // region Font creation

        // Create font parameters for title and time fonts
        FreeTypeFontParameter titleFontParam = new FreeTypeFontParameter();
        FreeTypeFontParameter timeFontParam = new FreeTypeFontParameter();
        titleFontParam.size = 46;
        timeFontParam.size = 32;

        titleFont = game.generateFont(Gdx.files.internal("fonts/dot-matrix.ttf"), titleFontParam);
        normalFont = game.generateFont(Gdx.files.internal("fonts/dot-matrix.ttf"), timeFontParam);

        // endregion

        //Hashmap containing GMT offsets for the cities in use by this task
        Map<String, Integer> cityOffsets = new HashMap<String, Integer>();
        cityOffsets.put("London", 0);
        cityOffsets.put("Tokyo", 9);
        cityOffsets.put("Beijing", 8);
        cityOffsets.put("New York", -5);

        // Temp array to use when selecting a random city
        String[] cities = new String[] { "London", "Tokyo", "Beijing", "New York" };

        // Generate random task variables
        Random random = new Random();

        // Random starting variables for current time
        int startHour = random.nextInt(24);
        int startMinute = random.nextInt(60);

        // Generate random target time depending on start hour and a randomly picked city offset
        tgtMinute = startMinute;
        String chosenCity = cities[random.nextInt(cities.length)];
        int tgtHourTemp = startHour + cityOffsets.get(chosenCity);

        // Wrap target hour around, TODO: This branches so is not an ideal solution
        tgtHour = (tgtHourTemp < 0) ? tgtHourTemp + 24 : tgtHourTemp % 24;

        // Create sub-tables
        Table leftTable = new CurrentTimeWidget(game, startHour, startMinute);
        Table centerTable = new Table();
        Table rightTable = new TimeZoneListWidget(game, cityOffsets);

        // Add sub-tables to main table
        table.row().expandY();
        table.add(leftTable).expandX().align(Align.top);
        table.add(centerTable).width(500).fill();
        table.add(rightTable).expandX().align(Align.top);

        // Create button styles
        ImageButtonStyle upBtnStyle = new ImageButtonStyle();
        ImageButtonStyle downBtnStyle = new ImageButtonStyle();
        Drawable triangleUp = new TextureRegionDrawable(game.spriteSheet.findRegion("triangleUp"));
        Drawable triangleDown = new TextureRegionDrawable(game.spriteSheet.findRegion("triangleDown"));

        upBtnStyle.imageUp = triangleUp;
        downBtnStyle.imageUp = triangleDown;

        // Create time step buttons
        ImageButton hourUp = new ImageButton(upBtnStyle);
        hourUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clockWidget.setHour(clockWidget.getHour() + 1);
                game.playSound("beep");
                return true;
            }
        });

        ImageButton hourDown = new ImageButton(downBtnStyle);
        hourDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clockWidget.setHour(clockWidget.getHour() - 1);
                game.playSound("beep");
                return true;
            }
        });

        ImageButton minuteUp = new ImageButton(upBtnStyle);
        minuteUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clockWidget.setMinute(clockWidget.getMinute() + 1);
                game.playSound("beep");
                return true;
            }
        });

        ImageButton minuteDown = new ImageButton(downBtnStyle);
        minuteDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clockWidget.setMinute(clockWidget.getMinute() - 1);
                game.playSound("beep");
                return true;
            }
        });

        // Clock is created with random initial time
        clockWidget = new ClockWidget(game, random.nextInt(24), random.nextInt(60));

        LabelStyle cityLabelStyle = new LabelStyle();
        cityLabelStyle.font = titleFont;
        Label cityLabel = new Label(chosenCity, cityLabelStyle);
        cityLabel.setAlignment(Align.center);

        // Define centerTable layout
        centerTable.row().height(70);
        centerTable.add(cityLabel).fill().colspan(2);
        centerTable.row().height(50);
        centerTable.add(hourUp).width(50);
        centerTable.add(minuteUp).width(50);
        centerTable.row().height(150);
        centerTable.add(clockWidget).fill().colspan(2);
        centerTable.row().height(50);
        centerTable.add(hourDown).width(50);
        centerTable.add(minuteDown).width(50);
    }

    @Override
    public void act(float delta) {
        // Check if task is complete
        if (clockWidget.getHour() == tgtHour && clockWidget.getMinute() == tgtMinute)
            setComplete(true);
        //NOTE: Currently has no failure condition

        super.act(delta);
    }
}
