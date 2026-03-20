package com.example.project4

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import androidx.core.graphics.scale

class GameView : View {
    private var ship : Bitmap
    private var enemy : Bitmap
    private var paint : Paint
    private var galaxian: Galaxian
    private var width: Int = 0
    private var height: Int = 0

    constructor(context: Context, width: Int, height : Int): super(context){
        paint = Paint()
        paint.strokeWidth = 20f
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        this.width = width
        this.height = height
        val w = 250
        val h = 250
        ship = BitmapFactory.decodeResource(resources, SHIP)
        ship = ship.scale(w, h)
        enemy = BitmapFactory.decodeResource(resources, ENEMY)
        enemy = enemy.scale(125, 125)
        galaxian = Galaxian(context)
        galaxian  = Galaxian(context, width.toFloat(), height.toFloat(), height * .0003f,
            ship.width.toFloat(), ship.height.toFloat(), enemy.height.toFloat())
        galaxian.setDeltaTime(DELTA_TIME.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //canvas.drawBitmap(ship, (width - ship.width) / 2f, 2000f, paint)

        canvas.drawBitmap(ship, null, galaxian.getShipRect(), paint)
        for (e in galaxian.enemyList) {
            canvas.drawBitmap(enemy, null, e.rect, paint)
        }

        val bullet = galaxian.getBulletCenter()
        canvas.drawCircle(bullet!!.x.toFloat(), bullet.y.toFloat(), galaxian.getBulletRadius().toFloat(), paint)
    }

    fun getGalaxian() : Galaxian {
        return galaxian
    }

    companion object {
        val DELTA_TIME : Long = 100L
        val SHIP : Int = R.drawable.ship
        val ENEMY : Int = R.drawable.enemy
    }
}