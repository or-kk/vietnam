package io.orkk.vietnam.data.remote.file

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.File

interface FileRepository {

    @GET
    @Streaming
    suspend fun downloadFile(
        @Url fileUrl: String,
        outputFile: File,
        progressCallback: (Float) -> Unit
    ): Result<File>

    fun saveFile(
        responseBody: ResponseBody,
        outputFile: File,
        progressCallback: (Float) -> Unit
    )
}