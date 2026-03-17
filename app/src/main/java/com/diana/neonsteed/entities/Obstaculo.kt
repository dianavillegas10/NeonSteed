package com.diana.neonsteed.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

class Obstaculo(x: Float, y: Float, width: Float, height: Float, val esPajaro: Boolean = false)
    : GameObject(x, y, width, height) {

    fun update(speed: Float) {
        // Los pájaros son un 20% más rápidos
        x -= if (esPajaro) speed * 1.2f else speed
    }

    // El GameView decide qué 'sprite' pasarle (cactus, pájaro o techo)
    fun draw(canvas: Canvas, sprite: Bitmap) {
        val rectDestino = RectF(x, y, x + width, y + height)
        canvas.drawBitmap(sprite, null, rectDestino, null)
    }

    override fun getCollisionRect(): RectF {
        return RectF(x, y, x + width, y + height)
    }
}