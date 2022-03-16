package fr.iutlens.dubois.carte

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class finalscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalscreen)

        val sharedPref = this.getSharedPreferences(
            "notes", Context.MODE_PRIVATE)
         val music = sharedPref.getInt("music", -1)
    }
}


