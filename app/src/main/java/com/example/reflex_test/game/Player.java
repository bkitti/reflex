package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.reflex_test.R;

public class Player {
    private static final int DEFAULT_SPEED = 20;
    private double positionX;
    private double positionY;
    private double radius;
    private int speed;
    private Paint paint;

    public Player(Context context, double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.speed = DEFAULT_SPEED;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }
    public void draw(Canvas canvas) {
        // Draw the player
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

    public void update() {
    }

    public void slowDown() {
        speed = DEFAULT_SPEED/2;
    }
    public void speedUp() {
        speed = DEFAULT_SPEED;
    }
}
