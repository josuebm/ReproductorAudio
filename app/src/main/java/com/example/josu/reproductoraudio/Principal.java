package com.example.josu.reproductoraudio;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Principal extends Activity{

    private final String BOTON_PLAY = "botonPlay";
    private TextView tv;
    private ListView lv;
    private Cursor cursor;
    private GestorAudio gv;
    private Adaptador ad;
    private  ImageButton btPlay;
    private BroadcastReceiver receptor;
    private LinearLayout botonera;
    private int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btPlay = (ImageButton)findViewById(R.id.btPlay);
        botonera = (LinearLayout)findViewById(R.id.botonera);
        lv = (ListView) findViewById(R.id.lista);
        gv = new GestorAudio(this);
        cargarLista(null);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (cursor.moveToPosition(position)) {
                    pos = position;
                    anadir();
                    botonera.setVisibility(View.VISIBLE);
                    play();
                }
            }
        });
        receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    if(bundle.getString("resultado").equals("stop")){
                        btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
                        btPlay.setTag("play");
                        next();
                    }else if(bundle.getString("resultado").equals("play")){
                        btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
                        btPlay.setTag("stop");
                    }
                }}};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receptor, new IntentFilter(BOTON_PLAY));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receptor);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_grabar) {
            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play(){
        Intent intent = new Intent(this, ServicioAudio.class);
        intent.setAction(ServicioAudio.PLAY);
        startService(intent);
        btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_stop));
        btPlay.setTag("stop");
    }

    public void stop(){
        Intent intent = new Intent(this, ServicioAudio.class);
        intent.setAction(ServicioAudio.STOP);
        startService(intent);
        btPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
        btPlay.setTag("play");
    }

    public void cargarLista(String orden){
        if(orden == null)
            cursor = gv.getCursor();
        else
            cursor = gv.getCursor(orden);
        ad = new Adaptador(this, cursor);
        lv.setAdapter(ad);
    }

    public void tostada(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    public void anadir(){
        Audio a = GestorAudio.getRow(cursor);
        Intent intent = new Intent(Principal.this, ServicioAudio.class);
        intent.putExtra("cancion", a.getRuta());
        intent.setAction(ServicioAudio.ADD);
        startService(intent);
    }

    public void reproducir(View v){
        if(btPlay.getTag().equals("play")){
            play();
        }else if (btPlay.getTag().equals("stop")){
            stop();
        }
    }

    public void anterior(View v){
        cursor.moveToPosition(pos);
        if(!cursor.isFirst()){
            cursor.moveToPrevious();
            pos = cursor.getPosition();
            stop();
            anadir();
            play();
        }
    }

    public void siguiente(View v){
        next();
    }

    public void next(){
        cursor.moveToPosition(pos);
        if(!cursor.isLast()){
            cursor.moveToNext();
            pos = cursor.getPosition();
            stop();
            anadir();
            play();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ServicioAudio.class));
    }
}

