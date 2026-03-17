package com.diana.neonsteed

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {

    private var running: Boolean = false
    private var canvas: Canvas? = null

    // Controlamos el bucle del juego
    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {
        while (running) {
            canvas = null
            try {
                // Bloqueamos el lienzo para dibujar
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    // Actualizamos físicas y dibujamos
                    this.gameView.update() // <--- Crearemos esta función en GameView
                    this.gameView.draw(canvas!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (canvas != null) {
                    try {
                        // Soltamos el lienzo para que se vea en pantalla
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}