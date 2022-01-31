package fr.iutlens.dubois.carte

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ClickerActivity : AppCompatActivity() {
    private var clc: Int=0
    private var max: Int=20
    //private var hautbarre: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)

        val adder: Button = findViewById(R.id.bout)

        adder.setOnClickListener {
            clc++
            val res: TextView = findViewById(R.id.vueTexte)

            if (clc <= max) {
                res.text=clc.toString()
            } else {
                res.text="Tu as atteint le maximum ("+max.toString()+")"
            }
        }
    }

}