package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private Player player;
    private Obstacle obstacle;

    public Game(Context context) {
        super(context);
        getHolder().addCallback(this);

        gameLoop = new GameLoop(this, getHolder());

        player = new Player(getContext(), 500, 500, 30);
        obstacle = new Obstacle(getContext(), 600, 600, 30);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent (android.view.MotionEvent event) {
        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN:
                player.slowDown();
                return true;
            case android.view.MotionEvent.ACTION_UP:
                player.speedUp();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // Start the game loop
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Respond to surface changes
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Stop the game loop
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawUPS(canvas);
        player.draw(canvas);
        obstacle.draw(canvas);
    }

    public void update() {
        player.update();
        obstacle.update();
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = "UPS: " + gameLoop.getAverageUPS();
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
        paint.setColor(color);
        paint.setTextSize(70);
        canvas.drawText(averageUPS, 20, 80, paint);
    }

}
