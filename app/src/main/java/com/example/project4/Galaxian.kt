package com.example.project4

import android.content.Context
import android.graphics.Rect
import kotlin.random.Random
import android.graphics.Point
import kotlin.math.min
import kotlin.math.max
import kotlin.random.nextInt

class Galaxian (private val context: Context){

    private val enemies = Random.nextInt(5, 11) // 5 - 10 enemies
    private var enemySize = 0f // random size
    private var prevDelay = 0
    private var destroyed = 0
    private var status = ""
    private var gameOver = false
    private var gameWon = false

    private var shipW = 0f // random height
    private var shipH = 0f // random width
    private var shipX = 0f
    private var shipY = 0f
    private var shipAlive = true
    private var shipRect  : Rect = Rect()
    private var fired = false
    private var bulletSpeed = -2 // neg is going up i guess
    private var bulletCenter : Point = Point()
    private var bulletRadius = 10
    private var deltaTime = 0
    private var screenWidth : Float = 0f
    private var screenHeight : Float = 0f


    data class Enemy (
        var x : Float,
        var y : Float,
        var og_x: Float,
        var og_y: Float,
        var dx : Float = 0f, // Horizontal speed
        var dy : Float = 0f, // Vertical speed
        var rect : Rect = Rect(), // had to change it from Rect? = null cus was getting an error when doing rect.set 
        var alive : Boolean = true,
        var falling : Boolean = false,
        var delay : Int = Random.nextInt(0, 100)
    )

    val enemyList = mutableListOf<Enemy>()
    constructor (context : Context, screenW : Float, screenH : Float, initES: Float, shipWidth : Float, shipHeight : Float, eSize : Float) : this(context) {
        // context, screenWidth, screenHeight, enemySpeed, ...

        enemySize = eSize
        shipW = shipWidth
        shipH = shipHeight
        screenWidth = screenW
        screenHeight = screenH
        var spacing = ((screenW) - (enemies * enemySize)) / (enemies + 1)
        for (i in 0 until enemies) {
            var x = (spacing) + (i * (enemySize + spacing))
            var y = 80f
            enemyList.add(Enemy(x = x, y = y, og_x = x, og_y = y)) // y is some random top margin
        }
        setEnemyRect()
        setEnemySpeed(initES)

        setShipCord(screenW, screenH)
        updateShipRect()
        bulletCenter = Point((shipX + (shipW / 2)).toInt(), shipY.toInt())
        updateBullet()
        shipAlive = true
        fired = false
    }

    fun setDelay() {
        for (enemy in enemyList) {
            enemy.delay += prevDelay
            prevDelay += 5
        }
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
        shipX = (w / 2) - (shipW / 2)
        shipY = h - 100f - shipH
    }

    fun setDeltaTime(dt : Int) {
        if (dt > 0) {
            deltaTime = dt
        }
    }

    private fun updateShipRect() {
        shipRect.set(
            shipX.toInt(),
            shipY.toInt(),
            (shipX + shipW).toInt(),
            (shipY + shipH).toInt()
        )
    }

    fun updateShipCord(newX : Float) {
        shipX = max(0f, min(screenWidth - shipW, newX - (shipW / 2)))
        resetBullet()
    }

    private fun updateEnemies(w : Float, h: Float) {
        // did enemy hit wall?
        for (enemy in enemyList) {
            if (!enemy.alive) continue // check enemy even alive otw it dont matter

            if (!enemy.falling) {
                enemy.delay--
                if (enemy.delay <= 0) {
                    enemy.falling = true // starts falling if delay runs out
                } else {
                    continue
                }
            }

            enemy.x += (enemy.dx * deltaTime)
            enemy.y += (enemy.dy * deltaTime)

            if (enemy.x <= 0 || (enemy.x + enemySize) >= w) {
                enemy.dx *= (-1) // flip direction
            }

            if (enemy.y > h) {
                enemy.y = enemy.og_y
                enemy.x = enemy.og_x
            }

            enemy.rect.set(
                enemy.x.toInt(),
                enemy.y.toInt(),
                (enemy.x + enemySize).toInt(),
                (enemy.y + enemySize).toInt()
            )
        }
    }

    fun getShipRect() : Rect {
        return shipRect
    }

    fun getShipStatus() : Boolean {
        return shipAlive
    }

    private fun resetBullet() {
        bulletCenter.x = (shipX + (shipW / 2)).toInt()
        bulletCenter.y = shipY.toInt()
    }
    fun fireBullet() {
        if (!fired) {
            fired = true
            resetBullet()
        }
    }

    private fun updateBullet() {
        if (!fired) return
        bulletCenter.y += (bulletSpeed * deltaTime)

        if ((bulletCenter.y - bulletRadius) <= 0) {
            fired = false
            resetBullet()
        }
    }

    private fun checkBulletHits() {
        for (enemy in enemyList) {
            if (enemy.alive && enemy.rect.intersects(
                bulletCenter.x - bulletRadius, bulletCenter.y - bulletRadius,
                bulletCenter.x + bulletRadius, bulletCenter.y + bulletRadius
            )) {
                enemy.alive = false
                destroyed++
                fired = false
                resetBullet()
            }
        }
    }

    private fun checkShipCollision() {
        if (!shipAlive) return
        for (enemy in enemyList) {
            if (enemy.alive && Rect.intersects(enemy.rect, shipRect)) {
                shipAlive = false
                enemy.alive = false
                status = "YOU LOST !!"
                gameOver = true
                saveScore()
            }
        }
    }

    private fun checkWin() {
        for (enemy in enemyList) {
            if (enemy.alive) {
                return // clearly not won if an enemy is still aliv
            }
        }
        status = "YOU WON !!"
        gameOver = true
        gameWon = true
        saveScore()
    }

    fun update(w: Float, h: Float) {
        updateEnemies(w, h)
        updateBullet()
        updateShipRect()
        checkBulletHits()
        checkShipCollision()
        checkWin()
    }

    private fun saveScore() {
        val sp = context.getSharedPreferences("galaxian", Context.MODE_PRIVATE)
        val best = sp.getInt("bestScore", 0)
        if (destroyed > best) {
            sp.edit().putInt("bestScore", destroyed).apply()
        }
    }

    fun getCurrScore() : Int {
        return destroyed
    }

    fun getStatus() : String {
        return status
    }

    fun getBulletRadius() : Int {
        return bulletRadius
    }

    fun getBulletCenter() : Point? {
        return bulletCenter
    }

    fun gameOver() : Boolean {
        return gameOver
    }

    fun getWinLoss() : Boolean {
        return gameWon
    }
}