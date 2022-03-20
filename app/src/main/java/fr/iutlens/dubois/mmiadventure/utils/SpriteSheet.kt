package fr.iutlens.dubois.mmiadventure.utils

import android.content.Context
import android.graphics.*
import java.util.HashMap

class SpriteSheet(val n: Int, val m: Int) {
    private var bitmap: Bitmap? = null
    private var sprite: Array<Bitmap?>? = null
    var w = 0
    var h = 0
/*    private val dst: RectF = RectF()
    private val src: Rect = Rect()
*/
    companion object {
        private var map: MutableMap<Int, SpriteSheet> =  HashMap<Int, SpriteSheet>()
        private var paint = Paint().apply { isAntiAlias = true }

        fun register(id: Int, n: Int, m: Int, context: Context?) {
            map[id] = SpriteSheet(n, m)
            if (context != null) {
                Companion[context, id]
            }
        }

        fun createCroppedBitmap(
            src: Bitmap?,
            left: Int,
            top: Int,
            width: Int,
            height: Int
        ): Bitmap {
            /*
		    bug: returns incorrect region, so must do it manually
		    return Bitmap.createBitmap(src, left, top,width, height);
		  */
            val offset = 0
            val pixels = IntArray(width * height)
            src!!.getPixels(pixels, offset, width, left, top, width, height)
            return Bitmap.createBitmap(pixels, width, height, src.config)
        }

        operator fun get(context: Context, id: Int): SpriteSheet? {
            val result = map[id]
            if (result!!.bitmap == null) result.load(context, id)
            return result
        }

        operator fun get(id: Int): SpriteSheet? {
            return map[id]
        }

    }

    constructor(context: Context, id: Int, n: Int, m: Int) : this(n, m) {
        load(context, id)
    }

    private fun load(context: Context, id: Int) {
        bitmap = Utils.loadImage(context, id)
        w = bitmap!!.width / n
        h = bitmap!!.height / m
    }

    fun paint(canvas: Canvas, ndx: Int, x: Float, y: Float) {
        /*	int i = ndx%n;
		int j = ndx/n;
		src.set(i*w, j*h, (i+1)*w-1, (j+1)*h-1);
		dst.set(x,y,x+w,y+h);
		canvas.drawBitmap(bitmap, src, dst, paint); */
        canvas.drawBitmap(getBitmap(ndx)!!, x, y, paint)
    }

    private fun getBitmap(ndx: Int): Bitmap? {
        if (sprite == null) sprite = arrayOfNulls(n * m)
        if (sprite!![ndx] == null) {
            val i = ndx % n
            val j = ndx / n
            sprite!![ndx] = createCroppedBitmap(bitmap, i * w, j * h, w, h)
        }
        return sprite!![ndx]
    }

    init {
        bitmap = null
        sprite = null
    }
}