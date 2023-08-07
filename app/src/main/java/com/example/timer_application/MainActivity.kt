package com.example.timer_application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.timer_application.databinding.ActivityMainBinding
import kotlin.math.roundToInt
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timerStarted = false
    private lateinit var serviceIntent: Intent //A variable of type intent
    private var time = 0.0
    private lateinit var view: View
    var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //I believe this is how we access the views

        binding.button3.setOnClickListener {
            clicked = true
            startStopTimer()}
        serviceIntent = Intent(applicationContext, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.Timer_UPDATED))
    }



    override fun onPause() {
        stopService(serviceIntent)
        super.onPause()
    }
    override fun onResume() {

        if (!clicked)
        {
            onPause()
        }
        else
        {
                serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
                startService(serviceIntent)

        }
        super.onResume()

    }
    private fun startStopTimer() {
        if(timerStarted)
        {
            stopTimer()
           //binding.relativelayout.visibility = View.VISIBLE
        }
        else
        {
            startTimer( )
            //binding.relativelayout.visibility = View.GONE
        }
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.button3.text = "start"
        //binding.button3.jcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timerStarted = false
    }

    private fun startTimer() {
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceIntent)
        binding.button3.text = "stop"
        //binding.button3.jcon = getDrawable(R.drawable.ic_baseline_pause_24)
        timerStarted = true
    }

    private  val updateTime: BroadcastReceiver = object : BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA,0.0)
            binding.textView2.text = getTimeStringFromDouble(time) //binding the time with the text
        }
    }
    private fun getTimeStringFromDouble(time: Double): String{
        val result= time.roundToInt()
        val hours = result % 86400 / 3600
        val minutes = result % 86400 % 3600 / 60
        val seconds = result % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, min: Int, sec: Int): String =  String.format("%02d:%02d:%02d", hours, min, sec) //return time in the right format
}



