package com.example.duckhuntingv3

import com.example.project4.MainActivity
import java.util.TimerTask

class GameTimerTask : TimerTask {
    private var activity : MainActivity
    constructor( activity : MainActivity ) {
        this.activity = activity
    }

    override fun run() {
        // update model
        activity.updateModel()
        // update view
        activity.updateView()
    }
}