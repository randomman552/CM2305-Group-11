package com.socialgame.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.socialgame.game.SocialGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1240 ;
		config.height = 720;
		config.fullscreen = false;
		config.forceExit = true;
		new LwjglApplication(new SocialGame(), config);
	}
}
