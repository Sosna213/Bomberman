package Bomberman;

import java.io.Serializable;

public class Bomb implements Serializable {
    public String type;
    public int range;

    public Bomb(String type, int range) {
        this.type = type;
        this.range = range;
    }
}
