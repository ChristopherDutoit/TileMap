package fr.iutlens.dubois.carte.sprite




import android.graphics.Canvas
import android.graphics.RectF
import androidx.core.graphics.withTranslation
import fr.iutlens.dubois.carte.utils.SpriteSheet


/**
 *
 *
 * Created by dubois on 27/12/2017.
 */
class BasicSprite(private val spriteSheet: SpriteSheet,
                  private val tiledArea: TiledArea,
                  var x: Float, var y: Float,
                  var ndx : Int = 0) :
    Sprite {

    constructor(id: Int, tiledArea: TiledArea, x: Float, y: Float, ndx : Int=0) : this(SpriteSheet[id]!!,tiledArea, x, y,ndx);

    // Taille d'une case de la grille en pixels
    val gridW = tiledArea.w
    val gridH = tiledArea.h

    // centre du sprite (en pixels)
    fun xCenter() = x * gridW
    fun yCenter() = y * gridH

    // taille du sprite en pixels, divisée par deux (pour le centrage)
    private val w2 = spriteSheet.w / 2f
    private val h2 = spriteSheet.h / 2f

    override fun paint(canvas: Canvas) {
        canvas.withTranslation(xCenter(),yCenter()) {
           // canvas.scale(3f,3f)
            spriteSheet.paint(this, ndx, -w2, -h2)
        }
    }

//rectangle occuppé par le sprite
    override val boundingBox: RectF
        get() = RectF(
            xCenter() - w2,
            yCenter() - h2,
            xCenter() + w2,
            yCenter() + h2
        )

}