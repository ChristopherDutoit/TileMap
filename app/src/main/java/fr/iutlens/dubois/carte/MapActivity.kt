package fr.iutlens.dubois.carte

import android.content.Intent
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import fr.iutlens.dubois.carte.sprite.BasicSprite
import fr.iutlens.dubois.carte.sprite.SpriteList
import fr.iutlens.dubois.carte.sprite.TiledArea
import fr.iutlens.dubois.carte.transform.FitTransform
import fr.iutlens.dubois.carte.transform.FocusTransform
import fr.iutlens.dubois.carte.utils.SpriteSheet
import kotlin.math.abs

class MapActivity : AppCompatActivity() {

    private val map by lazy { TiledArea(R.drawable.decor, Decor(Decor.map)) }
    private val room by lazy { TiledArea(R.drawable.decor, Decor(Decor.room)) }
    private val hero by lazy { BasicSprite(R.drawable.car, map, 4.5F, 2.5F) }
    private val gameView by lazy { findViewById<GameView>(R.id.gameView) }


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_map)
        // Chargement des feuilles de sprites
        SpriteSheet.register(R.drawable.decor, 5, 4, this)
        SpriteSheet.register(R.drawable.car, 3, 1, this)

        // Par défaut on démarre sur la configuration MapActivity
        configMap()

    }

    fun onClickAccueil(view: android.view.View) {
        val intent = Intent(this, AccueilActivity::class.java)
        startActivity(intent)
    }

    private fun configDrag() {
        // Création des différents éléments à afficher dans la vue
        val list = SpriteList() // Notre liste de sprites
        for(i in 1..7){ // On crée plusieurs sprites aléatoires
            list.add(
                BasicSprite(R.drawable.car, room,
                (room.data.sizeX*Math.random()).toFloat(),
                (room.data.sizeY*Math.random()).toFloat(),
                (0..2).random())
            )
        }

        // Configuration de gameView
        gameView.apply {
            background = room
            sprite = list
            transform = FitTransform(this, room, Matrix.ScaleToFit.CENTER)
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

    private fun configMap() {
        // Configuration de gameView
        gameView.apply {
            background = map
            sprite = hero
            transform = FocusTransform(this, map, hero,6)
        }
        gameView.onTouch = this::onTouchMap
        gameView.invalidate()
    }

    private fun onTouchMap(
        point: FloatArray,
        event: MotionEvent
    ) = if (event.action == MotionEvent.ACTION_DOWN) {
        var dx = point[0] - hero.xCenter() // calcule le vecteur entre le sprite et la zone touchée
        var dy = point[1] - hero.yCenter()
        //       Log.d("move", "$dx/$dy")
        if (abs(dx) > abs(dy)) { // calcule la direction du déplacement
            dx = if (dx > 0) 1f else -1f // on se déplace de plus ou moins une case
            dy = 0f
        } else {
            dx = 0f
            dy = if (dy > 0) 1f else -1f
        }
        if (valid(hero.x+dx,hero.y+dy,-dx,-dy) && valid(hero.x,hero.y,dx,dy)){
            hero.x += dx
            hero.y += dy
            val intent = Intent(this, ClickerActivity::class.java)
            val music = Intent(this, MusicActivity::class.java)
            val puzzle = Intent(this, PuzzleActivity::class.java)
            val credits = Intent(this, CreditsActivity::class.java)
            when (hero.x to hero.y){
                4.5f to 1.5f -> startActivity(intent)
                11.5f to 1.5f -> startActivity(music)
                17.5f to 1.5f -> startActivity(puzzle)
                20.5f to 1.5f -> startActivity(credits)
                21.5f to 1.5f -> startActivity(credits)
            }
        }

        gameView.invalidate()
        true
    } else false


    private fun valid(x: Float, y: Float, dx: Float, dy: Float): Boolean {
        val x1 = (x-0.5).toInt()
        val y1 = (y-0.5).toInt()


        val typeCase = getType(y1, x1) ?: return false

        Log.d("valid",typeCase.toString())
        Log.d("dx",dx.toString())

        return when(typeCase){
            11 -> true
            15 -> dx <= 0 // mur droit
            10 -> dx >=0 //mur gauche
            7 -> dy >= 0 // porte gauche
            8 -> dy >= 0 // portedroite

            else -> false
        }
    }

    private fun getType(y: Int, x: Int): Int? {

        if (x < 0 || y < 0) {
            return null
        }

        if (x >= map.data.sizeX || y >= map.data.sizeY) {
            return null
        }
        val typeCase = map.data.get(y, x)
        return typeCase
    }
}