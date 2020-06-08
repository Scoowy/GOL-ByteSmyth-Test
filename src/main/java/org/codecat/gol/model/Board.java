package org.codecat.gol.model;

/**
 * Class
 *
 * @author Scoowy
 * @version 2020.06.03.2049
 */
public interface Board {
    Board copy();

    CellState getState(int x, int y);

    void setState(int x, int y, CellState cellState);

    int getWidth();

    int getHeight();
}
