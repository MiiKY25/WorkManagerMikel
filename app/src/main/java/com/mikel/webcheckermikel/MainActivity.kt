package com.mikel.webcheckermikel

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mikel.webcheckermikel.R
import com.mikel.webcheckermikel.WebCheckerWorker
import java.util.concurrent.TimeUnit
import android.Manifest
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import java.util.UUID

/**
 * Actividad principal de la aplicación que permite configurar una URL y una palabra clave
 * para buscar en una página web periódicamente. Proporciona la funcionalidad para iniciar y
 * detener la tarea de fondo que realiza la búsqueda.
 *
 * <p>Esta actividad utiliza `WorkManager` para manejar tareas en segundo plano. Los valores
 * de URL y palabra clave se guardan dinámicamente en `SharedPreferences` para ser utilizados
 * por el `WebCheckerWorker`.</p>
 *
 * @see WebCheckerWorker
 */
class MainActivity : Activity() {

    // Etiqueta única para identificar los trabajos programados
    private val workTag = "WebCheckerWork"

    // ID del trabajo en ejecución, generado dinámicamente
    private var workId = UUID.randomUUID()
    private var semaforo = "R"

    /**
     * Metodo que se ejecuta al crear la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Solicitar permisos para notificaciones si es necesario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        // Inicializar campos de texto
        val urlField = findViewById<EditText>(R.id.txtURL)
        val wordField = findViewById<EditText>(R.id.txtPalabra)

        // Inicializar botones
        val btnPlay = findViewById<Button>(R.id.btnON)
        val btnStop = findViewById<Button>(R.id.btnOFF)

        // Deshabilitar el botón "Stop" por defecto
        btnStop.isEnabled = false

        // Listener para el botón "Play"
        btnPlay.setOnClickListener {
            val urlText = urlField.text.toString().trim()  // Obtener el texto de la URL
            val wordText = wordField.text.toString().trim()  // Obtener el texto de la palabra

            // Verificar que ambos campos no estén vacíos
            if (urlText.isEmpty() || wordText.isEmpty()) {
                // Si alguno de los campos está vacío, muestra un mensaje de error
                Toast.makeText(this, "Por favor, ingresa una URL y una palabra", Toast.LENGTH_SHORT).show()
            } else {
                // Si ambos campos contienen texto, continuar con el flujo normal
                this.semaforo = "V"
                val sharedPreferences = getSharedPreferences("WebCheckerPrefs", MODE_PRIVATE)
                sharedPreferences.edit().putString("semaforo", semaforo).apply()

                WorkManager.getInstance(this).cancelAllWorkByTag(this.workTag)
                val workRequest = PeriodicWorkRequestBuilder<WebCheckerWorker>(15, TimeUnit.MINUTES)
                    .addTag(this.workTag)
                    .build()

                // Encolar el trabajo periódico
                WorkManager.getInstance(this).enqueue(workRequest)

                // Guardar el ID del trabajo en ejecución
                this.workId = workRequest.id

                // Deshabilitar el botón "Play" y habilitar el botón "Stop"
                btnPlay.isEnabled = false
                btnStop.isEnabled = true
            }
        }

        // Listener para el botón "Stop"
        btnStop.setOnClickListener {
            println("Pulso boton stop")
            this.semaforo = "R"
            val sharedPreferences = getSharedPreferences("WebCheckerPrefs", MODE_PRIVATE)
            sharedPreferences.edit().putString("semaforo", semaforo).apply()

            stopWork()

            // Habilitar el botón "Play" y deshabilitar el botón "Stop"
            btnPlay.isEnabled = true
            btnStop.isEnabled = false
        }

        // Listener para capturar cambios en el campo de texto de la URL
        urlField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se realiza ninguna acción antes del cambio de texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Se puede manejar el texto mientras cambia (opcional)
            }

            override fun afterTextChanged(s: Editable?) {
                val url = s.toString()
                val sharedPreferences = getSharedPreferences("WebCheckerPrefs", MODE_PRIVATE)
                sharedPreferences.edit().putString("url", url).apply()
                println("URL ingresada: $url")
            }
        })

        // Listener para capturar cambios en el campo de texto de la palabra clave
        wordField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se realiza ninguna acción antes del cambio de texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Se puede manejar el texto mientras cambia (opcional)
            }

            override fun afterTextChanged(s: Editable?) {
                val word = s.toString()
                val sharedPreferences = getSharedPreferences("WebCheckerPrefs", MODE_PRIVATE)
                sharedPreferences.edit().putString("word", word).apply()
                println("Palabra ingresada: $word")
            }
        })
    }

    /**
     * Metodo para detener la tarea de fondo y cerrar la aplicación.
     */
    private fun stopWork() {
        // Cancelar trabajos específicos por ID
        WorkManager.getInstance(this).cancelWorkById(this.workId)
        // Prune trabajos antiguos
        WorkManager.getInstance(this).pruneWork()
        WorkManager.getInstance(this).getWorkInfosByTag(workTag).get().forEach { workInfo ->
            println("Trabajo ID: ${workInfo.id}, Estado: ${workInfo.state}")

            // Cancelar solo si el trabajo está encolado o en ejecución
            if (workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING) {
                WorkManager.getInstance(this).cancelWorkById(workInfo.id)
                println("Trabajo con ID ${workInfo.id} cancelado")
            }
        }
    }
}
