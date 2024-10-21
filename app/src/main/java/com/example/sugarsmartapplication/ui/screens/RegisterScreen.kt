package com.example.sugarsmart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.sugarsmartapplication.MainActivity
import com.example.sugarsmartapplication.ui.theme.SugarSmartApplicationTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : ComponentActivity() {  // Cambiado a RegisterActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SugarSmartApplicationTheme {
                RegisterScreen()  // Muestra la pantalla de registro aquí
            }
        }
    }
}

@Composable
fun RegisterScreen() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Obtén el contexto de la actividad
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var glucosa by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        TextField(
            value = glucosa,
            onValueChange = { glucosa = it },
            label = { Text("Nivel de Glucosa") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val userData = hashMapOf(
                                "email" to user?.email,
                                "glucosa" to glucosa.toInt(),
                                "likedSnacks" to listOf<String>(),
                                "dislikedSnacks" to listOf<String>()
                            )

                            // Guardar datos en Firestore
                            db.collection("users").document(user!!.uid).set(userData)
                                .addOnSuccessListener {
                                    // Mostrar Toast al guardar datos
                                    Toast.makeText(context, "Registro exitoso. Colección creada.", Toast.LENGTH_SHORT).show()

                                    // Regresar a MainActivity
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                    (context as ComponentActivity).finish() // Finaliza la actividad de registro
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = "Error al guardar datos en Firestore: ${e.message}"
                                }
                        } else {
                            errorMessage = "Error en el registro: ${task.exception?.message}"
                        }
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
