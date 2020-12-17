package mx.tecnm.tepic.ladm_u3_practica1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.activity_main3.lista
import java.lang.Exception

class MainActivity3 : AppCompatActivity() {
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        button4.setOnClickListener {
            Buscar()
        }

        lista.setOnItemClickListener{ adapterView, view, i, l ->
            mostrarAlertEliminarActualizar(i)
        }

        button5.setOnClickListener {
            finish()
        }
    }

    private fun mostrarAlertEliminarActualizar(posicion: Int) {
        var idLista = listaID.get(posicion)

        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("¿Que DESEA HACER CON LA ACTIVIDAD?")
            .setPositiveButton("ELIMINAR") {d,i-> eliminar(idLista)}
            .setNeutralButton("VER DETALLES") {d,i-> llamarVentanaDetalles(idLista)}
            .setNegativeButton("CANCELAR") {d,i->}
            .show()
    }

    private fun llamarVentanaDetalles(idLista: String) {
        var ventana = Intent(this,MainActivity4::class.java)
        var c = CLASEActividad("","","")
        c.asignarPuntero(this)

        ventana.putExtra("id",idLista)

        startActivity(ventana)
    }
    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(s)
            .setPositiveButton("OK"){
                    d,i-> d.dismiss()
            }
            .show()
    }
    private fun eliminar(id:String) {
        var c = CLASEActividad("","","")
        c.asignarPuntero(this)

        if(c.eliminar(id)) {
            mensaje("SE HA ELIMINADO LA ACTIVIDAD Y EVIDENCIA")

            var d = Evidencia("",ByteArray(0))
            d.asignarPuntero(this)
            d.eliminarPorAct(id)

            fcapA2.setText("")
            var v = Array<String>(0,{""})
            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

        } else {
            mensaje("ERROR EN LA ELIMINACION")
        }
    }

    private fun Buscar(){
        try {
            var c = CLASEActividad("","","")
            c.asignarPuntero(this)
            var datos = c.ConsultaPFecha(fcapA2.text.toString())

            var tamaño = datos.size-1
            var v = Array<String>(datos.size,{""})

            listaID = ArrayList<String>()
            (0..tamaño).forEach {
                var actividad = datos[it]
                var item = actividad.desc+"\n"+actividad.fcap+"\n"+actividad.fent
                v[it] = item
                listaID.add(actividad.id.toString())
            }

            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

            lista.setOnItemClickListener{ adapterView, view, i, l ->
                mostrarAlertEliminarActualizar(i)
            }
        }catch (e: Exception){
            mensaje(e.message.toString())
        }
    }
}