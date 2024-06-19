package io.orkk.vietnam.data.remote.file

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class FileRepositoryImpl(
    private val okHttpClient: OkHttpClient
) : FileRepository {

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

    override fun unzipFile(
        zipFile: File,
        outputFile: File?,
        progressCallback: (Float) -> Unit
    ): Result<Boolean> {
        return try {
            ZipInputStream(FileInputStream(zipFile)).use { zipInputStream ->
                val totalSize = zipFile.length().toFloat()
                var unzippedSize = 0f
                var entry: ZipEntry?

                while (zipInputStream.nextEntry.also { entry = it } != null) {
                    val entryName = entry?.name
                    if (entry?.isDirectory == true) {
                        if (entryName != null) {
                            File(outputFile, entryName).mkdirs()
                        }
                    } else {
                        val newFile = entryName?.let { File(outputFile, it) }
                        if (newFile != null) {
                            newFile.parentFile?.mkdirs()
                        }

                        FileOutputStream(newFile).use { fileOutputStream ->
                            val buffer = ByteArray(4096)
                            var length: Int
                            while (zipInputStream.read(buffer).also { length = it } > 0) {
                                fileOutputStream.write(buffer, 0, length)
                                unzippedSize += length
                                progressCallback(unzippedSize / totalSize * 100)
                            }
                        }
                    }
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}