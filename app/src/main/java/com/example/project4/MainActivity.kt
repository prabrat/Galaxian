package com.example.project4

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.duckhuntingv3.GameTimerTask
import java.util.Timer

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var galaxian: Galaxian
    private var width : Int = 0
    private var height : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        width = resources.displayMetrics.widthPixels
        height = resources.displayMetrics.heightPixels

        gameView = GameView(this, width, height)
        galaxian = gameView.getGalaxian()
        setContentView(gameView)

        var timer : Timer = Timer()
        var task : GameTimerTask = GameTimerTask(this)
        timer.schedule(task, 0L, GameView.DELTA_TIME)
    }

    fun updateModel() {
        galaxian.update(width.toFloat(), height.toFloat())
    }

}