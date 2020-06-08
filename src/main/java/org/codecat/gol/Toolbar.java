package org.codecat.gol;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import org.codecat.gol.model.CellState;

/**
 * Class
 *
 * @author Scoowy
 * @version 2020.06.03.1347
 */
public class Toolbar extends ToolBar {

    private MainView mainView;

    private Simulator simulator;

    public Toolbar(MainView mainView) {
        this.mainView = mainView;
        Button draw = new Button("Draw");
        draw.setOnAction(this::handleDraw);
        Button erase = new Button("Erase");
        erase.setOnAction(this::handleErase);
        Button step = new Button("Step");
        step.setOnAction(this::handleStep);
        Button reset = new Button("Reset");
        reset.setOnAction(this::handleReset);
        Button start = new Button("Start");
        start.setOnAction(this::handleStart);
        Button stop = new Button("Stop");
        stop.setOnAction(this::handleStop);

        this.getItems().addAll(draw, erase, reset, step, start, stop);
    }

    private void handleStop(ActionEvent event) {
        this.simulator.stop();
    }

    private void handleStart(ActionEvent event) {
        switchToSimulatingState();
        this.simulator.start();
    }

    private void handleReset(ActionEvent event) {
        this.mainView.setApplicationState(MainView.EDITING);
        this.simulator = null;
        this.mainView.draw();
    }

    private void handleStep(ActionEvent event) {
        System.out.println("Step");

        switchToSimulatingState();

        this.mainView.getSimulation().step();
        this.mainView.draw();
    }

    private void switchToSimulatingState() {
        if (this.mainView.getApplicationState() == MainView.EDITING) {
            this.mainView.setApplicationState(MainView.SIMULATING);
            this.simulator = new Simulator(this.mainView, this.mainView.getSimulation());
        }
    }

    private void handleErase(ActionEvent event) {
        System.out.println("Erase");
        this.mainView.setDrawMode(CellState.DEAD);
    }

    private void handleDraw(ActionEvent event) {
        System.out.println("Draw");
        this.mainView.setDrawMode(CellState.ALIVE);
    }

}
