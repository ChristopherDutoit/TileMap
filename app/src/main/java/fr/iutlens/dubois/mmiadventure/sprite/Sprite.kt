package fr.iutlens.dubois.mmiadventure.sprite

import android.graphics.Canvas
import android.graphics.RectF

interface Sprite {
    fun paint(canvas: Canvas)
    val boundingBox: RectF
}