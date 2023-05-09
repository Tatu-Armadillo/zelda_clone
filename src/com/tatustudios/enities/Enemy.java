package com.tatustudios.enities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.tatustudios.main.Game;
import com.tatustudios.main.Sound;
import com.tatustudios.world.Camera;
import com.tatustudios.world.World;

public class Enemy extends Entity {

    private int speed = 1;
    private int maskx = 8;
    private int masky = 8;
    private int maskw = 10;
    private int maskh = 10;

    private int frames = 0;
    private int maxFrames = 20;
    private int index = 0;
    private int maxIndex = 1;
    private BufferedImage[] sprites;

    private int life = 10;

    private boolean isDemaged = false;
    private int demageFrames = 10;
    private int demageCurrent = 0;

    public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, null);
        sprites = new BufferedImage[2];
        sprites[0] = Game.spritesheet.getSprit(112, 16, 16, 16);
        sprites[1] = Game.spritesheet.getSprit(128, 16, 16, 16);
    }

    @Override
    public void tick() {

        if (!isColiddingWithPlayer()) {
            if (x < Game.player.x && World.isFree(x + speed, y)
                    && !isColidding(x + speed, y)) {
                x += speed;
            } else if (x > Game.player.x && World.isFree(x - speed, y)
                    && !isColidding(x - speed, y)) {
                x -= speed;
            }
            if (y < Game.player.y && World.isFree(x, y + speed)
                    && !isColidding(x, y + speed)) {
                y += speed;
            } else if (y > Game.player.y && World.isFree(x, y - speed)
                    && !isColidding(x, y - speed)) {
                y -= speed;
            }
        } else {
            if (Game.rand.nextInt(100) < 10) {
                Sound.hurtEffect.play();
                Game.player.life -= Game.rand.nextInt(3);
                Game.player.isDemaged = true;
            }
        }

        frames++;
        if (frames == maxFrames) {
            frames = 0;
            index++;
            if (index > maxIndex) {
                index = 0;
            }
        }

        collidingBullet();

        if (life <= 0) {
            destroySelf();
        }

        if (isDemaged) {
            this.demageCurrent++;
            if (this.demageCurrent == this.demageFrames) {
                this.demageCurrent = 0;
                this.isDemaged = false;
            }
        }

    }

    public void destroySelf() {
        Game.enemies.remove(this);
        Game.entities.remove(this);
    }

    public void collidingBullet() {
        for (int i = 0; i < Game.bullets.size(); i++) {
            Entity e = Game.bullets.get(i);
            if (Entity.isColidding(this, e)) {
                isDemaged = true;
                life--;
                Game.bullets.remove(i);
                return;
            }
        }
    }

    public boolean isColiddingWithPlayer() {
        Rectangle enemyCurrent = new Rectangle(this.x + maskx, this.y + masky, maskw, maskh);
        Rectangle player = new Rectangle(Game.player.x, Game.player.y, 16, 16);
        return enemyCurrent.intersects(player);
    }

    public boolean isColidding(int xNext, int yNext) {
        Rectangle enemyCurrent = new Rectangle(xNext + maskx, yNext + masky, maskw, maskh);
        for (var e : Game.enemies) {
            if (e == this) {
                continue;
            }
            Rectangle targetEnemy = new Rectangle(e.x + maskx, e.y + masky, maskw, maskh);
            if (enemyCurrent.intersects(targetEnemy)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
        if (!isDemaged) {
            g.drawImage(sprites[index], this.x - Camera.x, this.y - Camera.y, null);
        } else {
            g.drawImage(Entity.ENEMY_FEEDBACK, this.x - Camera.x, this.y - Camera.y, null);
        }

    }

}
