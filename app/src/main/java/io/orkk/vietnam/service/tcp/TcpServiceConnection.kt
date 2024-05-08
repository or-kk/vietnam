package io.orkk.vietnam.service.tcp

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import timber.log.Timber

class TcpServiceConnection : ServiceConnection {
    var tcpService: TcpService? = null
    private var isBound: Boolean = false
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as TcpService.TcpServiceBinder
        tcpService = binder.service
        isBound = true
        Timber.i("TcpServiceConnection -> onServiceConnected")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isBound = false
        Timber.i("TcpServiceConnection -> onServiceDisconnected")
    }
}