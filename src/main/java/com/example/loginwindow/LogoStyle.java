package com.example.loginwindow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LogoStyle {
    /**
     * tworzy animacje z logo firmy !
     * @param Logo logo
     */
    LogoStyle(ImageView Logo){
        Reflection reflection = new Reflection();
        ColorAdjust colorAdjust = new ColorAdjust(0,0,0,0);
        colorAdjust.setBrightness(0.5);

        reflection.setInput(colorAdjust);
        Logo.setEffect(reflection);

        AtomicBoolean effects = new AtomicBoolean(true);
        AtomicReference<Double> brightness = new AtomicReference<>(0.5);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, evt -> {
                }),
                new KeyFrame(Duration.millis(80), evt -> {
                    if(effects.get()){
                        if (colorAdjust.getBrightness() < 0){
                            effects.set(false);
                        }
                        brightness.updateAndGet(v -> v - 0.01);
                        colorAdjust.setBrightness(brightness.get());
                    }else {
                        if (colorAdjust.getBrightness() > 0.5){
                            effects.set(true);
                        }
                        brightness.updateAndGet(v -> (v + 0.01));
                        colorAdjust.setBrightness(brightness.get());
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
