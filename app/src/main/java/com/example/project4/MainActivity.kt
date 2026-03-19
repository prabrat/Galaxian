package com.example.project4

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val width : Int = resources.displayMetrics.widthPixels
        val height : Int = resources.displayMetrics.heightPixels

        gameView = GameView(this, width, height)
        setContentView(gameView)
    }
}