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
    double MAX_X;
    double MAX_Y;
    private boolean isGameOver = false;
    private OnGameEventListener listeners;
    private int highScore = 0;

    public Game(Context context) {
        super(context);

        MAX_X = context.getResources().getDisplayMetrics().widthPixels;
        MAX_Y = context.getResources().getDisplayMetrics().heightPixels;

        getHolder().addCallback(this);

        gameLoop = new GameLoop(this, getHolder());

        double playerSize = MAX_X/40;
        player = new Player(getContext(), MAX_X/10, playerSize, playerSize);

        double obstacleSize = MAX_X/20;

        obstacles = new ArrayList<>();
        double[] positions = new double[]{MAX_Y/6, 2*MAX_Y/6, 3*MAX_Y/6, 4*MAX_Y/6, 5*MAX_Y/6};
        for (int i=0; i<20; i++) {
            double randomPosition = positions[(int) (Math.random()*5)];
            obstacles.add(new Obstacle(getContext(), MAX_X + i*MAX_X/1.5, randomPosition-obstacleSize/2, 15, (float) obstacleSize));
        }

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent (android.view.MotionEvent event) {
        switch (event.getAction()) {
            case android.view.MotionEvent.ACTION_DOWN:
                if (isGameOver) {
                    exitGame();
                }
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
        // drawUPS(canvas);
        drawPoints(canvas);
        player.draw(canvas);
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(canvas);
        }
        if (isGameOver) {
            drawGameOver(canvas);
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
                player.addPoints(-obstacle.getSpeed());
                iterator.remove();
            } else if (obstacle.isFinished()) {
                player.addPoints(obstacle.getSpeed());
                iterator.remove();
            }
        }

        if (obstacles.isEmpty()) {
            gameOver();
        }
    }

    private void gameOver() {
        isGameOver = true;
    }

    public void drawUPS(Canvas canvas) {
        String averageUPS = "UPS: " + gameLoop.getAverageUPS();
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
        paint.setColor(color);
        paint.setTextSize(70);
        canvas.drawText(averageUPS, 20, 80, paint);
    }

    public void drawPoints (Canvas canvas) {
        String points = "Points: " + player.getPoints();
        if (highScore > 0) {
            points += " High Score: " + highScore;
        }
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
        paint.setColor(color);
        paint.setTextSize(70);
        canvas.drawText(points, (float) (MAX_X/3), 80, paint);
    }

    public void drawGameOver(Canvas canvas) {
        String gameOver = "Game Over";
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
        paint.setColor(color);
        paint.setTextSize(150);
        canvas.drawText(gameOver, (float) (MAX_X/4), (float) (MAX_Y/2), paint);
    }

    public void exitGame() {
        gameLoop.stopLoop();
        if (listeners != null) {
            listeners.onGameEnd(player.getPoints());
        }
        getHolder().getSurface().release();
    }

    public void addOnGameEventListener(Game.OnGameEventListener listeners) {
        this.listeners = listeners;
    }

    public void setActualHighScore(int actualHighScore) {
        this.highScore = actualHighScore;
    }

    public abstract static class OnGameEventListener {
        public abstract void onGameEnd(int score);
    }
}
