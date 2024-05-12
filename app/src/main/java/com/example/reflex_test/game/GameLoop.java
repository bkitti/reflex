package com.example.reflex_test.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    // UPS = update per second
    public static final double MAX_UPS = 60.0;
    private static final double UPS_PERIOD = 1000.0/MAX_UPS;
    private boolean isRunning = false;
    private final Game game;
    private final SurfaceHolder holder;
    private double averageUPS;
    private int totalUpdateCount = 0;

    GameLoop(Game game, SurfaceHolder holder) {
        this.game = game;
        this.holder = holder;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    public void stopLoop() {
        isRunning = false;
    }

    @Override
    public void run() {
        super.run();
        int updateCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        Canvas canvas = null;
        startTime = System.currentTimeMillis();
        while (isRunning) {
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    game.update(totalUpdateCount);
                    updateCount++;
                    totalUpdateCount++;
                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            // if sleeptime > 0, must wait
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // if sleeptime < 0, update needed
            if (sleepTime < 0 && updateCount < MAX_UPS - 1) { // guarantee the max ups
                game.update(totalUpdateCount);
                updateCount++;
                totalUpdateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            }

            elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 1000) {
                averageUPS = updateCount / (elapsedTime/1000.0);
                updateCount = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }



    public double getAverageUPS() {
        return averageUPS;
    }

}
