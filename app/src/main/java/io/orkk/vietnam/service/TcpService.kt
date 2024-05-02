package io.orkk.vietnam.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.orkk.vietnam.R
import io.orkk.vietnam.model.tcpip.ReceivePacket
import io.orkk.vietnam.utils.packet.ConvertReceivePacketToData
import io.orkk.vietnam.utils.packet.PacketFactory
import io.orkk.vietnam.model.tcpip.RXPackets
import io.orkk.vietnam.model.tcpip.RequestPacket
import io.orkk.vietnam.model.tcpip.TXPackets.Companion.getTransmitCommandNameByHexadecimal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import kotlin.Exception

@AndroidEntryPoint
class TcpService : LifecycleService() {

    private val tcpServiceBinder: IBinder = TcpServiceBinder()
    private val connectJob = SupervisorJob()
    private val connectScope = CoroutineScope(Dispatchers.IO + connectJob)
    private lateinit var socket: Socket
    private lateinit var sendSocket: DataOutputStream
    private lateinit var receiveSocket: DataInputStream
    @Inject lateinit var sendPacketQueue: SendPacketQueue
    private lateinit var convertReceivePacketToData: ConvertReceivePacketToData
    private lateinit var connectCheckHandler: Handler
    private lateinit var connectSocketThread: ConnectSocketThread
    private lateinit var disconnectSocketThread: DisconnectSocketThread
    private lateinit var reConnectSocketThread: ReConnectSocketThread
    private lateinit var receiveSocketThread: ReceiveSocketThread
    private var sendOffsetTime: Long = 0
    private var pingTime: Long = System.currentTimeMillis()
    private var isConnectSocket: Boolean = false
    private var isConnectSocketError: Boolean = false
    private var isReceivePacket: Boolean = false
    private var isReceiveSocketError: Boolean = false
    private var isTryRestartConnectSocket: Boolean = true

    private var isReconnect: Boolean = false

    override fun onCreate() {
        super.onCreate()
        initTcpService()
        startConnectSocketThread()
    }

    override fun onDestroy() {
        super.onDestroy()
        connectJob.cancel()
        interruptConnectSocketThread()
        this@TcpService.stopSelf()
        startDisConnectSocketThread()
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return tcpServiceBinder
    }

    inner class TcpServiceBinder : Binder() {
        val service: TcpService
            get() = this@TcpService
    }

    private fun initTcpService() {
        SendPacketQueue.initQueue()
        convertReceivePacketToData = ConvertReceivePacketToData(sendPacketQueue)
        connectCheckHandler = ConnectCheckHandler()
        connectSocketThread = ConnectSocketThread()
        reConnectSocketThread = ReConnectSocketThread()
        disconnectSocketThread = DisconnectSocketThread()
        receiveSocketThread = ReceiveSocketThread()
    }

    private fun startConnectSocketThread() {
        connectSocketThread.start()
    }

    private fun interruptConnectSocketThread() {
        try {
            if (connectSocketThread.isAlive) {
                connectSocketThread.interrupt()
                Timber.i("interruptConnectSocketThread -> interrupt")
            }
        } catch (e: Exception) {
            Timber.e("interruptConnectSocketThread -> Exception -> ${e.message}")
        }
    }

    private fun startDisConnectSocketThread() {
        disconnectSocketThread.start()
    }

    private fun startReconnectSocketThread() {
        reConnectSocketThread.start()
    }

    private fun interruptReconnectSocketThread() {
        try {
            if (reConnectSocketThread.isAlive) {
                reConnectSocketThread.interrupt()
                Timber.i("interruptReconnectSocketThread -> interrupt")
            }
        } catch (e: Exception) {
            Timber.e("interruptReconnectSocketThread -> Exception -> ${e.message}")
        }
    }

    private fun startReceiveThreadStart() {
        lifecycleScope.launch(Dispatchers.Main) {
            Toasty.info(applicationContext, R.string.tcp_connect_socket_success, Toast.LENGTH_SHORT, false).show()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            receiveSocketThread.start()
        }

        // for sign in test
//        lifecycleScope.launch(Dispatchers.IO) {
//            delay(1000L)
//            PacketFactory.makePacket(command = TXPackets.COMMAND_SIGN_IN, obj = SignInItem(0.0, 4, 4, 0.toByte(), "T1-E2-S3-T4-P5-C6")).apply {
//                sendPacketQueue.enQueue(TXPackets.COMMAND_SIGN_IN, this)
//            }
//        }
    }

    private fun restartSocketThread(message: String) {
        Timber.d("restartSocketThread -> message -> $message")
        sendOffsetTime = 0
        connectCheckHandler.apply {
            if (!this.hasMessages(HANDLER_MESSAGE_TRY_RECONNECT) && isTryRestartConnectSocket) {
                this.sendEmptyMessageDelayed(HANDLER_MESSAGE_TRY_RECONNECT, RECONNECT_TIME)
                isConnectSocket = false
                isTryRestartConnectSocket = false
            }
        }
    }

