package com.socialgame.game.HUD;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.utils.Disposable;
import com.socialgame.game.SocialGame;
import org.graalvm.compiler.phases.common.NodeCounterPhase;

public class HUD extends Group {
    protected SocialGame game;


    private ProgressBars progressBar;
    private ProgressBars hazardBar;

    public HUD (SocialGame game) {

    }
}
