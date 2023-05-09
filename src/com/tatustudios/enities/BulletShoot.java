package com.tatustudios.enities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.tatustudios.main.Game;
import com.tatustudios.world.Camera;

public class BulletShoot extends Entity {

    private double dx;
    private double dy;
    private int spd = 4;
    private int life = 30;    
    private int curLife = 0;    

    public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
        super(x, y, width, height, null);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void tick() {
        x += dx * spd;
        y += dy * spd;
        curLife++;
        if (curLife == life) {
            Game.bullets.remove(this);
            return;
        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(this.x - Camera.x, this.y - Camera.y, width, height);
    }
}
