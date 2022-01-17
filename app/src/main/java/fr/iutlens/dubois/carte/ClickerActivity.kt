package fr.iutlens.dubois.carte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ClickerActivity : AppCompatActivity() {
    private var clc: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)

        val adder: Button = findViewById(R.id.bout)
        val txt: TextView = findViewById(R.id.vueTexte)

        adder.setOnClickListener {
            clc++
        }

        txt.text= clc.toString()
    }

}