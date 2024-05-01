package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;

import com.example.reflex_test.R;

public class Player extends GameObject {
    private static final int DEFAULT_SPEED = 20;
    private double radius;
    private boolean upDirection = false;

    public Player(Context context, double positionX, double positionY, double radius) {
        super(context, positionX, positionY, DEFAULT_SPEED, R.color.player);
        this.radius = radius;
        this.speed = DEFAULT_SPEED;
    }
    public void draw(Canvas canvas) {
        // Draw the player
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

    public void update() {
        if (positionY <= this.radius) {
            upDirection = false;
        } else if (positionY >= MAX_Y - this.radius) {
            upDirection = true;
        }
        if (upDirection) {
            positionY -= speed;
        } else {
            positionY += speed;
        }
    }

    public void slowDown() {
        speed = DEFAULT_SPEED/2;
    }
    public void speedUp() {
        speed = DEFAULT_SPEED;
    }

    public double getRadius() {
        return radius;
    }
}
