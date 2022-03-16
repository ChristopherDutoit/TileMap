package fr.iutlens.dubois.carte

import android.content.Context
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.Sprite
import fr.iutlens.dubois.carte.sprite.SpriteList
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet
import kotlin.math.round


class PuzzleActivity : AppCompatActivity() {

    private val cir by lazy { TiledArea(R.drawable.eaut, Decor(Decor.circ)) }
    private val gameView by lazy { findViewById<GameView>(R.id.PuzzlegameView) }
    var moyennePuzzle = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        SpriteSheet.register(R.drawable.eaut, 5, 4, this)
        SpriteSheet.register(R.drawable.eau, 5, 4, this)

        Toast.makeText(
            this,
            "! Objectif: place précisément les pièces du Puzzle !",
            Toast.LENGTH_LONG
        ).show()

        val adder: Button = findViewById(R.id.termine)
        adder.setOnClickListener {
            end()
        }

        val retour: Button = findViewById(R.id.retour)
        retour.setOnClickListener {
            finish()

        }

        configDrag()
    }

    private fun configDrag() {
        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        for (i in 0 until 5 * 4) { // On crée plusieurs sprites aléatoires
            list.add(
                BasicSprite(
                    R.drawable.eau, cir,
                    (cir.data.sizeX * Math.random()).toFloat(),
                    (cir.data.sizeY * Math.random()).toFloat(),
                    i
                )
            )
        }

        // Configuration de gameView
        gameView.apply {
            background = cir
            sprite = list
            transform = FitTransform(this, cir, Matrix.ScaleToFit.CENTER)
        }
        gameView.onTouch = this::onTouchDrag
        gameView.invalidate() // On demande à rafraîchir la vue

    }

    private fun onTouchDrag(
        point: FloatArray,
        event: MotionEvent,
    ): Boolean {
        val list = gameView.sprite as? SpriteList
            ?: return false // On récupère la liste (quitte si erreur)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> list.setTarget(
                point[0],
                point[1]
            ) != null // Sélection du sprite aux coordonnées cliquées
            MotionEvent.ACTION_MOVE -> {
                (list.target as? BasicSprite)?.let {
                    // On déplace le sprite sélectionné aux nouvelles coordonnées
                    it.x = point[0] / it.gridW // Attentions aux unités, ici 1 = une tuile
                    it.y = point[1] / it.gridH
                    gameView.invalidate() // On demande la mise à jour
                    true
                } ?: false
            }
            MotionEvent.ACTION_UP -> {  // On déselectionne
                check(list)
                list.target = null
                true
            }
            else -> false
        }
    }

    fun check(list: SpriteList) {
        for (sprite in list.list) {
            val basic = sprite as BasicSprite

            var erreur = 0f

            val dx = basic.x - basic.ndx % 5 + 0.5f
            val dy = basic.y - basic.ndx / 4 + 0.5f

            erreur += dx * dx + dy * dy
            //Log.d("erreur actuelle", "$erreur")

            val note = 20 - 2 * erreur
            //Log.d("note actuelle", "$note")

            //val x = round(basic.x-0.5f)*0.5f
            //val y = round(basic.y-0.5f)*0.5f

            //Log.d("check", "${basic.ndx}:(${basic.x},${basic.y})")

            moyennePuzzle += note
        }
        moyennePuzzle /= 20
        //Log.d("moyenne", "${moyennePuzzle}")
    }

    private fun end() {
        val cont = findViewById<View>(R.id.constraintLayout)
        val Moy: TextView = findViewById(R.id.Moyenne)
        val Crit: TextView = findViewById(R.id.Critere)
        Moy.text = Math.round(moyennePuzzle).coerceIn(0..20).toString()
        cont.visibility = VISIBLE

        val sharedPref = this?.getSharedPreferences(
            "notes", Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putInt("puzzle", moyennePuzzle.toInt())
            apply()
        }

            if (moyennePuzzle == 20f) {
                Crit.text = "Parfait"
            } else {
                if (moyennePuzzle >= 15f && moyennePuzzle < 20f) {
                    Crit.text = "Très bien"
                } else {
                    if (moyennePuzzle >= 10f && moyennePuzzle < 15f) {
                        Crit.text = "Bien"
                    } else {
                        if (moyennePuzzle <= 10f) {
                            Crit.text = "Mauvais"
                        }
                    }
                }
            }
        }
    }