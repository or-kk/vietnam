package io.orkk.vietnam.service

import javax.inject.Inject

class SendPacketQueue @Inject constructor() {
    private var commandArrayDeque = ArrayDeque<Int>(MAX_PACKET_SIZE)
    private var dataArrayDeque = ArrayDeque<Any>(MAX_PACKET_SIZE)

    private var packetCommand: Int = 0
    var packetData: Any = 0

    fun initQueue() {
        commandArrayDeque = ArrayDeque<Int>(MAX_PACKET_SIZE)
        dataArrayDeque = ArrayDeque<Any>(MAX_PACKET_SIZE)
    }

    fun enQueue(commend: Int, objects: Any?) {
        commandArrayDeque.add(commend)
        if (objects != null) {
            dataArrayDeque.add(objects)
        }
    }

    fun deQueue(): Int {
        packetCommand = commandArrayDeque.first()
        packetData = dataArrayDeque.first()

        commandArrayDeque.removeFirst()
        dataArrayDeque.removeFirst()

        return packetCommand
    }

    fun check(): Boolean {
        return !(commandArrayDeque.isEmpty() && dataArrayDeque.isEmpty())
    }

    companion object {
        private const val MAX_PACKET_SIZE = 256
    }
}