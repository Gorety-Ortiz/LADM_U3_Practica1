package mx.tecnm.tepic.ladm_u3_practica1

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main5.*
import java.io.ByteArrayOutputStream

class MainActivity5 : AppCompatActivity() {
    val permisogaleria = 1
    val permisocamara = 2
    var toma: Uri? = null
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        var extras = intent.extras
        id = extras!!.getString("id").toString()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        Volver2.setOnClickListener {
            finish()
        }
        galeria.setOnClickListener(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permisoArchivos = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permisoArchivos,permisogaleria)
                } else {
                    abreGaleria()
                }
            } else {
                abreGaleria()
            }
        }

        camara.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permisoCam = arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permisoCam,permisocamara)
                } else {
                    abreCamara()
                }
            } else {
                abreCamara()
            }
        }
        galeria.setOnClickListener(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permisoArchivos = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permisoArchivos,permisogaleria)
                } else {
                    abreGaleria()
                }
            } else {
                abreGaleria()
            }
        }

        IEvidencia.setOnClickListener {
            val imagen = (evidencia.drawable as BitmapDrawable).bitmap
            var imagenBytes = convierteBytes(imagen)

            var evi = Evidencia(
                id,
                imagenBytes
            )

            evi.asignarPuntero(this)

            var res = evi.insertar()

            if(res == true) {
                mensaje("SE HA AGREGADO LA EVIDENCIA CORRECTAMENTE")
                evidencia.setImageResource(R.drawable.sinimagen)
            } else {
                mensaje("HUBO UN ERROR AL AGREGAR LA EVIDENCIA")
            }
        }

    }

    private fun mensaje(s: String) {
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(s)
            .setPositiveButton("OK"){
                    d,i-> d.dismiss()
            }
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            permisogaleria -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    abreGaleria()
                else
                    Toast.makeText(this,"CONSEDA EL PERMISO PARA ACCEDER A LA GALERIA", Toast.LENGTH_LONG)
                        .show()
            }
            permisocamara -> {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    abreCamara()
                else
                    Toast.makeText(this,"PERMISO NO CONSEGUIDO", Toast.LENGTH_LONG)
                        .show()
            }
        }
    }
    private fun abreGaleria() {
        val galeriaIntent = Intent(Intent.ACTION_PICK)
        galeriaIntent.type = "image/*"
        startActivityForResult(galeriaIntent,permisogaleria)
    }


    private fun abreCamara() {
        val bitsFoto = ContentValues()
        bitsFoto.put(MediaStore.Images.Media.TITLE,"Nueva Imagen")
        toma = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,bitsFoto)
        val camaraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,toma)
        startActivityForResult(camaraIntent,permisocamara)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == permisogaleria) {
            evidencia.setImageURI(data?.data)
        }
        if(resultCode == Activity.RESULT_OK && requestCode == permisocamara) {
            evidencia.setImageURI(toma)
        }
    }

    private fun convierteBytes(bitmap : Bitmap):ByteArray{
        var stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,5,stream)

        return stream.toByteArray()
    }
}