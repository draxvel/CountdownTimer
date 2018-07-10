package com.tkachuk.timertutorial.util

import android.content.Context
import android.preference.PreferenceManager
import com.tkachuk.timertutorial.TimerActivity

class PrefUtil {
    companion object {
        fun getTimerLenght(context: Context): Int{
            //placeholder
            return 1
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.tkachuk.timertutorial.util.previous_timer_length"

        fun getPreviousTimerLengthSeconds(context: Context):Long{
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
            return preferenceManager.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        private const val TIMER_STATE_ID = "com.tkachuk.timertutorial.util.timer_state"

        fun getTimerState(context: Context): TimerActivity.TimerState{
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferenceManager.getInt(TIMER_STATE_ID, 0)
            return TimerActivity.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        private const val SECONDS_REMAINING_ID = "com.tkachuk.timertutorial.util.seconds_remaining"

        fun getSecondsRemaining(context: Context):Long{
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
            return preferenceManager.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }
    }
}