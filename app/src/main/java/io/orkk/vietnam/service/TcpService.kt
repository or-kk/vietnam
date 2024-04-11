package io.orkk.vietnam.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import es.dmoral.toasty.Toasty
import io.orkk.vietnam.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.DataInputStream
import java.io.DataOutputStream
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket

class TcpService : LifecycleService(), Runnable {

    private val tcpServiceBinder: IBinder = TcpServiceBinder()

    private val connectJob = SupervisorJob()
    private val connectScope = CoroutineScope(Dispatchers.IO + connectJob)
    private lateinit var socket: Socket
    private lateinit var sendSocket: DataOutputStream
    private lateinit var receiveSocket: DataInputStream

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectJob.cancel()
        this@TcpService.stopSelf()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return tcpServiceBinder
    }

    inner class TcpServiceBinder : Binder() {
        val service: TcpService
            get() = this@TcpService
    }


    override fun run() {
        TODO("Not yet implemented")
    }

    inner class ConnectThread : Thread() {
        override fun run() {
            super.run()

            try {
                socketConnect("202.68.227.57", 20406)
            } catch (e: Exception) {
                Timber.e("ConnectThread exception -> ${e.message}")
            }
        }
    }

    private fun socketConnect(ip: String, port: Int) {
        Timber.i("TcpService Socket info IP -> $ip  PORT -> $port")
        connectScope.launch {
            InetSocketAddress(ip, port).let {
                socket = Socket().apply {
                    soTimeout = SOCKET_TIMEOUT
                    connect(it, SOCKET_TIMEOUT)
                    sendSocket = DataOutputStream(this.getOutputStream())
                    receiveSocket = DataInputStream(this.getInputStream())
                }
            }
        }
        Toasty.info(applicationContext, R.string.tcp_connect_socket_success, Toast.LENGTH_SHORT, false).show()
    }


    companion object {
        private const val SOCKET_TIMEOUT = 30000
        private const val SEND_PACKET_TIME = 500
        private const val RECONNECT_TIME = 5000L
        private const val SEND_PING_PACKET_TIME = 10000
        private const val CONNECT_SOCKET_DELAY_TIME = 1000L
        private const val MESSAGE_COMM_RECONNECT = 101
        private const val MESSAGE_COMM_ERROR_CHECK = 102
        private const val CHECK_RECEIVE_TIME = 120000
    }
}