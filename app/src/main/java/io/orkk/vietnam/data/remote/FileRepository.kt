package io.orkk.vietnam.data.remote

interface FileRepository {

    suspend fun downloadFile(remoteFilePath: String, localFilePath: String): Result<Boolean>
}