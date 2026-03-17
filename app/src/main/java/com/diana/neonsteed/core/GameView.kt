package com.diana.neonsteed

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.diana.neonsteed.entities.*

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var thread: GameThread? = null
    private val pref = context.getSharedPreferences("NeonSteedPrefs", Context.MODE_PRIVATE)
    private var highScore = pref.getInt("highscore", 0)

    // --- CARGA DE IMÁGENES ---
    private val spriteCaballito = loadBitmap(R.drawable.caballito)
    private val spriteCactus = loadBitmap(R.drawable.cactus)
    private val spriteBuitre = loadBitmap(R.drawable.buitre)
    private val spriteTecho = loadBitmap(R.drawable.estalactita) // Asegúrate de tener esta foto

    private var caballito: Caballito? = null
    private var fondo: Fondo? = null
    private val obstaculos = mutableListOf<Obstaculo>()

    // Pinceles mejorados con tamaños escalados
    private var baseScale = 1f // Se calculará en surfaceCreated
    private val floorPaint = Paint().apply { color = Color.parseColor("#D2B48C"); style = Paint.Style.FILL }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        isFakeBoldText = true
        setShadowLayer(5f, 2f, 2f, Color.BLACK)
    }
    private val sunPaint = Paint().apply { color = Color.parseColor("#FFD54F") }

    private var score = 0
    private var level = 1
    private var currentSpeed = 0f // Se escalará
    private var currentInterval = 65
    private var frameCounter = 0
    private var isGameOver = false

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
        isFocusable = true
    }

    private fun loadBitmap(resId: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, resId)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // --- 1. CALCULAR ESCALA BASE ---
        // Usamos una anchura de referencia (ej: 1080px) para calcular la escala
        baseScale = width / 1080f

        // Ajustamos tamaños de texto y pinceles según la escala
        textPaint.textSize = 70f * baseScale
        floorPaint.strokeWidth = 20f * baseScale

        // Velocidad inicial escalada
        currentSpeed = 25f * baseScale

        val groundY = height - (200f * baseScale)
        // El caballito aparece apoyado en el suelo directamente, escalado
        val horseWidth = 300f * baseScale
        val horseHeight = 300f * baseScale
        caballito = Caballito(horseWidth, groundY - horseHeight)

        // Pasamos la altura del suelo escalada al Fondo
        fondo = Fondo(width.toFloat(), height.toFloat(), 200f * baseScale)

        thread?.setRunning(true)
        if (thread?.state == Thread.State.NEW) thread?.start()
    }

    fun update() {
        if (isGameOver) return

        level = (score / 15) + 1
        // Velocidad sube con el nivel, escalada
        currentSpeed = (25f + (level * 2f)) * baseScale

        val groundLevel = height - (200f * baseScale)
        caballito?.update(groundLevel)
        fondo?.update(currentSpeed)

        frameCounter++
        if (frameCounter >= currentInterval) {
            generarObstaculo()
            frameCounter = 0
            if (currentInterval > 35) currentInterval--
        }

        val iterator = obstaculos.iterator()
        while (iterator.hasNext()) {
            val obs = iterator.next()
            obs.update(currentSpeed)

            if (caballito != null && RectF.intersects(caballito!!.getCollisionRect(), obs.getCollisionRect())) {
                morir()
            }

            if (obs.x + obs.width < 0) {
                score++
                iterator.remove()
            }
        }
    }

    private fun generarObstaculo() {
        val spawnX = width.toFloat()
        val suerte = (0..2).random()
        val groundY = height - (200f * baseScale)

        when (suerte) {
            // Todos los tamaños y posiciones se multiplican por baseScale
            0 -> { // Cactus
                val w = 130f * baseScale
                val h = 180f * baseScale
                obstaculos.add(Obstaculo(spawnX, groundY - h, w, h, false))
            }
            1 -> { // Techo
                val w = 150f * baseScale
                val h = 280f * baseScale
                obstaculos.add(Obstaculo(spawnX, 0f, w, h, false))
            }
            2 -> { // Buitre
                val w = 140f * baseScale
                val h = 110f * baseScale
                // Rango de altura escalado
                val yMin = groundY - (450f * baseScale)
                val yMax = groundY - (150f * baseScale)
                val yPajaro = (yMin.toInt()..yMax.toInt()).random().toFloat()
                obstaculos.add(Obstaculo(spawnX, yPajaro, w, h, true))
            }
        }
    }

    private fun morir() {
        isGameOver = true
        if (score > highScore) {
            highScore = score
            pref.edit().putInt("highscore", highScore).apply()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        val bgColor = when(level) {
            1 -> "#FFCC80"
            2 -> "#FFAB91"
            3 -> "#CE93D8"
            else -> "#5C6BC0"
        }
        canvas.drawColor(Color.parseColor(bgColor))

        val sunColor = if (level < 4) "#FFD54F" else "#ECEFF1"
        sunPaint.color = Color.parseColor(sunColor)
        // Sol escalado y posicionado proporcionalmente
        canvas.drawCircle(width * 0.8f, 200f * baseScale, 100f * baseScale, sunPaint)

        // Dibujamos el suelo como un rectángulo relleno
        canvas.drawRect(0f, height - (200f * baseScale), width.toFloat(), height.toFloat(), floorPaint)

        fondo?.draw(canvas, floorPaint) // Para las líneas decorativas si las tiene

        for (obs in obstaculos) {
            val sprite = when {
                obs.esPajaro -> spriteBuitre
                obs.y == 0f -> spriteTecho
                else -> spriteCactus
            }
            canvas.drawBitmap(sprite, null, obs.getCollisionRect(), null)
        }

        caballito?.let {
            canvas.drawBitmap(spriteCaballito, null, it.getCollisionRect(), null)
        }

        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("PUNTOS: $score", 50f * baseScale, 120f * baseScale, textPaint)
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("RECORD: $highScore", width - (50f * baseScale), 120f * baseScale, textPaint)

        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText("NIVEL $level", width / 2f, 120f * baseScale, textPaint)

        if (isGameOver) {
            canvas.drawColor(Color.parseColor("#AA000000"))
            textPaint.color = Color.WHITE
            textPaint.textSize = 120f * baseScale
            canvas.drawText("¡TE HAN ATRAPADO!", width / 2f, height / 2f, textPaint)
            textPaint.textSize = 60f * baseScale
            canvas.drawText("Toca para volver a cabalgar", width / 2f, height / 2f + (120f * baseScale), textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isGameOver) {
                resetGame()
            } else {
                caballito?.saltar()
            }
        }
        return true
    }

    private fun resetGame() {
        score = 0
        level = 1
        currentSpeed = 25f * baseScale // Velocidad escalada
        currentInterval = 65
        isGameOver = false
        obstaculos.clear()

        val groundY = height - (200f * baseScale)
        val horseHeight = 160f * baseScale
        caballito?.y = groundY - horseHeight
        caballito?.velocity = 0f
    }

    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, he: Int) {}
    override fun surfaceDestroyed(h: SurfaceHolder) {
        thread?.setRunning(false)
        try { thread?.join() } catch (e: Exception) { e.printStackTrace() }
    }
}