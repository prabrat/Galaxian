package com.example.project4

import android.graphics.Rect
import kotlin.random.Random

class Galaxian {

    private var enemies = 0
    private var enemySize = 35f // random size
    private var enemyRect : Rect? = null
    private var destroyed = 0

    private var shipW = 50f // random height
    private var shipH = 50f // random width
    private var shipX = 0f
    private var shipY = 0f
    private var shipAlive = true

    private var bulletSize = 5
    private var bulletX = 0f
    private var bulletY = 0f
    private var fired = false
    private var bulletSpeed = -5f // neg is going up i guess
    private var bulletRect : Rect? = null


    data class Enemy (
        var x : Float,
        var y : Float,
        var dx : Float = 0f, // Horizonatal speed
        var dy : Float = 0f, // Vertical spped
        var rect : Rect = Rect(), // had to change it from Rect? = null cus was getting an error when doing rect.set 
        var alive : Boolean = true
    )

    val enemyList = mutableListOf<Enemy>()
    constructor (screenW : Float, screenH : Float, initES: Float) { // screenWidth, screenHeight, enemySpeed
        enemies = Random.nextInt(5, 11) // between 5-10 enemies
        var spacing = (screenW / enemies) - enemySize
        for (i in 0..enemies) {
            enemyList.add(Enemy(x = spacing + (i * enemySize), y = 50f)) // y is some random top margin
        }
        setEnemyRect()
        setEnemySpeed(initES)

        setShipCord(screenW, screenH)
        shipAlive = true
        fired = false
    }

    fun setEnemyRect() {
        for (enemy in enemyList) {
            enemy.rect = Rect(
                enemy.x.toInt(),
                enemy.y.toInt(),
                (enemy.x + enemySize).toInt(),
                (enemy.y + enemySize).toInt()
            )
        }
    }

    fun setEnemySpeed(speed : Float) {
        if (speed > 0) { // always going down so speed cant be neg
            for (enemy in enemyList) {
                val direc = if (Random.nextBoolean()) 1 else -1
                enemy.dx = direc * speed
                enemy.dy = speed
            }
        }
    }

    fun setShipCord(w : Float, h : Float) {
        shipX = w / 2
        shipY = h - 50f
    }

    fun updateEnemies(w : Float, h: Float) {
        // did enemy hit wall?
        for (enemy in enemyList) {
            if (enemy.alive) continue // check enemy even alive otw it dont matter
            enemy.x += enemy.dx
            enemy.y += enemy.dy

            if (enemy.x <= 0 || (enemy.x + enemySize) >= w) {
                enemy.dx *= (-1) // flip direction
            }

            if (enemy.y > h) {
                enemy.y = 0f
            }

            enemy.rect.set(
                enemy.x.toInt(),
                enemy.y.toInt(),
                (enemy.x + enemySize).toInt(),
                (enemy.y + enemySize).toInt()
            )
        }
    }


}