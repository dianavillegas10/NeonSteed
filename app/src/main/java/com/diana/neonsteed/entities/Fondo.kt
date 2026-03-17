package com.diana.neonsteed.entities

import android.graphics.Canvas
import android.graphics.Paint

// Añadimos floorHeight al constructor
class Fondo(val width: Float, val height: Float, val floorHeight: Float) {
    var floorX = 0f

    fun update(speed: Float) {
        floorX -= speed
        if (floorX <= -width) {
            floorX = 0f
        }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // Usamos floorHeight para la posición Y
        val yPos = height - floorHeight
        // Dibujamos líneas decorativas sobre el suelo relleno
        canvas.drawLine(floorX, yPos, floorX + width, yPos, paint)
        canvas.drawLine(floorX + width, yPos, floorX + width * 2, yPos, paint)
    }
}