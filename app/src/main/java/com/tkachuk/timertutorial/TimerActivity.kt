package com.tkachuk.timertutorial

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.tkachuk.timertutorial.util.PrefUtil

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.util.*
import kotlin.concurrent.timer

class TimerActivity : AppCompatActivity() {

    enum class TimerState{
        STOP,
        PAUSE,
        RUNNING
    }

    private lateinit var timer: CountDownTimer
    private var timerLenghtSeconds: Long = 0
    private var timerState = TimerState.STOP

    private var secondRemarning = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "     Timer"

        fab_play.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.RUNNING
            updateButtons()
        }

        fab_pause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.PAUSE
            updateButtons()
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel()
            onTimerFinish()
        }
    }


    override fun onResume() {
        super.onResume()

        initTimer()

        //TODO: remove background timer, hide notification
    }

    override fun onPause() {
        super.onPause()
        if(timerState == TimerState.RUNNING){
            timer.cancel()
            //TODO: start background timer, show notification
        }
        else if(timerState == TimerState.PAUSE){
            //TODO: show notification
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLenghtSeconds, this)
        PrefUtil.setSecondsRemaining(secondRemarning, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)

        if(timerState == TimerState.STOP){
            setNewTimerLength()
        }else{
            setPreviousTimerLength()
        }

        secondRemarning = if(timerState == TimerState.RUNNING || timerState == TimerState.PAUSE)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLenghtSeconds

        //TODO; change secondsRemainig according to where the backround timer stopped

        //resume where we left off
        if(timerState == TimerState.RUNNING)
            startTimer()

        updateButtons()
        updateCountDownUI()
    }

    private fun onTimerFinish(){
        timerState = TimerState.STOP

        setNewTimerLength()

        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLenghtSeconds, this)
        secondRemarning = timerLenghtSeconds

        updateButtons()
        updateCountDownUI()
    }

    private fun startTimer(){
        timerState = TimerState.RUNNING

        timer = object : CountDownTimer(secondRemarning*1000, 1000){
            override fun onFinish() = onTimerFinish()

            override fun onTick(millisUntilFinished: Long) {
                secondRemarning = millisUntilFinished/1000
                updateCountDownUI()
            }
        }.start()
    }

    private fun setPreviousTimerLength(){
        timerLenghtSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_countdown.max = timerLenghtSeconds.toInt()
    }

    private fun setNewTimerLength(){
        val lengthInMInutes = PrefUtil.getTimerLenght(this)
        timerLenghtSeconds = (lengthInMInutes*60L)
        progress_countdown.max = timerLenghtSeconds.toInt()
    }

    private fun updateCountDownUI(){
        val minutesUntilFinished = secondRemarning/ 60
        val secondInMinuteUntilFinished = secondRemarning - minutesUntilFinished*60
        val secondsStr = secondInMinuteUntilFinished.toString()
        textView.text = "$minutesUntilFinished:${
        if(secondsStr.length == 2) secondsStr
        else "0"+ secondsStr}"
        progress_countdown.progress = (timerLenghtSeconds - secondRemarning).toInt()
    }

    private fun updateButtons(){
        when (timerState){
            TimerState.RUNNING -> {
                fab_play.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.STOP ->{
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.PAUSE->{
                fab_play.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
