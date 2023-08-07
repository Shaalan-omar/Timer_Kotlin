package com.example.timer_application

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

class TimerService: Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    private val timer = Timer()


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(TIME_EXTRA, 0.0) //As we need to pass the timee variable from the main activity to our service
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy() //we want to stop the timer  when our service is destroyed
    }
    private inner class TimeTask(private var time: Double): TimerTask()
    {
        override fun run()
        {
            val intent = Intent(Timer_UPDATED)
            time++
            intent.putExtra(TIME_EXTRA, time)
            sendBroadcast(intent)
        }
    }
    companion object
    {
        const val Timer_UPDATED = "timerUpdated"
        const val TIME_EXTRA = "timeExtra"
    }

}