    private fun connectSocket(ip: String, port: Int) {
        Timber.i("TcpService connectSocket info -> IP : $ip PORT : $port")
        connectScope.launch {
            InetSocketAddress(ip, port).let {
                try {
                    socket = Socket().apply {
                        soTimeout = SOCKET_TIMEOUT
                        connect(it, SOCKET_TIMEOUT)
                        sendSocket = DataOutputStream(this.getOutputStream())
                        receiveSocket = DataInputStream(this.getInputStream())
                    }
                } catch (e: ConnectException) {
                    Timber.e("connectSocket -> Exception -> Network is unreachable")
                    isConnectSocketError = true
//                    initSocket()
                }
            }
        }
    }

    private fun initSocket() {
        connectScope.launch {
            if (!isReconnect && ::sendSocket.isInitialized && ::receiveSocket.isInitialized && ::socket.isInitialized) {
                isReconnect = true
                try {
                    sendSocket.close()
                    receiveSocket.close()
                    socket.close()
                } catch (e: IOException) {
                    Timber.e("InitSocketThread -> IOException -> ${e.message}")
                }
                restartSocketThread("Reconnect Finish")
            } else {
                restartSocketThread("Socket is not connected")
            }
        }
    }

    inner class ConnectSocketThread : Thread() {
        override fun run() {
            super.run()
            try {
                connectSocket("202.68.227.57", 20406)

                runBlocking {
                    connectScope.launch {
                        while (!isConnectSocket) {
                            if (::socket.isInitialized) {
                                Timber.i("ConnectSocketThread -> socket isInitialized")
                                if (socket.isConnected) {
                                    Timber.i("ConnectSocketThread -> socket isConnected")
                                    isConnectSocket = true
                                    isConnectSocketError = false
                                    startReceiveThreadStart()
                                }
                            }
                        }
                        sendOffsetTime = System.currentTimeMillis()
                        delay(CONNECT_SOCKET_DELAY_TIME)
                    }
                }

                if (!connectCheckHandler.hasMessages(HANDLER_MESSAGE_CONNECT_ERROR_CHECK)) {
                    connectCheckHandler.sendEmptyMessage(HANDLER_MESSAGE_CONNECT_ERROR_CHECK)
                }
            } catch (e: Exception) {
                Timber.e("ConnectSocketThread -> exception -> ${e.message}")
            }
        }
    }

    inner class DisconnectSocketThread : Thread() {
        override fun run() {
            super.run()
            if (socket.isConnected) {
                try {
                    sendSocket.close()
                    receiveSocket.close()
                } catch (e: IOException) {
                    Timber.e("DisconnectSocketThread -> IOException -> ${e.message}")
                }
                socket.close()
                Timber.e("DisconnectSocketThread -> disconnect")
                if (connectCheckHandler.hasMessages(HANDLER_MESSAGE_CONNECT_ERROR_CHECK)) {
                    connectCheckHandler.removeMessages(HANDLER_MESSAGE_CONNECT_ERROR_CHECK)
                }
            }
        }
    }

