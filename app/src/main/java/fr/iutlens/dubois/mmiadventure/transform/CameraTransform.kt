package fr.iutlens.dubois.mmiadventure.transform

import android.graphics.Matrix

interface CameraTransform {
    fun getPoint(x: Float, y: Float): FloatArray
    fun get(): Matrix
}