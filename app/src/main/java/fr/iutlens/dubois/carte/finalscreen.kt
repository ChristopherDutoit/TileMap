package fr.iutlens.dubois.carte

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import fr.iutlens.dubois.carte.sprite.SpriteList

class finalscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalscreen)

        val sharedPref = this.getSharedPreferences(
            "notes", Context.MODE_PRIVATE)
         val music = sharedPref.getInt("music", -1)
        val puzzle :Int =  sharedPref.getInt("puzzle", -1)
        val clicker :Int = sharedPref.getInt("clicker", -5)/5

        val moyenne :Int = (music+puzzle+clicker)/3


        val musicTxt: TextView by lazy  { findViewById(R.id.textView18) }
        val puzzleTxt: TextView by lazy  { findViewById(R.id.textView20) }
        val clickerTxt: TextView by lazy  { findViewById(R.id.textView21) }
        val moyenneTxt: TextView by lazy  { findViewById(R.id.textView24) }
        val resetBtn : Button by lazy { findViewById(R.id.button2)}
        val monsieur :ImageView by lazy{findViewById(R.id.imageView)}


        musicTxt.setText(music.toString())
        puzzleTxt.setText(puzzle.toString())
        clickerTxt.setText(clicker.toString())
        moyenneTxt.setText(moyenne.toString()+"/20")
        Log.d("music", music.toString())

        if(moyenne >= 10){
            monsieur.setImageResource(R.drawable.ordi_win)
        }else{
            monsieur.setImageResource(R.drawable.ordi_face_sad)
        }

        resetBtn.setOnClickListener {
        with (sharedPref.edit()) {
            clear()
            apply()
        }
            val intent = Intent(this, AccueilActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

}


