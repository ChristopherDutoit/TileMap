package fr.iutlens.dubois.mmiadventure.sprite

import android.graphics.Canvas
import android.graphics.RectF

class SpriteList : Sprite {

    val list = ArrayList<Sprite>()

    fun add(sprite : Sprite){
        list.add(sprite)
    }


    var target : Sprite? = null

    fun setTarget(x : Float, y : Float): Sprite? {
        target = list.firstOrNull() { it.boundingBox.contains(x,y) }
        return target
    }


    override fun paint(canvas: Canvas) {
        list.forEach { it.paint(canvas) }
    }

    private fun computeBound() : RectF {
        var result : RectF? = null
        list.forEach {
            val box = it.boundingBox
            result?.union(box)
            result = result ?: box
        }
        return result!!
    }

    override val boundingBox: RectF
        get() = computeBound()
}