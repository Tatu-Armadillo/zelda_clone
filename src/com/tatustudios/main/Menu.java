package com.tatustudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.tatustudios.enities.GameStage;

public class Menu {

    public String[] options = { "Novo Jogo", "Carregar Jogo", "Sair" };
    public int currentOption = 0;
    public int maxOption = options.length - 1;

    public boolean up;
    public boolean down;
    public boolean enter;
    public boolean pause;

    public void tick() {
        if (up) {
            up = false;
            currentOption--;
            if (currentOption < 0) {
                currentOption = maxOption;
            }
        } else if (down) {
            down = false;
            currentOption++;
            if (currentOption > maxOption) {
                currentOption = 0;
            }
        }

        if (enter) {
            enter = false;
            if (currentOption == 0) {
                Game.gameStage = GameStage.NORMAL;
                pause = false;
            } else if (currentOption == 2) {
                System.exit(1);
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
        g.setColor(Color.CYAN);
        g.setFont(new Font("arial", Font.BOLD, 36));
        g.drawString(">Clone Zelda<", (Game.WIDTH * Game.SCALE) / 2 - 140, (Game.HEIGHT * Game.SCALE) / 2 - 160);
        
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 24));
        if (!pause) {
            g.drawString(options[0], (Game.WIDTH * Game.SCALE) / 2 - 80, 200);
            g.drawString(options[1], (Game.WIDTH * Game.SCALE) / 2 - 100, 230);
            g.drawString(options[2], (Game.WIDTH * Game.SCALE) / 2 - 40, 260);
        } else {
            g.drawString("Resumir", (Game.WIDTH * Game.SCALE) / 2 - 80, 200);
            currentOption = 0;
        }

        if (currentOption == 0) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 120, 200);
        } else if (currentOption == 1) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 120, 230);
        } else if (currentOption == 2) {
            g.drawString(">", (Game.WIDTH * Game.SCALE) / 2 - 120, 260);
        }

    }

}
