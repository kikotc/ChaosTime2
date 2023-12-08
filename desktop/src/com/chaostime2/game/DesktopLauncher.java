package com.chaostime2.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.chaostime2.game.MainGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Chaos Time");
		config.setWindowedMode(1920, 1080);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new MainGame(), config);
	}
}
