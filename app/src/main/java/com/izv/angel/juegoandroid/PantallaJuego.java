package com.izv.angel.juegoandroid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PantallaJuego extends SurfaceView implements SurfaceHolder.Callback {
    //en esta clase se establecen las normas y las condiciones que pasan en la pantalla, así como el escenario en sí
    private Bitmap bmpt;
    private Bitmap bmp;
    private Bitmap bmpc;
    private int alto, ancho;
    private TiempoJuego tiempoJuego;
    private List<DuendesDam> duendesDams = new ArrayList<DuendesDam>();
    private List<TiempoDuendes> temps = new ArrayList<TiempoDuendes>();
    private List<JefeDam> bossex = new ArrayList<JefeDam>();
    private List<ComodinDam> comodin = new ArrayList<ComodinDam>();
    private long lastClick = 0;
    private Bitmap bmpBlood;
    private Bitmap floor;
    private double tiempoI, tiempoT;
    boolean terminado = false;
    public static int poblacion = 4;
    public static int poblacionBoss = 6;
    private SoundPool sound;

    private int smash;
    private int urceraduum;
    private int omarduum;


    public PantallaJuego(Context context) {
        //constructor
        super(context);
        getHolder().addCallback(this);
        tiempoJuego = new TiempoJuego(this);
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.mipmap.blood1);
        floor = BitmapFactory.decodeResource(getResources(), R.mipmap.floor);
        tiempoI = (double) Calendar.getInstance().getTimeInMillis();
        sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        smash = sound.load(getContext(), R.raw.smash, 1);
        urceraduum = sound.load(getContext(), R.raw.duum, 3);
        omarduum=sound.load(getContext(),R.raw.duumeleminar,1);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //este metodo llama a los metodos creadores de duendes siempre que su numero sea menor que su 'poblacion', una variable establecida

        for (int i = 0; i < poblacion; i++) {
            crearDuendesDam();

        }

        for (int i = 0; i < poblacionBoss; i++) {
            crearJefeDam();

        }

        for (int i = 0; i < poblacionBoss; i++) {
            crearComodinDam();

        }

     //Establecemos el fondo del escenario, y que se muestre mientras se esta jugando
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.mipmap.floor);
        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        floor = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);
        tiempoJuego.setFuncionando(true);
        tiempoJuego.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        alto = height;
        ancho = width;
        DuendesDam.setDimension(ancho, alto);
        JefeDam.setDimension(ancho, alto);
        ComodinDam.setDimension(ancho, alto);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reintentar = true;
        tiempoJuego.setFuncionando(false);
        while (reintentar) {
            try {
                tiempoJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(floor, 0, 0, null);
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (DuendesDam duendesDam : duendesDams) {
            duendesDam.onDraw(canvas);

        }
        for (JefeDam jefeDam : bossex) {

            jefeDam.onDraw(canvas);
        }
        for (ComodinDam comodinDam : comodin) {

            comodinDam.onDraw(canvas);
        }


    }

    private void crearDuendesDam() {
        //llamamos al metodo crear duende y lo metemos en uno para almacenarlo varias veces y crear varios a la vez
        //lo mismo sucede con crear Jefe y con crear Comodin
        duendesDams.add(crearDuendeDam(R.mipmap.pj1));
        duendesDams.add(crearDuendeDam(R.mipmap.pj2));
        duendesDams.add(crearDuendeDam(R.mipmap.pj3));
        duendesDams.add(crearDuendeDam(R.mipmap.pj4));
        duendesDams.add(crearDuendeDam(R.mipmap.pj5));
        duendesDams.add(crearDuendeDam(R.mipmap.pj6));
        duendesDams.add(crearDuendeDam(R.mipmap.pj7));
        duendesDams.add(crearDuendeDam(R.mipmap.pj8));


    }

    private void crearJefeDam() {

        bossex.add(crearJefeDam(R.mipmap.pj9));

    }

    private void crearComodinDam() {
        comodin.add(crearComodinDam(R.mipmap.pj10));

    }


    private DuendesDam crearDuendeDam(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new DuendesDam(this, bmp);
    }

    private JefeDam crearJefeDam(int resouceBoss) {
        Bitmap bmpt = BitmapFactory.decodeResource(getResources(), resouceBoss);
        return new JefeDam(this, bmpt);
    }
    private ComodinDam crearComodinDam(int resouceComodin) {
        Bitmap bmpc = BitmapFactory.decodeResource(getResources(), resouceComodin);
        return new ComodinDam(this, bmpc);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //aqui tratamos lo que les pasa cuando este son presionados con el dedo
        if (System.currentTimeMillis() - lastClick > 1) {
            //if que condiciona la separacion entre click y click

            lastClick = System.currentTimeMillis();
            //recojemos las variables de posicion

            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = duendesDams.size() - 1; i >= 0; i--) {
                    //recorremos un for por el array de duendes que se creó previamente
                    DuendesDam duendesDam = duendesDams.get(i);
                    if (duendesDam.isCollition(x, y)) {
//if que nos indica que si la posicion donde se encuentre el duende es presionada, borrará el duende, sonara un sonido, y saldra otro bitmap con sangre

                        //lo mismo sucede con jefeDAm y comodín Dam, solo cambiando los valores de las variables
                        duendesDams.remove(duendesDam);

                        sound.play(smash, 0.8f, 0.8f, 0, 0, 1.5f);
                        temps.add(new TiempoDuendes(temps, this, x, y, bmpBlood));
                        break;
                    }
                }
                for (int i = bossex.size() - 1; i >= 0; i--) {
                    JefeDam jefeDam = bossex.get(i);
                    if (jefeDam.isCollition(x, y)) {


                        bossex.remove(jefeDam);

                        crearDuendesDam();
                        sound.play(urceraduum, 0.8f, 0.8f, 0, 0, 1.5f);
                        temps.add(new TiempoDuendes(temps, this, x, y, bmpBlood));
                        break;


                    }


                }
                for (int i = comodin.size() - 1; i >= 0; i--) {


                    ComodinDam comodinDam = comodin.get(i);
                    if (comodinDam.isCollition(x, y)) {
                        for (int iso = bossex.size() - 1; iso >= 0; iso--) {

                            JefeDam jefeDam = bossex.get(iso);
                           /** bossex.remove(jefeDam);
**/
                        }

                        comodin.remove(comodinDam);





                        sound.play(omarduum, 0.8f, 0.8f, 0, 0, 1.5f);
                        temps.add(new TiempoDuendes(temps, this, x, y, bmpBlood));




                        break;


                    }


                }
            }


            if (duendesDams.size() == 0 && terminado == false) {
                //metodo que nos dice que si el array de duendes llega a 0, el juego termina, y nos recoge el tiempo empleado para mostrarnoslo en pantalla
                terminado = true;
                tiempoT = (double) ((Calendar.getInstance().getTimeInMillis() - tiempoI) / 1000.00);
                Log.v("tiempo", tiempoT + "");
                Intent i = new Intent(this.getContext(), Principal.class);
                i.putExtra("tiempo", tiempoT);
                ((Activity) getContext()).setResult(Activity.RESULT_OK, i);
                ((Activity) getContext()).finish();
            }







              }
        return true;  }
}
