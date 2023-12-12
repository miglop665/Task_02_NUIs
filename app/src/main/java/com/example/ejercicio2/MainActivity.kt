package com.example.ejercicio2

//Importamos las librerias
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.speech.RecognizerIntent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    //Creamos las variables
    private val REQ_CODE = 100
    private lateinit var textView1: TextView
    private lateinit var textView2: TextView
    private lateinit var textCambioColor: TextView
    private var isFirstExecution = true // Variable para rastrear la primera ejecución
    private lateinit var toolbar: Toolbar




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Guardamos los valores de las ids de nuestro activity_main en nuestras variables creadas aqui
        setContentView(R.layout.activity_main)
        textView1 = findViewById(R.id.text1)
        textView2 = findViewById(R.id.text2)
        textCambioColor = findViewById(R.id.text)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val speak = findViewById<ImageView>(R.id.speak)
        //Hacemos que al pulsar el micro se active el dictado por voz de google
        speak.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hable usted")
            //Cambia el color del texto luego de hacer click
            textCambioColor.setTextColor(Color.parseColor("#FF0000"))
            try {
                startActivityForResult(intent, REQ_CODE)
            } catch (a: ActivityNotFoundException) {
                //por si no tuvieramos opcion de micro en nuestro dispositivo
                Toast.makeText(applicationContext, "Repite de nuevo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Hacemos que se vean los 3 puntitos de un futuro menu que nos podria llevar a otras ventanas de nuestro asistente
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //En base al resultado que recibamos del dictado por voz lo escribimos por pantalla
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val recognizedText = result?.get(0)
                    // Dividir el texto en líneas separadas
                    val lines = recognizedText?.split("\n")
                    // Asignar cada línea al TextView correspondiente según la ejecución
                    if (lines != null) {
                        if (isFirstExecution) {
                            textView1.text = lines.joinToString("\n")


                            isFirstExecution = false
                        } else {
                            textView2.text = lines.joinToString("\n")
                            isFirstExecution = true
                        }
                    }
                }else{
                    //Lo devuelve a color negro
                    textCambioColor.setTextColor(Color.parseColor("#000000"))
                }
            }
        }
    }

}
