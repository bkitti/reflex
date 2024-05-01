package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;

import com.example.reflex_test.R;

public class Obstacle extends GameObject {
    private static final int DEFAULT_SPEED = 20;
    private double radius;

    public Obstacle(Context context, double positionX, double positionY, double radius) {
        super(context, positionX, positionY, DEFAULT_SPEED, R.color.player);
        this.radius = radius;
    }
    public void draw(Canvas canvas) {
        // Draw the player
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

    public void update() {
    }

}
