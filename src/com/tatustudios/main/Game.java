package com.tatustudios.main;

import javax.swing.JFrame;

import com.tatustudios.enities.BulletShoot;
import com.tatustudios.enities.Enemy;
import com.tatustudios.enities.Entity;
import com.tatustudios.enities.GameStage;
import com.tatustudios.enities.Player;
import com.tatustudios.graficos.Spritesheet;
import com.tatustudios.graficos.UI;
import com.tatustudios.world.World;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {

    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static final int WIDTH = 240;
    public static final int HEIGHT = 160;
    public static final int SCALE = 3;

    private int CUR_LEVEL = 1;
    private int MAX_LEVEL = 3;

    private BufferedImage image;
    public UI ui;

    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<BulletShoot> bullets;
    public static Spritesheet spritesheet;
    public static World world;
    public static Player player;
    public static Random rand;

    public static GameStage gameStage = GameStage.MENU;
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean restartGame;
    public Menu menu;

    public Game() {
        Sound.musicBackground.loop();
        rand = new Random();
        addKeyListener(this);
        addMouseListener(this);
        setPreferredSize((new Dimension(WIDTH * SCALE, HEIGHT * SCALE)));
        initFrame();

        ui = new UI();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        entities = new ArrayList<Entity>();
        enemies = new ArrayList<Enemy>();
        bullets = new ArrayList<BulletShoot>();
        spritesheet = new Spritesheet("/spritesheet.png");
        player = new Player(0, 0, 16, 16, spritesheet.getSprit(32, 0, 16, 16));
        entities.add(player);
        world = new World("/level1.png");
        menu = new Menu();
    }

    public void initFrame() {
        frame = new JFrame("Zelda Clone");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        if (gameStage.equals(GameStage.NORMAL)) {
            this.restartGame = false;
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.tick();
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).tick();
            }

            if (enemies.isEmpty()) {
                CUR_LEVEL++;
                if (CUR_LEVEL > MAX_LEVEL) {
                    CUR_LEVEL = 1;
                }
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if (gameStage.equals(GameStage.GAME_OVER)) {
            this.framesGameOver++;
            if (this.framesGameOver == 30) {
                this.framesGameOver = 0;
                if (this.showMessageGameOver) {
                    this.showMessageGameOver = false;
                } else {
                    this.showMessageGameOver = true;
                }
            }
            if (restartGame) {
                this.restartGame = false;
                gameStage = GameStage.NORMAL;
                CUR_LEVEL = 1;
                String newWorld = "level" + CUR_LEVEL + ".png";
                World.restartGame(newWorld);
            }
        } else if (gameStage.equals(GameStage.MENU)) {
            menu.tick();
        }

    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        world.render(g);
        for (Entity e : entities) {
            e.render(g);
        }
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).render(g);
        }
        ui.render(g);

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.setFont(new Font("arial", Font.BOLD, 17));
        g.setColor(Color.white);
        g.drawString("Ammo: " + player.ammo, 580, 20);

        if (gameStage.equals(GameStage.GAME_OVER)) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(new Color(0, 0, 0, 100));
            g2.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
            g.setFont(new Font("arial", Font.BOLD, 36));
            g.setColor(Color.white);
            g.drawString("GAME OVER", (WIDTH * SCALE) / 2 - 50, (HEIGHT * SCALE) / 2 - 20);
            g.setFont(new Font("arial", Font.BOLD, 32));
            if (showMessageGameOver) {
                g.drawString(">Pressione Enter para reiniciar<", (WIDTH * SCALE) / 2 - 200, (HEIGHT * SCALE) / 2 + 40);
            }
        } else if (gameStage.equals(GameStage.MENU)) {
            menu.render(g);
        }
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double emountOfTicks = 60.0;
        double ns = 1000000000 / emountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();

        requestFocus();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                frames++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer += 1000;
            }
        }

        stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.up = true;
            if (gameStage.equals(GameStage.MENU)) {
                menu.up = true;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.down = true;
            if (gameStage.equals(GameStage.MENU)) {
                menu.down = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_X) {
            player.shoot = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.restartGame = true;
            if (gameStage.equals(GameStage.MENU)) {
                menu.enter = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gameStage = GameStage.MENU;
            menu.pause = true;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            player.up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
            player.down = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mouseShoot = true;
        player.mx = e.getX() / 3;
        player.my = e.getY() / 3;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

}
