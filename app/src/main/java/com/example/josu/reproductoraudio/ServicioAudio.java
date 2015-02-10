package com.example.josu.reproductoraudio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;

public class ServicioAudio extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener{


    private final String BOTON_PLAY = "botonPlay";
    private MediaPlayer mp;
    private enum Estados{
        idle,
        initialized,
        prepairing,
        prepared,
        started,
        paused,
        completed,
        sttoped,
        end,
        error
    };
    private Estados estado;
    public static final String PLAY="play";
    public static final String STOP="stop";
    public static final String ADD="add";
    public static final String PAUSE="pause";
    private String rutaCancion=null;
    private boolean reproducir;

    @Override
    public void onCreate() {
        super.onCreate();
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int r = am.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(r==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            mp = new MediaPlayer();
            mp.setOnPreparedListener(this);
            mp.setOnCompletionListener(this);
            mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
            estado = Estados.idle;
        } else {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        mp.release();
        mp = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        String dato = intent.getStringExtra("cancion");
        if(action.equals(PLAY)){
            play();
        }else if(action.equals(ADD)){
            add(dato);
        }else if(action.equals(STOP)){
            stop();
        }else if(action.equals(PAUSE)) {
            pause();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrepared(final MediaPlayer mp) {
        estado = Estados.prepared;
        if(reproducir){
            mp.start();
            estado = Estados.started;
            Intent intent = new Intent(BOTON_PLAY);
            intent.putExtra("resultado", "play");
            sendBroadcast(intent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        estado = Estados.completed;
        Intent intent = new Intent(BOTON_PLAY);
        intent.putExtra("resultado", "stop");
        sendBroadcast(intent);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mp.setVolume(1f, 1f);
                play();
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                mp.setVolume(0.1f, 0.1f);
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                mp.setVolume(0.1f, 0.1f);
                pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                mp.setVolume(0.1f, 0.1f);
                break;
        }
    }


    private void play(){
        if(rutaCancion != null){
            if(estado == Estados.error){
                estado = Estados.idle;
            }
            if(estado == Estados.idle){
                reproducir = true;
                try {
                    mp.setDataSource(rutaCancion);
                    estado = Estados.initialized;
                } catch (IOException e) {
                    estado= Estados.error;
                }
            }
            if(estado == Estados.initialized ||
                    estado == Estados.sttoped){
                reproducir = true;
                mp.prepareAsync();
                estado = Estados.prepairing;
            } else if(estado == Estados.prepairing) {
                reproducir = true;
            }
            if(estado == Estados.prepared ||
                    estado == Estados.paused ||
                    estado == Estados.completed ||
                    estado == Estados.started) {
                mp.start();
                Intent intent = new Intent(BOTON_PLAY);
                intent.putExtra("resultado", "play");
                sendBroadcast(intent);
                estado = Estados.started;
            }
        }
    }

    private void stop(){
        if(estado == Estados.prepared ||
                estado == Estados.started ||
                estado == Estados.paused ||
                estado == Estados.completed){
            mp.seekTo(0);
            mp.stop();
            estado = Estados.sttoped;
        }
        reproducir = false;
    }

    private void pause() {
        if(estado == Estados.paused) {
            mp.pause();
            estado = Estados.paused;
        }
    }

    private void add(String cancion){
        mp.reset();
        estado = Estados.idle;
        this.rutaCancion = cancion;
        Log.v("ADD", cancion);
    }

    public void tostada(String mensaje){
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }
}