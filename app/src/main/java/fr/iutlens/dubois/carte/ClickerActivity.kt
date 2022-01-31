package fr.iutlens.dubois.carte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import org.w3c.dom.Text

class ClickerActivity : AppCompatActivity() {
    private var ajout=0.85f
    private var timer: Int=1

    override fun onCreate(savedInstanceState: Bundle?) {
        title="Clique pour éviter de dormir !"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clicker)

        val adder: Button = findViewById(R.id.bout)
        adder.setOnClickListener {
            if (ajout>=0.02 && ajout<1) {
                ajout-=0.02f
                progres(ajout)
            }
        }

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                mainHandler.postDelayed(this, 10)
                findViewById<TextView>(R.id.test1).text=ajout.toString()
                if (ajout>=1) {
                    title="Perdu !"
                } else {
                    ajout+=0.001f
                    progres(ajout)
                }
            }
        })


    }

    private fun progres(fl: Float) {
        val set = ConstraintSet()
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraint_layout)
        set.clone(constraintLayout)

        set.setVerticalBias(R.id.dark_arrow_clicker, fl)
        set.applyTo(constraintLayout)
    }
}