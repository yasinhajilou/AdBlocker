package com.yasinhajilou.disconnector

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yasinhajilou.disconnector.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var vpnConnection = false
    private val vpnIntentCode = 101
    private lateinit var vpnUtil: VpnUtil
    private lateinit var sharedPreferenceUtil: SharedPreferenceUtil
    private lateinit var alarmManager: AlarmManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setUpSwitchView()

        vpnUtil = VpnUtil(this)

        sharedPreferenceUtil = SharedPreferenceUtil(this)

        val notifUtil = NotificationUtil(this)
        notifUtil.createNotificationChannel()

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        activityMainBinding.ivVpnPower.setOnClickListener {
            vpnConnection = vpnUtil.isVpnConnected()
            if (vpnConnection) {
                val intent = vpnUtil.disconnectVpn()
                if (intent != null)
                    startActivityForResult(intent, vpnIntentCode)
                else
                    onActivityResult(vpnIntentCode, RESULT_OK, null)
            } else {
                showToast("Vpn is not connected!")
            }
        }


        activityMainBinding.layoutBottomSheet.switchLighthouse.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setAlarm()
            } else {
                cancelAlarm()
            }

            sharedPreferenceUtil.editAlarmStatus(checked)
        }
    }

    fun setUpSwitchView() {
        val state = sharedPreferenceUtil.getAlarmStatus()
        activityMainBinding.layoutBottomSheet.switchLighthouse.isChecked = state
    }

    fun cancelAlarm() {
        alarmManager.cancel(getPendingIntent())
        getPendingIntent().cancel()
    }

    fun setAlarm() {
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            getPendingIntent()
        )
    }

    override fun onResume() {
        super.onResume()
        handleAnimationView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            viewVpnOff()
            showToast("disconnected")
        }
    }

    private fun finishAnimation() {
        activityMainBinding.rippleAnimation.progress = 0f
        activityMainBinding.rippleAnimation.pauseAnimation()
    }

    private fun handleAnimationView() {
        vpnConnection = vpnUtil.isVpnConnected()
        if (vpnConnection) {
            viewVpnOn()
        } else {
            viewVpnOff()
        }
    }

    private fun viewVpnOff() {
        activityMainBinding.tvVpnStatus.text = getString(R.string.vpn_off)
        finishAnimation()
    }

    private fun viewVpnOn() {
        activityMainBinding.tvVpnStatus.text = getString(R.string.vpn_on)
        activityMainBinding.rippleAnimation.playAnimation()
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(this, 0, intent, 0)
    }
}