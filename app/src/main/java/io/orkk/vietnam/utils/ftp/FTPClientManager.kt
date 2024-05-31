package io.orkk.vietnam.utils.ftp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileOutputStream

class FTPClientManager() {
    private val server = "ftp.example.com"
    private val port = 21
    private val user = "username"
    private val password = "password"

    suspend fun downloadFile(
        remoteFilePath: String,
        localFilePath: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val ftpClient = FTPClient()
            try {
                ftpClient.connect(server, port)
                ftpClient.login(user, password)
                ftpClient.enterLocalPassiveMode()
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE)

                val outputStream = FileOutputStream(File(localFilePath))
                val success = ftpClient.retrieveFile(remoteFilePath, outputStream)
                outputStream.close()
                success
            } catch (e: Exception) {
                e.printStackTrace()
                false
            } finally {
                if (ftpClient.isConnected) {
                    ftpClient.logout()
                    ftpClient.disconnect()
                }
            }
        }
    }
}