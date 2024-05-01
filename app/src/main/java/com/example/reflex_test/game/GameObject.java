package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public abstract class GameObject {
    protected double MAX_X;
    protected double MAX_Y;
    protected double positionX;
    protected double positionY;
    protected int speed;
    protected Paint paint;


    public GameObject(Context context, double positionX, double positionY, int speed, int color) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.speed = speed;
        MAX_X = context.getResources().getDisplayMetrics().widthPixels;
        MAX_Y = context.getResources().getDisplayMetrics().heightPixels;
        paint = new Paint();;
        paint.setColor(ContextCompat.getColor(context, color));
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

}
