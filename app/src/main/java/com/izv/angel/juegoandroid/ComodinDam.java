package com.izv.angel.juegoandroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by catoira on 31/03/16.
 */   //En esta creamos un duende comodin , el cual , al pulsarlo eliminara a un JefeDam
public class ComodinDam {

    //Direcciones establecidas hacia donde se va a dirigir
    int[] DIRECTION_TO_ANIMATION_MAP = { 3, 1, 0, 2 };
    private int x = 0;
    private int y = 0;
    private int xSpeed = 5;
    private int ySpeed;
    private PantallaJuego pantallaJuego;

    private Bitmap bmpt;
    private static int anchoMax=10, altoMax=10;
    private static final int BMPT_ROWS = 5;
    private static final int BMPT_COLUMNS = 1;
    private static final int MAX_SPEED = 10;
    private int width;
    private int height;
    private int frameActual=0;



//Constructor
    public ComodinDam(PantallaJuego pantallaJuego, Bitmap bmpt) {
        this.pantallaJuego = pantallaJuego;
        this.bmpt=bmpt;

        this.width = bmpt.getWidth() ;
        this.height = bmpt.getHeight() ;

        Random rnd = new Random();
        x = rnd.nextInt(pantallaJuego.getWidth() - width);
        y = rnd.nextInt(pantallaJuego.getHeight() - height);
        xSpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
        ySpeed = rnd.nextInt(MAX_SPEED * 2) - MAX_SPEED;
    }


    private void update() {
        //metodo de refresco de la velocidad dependiendo de hacia donde se dirija
        if (x >= pantallaJuego.getWidth() - width - xSpeed || x + xSpeed <= 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y >= pantallaJuego.getHeight() - height - ySpeed || y + ySpeed <= 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        frameActual = ++frameActual % BMPT_COLUMNS;
    }

    public void onDraw(Canvas canvas) {
        //invocamos el metodo update que nos registra la velocidad y a continuacion iniciamos el metodo ondraw que nos lo dibuja
        update();
        int srcX = frameActual * width;
        int srcY = getAnimationRow() * height;
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmpt, src, dst, null);
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int getAnimationRow() {
        //aqui establecemos las posiciones "aleatorias de a donde se dirige"

        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = (int) Math.round(dirDouble) % BMPT_ROWS;
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public static void setDimension(int ancho, int alto){
        //dimensiones del duende
        anchoMax=ancho;
        altoMax=alto;
    }

    public boolean isCollition(float x2, float y2) {
        //limites de pantalla
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }








}
