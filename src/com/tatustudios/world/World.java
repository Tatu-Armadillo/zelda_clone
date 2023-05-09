package com.tatustudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.tatustudios.enities.Bullet;
import com.tatustudios.enities.Enemy;
import com.tatustudios.enities.Entity;
import com.tatustudios.enities.LifePack;
import com.tatustudios.enities.Player;
import com.tatustudios.enities.Weapon;
import com.tatustudios.graficos.Spritesheet;
import com.tatustudios.main.Game;

public class World {

    public static Tile[] tiles;
    public static int WIDTH;
    public static int HEIGHT;
    public static final int TILE_SIZE = 16;

    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            tiles = new Tile[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();

            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

                    if (pixelAtual == 0xFF000000) {
                        // CHAO
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    } else if (pixelAtual == 0xFFFFFFFF) {
                        // PAREDE
                        tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
                    } else if (pixelAtual == 0xFF0026FF) {
                        // PLAYER
                        Game.player.setX(xx * 16);
                        Game.player.setY(yy * 16);
                    } else if (pixelAtual == 0xFFFF0000) {
                        // ENEMY
                        Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
                        Game.entities.add(en);
                        Game.enemies.add(en);
                    } else if (pixelAtual == 0xFFFF6A00) {
                        // WEAPON
                        Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
                    } else if (pixelAtual == 0xFFFF7F7F) {
                        // LIFE PACK
                        LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN);
                        pack.setMask(8, 8, 8, 8);
                        Game.entities.add(pack);
                    } else if (pixelAtual == 0xFFFFD800) {
                        // BULLET
                        Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
                    } else {
                        // CHAO
                        tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restartGame(String level) {
        Game.entities = new ArrayList<Entity>();
        Game.enemies = new ArrayList<Enemy>();
        Game.spritesheet = new Spritesheet("/spritesheet.png");
        Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprit(32, 0, 16, 16));
        Game.entities.add(Game.player);
        Game.world = new World("/" + level);
        return;
    }

    public void render(Graphics g) {
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart + (Game.WIDTH >> 4);
        int yfinal = ystart + (Game.HEIGHT >> 4);

        for (int xx = xstart; xx <= xfinal; xx++) {
            for (int yy = ystart; yy <= yfinal; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
                    continue;
                }
                var calc = xx + (yy * WIDTH);
                Tile tile = tiles[calc];
                tile.render(g);
            }
        }
    }

    public static boolean isFree(int xNext, int yNext) {
        int x1 = xNext / TILE_SIZE;
        int y1 = yNext / TILE_SIZE;

        int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = yNext / TILE_SIZE;

        int x3 = xNext / TILE_SIZE;
        int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
                || (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
                || (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
                || (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

    }

}
