package dev.pedroayon.pdm_05_layouts

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btn_frame).setOnClickListener {
            startActivity(Intent(this, FrameLayoutActivity::class.java))
        }

        findViewById<Button>(R.id.btn_grid).setOnClickListener {
            startActivity(Intent(this, GridLayoutActivity::class.java))
        }

        findViewById<Button>(R.id.btn_linear).setOnClickListener {
            startActivity(Intent(this, LinearLayoutActivity::class.java))
        }

        findViewById<Button>(R.id.btn_relative).setOnClickListener {
            startActivity(Intent(this, RelativeLayoutActivity::class.java))
        }

        findViewById<Button>(R.id.btn_table).setOnClickListener {
            startActivity(Intent(this, TableLayoutActivity::class.java))
        }
    }
}