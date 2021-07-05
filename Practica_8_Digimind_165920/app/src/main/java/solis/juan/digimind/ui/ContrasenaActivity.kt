package solis.juan.digimind.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import solis.juan.digimind.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ContrasenaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrasena)

        auth = Firebase.auth

        val btn_restablecer: Button = findViewById(R.id.btn_restablecer)

        btn_restablecer.setOnClickListener {
            val et_correo: EditText = findViewById(R.id.et_correo_cont)

            var correo: String = et_correo.text.toString()

            if (!correo.isNullOrBlank()) {

                auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Se envio el correo correctamente.", Toast.LENGTH_LONG).show()

                            val intent: Intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(this, "Error al enviar el correo.", Toast.LENGTH_LONG).show()
                        }
                    }

            } else {
                Toast.makeText(this, "Ingresar correo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}