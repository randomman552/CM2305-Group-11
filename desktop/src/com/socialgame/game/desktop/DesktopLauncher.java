package com.socialgame.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.socialgame.game.SocialGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 2560 ;
		config.height = 1440;
		config.fullscreen = true;
		config.forceExit = true;
		new LwjglApplication(new SocialGame(), config);
	}
}
