package com.example.josu.reproductoraudio;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Josué on 23/01/2015.
 */
public class Adaptador extends CursorAdapter{

    Context contexto;

    public Adaptador(Context context, Cursor c) {
        super(context, c, true);
        contexto = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.lista_detalle, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv1, tv2, tv3;
        ImageView iv;
        tv1 = (TextView)view.findViewById(R.id.tvTitulo);
        tv2 = (TextView)view.findViewById(R.id.tvTamano);
        tv3 = (TextView)view.findViewById(R.id.tvDuracion);
        iv = (ImageView)view.findViewById(R.id.ivMiniatura);
        Audio a = GestorAudio.getRow(cursor);
        tv1.setText(a.getNombre());
        tv2.setText(tamano(a.getTamano()));
        tv3.setText(duracion(a.getDuracion()));
        iv.setImageDrawable(context.getResources().getDrawable(R.drawable.musical));
    }

    public String duracion(String ms){
        String duracion;
        int min=0, seg;
        seg = Integer.parseInt(ms) / 1000;
        while(seg >= 60){
            min++;
            seg -= 60;
        }
        if(String.valueOf(min).length() <= 1)
            duracion = "0" + min;
        else
            duracion = min+"";
        if(String.valueOf(seg).length() <= 1)
            duracion += ":" + "0" + seg;
        else
            duracion += ":" + seg;
        return duracion;
    }

    public String tamano(String b){
        long bytes = Long.valueOf(b);
        int unit = 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        if(exp == 1)
            return String.format("%d KB", (int)(bytes / Math.pow(unit, exp)));
        String pre = ("KMGTPE").charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
