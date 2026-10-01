package com.example.simpleandroidgame

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var livesTextView: TextView
    private lateinit var startButton: Button
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.scoreTextView)
        livesTextView = findViewById(R.id.livesTextView)
        startButton = findViewById(R.id.startButton)
        gameView = findViewById(R.id.gameView)

        gameView.onGameStateUpdated = { score, lives, isGameOver ->
            scoreTextView.text = "Skor: $score"
            livesTextView.text = "Nyawa: $lives"
            if (isGameOver) {
                startButton.text = "Main Lagi"
                startButton.isEnabled = true
            }
        }

        startButton.setOnClickListener {
            startButton.text = "Restart"
            gameView.startGame()
        }
    }
}
