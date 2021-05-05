package com.yasinhajilou.disconnector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context , "Called" , Toast.LENGTH_LONG).show()
    }
}