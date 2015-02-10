package com.example.josu.reproductoraudio;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by Josué on 23/01/2015.
 */
public class GestorAudio {

    Context contexto;


    public GestorAudio(Context c){
        contexto = c;
    }


    public static Audio getRow(Cursor c) {
        Audio objeto = new Audio();
        objeto.setId(c.getString(0));
        objeto.setNombre(c.getString(1));
        objeto.setRuta(c.getString(2));
        objeto.setMime(c.getString(3));
        objeto.setFecha(c.getString(4));
        objeto.setTamano(c.getString(5));
        objeto.setDuracion(c.getString(6));
        return objeto;
    }

    public Cursor getCursor() {
        String[] columnas = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.DATE_MODIFIED, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION};
        Cursor cursor = contexto.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columnas, null, null, MediaStore.Audio.Media.DATE_MODIFIED + " desc");
        return cursor;
    }

    public Cursor getCursor(String orden) {
        String[] columnas = {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.DATE_MODIFIED, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION};
        Cursor cursor = contexto.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columnas, null, null, orden);
        return cursor;
    }
}
