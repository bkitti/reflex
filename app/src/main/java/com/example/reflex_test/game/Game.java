package com.example.reflex_test.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private Player player;
    private List<Obstacle> obstacles;

    public Game(Context context) {
        super(context);

        double MAX_X = context.getResources().getDisplayMetrics().widthPixels;
        double MAX_Y = context.getResources().getDisplayMetrics().heightPixels;

        getHolder().addCallback(this);

        gameLoop = new GameLoop(this, getHolder());

        double playerSize = MAX_X/40;
        player = new Player(getContext(), MAX_X/10, playerSize, playerSize);

        double obstacleSize = MAX_X/20;

        obstacles = new ArrayList<>();
        for (int i=0; i<10; i++) {
            obstacles.add(new Obstacle(getContext(), MAX_X + i*MAX_X/2, MAX_Y/2-obstacleSize/2, 10, (float) obstacleSize));
        }

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
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(canvas);
        }
    }

    public void update() {
        player.update();
        for (Obstacle obstacle : obstacles) {
            obstacle.update();
        }

        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            if (GameObject.isColliding(obstacle, player)) {
                iterator.remove();
            }
        }
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
