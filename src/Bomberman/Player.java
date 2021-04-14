package Bomberman;

import java.io.Serializable;


public class Player implements Serializable{
    public String name;
    public float posX;
    public float posY;
    public String bombType;
    public int range = 2;
    public boolean alive = true;


    public Player(String name) {
        this.name = name;
        this.bombType = "normal";
    }

    public String getBombType() {
        return bombType;
    }

    public int getRange() {
        return range;
    }
}
