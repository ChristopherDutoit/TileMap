package fr.iutlens.dubois.mmiadventure

import android.content.Context
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import fr.iutlens.dubois.mmiadventure.sprite.BasicSprite
import fr.iutlens.dubois.mmiadventure.sprite.Sprite
import fr.iutlens.dubois.mmiadventure.sprite.SpriteList
import fr.iutlens.dubois.mmiadventure.sprite.TiledArea
import fr.iutlens.dubois.mmiadventure.transform.FitTransform
import fr.iutlens.dubois.mmiadventure.utils.SpriteSheet
import kotlin.math.roundToInt

class MusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        // Chargement des feuilles de sprites
        SpriteSheet.register(R.drawable.decor, 5, 4, this)
        SpriteSheet.register(R.drawable.music_map, 6, 3, this)
        SpriteSheet.register(R.drawable.spritemusic, 6, 1, this)

        configDrag()
        exportBtn.setOnClickListener {
            val list : SpriteList = gameView.sprite as SpriteList
            check(list)
        }

        startBtn.setOnClickListener {
            bk.setVisibility(View.GONE)
            exportBtn.setVisibility(View.VISIBLE)
            startBtn.setVisibility(View.GONE)
            rules.setText(" ")

        }

        accueilBtn.setOnClickListener{
            finish()
        }

     }
    private val map by lazy { TiledArea(R.drawable.decor, Decor(Decor.map)) }
    private val room by lazy { TiledArea(R.drawable.decor, Decor(Decor.room)) }
    private val music by lazy { TiledArea(R.drawable.music_map, Decor(Decor.music)) }
    private val hero by lazy { BasicSprite(R.drawable.spritemusic, map, 8.5F, 4.5F) }
    private val gameView by lazy { findViewById<GameView>(R.id.MusicGameView) }
    private val exportBtn :Button by lazy  { findViewById(R.id.exportBtn) }
    private val bk :TextView by lazy  { findViewById(R.id.backgroud) }
    private val accueilBtn : Button by lazy { findViewById(R.id.backacceuil)}
    private val noteTxt :TextView by lazy  { findViewById(R.id.textView5) }
    private val votreNote :TextView by lazy  { findViewById(R.id.textView4) }
    private val rules:TextView by lazy  { findViewById(R.id.textView14) }
    private val startBtn : Button by lazy { findViewById(R.id.startBtn)}


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
        val list = gameView.sprite as? SpriteList ?: return false// On récupère la liste (quitte si erreur)
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

    private fun check(list: SpriteList) {
        var cooX :FloatArray = floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F)
        var cooY :FloatArray = floatArrayOf(0F, 0F, 0F, 0F, 0F, 0F)
        var Xok :Float = 0F
        var Yok :Float = 0.50F
        var note:Float = 20F
        var malus:Float =0F
            var i = 0
            list.list.forEach { s : Sprite ->
                var basic = s as BasicSprite
                cooX[i]= basic.x.toFloat()
                cooY[i]= basic.y.toFloat()
                Xok  =  i.toFloat() +2.50F
                Yok= 0.5F
                if(Xok <= cooX[i]) {
                    malus = (cooX[i] - Xok )*10
                }else{
                    malus = (Xok - cooX[i])*10
                }
                note -= malus
                if(Yok <= cooY[i]) {
                    malus = (cooY[i] - Yok )*5
                }else{
                    malus = (Yok - cooY[i])*5
                }
                note -= malus
                 i++

            }

        if(note < 0){
            note = 0F
        }

        bk.setVisibility(View.VISIBLE)
        exportBtn.setVisibility(View.GONE)
        accueilBtn.setVisibility(View.VISIBLE)

        val finalnote  = note.roundToInt()
        noteTxt.setText(finalnote.toString())
        votreNote.setText("Votre note :")
        Log.d("list :", note.toString())
        val sharedPref = this.getSharedPreferences(
            "notes", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("music", finalnote)
            apply()
        }



    }

}






