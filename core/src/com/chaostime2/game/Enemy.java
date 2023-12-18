package com.chaostime2.game;

public class Enemy {

    public static int health = 20;
    boolean damaged = false;


    public Enemy(int health){
        this.health = health;
    }

    public static void update(float deltaTime, boolean damaged){

        if(damaged){
            health-=10;
        }
    }
}
