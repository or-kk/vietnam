package io.orkk.vietnam.model.tcpip

data class ReceivePacket(
    var command: Int = 0,
    var length: Short = 0,
    var data: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReceivePacket

        if (command != other.command) return false
        if (length != other.length) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = command
        result = 31 * result + length
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }

    fun getByteFromData(index: Int): Byte = if (data != null && data!!.size > index) data!![index] else 0
}
