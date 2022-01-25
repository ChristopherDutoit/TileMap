package fr.iutlens.dubois.carte

import android.graphics.Matrix
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.SpriteList
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet


class PuzzleActivity : AppCompatActivity() {

    private val cir by lazy { TiledArea(R.drawable.circuit, Decor(Decor.circ)) }
    private val gameView by lazy { findViewById<GameView>(R.id.PuzzlegameView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)

        SpriteSheet.register(R.drawable.circuit, 5, 4, this)

        configDrag()
    }

    private fun configDrag() {
        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        for(i in 0 until 5*4){ // On crée plusieurs sprites aléatoires
            list.add(BasicSprite(R.drawable.circuit, cir,
                (cir.data.sizeX*Math.random()).toFloat(),
                (cir.data.sizeY*Math.random()).toFloat(),
               i))
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
    ) : Boolean {
        val list = gameView.sprite as? SpriteList ?: return false // On récupère la liste (quitte si erreur)
        return when(event.action) {
            MotionEvent.ACTION_DOWN -> list.setTarget(point[0], point[1]) != null // Sélection du sprite aux coordonnées cliquées
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
                list.target = null
                true
            }
            else -> false
        }
    }
}