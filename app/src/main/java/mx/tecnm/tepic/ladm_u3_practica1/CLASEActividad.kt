package mx.tecnm.tepic.ladm_u3_practica1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import java.util.ArrayList

class CLASEActividad (d:String, fc:String, fe: String) {
    var id = 0
    var desc = d
    var fcap = fc
    var fent = fe

    val nombre = "Tareas"
    var puntero : Context?= null

    fun asignarPuntero(p: Context) {
        puntero = p
    }

    fun insertar():Boolean {
        try {
            var base = BD(puntero!!, nombre, null, 1)
            var insertar = base.writableDatabase
            var datos = ContentValues()

            datos.put("DESCRIPCION", desc)
            datos.put("FECHA_CAPTURA", fcap)
            datos.put("FECHA_ENTREGA", fent)

            var res = insertar.insert("ACTIVIDADES", "ID_ACTIVIDAD", datos)

            if (res.toInt() == -1) {
                return false
            }
        }catch (e: SQLiteException) {
            return false
        }
        return true
    }


    fun recuperarDatos(): ArrayList<CLASEActividad> {
        var data = ArrayList<CLASEActividad>()
        try{
            var bd = BD(puntero!!,nombre,null,1 )
            var select = bd.readableDatabase
            var columnas = arrayOf("*")

            var cursor  = select.query("ACTIVIDADES", columnas, null, null, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var temp = CLASEActividad(cursor.getString(1),cursor.getString(2),cursor.getString(3))

                    temp.id = cursor.getInt(0)
                    data.add(temp)
                }while (cursor.moveToNext())
            }else{
            }
        }catch (e: SQLiteException){
        }
        return data
    }

    fun consultaID(id:String): CLASEActividad{
        var registro = CLASEActividad("","","")

        try {
            var bd = BD(puntero!!, nombre, null, 1)
            var select = bd.readableDatabase
            var busca = arrayOf("*")
            var buscaID = arrayOf(id)

            var res = select.query("ACTIVIDADES", busca, "ID_ACTIVIDAD = ?",buscaID, null, null, null)
            if(res.moveToFirst()){
                registro.id = id.toInt()
                registro.desc = res.getString(1)
                registro.fcap = res.getString(2)
                registro.fent = res.getString(3)
            }
        }catch (e: SQLiteException){
            e.message.toString()
        }
        return registro
    }

    fun ConsultaPFecha(f:String): ArrayList<CLASEActividad> {
        var data = ArrayList<CLASEActividad>()
        try{
            var bd = BD(puntero!!,nombre,null,1 )
            var select = bd.readableDatabase
            var columnas = arrayOf("*")
            var fecha = arrayOf(f)

            var cursor  = select.query("ACTIVIDADES", columnas, "FECHA_CAPTURA = ?", fecha, null, null, null)
            if(cursor.moveToFirst()){
                do{
                    var temp = CLASEActividad(cursor.getString(1),cursor.getString(2),cursor.getString(3))

                    temp.id = cursor.getInt(0)
                    data.add(temp)
                }while (cursor.moveToNext())
            }else{
            }
        }catch (e: SQLiteException){
        }
        return data
    }
    fun actualizar():Boolean{
        try{
            var base = BD(puntero!!, nombre,null,1)
            var actualizar = base.writableDatabase
            var datos = ContentValues()
            var actualizarID = arrayOf(id.toString())

            datos.put("DESCRIPCION", desc)
            datos.put("FECHA_CAPTURA", fcap)
            datos.put("FECHA_ENTREGA", fent)

            var res = actualizar.update("ACTIVIDADES",datos,"ID_ACTIVIDAD = ?", actualizarID)
            if(res == 0){
                return false
            }
        }catch (e: SQLiteException){
            return false
        }
        return true
    }

    fun eliminar(id:String):Boolean{
        try{
            var base = BD(puntero!!, nombre,null,1)
            var eliminar = base.writableDatabase
            var eliminarID = arrayOf(id)

            var res = eliminar.delete("ACTIVIDADES","ID_ACTIVIDAD = ?",eliminarID)
            if(res.toInt() == 0){
                return false
            }
        }catch (e: SQLiteException){
            return false
        }
        return true
    }


}