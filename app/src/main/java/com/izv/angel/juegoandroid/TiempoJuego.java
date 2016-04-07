package com.izv.angel.juegoandroid;


import android.graphics.Canvas;

public class TiempoJuego extends  Thread {

    private PantallaJuego vista;
    private boolean funcionando = false;
    private static final long FPS = 15;

    public TiempoJuego(PantallaJuego vj) {
        this.vista = vj;
    }
    public void setFuncionando(boolean f) {
        funcionando = f;
    }
//En esta clase simplemente recogemos el tiempo que nos dura la partida, es decir, desde que le damos a comenzar hasta que muere el ultimo duende
    @Override
    public void run() {
        long inicio;
        long ticksPS = 1100 / FPS;
        long tiempoEspera;
        while (funcionando) {
            Canvas canvas = null;
            inicio = System.currentTimeMillis();
            try {
                canvas = vista.getHolder().lockCanvas();
                synchronized (vista.getHolder()) {
                    vista.draw(canvas);
                }
            }catch(Exception e){
            } finally {
                if (canvas != null) {
                    vista.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            tiempoEspera = ticksPS - (System.currentTimeMillis() - inicio);
            try {
                if (tiempoEspera > 0)
                    sleep(tiempoEspera);
                else
                    sleep(10);
            } catch (InterruptedException e) {}
        }
    }
}



