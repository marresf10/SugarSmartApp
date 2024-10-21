package com.example.sugarsmartapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseApp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sugarsmartapplication.ui.theme.SugarSmartApplicationTheme
import com.example.sugarsmart.LoginScreen
import com.example.sugarsmart.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Firebase
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        enableEdgeToEdge()

        setContent {
            SugarSmartApplicationTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") {
                            MainScreen(
                                modifier = Modifier.padding(innerPadding),
                                onLoginClick = { navController.navigate("loginScreen") },
                                onRegisterClick = { navController.navigate("registerScreen") }
                            )
                        }
                        composable("loginScreen") {
                            LoginScreen(onLoginSuccess = { navController.navigate("mainScreen") })
                        }
                        composable("registerScreen") {
                            RegisterScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onLoginClick: () -> Unit, onRegisterClick: () -> Unit) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Iniciar Sesión")
        }
        Button(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}