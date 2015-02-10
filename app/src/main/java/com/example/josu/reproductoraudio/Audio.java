package com.example.josu.reproductoraudio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josué on 23/01/2015.
 */
public class Audio implements Parcelable{

    String id, nombre, ruta, mime, fecha, tamano, duracion;

    public Audio() {
    }

    public Audio(String id, String nombre, String ruta, String mime, String fecha, String tamano, String duracion) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.mime = mime;
        this.fecha = fecha;
        this.tamano = tamano;
        this.duracion = duracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ruta='" + ruta + '\'' +
                ", mime='" + mime + '\'' +
                ", fecha='" + fecha + '\'' +
                ", tamano='" + tamano + '\'' +
                ", duracion='" + duracion + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.id);
        parcel.writeString(this.nombre);
        parcel.writeString(this.ruta);
        parcel.writeString(this.mime);
        parcel.writeString(this.fecha);
        parcel.writeString(this.tamano);
        parcel.writeString(this.duracion);
    }

    public Audio (Parcel p){
        this.id=p.readString();
        this.nombre=p.readString();
        this.ruta=p.readString();
        this.mime = p.readString();
        this.fecha= p.readString();
        this.tamano = p.readString();
        this.duracion = p.readString();
    }

    public static final Creator <Audio> CREATOR =
            new Creator <Audio>() {
                @Override
                public Audio createFromParcel(Parcel parcel) {
                    return new Audio(parcel);
                }
                @Override
                public Audio[] newArray(int i) {
                    return new Audio[i];
                }
            };
}
