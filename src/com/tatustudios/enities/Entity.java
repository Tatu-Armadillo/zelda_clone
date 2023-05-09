package com.tatustudios.enities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.tatustudios.main.Game;
import com.tatustudios.world.Camera;

public class Entity {

    public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprit(96, 0, 16, 16);
    public static BufferedImage WEAPON_EN = Game.spritesheet.getSprit(112, 0, 16, 16);
    public static BufferedImage BULLET_EN = Game.spritesheet.getSprit(96, 16, 16, 16);
    public static BufferedImage ENEMY_EN = Game.spritesheet.getSprit(112, 16, 16, 16);
    public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprit(144, 16, 16, 16);
    public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprit(128, 0, 16, 16);
    public static BufferedImage GUN_LEFT = Game.spritesheet.getSprit(144, 0, 16, 16);

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage sprite;

    public int maskx;
    public int masky;
    public int mwidth;
    public int mheight;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;

        this.maskx = 0;
        this.masky = 0;
        this.mwidth = width;
        this.mheight = height;
    }

    public void render(Graphics g) {
        g.drawImage(this.sprite, this.x - Camera.x, this.y - Camera.y, null);
    }

    public void tick() { }

    // TODO VERIFICAR SOBREESCRITA DO METODO
    public static boolean isColidding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.x + e1.maskx, e1.y + e1.masky, e1.mwidth, e1.mheight);
        Rectangle e2Mask = new Rectangle(e2.x + e2.maskx, e2.y + e2.masky, e2.mwidth, e2.mheight);
        return e1Mask.intersects(e2Mask);
    }

    public void setMask(int maskx, int masky, int mwidth, int mheight) {
        this.maskx = maskx;
        this.masky = masky;
        this.mwidth = mwidth;
        this.mheight = mheight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
