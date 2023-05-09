package com.tatustudios.enities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.tatustudios.main.Game;
import com.tatustudios.world.Camera;
import com.tatustudios.world.World;

public class Player extends Entity {

    public boolean right;
    public boolean left;
    public boolean up;
    public boolean down;
    private boolean moved;

    public int rightDir = 0;
    public int leftDir = 1;
    public int dir = rightDir;
    public int speed = 2;
    private int frames = 0;
    private int maxFrames = 5;
    private int index = 0;
    private int maxIndex = 3;

    public double life = 10;
    public double maxLife = 10;

    public int mx;
    public int my;

    private BufferedImage[] rightPlayer;
    private BufferedImage[] leftPlayer;
    private BufferedImage playerDemage;

    private boolean hasGun;

    public int ammo;

    public boolean isDemaged = false;
    private int demageFrames = 0;

    public boolean shoot;
    public boolean mouseShoot;

    public Player(int x, int y, int width, int height, BufferedImage sprite) {
        super(x, y, width, height, sprite);

        rightPlayer = new BufferedImage[4];
        leftPlayer = new BufferedImage[4];
        playerDemage = Game.spritesheet.getSprit(0, 16, 16, 16);

        for (int i = 0; i < 4; i++) {
            rightPlayer[i] = Game.spritesheet.getSprit(32 + (i * 16), 0, 16, 16);
        }

        for (int i = 0; i < 4; i++) {
            leftPlayer[i] = Game.spritesheet.getSprit(32 + (i * 16), 16, 16, 16);
        }
    }

    @Override
    public void tick() {
        moved = false;
        if (right && World.isFree(x + speed, y)) {
            moved = true;
            dir = rightDir;
            this.x += speed;
        } else if (left && World.isFree(x - speed, y)) {
            moved = true;
            dir = leftDir;
            this.x -= speed;
        }

        if (up && World.isFree(x, y - speed)) {
            moved = true;
            this.y -= speed;
        } else if (down && World.isFree(x, y + speed)) {
            moved = true;
            this.y += speed;
        }

        if (moved) {
            frames++;
            if (frames == maxFrames) {
                frames = 0;
                index++;
                if (index > maxIndex) {
                    index = 0;
                }
            }
        }

        this.checkCpllisonLifePack();
        this.checkCollisonAmmo();
        this.checkCollisonGun();

        if (isDemaged) {
            this.demageFrames++;
            if (this.demageFrames == 8) {
                this.demageFrames = 0;
                isDemaged = false;
            }
        }

        if (shoot) {
            shoot = false;
            if (hasGun && ammo > 0) {

                ammo--;
                int dx = 0;
                int px = 0;
                int py = 7;
                if (dir == rightDir) {
                    px = 18;
                    dx = 1;
                } else {
                    px = -4;
                    dx = -1;
                }

                BulletShoot bullet = new BulletShoot(this.x + px, this.y + py, 3, 3, null, dx, 0);
                Game.bullets.add(bullet);
            }
        }

        if (mouseShoot) {
            mouseShoot = false;
            if (hasGun && ammo > 0) {

                ammo--;
                double angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x));

                double dx = Math.cos(angle);
                double dy = Math.sin(angle);
                int px = 8;
                int py = 8;

                BulletShoot bullet = new BulletShoot(this.x + px, this.y + py, 3, 3, null, dx, dy);
                Game.bullets.add(bullet);
            }
        }

        if (life <= 0) {
            life = 0;
            Game.gameStage = GameStage.GAME_OVER;
        }

        this.updateCamera();
    }

    private void updateCamera() {
        Camera.x = Camera.clamp(this.x - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
        Camera.y = Camera.clamp(this.y - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
    }

    @Override
    public void render(Graphics g) {
        if (!isDemaged) {
            if (dir == rightDir) {
                g.drawImage(rightPlayer[index], this.x - Camera.x, this.y - Camera.y, null);
                if (hasGun) {
                    g.drawImage(Entity.GUN_RIGHT, this.x - Camera.x + 8, this.y - Camera.y, null);
                }
            } else if (dir == leftDir) {
                g.drawImage(leftPlayer[index], this.x - Camera.x + 8, this.y - Camera.y, null);
                if (hasGun) {
                    g.drawImage(Entity.GUN_LEFT, this.x - Camera.x, this.y - Camera.y, null);
                }
            }
        } else {
            g.drawImage(playerDemage, this.x - Camera.x, this.y - Camera.y, null);
        }
    }

    public void checkCollisonGun() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Weapon) {
                if (Entity.isColidding(this, e)) {
                    hasGun = true;
                    Game.entities.remove(e);
                }
            }
        }
    }

    public void checkCollisonAmmo() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Bullet) {
                if (Entity.isColidding(this, e)) {
                    ammo += 100;
                    Game.entities.remove(e);
                }
            }
        }
    }

    public void checkCpllisonLifePack() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof LifePack) {
                if (Entity.isColidding(this, e)) {
                    life += 8;
                    if (life >= 10) {
                        life = 10;
                    }
                    Game.entities.remove(e);
                }
            }
        }
    }

}
