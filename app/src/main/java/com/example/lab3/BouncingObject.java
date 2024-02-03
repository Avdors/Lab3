package com.example.lab3;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;

public class BouncingObject {
    public boolean visible;
    private final Paint paint; // Для рисования объекта
    private long lastHitTime;
    //private Bitmap splashImage;
    private static final long SPLASH_TIME = 200; // 1 seconds
    float x, y, xSpeed;
    int  ySpeed, overallSpeed; // Speed in x and y direction
    String letter; // Буква
    private float startY; // Начальная позиция по Y
    private float bounceHeight; // Текущая высота отскока
    private final float MIN_BOUNCE_HEIGHT = 10; // Минимальная высота отскока
    int screenWidthS, screenHeightS;
    public BouncingObject(String letter, float startX, float startY, float xSpeed, int ySpeed, int overallSpeed) {
        this.letter = letter;
        this.x = startX;
        this.y = startY;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.visible = true;
        this.paint = new Paint();
        this.paint.setTextSize(60);
        this.paint.setColor(Color.GREEN);
        this.startY = startY; // Сохраняем начальную позицию
        this.bounceHeight = startY;
    }
    public void updatePosition(int screenWidth, int screenHeight) {
        screenWidthS = screenWidth;
        screenHeightS = screenHeight;
       // long currentTime = System.currentTimeMillis();
        if (!visible) {
            return; //остановим поток если объект не виден
        }
        if (visible) {
                if (x + 50 >= screenWidth && xSpeed > 0) {
                    xSpeed = 0; // если объет дошел до края экрана, то он не смещается по горизонтали
                    ySpeed = ySpeed*3; //ускоряется скорость его отталкивания
            }
                if (y + 1 >= screenHeight && ySpeed > 0) {
                     ySpeed = -ySpeed; //разворачиваем объект если он достиг нижней точки экрана
                     bounceHeight *= 1.07; // рассчитываем новую максимальную точку, до которой будет отскок
            }
            if (y <= bounceHeight && ySpeed < 0) {
                ySpeed = ySpeed * -1; // разворачиваем объект, если он поднался до высоты прошлой точки по y плюс 7%
            }
            if(screenHeight - bounceHeight <= 20){
                visible = false;
            }
            x = x + xSpeed;
            y = y + ySpeed;
        }
    }
    public synchronized void hit() {
        if (visible) {
            visible = false;
            lastHitTime = System.currentTimeMillis();
        }
    }
    public void draw(Canvas canvas) {
        if (visible) {
            canvas.drawText(letter, x, y, paint);
        }
    }
    public boolean isVisible() {
        if (visible) {
            return true;
        }  else return false;
    }
}