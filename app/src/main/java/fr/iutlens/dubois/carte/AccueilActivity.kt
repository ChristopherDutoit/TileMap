package fr.iutlens.dubois.carte

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class AccueilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)
    }
}

/*fun onClick(view: View) {
    val textView = findViewById<TextView>(R.id.textView)
    if (textView.visibility == View.VISIBLE){
        textView.visibility = View.INVISIBLE
    } else {
        textView.visibility = View.VISIBLE
    }
} */