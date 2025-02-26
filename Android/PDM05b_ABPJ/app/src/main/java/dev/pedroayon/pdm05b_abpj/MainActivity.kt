package dev.pedroayon.pdm05b_abpj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.pedroayon.pdm05b_abpj.ui.components.MainScreen
import dev.pedroayon.pdm05b_abpj.ui.theme.PDM05b_ABPJTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PDM05b_ABPJTheme {
                MainScreen()
            }
        }
    }
}
