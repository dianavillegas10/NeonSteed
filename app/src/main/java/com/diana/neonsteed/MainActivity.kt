package com.diana.neonsteed

import android.content.Intent // IMPORTANTE: Para cambiar de pantalla
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isRegistering = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val emailField = findViewById<EditText>(R.id.emailEditText)
        val passwordField = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val toggleText = findViewById<TextView>(R.id.toggleRegisterText)

        toggleText.setOnClickListener {
            isRegistering = !isRegistering
            if (isRegistering) {
                loginButton.text = "CREAR CUENTA"
                toggleText.text = "Ya tengo cuenta, entrar"
            } else {
                loginButton.text = "INICIAR GALOPE"
                toggleText.text = "¿Eres nuevo? Regístrate aquí"
            }
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val pass = passwordField.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Escribe algo, ¡el neón te espera!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isRegistering) {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "¡Jinete Registrado!", Toast.LENGTH_SHORT).show()
                            // Al registrarse también lo mandamos al menú
                            irAlMenu()
                        } else {
                            Toast.makeText(this, "Error al registrar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "¡Bienvenido, Jinete!", Toast.LENGTH_SHORT).show()
                            // SALTO AL MENÚ
                            irAlMenu()
                        } else {
                            Toast.makeText(this, "Error al entrar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    // Función auxiliar para no repetir código
    private fun irAlMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish() // Cerramos la pantalla de login para que no pueda volver atrás con el botón del móvil
    }
}