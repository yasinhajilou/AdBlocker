package com.yasinhajilou.disconnector

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        vpnUtil = VpnUtil(this)

        activityMainBinding.ivSwitch.setOnClickListener {
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

        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent , 0)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            val currentHour = this.get(Calendar.HOUR_OF_DAY)
            val currentMin = this.get(Calendar.MINUTE + 1)
            set(Calendar.HOUR_OF_DAY, currentHour)
            set(Calendar.MINUTE, currentMin)
        }

        activityMainBinding.layoutBottomSheet.switchLighthouse.setOnCheckedChangeListener { _, checked ->
            if (checked)
                alarmManager.setRepeating(
                    AlarmManager.RTC,
                    calendar.timeInMillis,
                    90 * 1000,
                    pendingIntent
                )
            else {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }
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

}