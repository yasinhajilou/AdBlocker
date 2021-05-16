package com.yasinhajilou.disconnector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val data = intent.getIntExtra("NotificationButton", 0)

        val vpnUtil = VpnUtil(context)
        val notifUtil = NotificationUtil(context)

        //data = 0, app should show notification
        if (data == 0) {
            if (vpnUtil.isVpnConnected())
                notifUtil.showNotification()
        } else {
            //Notification button clicked
            vpnUtil.disconnectVpn()
            notifUtil.removeNotification()
        }
    }
}