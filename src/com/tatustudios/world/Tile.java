package com.tatustudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.tatustudios.main.Game;

public class Tile {

    public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprit(0, 0, 16, 16);
    public static BufferedImage TILE_WALL = Game.spritesheet.getSprit(16, 0, 16, 16);

    private BufferedImage sprite;
    private int x;
    private int y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }

}
