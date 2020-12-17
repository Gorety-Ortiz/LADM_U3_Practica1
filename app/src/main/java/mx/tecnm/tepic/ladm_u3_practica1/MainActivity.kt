package mx.tecnm.tepic.ladm_u3_practica1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var listaID = ArrayList<String>()
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (descripcion.text.toString() == "") {
                Toast.makeText(this,"FALTA AGREGAR DESCRIPCION", Toast.LENGTH_LONG)
                        .show()
                return@setOnClickListener
            }
            else if (Fcaptura.text.toString() == "") {
                Toast.makeText(this,"FALTA AGREGAR FECHA DE CAPTURA", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            else if (Fentrega.text.toString() == "") {
                Toast.makeText(this,"PORFAVOR RELLENE TODOS LOS CAMPOS", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            else if (descripcion.text.toString() == "" && Fcaptura.text.toString() == "" && Fentrega.text.toString() == "") {
                Toast.makeText(this,"NO SE PUEDE AGREGAR ACTIVIDAD, RELLENE LOS CAMPOS", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            var act = CLASEActividad(
                    descripcion.text.toString(),
                    Fcaptura.text.toString(),
                    Fentrega.text.toString()
            )

            act.asignarPuntero(this)

            var res = act.insertar()

            if(res == true) {
                mensaje("SE INSERTÓ NUEVA ACTIVIDAD")
                descripcion.setText("")
                Fcaptura.setText("")
                Fcaptura.setText("")
                llenarLista()
            } else {
                mensaje("ERROR! VUELVA A INTENTAR")
            }
        }

        button1.setOnClickListener {
            var ventana = Intent(this,MainActivity3::class.java)
            startActivityForResult(ventana,0)
        }

        llenarLista()
    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(s)
                .setPositiveButton("OK"){
                    d,i-> d.dismiss()
                }
                .show()
    }

    private fun llenarLista(){
        try {
            var c = CLASEActividad("","","")
            c.asignarPuntero(this)
            var datos = c.recuperarDatos()

            var tamaño = datos.size-1
            var v = Array<String>(datos.size,{""})

            listaID = ArrayList<String>()
            (0..tamaño).forEach {
                var actividad = datos[it]
                var item = "DESCRIPCION: "+actividad.desc+"\n"+"FECHA DE CAPTURA: "+actividad.fcap+"\n"+"FECHA DE ENTREGA: "+actividad.fent
                v[it] = item
                listaID.add(actividad.id.toString())
            }

            lista.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,v)

            lista.setOnItemClickListener{ adapterView, view, i, l ->
                mostrarAlertEAI(i)
            }
        }catch (e: Exception){
            mensaje(e.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        llenarLista()
    }

    private fun ActualizarActividad(idLista: String) {
        var ventana = Intent(this,MainActivity2::class.java)
        var c = CLASEActividad("","","")
        c.asignarPuntero(this)

        var d = c.consultaID(idLista).desc
        var fcap = c.consultaID(idLista).fcap
        var fent = c.consultaID(idLista).fent

        ventana.putExtra("id",idLista)
        ventana.putExtra("descripcion",d)
        ventana.putExtra("Fcaptura",fcap)
        ventana.putExtra("Fentrega",fent)

        startActivityForResult(ventana,0)
    }
    private fun mostrarAlertEAI(posicion: Int) {
        var idLista = listaID.get(posicion)

        AlertDialog.Builder(this)
            .setTitle("ATENCION")
            .setMessage("¿ACTIVIDAD?")
            .setNegativeButton("CANCELAR") {d,i-> }
            .setNeutralButton("ACTUALIZAR") {d,i-> ActualizarActividad(idLista)}
            .setPositiveButton("INSERTAR EVIDENCIA") {d,i-> EntregarEvidencia(idLista)}
            .show()
    }

    private fun EntregarEvidencia(idLista: String) {
        var ventana = Intent(this,MainActivity5::class.java)

        ventana.putExtra("id",idLista)

        startActivityForResult(ventana,0)
    }

}