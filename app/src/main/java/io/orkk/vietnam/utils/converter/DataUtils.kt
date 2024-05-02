package io.orkk.vietnam.utils.converter

import io.orkk.vietnam.model.Constants
import io.orkk.vietnam.model.language.LanguageType
import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DataUtils {

    companion object {

        fun convertByteToString(bytes: ByteArray): String? {
            try {
                return when (Constants.language) {
                    LanguageType.LANGUAGE_TYPE_KOR.code -> {
                        String(bytes, charset(LanguageType.LANGUAGE_TYPE_KOR.encoding))
                    }
                    LanguageType.LANGUAGE_TYPE_JPN.code -> {
                        String(bytes, charset(LanguageType.LANGUAGE_TYPE_JPN.encoding))
                    }
                    LanguageType.LANGUAGE_TYPE_USA.code -> {
                        String(bytes, charset(LanguageType.LANGUAGE_TYPE_USA.encoding))
                    }
                    else -> {
                        String(bytes, charset(LanguageType.LANGUAGE_TYPE_KOR.encoding))
                    }
                }
            } catch (e: UnsupportedEncodingException) {
                Timber.e("DataUtils -> convertByteToString UnsupportedEncodingException -> ${e.message}")
            }

            return null
        }

        fun convertStringToByte(value: String): ByteArray? {
            try {
                return when (Constants.language) {
                    LanguageType.LANGUAGE_TYPE_KOR.code -> {
                        value.toByteArray(charset(LanguageType.LANGUAGE_TYPE_KOR.encoding))
                    }
                    LanguageType.LANGUAGE_TYPE_JPN.code -> {
                        value.toByteArray(charset(LanguageType.LANGUAGE_TYPE_JPN.encoding))
                    }
                    LanguageType.LANGUAGE_TYPE_USA.code -> {
                        value.toByteArray(charset(LanguageType.LANGUAGE_TYPE_USA.encoding))
                    }
                    else -> {
                        value.toByteArray(charset(LanguageType.LANGUAGE_TYPE_KOR.encoding))
                    }

                }
            } catch (e: UnsupportedEncodingException) {
                Timber.e("DataUtils -> convertStringToByte UnsupportedEncodingException -> ${e.message}")
            }

            return null
        }

        fun convertIntToByte(value: Int): ByteArray {
            val bytes = ByteArray(4)
            bytes[3] = (value and 0xFF).toByte()
            bytes[2] = (value shr 8 and 0xFF).toByte()
            bytes[1] = (value shr 16 and 0xFF).toByte()
            bytes[0] = (value shr 24 and 0xFF).toByte()
            return bytes
        }

        fun convertIntToData(value: Int): ByteArray {
            val bytes = ByteArray(4)
            bytes[3] = (value shr 24 and 0xFF).toByte()
            bytes[2] = (value shr 16 and 0xFF).toByte()
            bytes[1] = (value shr 8 and 0xFF).toByte()
            bytes[0] = (value and 0xFF).toByte()
            return bytes
        }

        fun convertByteToInt(byteArray: ByteArray, offSet: Int): Int {
            var result = byteArray[offSet + 3].toInt() and 0xFF
            result = result or (byteArray[offSet + 2].toInt() shl 8 and 0xFF00)
            result = result or (byteArray[offSet + 1].toInt() shl 16 and 0xFF0000)
            result = result or (byteArray[offSet + 0].toInt() shl 24)

            return result
        }

        fun convertDoubleToByteArray(value: Double): ByteArray {
            val longValue = java.lang.Double.doubleToLongBits(value)
            return convertLongToByteArray(longValue)
        }

        private fun convertLongToByteArray(num: Long): ByteArray {
            val arBytes = ByteArray(8)
            arBytes[7] = (num shr 56 and 0xffL).toByte()
            arBytes[6] = (num shr 48 and 0xffL).toByte()
            arBytes[5] = (num shr 40 and 0xffL).toByte()
            arBytes[4] = (num shr 32 and 0xffL).toByte()
            arBytes[3] = (num shr 24 and 0xffL).toByte()
            arBytes[2] = (num shr 16 and 0xffL).toByte()
            arBytes[1] = (num shr 8 and 0xffL).toByte()
            arBytes[0] = (num and 0xffL).toByte()
            return arBytes
        }

        fun convertIntToByteArray(num: Int): ByteArray {
            val arBytes = ByteArray(4)
            arBytes[3] = (num shr 24 and 0xff).toByte()
            arBytes[2] = (num shr 16 and 0xff).toByte()
            arBytes[1] = (num shr 8 and 0xff).toByte()
            arBytes[0] = (num and 0xff).toByte()
            return arBytes
        }

        fun convertDecimalToHexString(value: Int): String = Integer.toHexString(value)

        fun convertStringToByteArray(value: String?): ByteArray? {
            return try {
                value?.toByteArray(charset("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                Timber.e(e.message)
                null
            }
        }

        fun convertShortToByteArray(num: Short): ByteArray {
            val ret = ByteArray(2)
            ret[1] = (num.toInt() and 0xff).toByte()
            ret[0] = (num.toInt() shr 8 and 0xff).toByte()
            return ret
        }

        fun convertByteToFloat(bytes: ByteArray): Float = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).float

        fun convertIntToHexString(value: Int): String = Integer.toHexString(value)
    }
}