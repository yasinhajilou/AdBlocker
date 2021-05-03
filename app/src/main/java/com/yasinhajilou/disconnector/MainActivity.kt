package com.yasinhajilou.disconnector

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import com.yasinhajilou.disconnector.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding
    var vpnConnection = false
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
            finishAnimation()
        }
    }

    fun finishAnimation() {
        activityMainBinding.rippleAnimation.progress = 0f
        activityMainBinding.rippleAnimation.pauseAnimation()
    }
}