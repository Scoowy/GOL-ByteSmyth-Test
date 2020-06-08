package org.codecat.gol.model;

/**
 * Interface
 *
 * @author Scoowy
 * @version 2020.06.07.1847
 */
public interface SimulationRule {
    CellState getNextState(int x, int y, Board board);
}
