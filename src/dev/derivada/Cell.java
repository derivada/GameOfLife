package dev.derivada;

public class Cell {
    private final int x, y;
    private boolean alive;
    private int neighbours;

    public Cell(int x, int y){
        // Creates a dead cell at the x, y coordinates
        this.x = x;
        this.y = y;
        this.neighbours = 0;
        this.alive = false;
    }

    public void kill(){
        this.alive = false;
    }
    public void resurrect(){
        this.alive = true;
    }
    public boolean isAlive(){
        return this.alive;
    }
    public void addNeighbour(){
        this.neighbours++;
    }
    public int getNeighbours(){
        return neighbours;
    }
    public void resetNeighbours(){
        this.neighbours = 0;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }



}
