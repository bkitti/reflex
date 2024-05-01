package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;

import com.example.reflex_test.R;

public class Obstacle extends GameObject {
    private float size;

    public Obstacle(Context context, double positionX, double positionY, int speed, float size) {
        super(context, positionX, positionY, speed, R.color.obstacle1);
        this.size = size;
    }
    public void draw(Canvas canvas) {
        // Draw the player
        canvas.drawRect((float) positionX, (float) positionY, (float) positionX + size*2, (float) positionY + size, paint);
    }

    public void update() {
        if (positionX <= 0-size*2) return;
        positionX -= speed;
    }

}
