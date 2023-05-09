package com.tatustudios.graficos;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Spritesheet {

    private BufferedImage spritesheet;

    public Spritesheet(String path) {
        try {
            spritesheet = ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprit(int x, int y, int width, int height) {
        return spritesheet.getSubimage(x, y, width, height);
    }

}
