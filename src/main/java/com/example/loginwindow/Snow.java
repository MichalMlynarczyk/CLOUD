package com.example.loginwindow;

import java.time.Duration;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Snow {

AnchorPane backGround;

Snow(AnchorPane backGround){
    this.backGround = backGround;
    run();
}

    /**
     * tworzy animacje płatków śniegu
     */
    public void run()  {
        final int size = 100; // 400
        final Circle[] rectangles = new Circle[size];
        final long[] delays = new long[size];
        final double[] angles = new double[size];
        final long duration = Duration.ofSeconds(20).toNanos(); // 3
        final Random random = new Random();

        for (int i = 0; i < size; i++) {
            rectangles[i] = new Circle(3, Color.WHITE);
            delays[i] = (long) (Math.random()*duration);
            angles[i] = 2 * Math.PI * random.nextDouble();
            backGround.getChildren().add(rectangles[i]);
            rectangles[i].toBack();
            rectangles[i].toBack();
            rectangles[i].setEffect(new BoxBlur());
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                final double width = 0.5 * backGround.getWidth();
                final double height = 0.5 * backGround.getHeight();
                final double radius = Math.sqrt(2) * Math.max(width, height);

                for (int i = 0; i < size; i++) {
                    Circle r = rectangles[i];
                    double angle = angles[i];
                    long t = (now - delays[i]) % duration;
                    double d = t*radius/duration;

                    r.setOpacity( (duration - t)/(double)duration );
                    r.setTranslateX(Math.cos(angle)*d + width);
                    r.setTranslateY(Math.sin(angle)*d + height);
                }
            }
        }.start();
    }
}