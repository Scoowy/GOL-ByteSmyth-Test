package org.codecat.gol;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import org.codecat.gol.model.Board;
import org.codecat.gol.model.BoundedBoard;
import org.codecat.gol.model.CellState;
import org.codecat.gol.model.StandardRule;

/**
 * Class
 *
 * @author Scoowy
 * @version 2020.06.03.1121
 */
public class MainView extends VBox {

    public static final int EDITING = 0;
    public static final int SIMULATING = 1;

    private InfoBar infoBar;
    private Canvas canvas;

    private Affine affine;

    private Simulation simulation;
    private Board initialBoard;

    private CellState drawMode = CellState.ALIVE;

    private int applicationState = EDITING;

    public MainView() {
        this.canvas = new Canvas(400, 400);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.canvas.setOnMouseMoved(this::handleMove);

        this.setOnKeyPressed(this::onKeyPressed);

        ToolBar toolBar = new Toolbar(this);

        this.infoBar = new InfoBar();
        this.infoBar.setDrawMode(drawMode);
        this.infoBar.setCursorPosition(0, 0);

        Pane spacer = new Pane();
        spacer.setMinSize(0, 0);
        spacer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(toolBar, this.canvas, spacer, infoBar);

        this.affine = new Affine();
        this.affine.appendScale(400 / 10f, 400 / 10f);

        this.initialBoard = new BoundedBoard(10, 10);
    }


    private void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.D) {
            this.drawMode = CellState.ALIVE;
            System.out.println("Draw mode");
        } else if (keyEvent.getCode() == KeyCode.E) {
            this.drawMode = CellState.DEAD;
            System.out.println("Erase mode");
        }
    }

    private void handleMove(MouseEvent event) {
        Point2D simCoord = this.getSimulationCoordinates(event);
        this.infoBar.setCursorPosition((int) simCoord.getX(), (int) simCoord.getY());
    }

    private void handleDraw(MouseEvent event) {

        if (this.applicationState == SIMULATING) {
            return;
        }

        Point2D simCoord = this.getSimulationCoordinates(event);

        int simX = (int) simCoord.getX();
        int simY = (int) simCoord.getY();

        System.out.println(simX + ", " + simY);

        this.initialBoard.setState(simX, simY, drawMode);
        draw();
    }

    private Point2D getSimulationCoordinates(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            return simCoord;
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException("Non invertible transform");
        }
    }

    public void draw() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, 0, 450, 450);

        if (this.applicationState == EDITING) {
            drawSimulation(this.initialBoard);
        } else {
            drawSimulation(this.simulation.getBoard());
        }

        g.setStroke(Color.GRAY);
        g.setLineWidth(0.05);
        for (int x = 0; x <= this.initialBoard.getWidth(); x++) {
            g.strokeLine(x, 0, x, 10);
        }

        for (int y = 0; y <= this.initialBoard.getHeight(); y++) {
            g.strokeLine(0, y, 10, y);
        }
    }

    private void drawSimulation(Board simulationToDraw) {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setFill(Color.BLACK);
        for (int x = 0; x < simulationToDraw.getWidth(); x++) {
            for (int y = 0; y < simulationToDraw.getHeight(); y++) {
                if (simulationToDraw.getState(x, y) == CellState.ALIVE) {
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setDrawMode(CellState mode) {
        this.drawMode = mode;
        this.infoBar.setDrawMode(mode);
    }

    public void setApplicationState(int applicationState) {
        if (applicationState == this.applicationState) {
            return;
        }

        if (applicationState == SIMULATING) {
            this.simulation = new Simulation(this.initialBoard, new StandardRule());
        }

        this.applicationState = applicationState;

        System.out.println("Application State: " + this.applicationState);
    }

    public int getApplicationState() {
        return applicationState;
    }
}
