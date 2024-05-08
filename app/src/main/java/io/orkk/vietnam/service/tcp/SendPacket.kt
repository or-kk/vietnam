package io.orkk.vietnam.service.tcp

data class SendPacket(
    var sendIndex: Int,
    var sendCommand: Int,
    var sendPacket: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SendPacket

        if (sendIndex != other.sendIndex) return false
        if (sendCommand != other.sendCommand) return false
        if (!sendPacket.contentEquals(other.sendPacket)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sendIndex
        result = 31 * result + sendCommand
        result = 31 * result + sendPacket.contentHashCode()
        return result
    }
}