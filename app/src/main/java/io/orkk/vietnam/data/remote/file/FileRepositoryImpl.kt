package io.orkk.vietnam.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.orkk.vietnam.model.config.ClubInfoConfigItem
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class FileRepositoryImpl(
    private val okHttpClient: OkHttpClient,
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : FileRepository {
    override suspend fun fetchClubInfoConfig(
        onSuccess: (List<ClubInfoConfigItem>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    try {
                        val jsonString = firebaseRemoteConfig.getString("club_info")
                        val gson = Gson()
                        val listType = object : TypeToken<List<ClubInfoConfigItem>>() {}.type
                        val golfClubFetchInfoList: List<ClubInfoConfigItem> = gson.fromJson(jsonString, listType)
                        onSuccess(golfClubFetchInfoList)
                    } catch (e: Exception) {
                        onFailure(e)
                    }
                }
            }
    }

    override suspend fun downloadFile(
        fileUrl: String,
        outputFile: File,
        progressCallback: (Float) -> Unit
    ): Result<File> {
        return try {
            val request = Request.Builder().url(fileUrl).build()
            val response = okHttpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                return Result.failure(IOException("Failed to download file: ${response.code}"))
            }

            val body = response.body ?: return Result.failure(IOException("Empty response body"))

            saveFile(body, outputFile, progressCallback)
            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun saveFile(
        responseBody: ResponseBody,
        outputFile: File,
        progressCallback: (Float) -> Unit
    ) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            inputStream = responseBody.byteStream()
            outputStream = FileOutputStream(outputFile)

            val buffer = ByteArray(4096)
            var downloaded: Long = 0
            val target: Long = responseBody.contentLength()
            var read: Int

            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
                downloaded += read
                progressCallback(downloaded.toFloat() / target * 100)
            }
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }
}