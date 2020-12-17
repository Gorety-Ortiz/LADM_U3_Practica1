package mx.tecnm.tepic.ladm_u3_practica1

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BD(
    context: Context?,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE ACTIVIDADES(ID_ACTIVIDAD INTEGER PRIMARY KEY AUTOINCREMENT,DESCRIPCION VARCHAR(1000),FECHA_CAPTURA DATE,"+
                "FECHA_ENTREGA DATE);")
        db?.execSQL("CREATE TABLE EVIDENCIAS(ID_EVIDENCIA INTEGER PRIMARY KEY AUTOINCREMENT,ID_ACTIVIDAD INTEGER,FOTO BLOB," +
                " FOREIGN KEY(ID_ACTIVIDAD) REFERENCES ACTIVIDADES(ID_ACTIVIDAD));")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}