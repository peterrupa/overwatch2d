package com.overwatch2d.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.overwatch2d.game.Overwatch2D;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.fullscreen = true;
        config.vSyncEnabled = true;
        config.width = 1280;
        config.height = 720;

		new LwjglApplication(new Overwatch2D(), config);
	}
}
