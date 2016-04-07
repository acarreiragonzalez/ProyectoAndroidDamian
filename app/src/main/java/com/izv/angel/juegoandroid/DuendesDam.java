package com.izv.angel.juegoandroid;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;
//Tiene la misma funcion que la clase Comodin Dam, solo cambiando el valor de las variables
public class DuendesDam {

    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private int x = 0;
    private int y = 0;
    private int xSpeed = 5;
    private int ySpeed;
    private PantallaJuego pantallaJuego;
    private Bitmap bmp;
    private static int anchoMax=0, altoMax=0;
    private static final int BMP_ROWS = 4;
    private static final int BMP_COLUMNS = 3;
    private static final int MAX_SPEED = 10;
    private int width;
    private int height;
    private int frameActual=0;

    public DuendesDam(PantallaJuego pantallaJuego, Bitmap bmp) {
        this.pantallaJuego = pantallaJuego;
        this.bmp=bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;

        Random rnd = new Random();
        x = rnd.nextInt(pantallaJuego.getWidth() - width);
        y = rnd.nextInt(pantallaJuego.getHeight() - height);
        xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
    }

    private void update() {
        if (x >= pantallaJuego.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y >= pantallaJuego.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        frameActual = ++frameActual % BMP_COLUMNS;
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = frameActual * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMP_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public static void setDimension(int ancho, int alto){
        anchoMax=ancho;
        altoMax=alto;
    }

    public boolean isCollition(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
}
