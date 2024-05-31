package io.orkk.vietnam.data.remote

import io.orkk.vietnam.utils.ftp.FTPClientManager

class FileRepositoryImpl(private val ftpClientManager: FTPClientManager) : FileRepository {

    override suspend fun downloadFile(remoteFilePath: String, localFilePath: String): Result<Boolean> {
        return try {
            val success = ftpClientManager.downloadFile(remoteFilePath, localFilePath)
            if (success) {
                Result.success(true)
            } else {
                Result.failure(Exception("Download failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}