package fr.iutlens.dubois.carte

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.Sprite
import fr.iutlens.dubois.carte.sprite.SpriteList
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet

class MusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        // Chargement des feuilles de sprites
        SpriteSheet.register(R.drawable.decor, 5, 4, this)
        SpriteSheet.register(R.drawable.music_map, 6, 3, this)
        SpriteSheet.register(R.drawable.spritemusic, 6, 1, this)

        configDrag()

     }
    private val map by lazy { TiledArea(R.drawable.decor, Decor(Decor.map)) }
    private val room by lazy { TiledArea(R.drawable.decor, Decor(Decor.room)) }
    private val music by lazy { TiledArea(R.drawable.music_map, Decor(Decor.music)) }
    private val hero by lazy { BasicSprite(R.drawable.spritemusic, map, 8.5F, 4.5F) }
    private val gameView by lazy { findViewById<GameView>(R.id.MusicGameView) }



    private fun configDrag() {
        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        for(i in 0..5){ // On crée plusieurs sprites aléatoires
            list.add(
                BasicSprite(R.drawable.spritemusic, music,
                    (music.data.sizeX*Math.random()).toFloat(),
                (music.data.sizeY*Math.random()).toFloat(),
                i)
            )
        }

        // Configuration de gameView
        gameView.apply {
            background = music
            sprite = list
            transform = FitTransform(this, music, Matrix.ScaleToFit.CENTER)
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
                check(list)
                true
            }
            else -> false
        }
    }

    private fun check(list: SpriteList) {
        var result =" "
        list.list.forEach { s : Sprite ->
            val basic = s as BasicSprite
            result += "(${basic.x},${basic.y}) "
        }
        Log.d("list :", result)

    }
}