package com.example.simpleandroidgame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val handler = Handler(Looper.getMainLooper())
    private var gameRunnable: Runnable? = null

    private var paddleX = 0f
    private var paddleY = 0f
    private val paddleWidth = 220f
    private val paddleHeight = 36f
    private val paddlePaint = Paint().apply {
        color = Color.parseColor("#03DAC5")
        style = Paint.Style.FILL
    }

    private val ballPaint = Paint().apply {
        color = Color.parseColor("#FF5722")
        style = Paint.Style.FILL
    }

    private val backgroundPaint = Paint().apply {
        color = Color.parseColor("#121212")
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 52f
        isFakeBoldText = true
    }

    private val balls = mutableListOf<Ball>()
    private var score = 0
    private var lives = 3
    private var gameRunning = false
    private var isGameOver = false
    private var lastSpawnTime = 0L

    var onGameStateUpdated: ((score: Int, lives: Int, isGameOver: Boolean) -> Unit)? = null

    data class Ball(
        var x: Float,
        var y: Float,
        var radius: Float,
        var speed: Float,
        var dx: Float
    )

    fun startGame() {
        score = 0
        lives = 3
        isGameOver = false
        balls.clear()
        lastSpawnTime = 0L
        gameRunning = true
        invalidate()
        onGameStateUpdated?.invoke(score, lives, false)
        gameRunnable = object : Runnable {
            override fun run() {
                if (!gameRunning) return
                updateGame()
                invalidate()
                handler.postDelayed(this, 16)
            }
        }
        handler.post(gameRunnable as Runnable)
    }

    fun stopGame() {
        gameRunning = false
        gameRunnable?.let { handler.removeCallbacks(it) }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paddleX = (w - paddleWidth) / 2f
        paddleY = h - paddleHeight - 90f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val paddleLeft = paddleX
        val paddleTop = paddleY
        val paddleRight = paddleX + paddleWidth
        val paddleBottom = paddleY + paddleHeight
        canvas.drawRect(paddleLeft, paddleTop, paddleRight, paddleBottom, paddlePaint)

        for (ball in balls) {
            canvas.drawCircle(ball.x, ball.y, ball.radius, ballPaint)
        }

        if (isGameOver) {
            canvas.drawText("Game Over", width / 2f - 180f, height / 2f, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x.coerceIn(0f, width.toFloat() - paddleWidth)
            paddleX = touchX
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun updateGame() {
        if (isGameOver) {
            stopGame()
            return
        }

        val now = System.currentTimeMillis()
        if (now - lastSpawnTime > 800) {
            spawnBall()
            lastSpawnTime = now
        }

        val iterator = balls.iterator()
        while (iterator.hasNext()) {
            val ball = iterator.next()
            ball.x += ball.dx
            ball.y += ball.speed

            if (ball.x - ball.radius <= 0f || ball.x + ball.radius >= width.toFloat()) {
                ball.dx *= -1f
            }

            val ballLeft = ball.x - ball.radius
            val ballRight = ball.x + ball.radius
            val ballTop = ball.y - ball.radius
            val ballBottom = ball.y + ball.radius

            val paddleLeft = paddleX
            val paddleRight = paddleX + paddleWidth
            val paddleTop = paddleY
            val paddleBottom = paddleY + paddleHeight

            val collision = ballBottom >= paddleTop && ballTop <= paddleBottom &&
                    ballRight >= paddleLeft && ballLeft <= paddleRight

            if (collision) {
                score += 1
                iterator.remove()
                onGameStateUpdated?.invoke(score, lives, false)
                continue
            }

            if (ball.y - ball.radius > height.toFloat()) {
                lives -= 1
                iterator.remove()
                if (lives <= 0) {
                    isGameOver = true
                    onGameStateUpdated?.invoke(score, lives, true)
                    stopGame()
                    return
                }
                onGameStateUpdated?.invoke(score, lives, false)
            }
        }
    }

    private fun spawnBall() {
        val radius = (18..36).random().toFloat()
        val x = (radius..(width - radius).toFloat()).random()
        val speed = (10..20).random().toFloat()
        val dx = ((-3..3).random().toFloat())
        val ball = Ball(x = x, y = -radius, radius = radius, speed = speed, dx = dx)
        balls.add(ball)
    }
}

