package com.diana.neonsteed.entities

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF // Importante añadir esto

class Caballito(x: Float, y: Float) : GameObject(x, y, 110f, 90f) {
    var velocity = 0f
    val gravity = 2.8f
    val jumpStrength = -45f

    fun update(groundLevel: Float) {
        velocity += gravity
        y += velocity

        // No pasar del suelo (ajustado al tamaño del caballito)
        if (y > groundLevel - height) {
            y = groundLevel - height
            velocity = 0f
        }
        // No salir por arriba
        if (y < 0) {
            y = 0f
            velocity = 0f
        }
    }

    fun saltar() {
        velocity = jumpStrength
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // CORRECCIÓN: Creamos un RectF temporal para el dibujo redondeado
        val rect = RectF(x, y, x + width, y + height)
        canvas.drawRoundRect(rect, 15f, 15f, paint)
    }
}