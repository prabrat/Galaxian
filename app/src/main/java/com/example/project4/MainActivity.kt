package com.example.project4

import android.content.Context
import android.media.SoundPool
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.duckhuntingv3.GameTimerTask
import java.util.Timer

class MainActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    private lateinit var galaxian: Galaxian
    private lateinit var detector : GestureDetector
    private lateinit var pool : SoundPool
    private var fireSoundId : Int = 0
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

        pool = SoundPool.Builder().build()
        fireSoundId = pool.load(this, R.raw.fire, 1)

        var th : TouchHandler = TouchHandler()
        detector = GestureDetector(this, th)
        detector.setOnDoubleTapListener(th)

        var timer : Timer = Timer()
        var task : GameTimerTask = GameTimerTask(this)
        timer.schedule(task, 0L, GameView.DELTA_TIME)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            detector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    fun updateModel() {
        if (!galaxian.gameOver()) {
            galaxian.update(width.toFloat(), height.toFloat())
        } else {
            gameView.postInvalidate()
        }
    }

    fun updateView() {
        gameView.postInvalidate()
    }

    fun singleTap(id : Int) {
        pool.play(id, 1f, 1f, 1, 0, 1f)
    }

    inner class TouchHandler : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e : MotionEvent) : Boolean {
            galaxian.fireBullet()
            singleTap(fireSoundId)
            return super.onSingleTapConfirmed(e)
        }
    }

}