package com.example.krishisangam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.krishisangam.auth.AuthNavGraph
import com.example.krishisangam.ui.theme.KrishiSangamTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        LanguageManager.applyLanguage(
            LanguageManager.getSavedLanguage(this)
        )

        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KrishiSangamTheme {
                AuthNavGraph()
            }
        }
    }
}