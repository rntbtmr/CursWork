package plannerApp.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimerHandler {
    private int minutes = 25;
    private int seconds = 0;
    @FXML
    private Label timerLabel;
    private Timeline timeline;
    private boolean isRunning = false;
    private boolean isBreak = false;
    private double angle = 90;
    private final double startX = 75;
    private final double startY = 75;
    private double endX = 75;
    private double endY = 10;


    @FXML
    private Canvas canvas;
    private GraphicsContext gc;



    public TimerHandler(

    ) {
    }

    @FXML
    public void reset() {
        stopTimer();
        isBreak = false;
        angle = 0;
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        endX = 75;
        endY = 10;
        gc.strokeLine(startX, startY, endX, endY);
        minutes = 25;
        seconds = 0;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);
    }

    @FXML
    public void toggleTimer() {
        if (isRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }
    private void startTimer() {
        isRunning = true;
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.strokeLine(startX, startY, endX, endY);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        updateTimer();
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void stopTimer() {
        isRunning = false;
        if (timeline != null) {
            timeline.stop();
        }
    }

    private void changeMode() {
        if (!isBreak) {
            isBreak = true;
            seconds = 0;
            minutes = 5;
        } else  {
            isBreak = false;
            seconds = 0;
            minutes = 25;
        }
        endX = 75;
        endY = 10;
        angle = 0;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);

    }

    private void updateTimer() {
        if (!isBreak) {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            endX = 75 - 65 * Math.cos(Math.toRadians(angle));
            endY = 75 - 65 * Math.sin(Math.toRadians(angle));

            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(75, 75, endX, endY);
            angle += 0.24;
        }
        else {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            endX = 75 - 65 * Math.cos(Math.toRadians(angle));
            endY = 10 - 65 * Math.sin(Math.toRadians(angle));

            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeLine(75, 75, endX, endY);
            angle += (double) 360 / (5 * 60);
        }
        if (seconds > 0) {
            seconds--;
        } else if (minutes > 0) {
            seconds = 59;
            minutes--;
        } else {
            minutes = 0;
            seconds = 0;
            changeMode();
        }

        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);
    }

}
