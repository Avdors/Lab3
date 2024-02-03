package com.example.lab3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Panel extends SurfaceView {
    private CopyOnWriteArrayList<BouncingObject> BouncingObject = new CopyOnWriteArrayList<>();
    //использую  ThreadPoolExecutor с фиксированным количеством потоков
    // так как мне нужно обрабатывать быстрые касания нет возможности создать очередь
    // в одном потоке, тогда я буду контролировать количество потоков, чтобы избежать перегруженности
    private ExecutorService touchEventExecutor;
    private Random random = new Random();
    private Bitmap bmp;
    private Handler gameUpdateHandler = new Handler();
    private SurfaceHolder holder;
    private GameManager gameLoopThread;
    /** Координата движения по Х=0*/
    private int x = 0;
    private int y = 0;
    /**Скорость изображения = 1*/
    private int xSpeed = 10;
    private int ySpeed = 10;
    private int overallSpeed = 7;
    private SoundPool sounds;
    private int soundId;

    private int score;
    private long lastHitTime;
    private static final long SIMULTANEOUS_HIT_THRESHOLD = 100; // милисекунды

    private Bitmap animationBitmap;
    private boolean showAnimation = false;
    private long animationStartTime;
    private final char[] letters = "VOLKOV".toCharArray();
    private int currentLetterIndex = 0;
    public Panel(Context context)
    {
        super(context);
        touchEventExecutor = Executors.newFixedThreadPool(6);
        gameLoopThread = new GameManager(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback()
        {
            /** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                touchEventExecutor.shutdownNow();
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder)
            {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                startGameUpdateLoop();
            }
            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
            }
        });
    }
    private void startGameUpdateLoop() {
        gameUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateGame();
                invalidate(); // Запрос на перерисовку
                gameUpdateHandler.postDelayed(this, 10); // Повторяем через 10 мс
            }
        }, 16);
    }
    private void updateGame() {
        for (BouncingObject object : BouncingObject) {
            object.updatePosition(getWidth(), getHeight());
        }
        // Удаляю объекты которые больше не видны
        BouncingObject.removeIf(object -> !object.isVisible());
    }
    private void createLetter(float touchX, float touchY) {
        sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            float xSpeed = 0.5f;
            int ySpeed = overallSpeed;
        String letter = String.valueOf(letters[currentLetterIndex]);
        BouncingObject.add(new BouncingObject(letter, touchX, touchY, xSpeed, ySpeed, overallSpeed));
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        for (BouncingObject object : BouncingObject) {
            object.draw(canvas);
                }
        }
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();
            createLetter(touchX, touchY);
            //меняем букву при нажатии
            if(currentLetterIndex == letters.length-1){
                currentLetterIndex = 0;
            }else currentLetterIndex++;
            return true;
        }
        return false;
    }
    public void stopGame() {
        if (gameLoopThread != null) {
            gameLoopThread.setRunning(false);
        }
        touchEventExecutor.shutdownNow(); // остановка других потоков
    }
}