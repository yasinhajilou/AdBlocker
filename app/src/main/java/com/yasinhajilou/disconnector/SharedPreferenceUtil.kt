package com.yasinhajilou.disconnector

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceUtil(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        "AppSP",
        AppCompatActivity.MODE_PRIVATE
    )
    private lateinit var editor: SharedPreferences.Editor
    private val KEY_SP_ALARM = "AlarmSwitch"
    fun editAlarmStatus(enabled: Boolean) {
        editor = sharedPreferences.edit()
        editor.putBoolean(KEY_SP_ALARM , enabled)
        editor.apply()
    }

    fun getAlarmStatus() =
        sharedPreferences.getBoolean(KEY_SP_ALARM , false)

}