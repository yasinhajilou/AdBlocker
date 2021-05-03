package com.yasinhajilou.disconnector

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yasinhajilou.disconnector.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private var vpnConnection = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        vpnConnection = VpnUtil.isVpnConnected(this)

        if (vpnConnection) {
            activityMainBinding.tvVpnStatus.text = getString(R.string.vpn_on)
            activityMainBinding.rippleAnimation.playAnimation()
        } else {
            activityMainBinding.tvVpnStatus.text = getString(R.string.vpn_off)
            finishAnimation()
        }

        activityMainBinding.ivSwitch.setOnClickListener {
            vpnConnection = VpnUtil.isVpnConnected(this)
            if (vpnConnection) {
                VpnUtil.disconnectVpn(this)
                finishAnimation()
                showToast("disconnected")
            } else {
                showToast("Vpn is not connected!")
            }
        }
    }

    private fun finishAnimation() {
        activityMainBinding.rippleAnimation.progress = 0f
        activityMainBinding.rippleAnimation.pauseAnimation()
    }

    private fun showToast(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}