    inner class ReConnectSocketThread : Thread() {
        override fun run() {
            super.run()
            Timber.i("ReConnectSocketThread -> Start -> Thread id : ${this.id} -> isReconnect : $isReconnect -> isConnectSocket : $isConnectSocket")
            try {

                while (!isConnectSocket) {
                    connectScope.launch {
                        runBlocking {
                            connectSocket("202.68.227.57", 20406)

                            if (::socket.isInitialized) {
                                Timber.i("ReConnectSocketThread -> socket isInitialized")
                                if (socket.isConnected) {
                                    Timber.i("ReConnectSocketThread -> socket isConnected")
                                    isConnectSocket = true
                                    isReconnect = false
                                    isConnectSocketError = false
//                                startReceiveThreadStart()
                                }
                            }
                            delay(CONNECT_SOCKET_DELAY_TIME)
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e("ReConnectSocketThread -> exception -> ${e.message}")
                isReconnect = false
            }
        }
    }

    @SuppressLint("HandlerLeak")
    inner class ConnectCheckHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            super.handleMessage(message)

            when (message.what) {
                HANDLER_MESSAGE_TRY_RECONNECT -> {
                    Timber.d("ConnectCheckHandler -> Message -> HANDLER_MESSAGE_TRY_RECONNECT")
                    Toasty.info(applicationContext, R.string.tcp_connect_socket_connect_fail_to_retry, Toast.LENGTH_SHORT, false).show()
                    startReconnectSocketThread()
                }

                HANDLER_MESSAGE_CONNECT_ERROR_CHECK -> {
                    if (convertReceivePacketToData.receiveTime != 0L && (System.currentTimeMillis() - convertReceivePacketToData.receiveTime > CHECK_RECEIVE_TIME)
                        || isConnectSocketError
                    ) {
                        convertReceivePacketToData.receiveTime = System.currentTimeMillis()
                        sendOffsetTime = 0
                        isConnectSocketError = false
                        initSocket()
                    }
                    this.sendEmptyMessageDelayed(message.what, ERROR_CHECK_TIME)
                }
            }
        }
    }

    inner class ReceiveSocketThread : Thread() {
        override fun run() {
            super.run()
            Timber.i("ReceiveSocketThread -> Start -> Thread id : ${this.id}")

            while (!this.isInterrupted) {
                try {
                    if (isConnectSocket) {
                        try {
                            var receiveCommand = 0
                            var receiveLength = 0
                            var readAvailableByte = receiveSocket.available()

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
                                        isReceivePacket = false
                                    }
                                }

                                readAvailableByte = receiveSocket.available()

                                sleep(10)
                            }
                        } catch (e: IOException) {
//                            Timber.e("ReceiveSocketThread -> IOException -> ${e.message}")
                        }

                        if (sendOffsetTime != 0L && (System.currentTimeMillis() - sendOffsetTime) > SEND_PACKET_TIME) {
                            sendPacket()
                        }

                        sleep(200)
                    }
                } catch (e: Exception) {
                    Timber.e("ReceiveSocketThread -> Exception -> ${e.message}")
                    isReceiveSocketError = true
                } catch (e: InterruptedException) {
                    Timber.e("ReceiveSocketThread -> InterruptedException -> ${e.message}")
                    isReceiveSocketError = true
                }
            }
        }
    }

    private fun sendPacket() {
        if (SendPacketQueue.check()) {
            SendPacketQueue.deQueue().also { command ->
                Timber.e("sendPacket deQueue -> ${getTransmitCommandNameByHexadecimal(command)}")
                if (command != 0) writeSendSocket(command, SendPacketQueue.packetData)
            }
        } else {
            if (pingTime != 0L && (System.currentTimeMillis() - pingTime) > SEND_PING_PACKET_TIME) {

                PacketFactory.lossPackets.size.let { lossPacketCount ->
                    with(PacketFactory) {
                        if (lossPacketCount > 0) {
                            packetStartIndex = lossPackets[0]
                            packetEndIndex = lossPackets[lossPacketCount - 1]
                        } else {
                            packetStartIndex = indexPacketReceive
                            packetEndIndex = 0
                        }
                    }
                }

                if (PacketFactory.indexPacketReceive >= PACKET_RECEIVE_INDEX_BASEMENT) {
                    with(PacketFactory) {
                        packetStartIndex = REQ_PACKET_START_INDEX
                        packetEndIndex = REQ_PACKET_END_INDEX
                    }
                }

                Timber.d("sendPacket -> COMMAND_REQUEST_PACKET -> indexOfPacketStart : ${PacketFactory.packetStartIndex} indexOfPacketEnd : ${PacketFactory.packetEndIndex}")
                PacketFactory.makePacket(RXPackets.RECEIVE_COMMAND_REQUEST_PACKET, RequestPacket(PacketFactory.packetStartIndex, PacketFactory.packetEndIndex)).apply {
                    SendPacketQueue.enQueue(RXPackets.RECEIVE_COMMAND_REQUEST_PACKET, this)
                }

                pingTime = System.currentTimeMillis()
            }
        }
    }

    private fun writeSendSocket(command: Int, obj: Any) {
        try {
            if (command != 0) {
                Timber.i("sendSocketWrite -> command -> $command")
                val sendPacket: SendPacket = obj as SendPacket
                sendSocket.write(sendPacket.sendPacket)
                sendSocket.flush()
            } else {
                Timber.d("sendSocketWrite -> sendData -> Command is null")
            }
        } catch (e: Exception) {
            Timber.e("sendSocketWrite -> sendData -> Exception -> ${e.message}")
            isConnectSocketError = true
        }
    }

    companion object {
        private const val SOCKET_TIMEOUT = 30000
        private const val SEND_PACKET_TIME = 500
        private const val RECONNECT_TIME = 5000L
        private const val ERROR_CHECK_TIME = 500L
        private const val SEND_PING_PACKET_TIME = 10000
        private const val CONNECT_SOCKET_DELAY_TIME = 1000L
        private const val CHECK_RECEIVE_TIME = 120000
        private const val PACKET_RECEIVE_INDEX_BASEMENT = 20
        private const val REQ_PACKET_START_INDEX = 12
        private const val REQ_PACKET_END_INDEX = 18

        private const val HANDLER_MESSAGE_TRY_RECONNECT = 101
        private const val HANDLER_MESSAGE_CONNECT_ERROR_CHECK = 102
    }
}