package com.diana.neonsteed.entities

import android.graphics.RectF // <--- ESTA LÍNEA ES LA QUE FALTA

open class GameObject(var x: Float, var y: Float, var width: Float, var height: Float) {

    // Devuelve el rectángulo para saber si chocan
    open fun getCollisionRect(): RectF {
        return RectF(x, y, x + width, y + height)
    }
}