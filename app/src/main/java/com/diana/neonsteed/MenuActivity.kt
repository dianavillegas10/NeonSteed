package com.diana.neonsteed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val playButton = findViewById<Button>(R.id.playButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Botón Jugar (por ahora solo nos avisa, pronto será el juego)
        playButton.setOnClickListener {
            Toast.makeText(this, "¡Preparando los establos neón...!", Toast.LENGTH_SHORT).show()
            // Aquí es donde lanzaremos la pantalla del juego real más adelante
        }

        // Botón Cerrar Sesión
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut() // Firebase cierra la sesión
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerramos el menú para volver al Login
        }
        playButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}