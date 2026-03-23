package com.example.project4

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.DragEvent
import android.view.View
import androidx.core.graphics.scale

class GameView : View {
    private var ship : Bitmap
    private var enemy : Bitmap
    private var paint : Paint
    private var galaxian: Galaxian
    private var width: Int = 0
    private var height: Int = 0
    private var status = ""
    private var score : Int

    constructor(context: Context, width: Int, height : Int): super(context){
        score = context.getSharedPreferences("galaxian", Context.MODE_PRIVATE).getInt("bestScore", 0)

        paint = Paint()
        paint.strokeWidth = 20f
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        this.width = width
        this.height = height
        ship = BitmapFactory.decodeResource(resources, SHIP)
        ship = ship.scale(175, 175)
        enemy = BitmapFactory.decodeResource(resources, ENEMY)
        enemy = enemy.scale(100, 100)
        galaxian  = Galaxian(context, width.toFloat(), height.toFloat(), height * .000075f,
            ship.width.toFloat(), ship.height.toFloat(), enemy.height.toFloat())
        galaxian.setDeltaTime(DELTA_TIME.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //canvas.drawBitmap(ship, (width - ship.width) / 2f, 2000f, paint)

        if (galaxian.getShipStatus()) {
            canvas.drawBitmap(ship, null, galaxian.getShipRect(), paint)
        }
        for (e in galaxian.enemyList) {
            if (e.alive) {
                canvas.drawBitmap(enemy, null, e.rect, paint)
            }
        }

        val bullet = galaxian.getBulletCenter()
        if (!galaxian.gameOver()) {
            canvas.drawCircle(bullet!!.x.toFloat(), bullet.y.toFloat(), galaxian.getBulletRadius().toFloat(), paint)
        } else {
            val currScore = galaxian.getCurrScore()
            score = if (currScore > score) currScore else score
            status = galaxian.getStatus()
            paint.textSize = 60f
            canvas.drawText(status, 60f, height/2f, paint)
            if (galaxian.getWinLoss()) { // true if won
                canvas.drawText("Enemies Destroyed: ${currScore}", 60f, height/2f + 90f, paint)
            } else {
                canvas.drawText("# of enemies destroyed: ${currScore}", 60f, height/2f + 90f, paint)
            }
            canvas.drawText("Best Score: ${score}", 60f, height/2f + 180f, paint)
        }
    }

    fun getGalaxian() : Galaxian {
        return galaxian
    }

    companion object {
        val DELTA_TIME : Long = 10L
        val SHIP : Int = R.drawable.ship
        val ENEMY : Int = R.drawable.enemy
    }
}