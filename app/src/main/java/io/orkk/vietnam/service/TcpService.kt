package io.orkk.vietnam.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import es.dmoral.toasty.Toasty
import io.orkk.vietnam.R
import io.orkk.vietnam.model.tcpip.ReceivePacket
import io.orkk.vietnam.utils.packet.ConvertReceivePacketToData
import io.orkk.vietnam.utils.packet.PacketManager
import io.orkk.vietnam.model.tcpip.RXPackets
import io.orkk.vietnam.utils.packet.RequestPacket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.Exception

class TcpService : LifecycleService() {

    private val tcpServiceBinder: IBinder = TcpServiceBinder()
    private val connectJob = SupervisorJob()
    private val connectScope = CoroutineScope(Dispatchers.IO + connectJob)
    private lateinit var socket: Socket
    private lateinit var sendSocket: DataOutputStream
    private lateinit var receiveSocket: DataInputStream
    private lateinit var sendPacketQueue: SendPacketQueue
    private lateinit var convertReceivePacketToData: ConvertReceivePacketToData

    private var sendOffsetTime: Long = 0
    private var pingTime: Long = System.currentTimeMillis()
    private var isReceivePacket: Boolean = false

    private var isConnect: Boolean = false
    private var isConnectError: Boolean = false

    override fun onCreate() {
        super.onCreate()

        sendPacketQueue = SendPacketQueue()

        ConnectThread().apply {
            start()
        }
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

    inner class ConnectThread : Thread() {
        override fun run() {
            super.run()
            try {
                socketConnect("202.68.227.57", 20406)
                receiveThreadStart()

                isConnect = true
            } catch (e: Exception) {
                Timber.e("ConnectThread -> exception -> ${e.message}")
            }
        }
    }

    private fun receiveThreadStart() {
        lifecycleScope.launch(Dispatchers.IO) {
            ReceiveThread().start()
        }
    }

    inner class ReceiveThread : Thread() {
        override fun run() {
            super.run()
            Timber.i("ReceiveThread -> Start -> Thread id : ${this.id}")

            while (!this.isInterrupted) {
                try {
                    if (isConnect) {

                        try {
                            var receiveCommand: Int = 0
                            var receiveLength: Int = 0
                            var readAvailableByte = receiveSocket.available()
                            sendOffsetTime = System.currentTimeMillis()

                            while (readAvailableByte > 0 || isReceivePacket) {

                                if (sendOffsetTime != 0L && (System.currentTimeMillis() - sendOffsetTime) > SEND_PACKET_TIME) {
                                    sendPacket()
                                }

                                if (!isReceivePacket) {
                                    if (readAvailableByte >= 6) {
                                        receiveCommand = receiveSocket.readInt()
                                        receiveLength = receiveSocket.readShort().toInt()
                                        isReceivePacket = true
                                    }
                                } else {
                                    if (receiveSocket.available() >= receiveLength) {
                                        if (receiveLength <= 0) {
                                            convertReceivePacketToData.convertPacket(ReceivePacket(receiveCommand, receiveLength.toShort(), null))
                                        } else {
                                            val dataRxBuffer = ByteArray(receiveLength)
                                            receiveSocket.read(dataRxBuffer, 0, receiveLength)
                                            convertReceivePacketToData.convertPacket(ReceivePacket(receiveCommand, receiveLength.toShort(), dataRxBuffer))
                                        }
                                    }
                                }

                                readAvailableByte = receiveSocket.available()

                                Thread.sleep(10)
                            }
                        } catch (e: IOException) {
                            Timber.e("ReceiveThread -> IOException -> ${e.message}")
                        }

                        if (sendOffsetTime != 0L && (System.currentTimeMillis() - sendOffsetTime) > SEND_PACKET_TIME) {
                            sendPacket()
                        }

                        Thread.sleep(200)
                    }
                } catch (e: Exception) {
                    Timber.e("ReceiveThread -> Exception -> ${e.message}")
                    isConnectError = true
                } catch (e: InterruptedException) {
                    Timber.e("ReceiveThread -> Exception -> ${e.message}")
                    isConnectError = true
                }
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

        lifecycleScope.launch(Dispatchers.Main) {
            Toasty.info(applicationContext, R.string.tcp_connect_socket_success, Toast.LENGTH_SHORT, false).show()
        }
    }

    private fun sendPacket() {
        if (sendPacketQueue.check()) {
            sendPacketQueue.deQueue().also { command ->
                if (command != 0) sendData(command, sendPacketQueue.packetData)
            }
        } else {
            if (pingTime != 0L && (System.currentTimeMillis() - pingTime) > SEND_PING_PACKET_TIME) {

                PacketManager.lossPackets.size.let { lossPacketCount ->

                    with(RequestPacket) {
                        if (lossPacketCount > 0) {
                            indexOfPacketStart = PacketManager.lossPackets[0]
                            indexOfPacketEnd = PacketManager.lossPackets[lossPacketCount - 1]
                        } else {
                            indexOfPacketStart = PacketManager.indexPacketReceive
                            indexOfPacketEnd = 0
                        }
                    }
                }

                if (PacketManager.indexPacketReceive >= 20) {
                    with(RequestPacket) {
                        indexOfPacketStart = 12
                        indexOfPacketEnd = 18
                    }
                }

                Timber.e(">>> PING >> COMMAND_REQ_PACKET >> indexOfPacketStart : ${RequestPacket.indexOfPacketStart}, indexOfPacketEnd : ${RequestPacket.indexOfPacketEnd}"
                )

                PacketManager.makePacket(RXPackets.COMMAND_REQ_PACKET, RequestPacket).apply {
                    sendPacketQueue.enQueue(RXPackets.COMMAND_REQ_PACKET, this)
                }

                pingTime = System.currentTimeMillis()
            }
        }
    }

    private fun sendData(command: Int, obj: Any) {
        try {
            if (command != 0 && obj != null && sendSocket != null) {
                val sendPacket: SendPacket = obj as SendPacket

                sendSocket.write(sendPacket.sendPacket)
                sendSocket.flush()
            } else {
                Timber.d("sendData -> sendData -> Command is null")
            }
        } catch (e: Exception) {
            Timber.e("sendData -> sendData -> Exception -> ${e.message}")
        }
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