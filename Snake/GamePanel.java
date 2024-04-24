import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Random;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    // Initial game resolution
    static final int width = 1000;                   // SCREEN_WIDTH (digit: pixels)
    static final int height = 700;                 // SCREEN_HEIGHT (digit: pixels)
    static final int object_size = 25;              // UNIT_SIZE
    static final int game_objects = (width * height) / object_size;     // The number of objects that can fit the game screen

    // Initial game parameters
    static final int delay = 75;
    // Data Structure: Arrays
    final int x[] = new int [game_objects];
    final int y[] = new int [game_objects];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            // Gridlines
            // for (int i = 0; i < height / object_size; i++) {
            //     g.drawLine(i * object_size, 0, i * object_size, height);
            //     g.drawLine(0, i * object_size, width, i * object_size);
            // }
            // Apple
            g.setColor(Color.red);  
            g.fillOval(appleX, appleY, object_size, object_size);
            // Snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], object_size, object_size);                
                } else {
                    // Ordinary green color for the snake body
                    g.setColor(new Color(45, 180, 0));
                    // RGB color for the snake body
                    // g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], object_size, object_size);                
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 48));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (width - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(width / object_size)) * object_size;
        appleY = random.nextInt((int)(height / object_size)) * object_size;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];            
        }

        // Conditions for each direction of the snake
        switch (direction) {
            // Up
            case 'U':
            y[0] = y[0] - object_size;
            break;
            // Down
            case 'D':
            y[0] = y[0] + object_size;
            break;
            // Left
            case 'L':
            x[0] = x[0] - object_size;
            break;
            // Right
            case 'R':
            x[0] = x[0] + object_size;
            break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Check if head touches left border (of the JFrame)
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border (of the JFrame)
        if (x[0] > width) {
            running = false;
        }
        // Check if head touches top border (of the JFrame)
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border (of the JFrame)
        if (y[0] > height) {
            running = false;
        }
        // Stop the timer
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Game Over text
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (width - metrics1.stringWidth("Game Over")) / 2, height / 2);
        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 48));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (width - metrics2.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed (KeyEvent e) {
            switch(e.getKeyCode()) {
                // Left
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
                // Right
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
                // Up
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
                // Down
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            }
        }
    }
}
