package dev.derivada;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int DELAY = 200;
    boolean running = false;
    private final ArrayList<Cell> aliveCells;
    private final Cell[][] cells;
    Timer timer;
    Random random;
    private long lastStepTime;
    GamePanel() {
        // Initialize cells
        cells = new Cell[SCREEN_WIDTH / UNIT_SIZE][SCREEN_HEIGHT/UNIT_SIZE];
        for(int i = 0; i<SCREEN_WIDTH/UNIT_SIZE; i++){
            for(int j = 0; j<SCREEN_HEIGHT/UNIT_SIZE; j++){
                cells[i][j] = new Cell(i, j);
            }
        }
        aliveCells = new ArrayList<Cell>();
        aliveCells.add(cells[5][5]);
        cells[5][5].resurrect();

        aliveCells.add(cells[6][6]);
        cells[6][6].resurrect();

        aliveCells.add(cells[6][7]);
        cells[6][7].resurrect();

        aliveCells.add(cells[7][5]);
        cells[7][5].resurrect();

        aliveCells.add(cells[7][6]);
        cells[7][6].resurrect();

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.darkGray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(40, 40, 40));

        if (aliveCells.size() > 0) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(new Color(240, 240, 240));

            // Dibujar celdas vivas
            for (Cell c : aliveCells) {
                g.fillRect(c.getX() * UNIT_SIZE, c.getY() * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }

            // Alive cells counter
            g.setColor(Color.ORANGE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Alive cells: " + aliveCells.size(), (SCREEN_WIDTH - metrics.stringWidth("Alive cells: " + aliveCells.size())) / 2, g.getFont().getSize());
            g.setFont(new Font("Arial", Font.ITALIC, 15));
            g.drawString("Last step time: " + aliveCells.size() + "ms", (SCREEN_WIDTH - metrics.stringWidth("Last step time: " + aliveCells.size() + "ms")) / 2, g.getFont().getSize()*4);
        } else {
            gameOver(g);
        }
        long currTime = System.currentTimeMillis();
        step();
        lastStepTime =  System.currentTimeMillis() - currTime;
    }


    public void step() {
        // The main algorithm for cell generation and destruction
        // All cells should be already initialized

        ArrayList<Cell> candidateCells = new ArrayList<Cell>();
        for (Cell c : aliveCells) {
            // Check neighbours of alive cells
            c.resetNeighbours();
            int x = c.getX(), y = c.getY();
            for (int i = Math.max(x - 1, 0); i < Math.min(x + 2, SCREEN_WIDTH / UNIT_SIZE); i++) {
                for (int j = Math.max(y - 1, 0); j < Math.min(y + 2, SCREEN_HEIGHT / UNIT_SIZE); j++) {
                    if(x==i && y == j) continue;
                    if (cells[i][j].isAlive()) {
                        // The neighbour of c alive, we increase the neighbour count of c
                        c.addNeighbour();
                    } else {
                        // The neighbour of c is dead, we add him as a candidate for resurrecting in this iteration
                        // and upjdate his neighbour count
                        if (!candidateCells.contains(cells[i][j]))
                            candidateCells.add(cells[i][j]);
                        cells[i][j].addNeighbour();
                    }
                }
            }
        }
        ListIterator<Cell> cellIt = aliveCells.listIterator();
        while(cellIt.hasNext()) {
            // Kill alive cells from underpopulation (neighbours < 2) or overpopulation (neighbours > 3)
            Cell c = cellIt.next();
            int neighbours = c.getNeighbours();
            if (neighbours > 3 || neighbours < 2) {
                c.kill();
                c.resetNeighbours();
                cellIt.remove();
            }
        }
        cellIt = candidateCells.listIterator();
        while(cellIt.hasNext()) {
            Cell c = cellIt.next();
            // Bring cells to life if they have exactly 3 neighbours
            if (c.getNeighbours() == 3) {
                c.resurrect();
                aliveCells.add(c);
            } else {
                c.resetNeighbours();
            }
        }
    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("All cells are dead", (SCREEN_WIDTH - metrics1.stringWidth("All cells are dead")) / 2, g.getFont().getSize());

        // Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {

        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    break;
                case KeyEvent.VK_RIGHT:
                    break;
                case KeyEvent.VK_UP:
                    break;
                case KeyEvent.VK_DOWN:
                    break;
            }
        }
    }
}