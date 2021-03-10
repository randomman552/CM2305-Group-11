package com.socialgame.game.tasks.async;

import com.badlogic.gdx.Gdx;
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

public class ClockCalibrationTask extends Task {
    private static class Clock extends Table {
        private final Label hourLabel;
        private final Label minuteLabel;

        public Clock(SocialGame game) {
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
        }

        public void setHour(int val) {
            val = (val < 0) ? (24 + val) % 24 : val % 24;
            if (val < 10) {
                hourLabel.setText("0" + val);
                return;
            }
            hourLabel.setText(val);
        }

        public void setMinute(int val) {
            val = (val < 0) ? (60 + val) % 60 : val % 60;
            if (val < 10) {
                minuteLabel.setText("0" + val);
                return;
            }
            minuteLabel.setText(val);
        }

        public int getHour() {
            return Integer.parseInt(hourLabel.getText().toString());
        }

        public int getMinute() {
            return Integer.parseInt(minuteLabel.getText().toString());
        }
    }

    private final Clock clock;

    public ClockCalibrationTask(SocialGame game, float x, float y) {
        super(game, x, y);

        // Create sub-tables
        Table leftTable = new Table();
        Table centerTable = new Table();
        Table rightTable = new Table();

        // Add sub-tables to main table
        table.row().expandY();
        table.add(leftTable).expandX();
        table.add(centerTable).width(500).fill();
        table.add(rightTable).expandX();

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
                clock.setHour(clock.getHour() + 1);
                return true;
            }
        });

        ImageButton hourDown = new ImageButton(downBtnStyle);
        hourDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clock.setHour(clock.getHour() - 1);
                return true;
            }
        });

        ImageButton minuteUp = new ImageButton(upBtnStyle);
        minuteUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clock.setMinute(clock.getMinute() + 1);
                return true;
            }
        });

        ImageButton minuteDown = new ImageButton(downBtnStyle);
        minuteDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                clock.setMinute(clock.getMinute() - 1);
                return true;
            }
        });

        // Define leftTable layout

        // Define centerTable layout
        clock = new Clock(game);
        centerTable.row().height(50);
        centerTable.add(hourUp).width(50);
        centerTable.add(minuteUp).width(50);
        centerTable.row().height(150);
        centerTable.add(clock).fill().colspan(2);
        centerTable.row().height(50);
        centerTable.add(hourDown).width(50);
        centerTable.add(minuteDown).width(50);

        // Define rightTable layout

    }
}
