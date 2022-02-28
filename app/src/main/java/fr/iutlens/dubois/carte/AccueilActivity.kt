package fr.iutlens.dubois.carte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AccueilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)
    }
    fun onClick(view:View ) {
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }


    fun onClickCredits(view: android.view.View) {
        val intent = Intent(this, CreditsActivity::class.java)
        startActivity(intent)
    }

}

