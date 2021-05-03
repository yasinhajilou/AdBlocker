package com.yasinhajilou.disconnector

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.VpnService
import androidx.appcompat.app.AppCompatActivity

class VpnUtil {
    companion object {
        fun disconnectVpn(applicationContext: Context) {
            val intent = VpnService.prepare(applicationContext)
        }
        fun isVpnConnected(context: Context): Boolean {
            val cm: ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetwork
            val caps: NetworkCapabilities? = cm.getNetworkCapabilities(activeNetwork)
            return caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) == true
        }
    }
}