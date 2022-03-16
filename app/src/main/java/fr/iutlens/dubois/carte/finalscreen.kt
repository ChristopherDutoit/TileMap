package fr.iutlens.dubois.carte

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import fr.iutlens.dubois.carte.sprite.SpriteList

class finalscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalscreen)

        val sharedPref = this.getSharedPreferences(
            "notes", Context.MODE_PRIVATE)
         val music = sharedPref.getInt("music", -1)
        val puzzle :Int = 15
        val clicker :Int = 3

        val moyenne :Int = (music+puzzle+clicker)/3


        val musicTxt: TextView by lazy  { findViewById(R.id.textView19) }
        val puzzleTxt: TextView by lazy  { findViewById(R.id.textView20) }
        val clickerTxt: TextView by lazy  { findViewById(R.id.textView21) }
        val moyenneTxt: TextView by lazy  { findViewById(R.id.textView24) }
        val resetBtn : Button by lazy { findViewById(R.id.button2)}


        musicTxt.setText(music.toString())
        puzzleTxt.setText(puzzle.toString())
        clickerTxt.setText(clicker.toString())
        moyenneTxt.setText(moyenne.toString()+"/20")
        Log.d("music", music.toString())

        resetBtn.setOnClickListener {
        with (sharedPref.edit()) {
            clear()
            apply()
        }}
    }

}


