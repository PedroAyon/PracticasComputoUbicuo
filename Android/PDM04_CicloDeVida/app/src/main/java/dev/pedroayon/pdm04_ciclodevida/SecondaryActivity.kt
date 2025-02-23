package dev.pedroayon.pdm04_ciclodevida

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.pedroayon.pdm04_ciclodevida.ui.theme.PDM04_CicloDeVidaTheme

class SecondaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "onCreate (Secondary)", Toast.LENGTH_SHORT).show()

        setContent {
            PDM04_CicloDeVidaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Button(onClick = {
                            finish()
                        }) {
                            Text("Terminar")
                        }
                    }
                }
            }
        }
    }

    // Métodos del ciclo de vida para la actividad secundaria
    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "onStart (Secondary)", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "onResume (Secondary)", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        Toast.makeText(this, "onPause (Secondary)", Toast.LENGTH_SHORT).show()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "onStop (Secondary)", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "onDestroy (Secondary)", Toast.LENGTH_SHORT).show()
    }
}