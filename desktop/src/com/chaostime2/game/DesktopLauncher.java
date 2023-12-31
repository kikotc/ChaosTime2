package com.chaostime2.game;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		DisplayMode primaryDesktopMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Chaos Time");
		//config.setFullscreenMode(primaryDesktopMode);
		config.setWindowedMode(1920,1080);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setWindowIcon("player.png");
		new Lwjgl3Application(new ChaosTime(), config);
	}
}
