package fr.iutlens.dubois.carte

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class CreditsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)
    }
    fun onClickAccueil(view: android.view.View) {
        val intent = Intent(this, AccueilActivity::class.java)
        startActivity(intent)
    }
}