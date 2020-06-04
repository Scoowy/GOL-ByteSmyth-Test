package org.codecat;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;

/**
 * Class
 *
 * @author Scoowy
 * @version 2020.06.03.1347
 */
public class Toolbar extends ToolBar {

    private MainView mainView;

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
        this.mainView.getSimulator().stop();
    }

    private void handleStart(ActionEvent event) {
        this.mainView.setApplicationState(MainView.SIMULATING);
        this.mainView.getSimulator().start();
    }

    private void handleReset(ActionEvent event) {
        this.mainView.setApplicationState(MainView.EDITING);
        this.mainView.draw();
    }

    private void handleStep(ActionEvent event) {
        System.out.println("Step");

        this.mainView.setApplicationState(MainView.SIMULATING);

        this.mainView.getSimulation().step();
        this.mainView.draw();
    }

    private void handleErase(ActionEvent event) {
        System.out.println("Erase");
        this.mainView.setDrawMode(Simulation.DEAD);
    }

    private void handleDraw(ActionEvent event) {
        System.out.println("Draw");
        this.mainView.setDrawMode(Simulation.ALIVE);
    }

}
