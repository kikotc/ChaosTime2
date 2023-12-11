package com.chaostime2.game;

import com.badlogic.gdx.math.Circle;
public class CircleTime extends Circle {

    public long cooldown;


    public void setCooldown(long time) {
        cooldown = time;
    }
    public long getCooldown() {
        return cooldown;
    }

}