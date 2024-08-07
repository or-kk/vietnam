package io.orkk.vietnam.screen.intro.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.local.PreferenceRepository
import io.orkk.vietnam.data.remote.file.FileRepository
import io.orkk.vietnam.screen.BaseViewModel
import io.orkk.vietnam.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val fileRepository: FileRepository
) : BaseViewModel() {

    private val _clubIndex = MutableLiveData<String>()
    val clubIndex: LiveData<String>
        get() = _clubIndex

    private val _downloadMainUrl = MutableLiveData<String>()
    val downloadMainUrl: LiveData<String>
        get() = _downloadMainUrl

    private val _latestAppDataVersion = MutableLiveData<String>()
    val latestAppDataVersion: LiveData<String>
        get() = _latestAppDataVersion

    private val _allAppDataDownloadProgress = MutableLiveData<Int>()
    val allAppDataDownloadProgress: LiveData<Int>
        get() = _allAppDataDownloadProgress

    private val _allAppDataDownloadResult = MutableLiveData<Result<File>>()
    val allAppDataDownloadResult: LiveData<Result<File>>
        get() = _allAppDataDownloadResult

    private val _allAppDataUnzipProgress = MutableLiveData<Int>()
    val allAppDataUnzipProgress: LiveData<Int>
        get() = _allAppDataUnzipProgress

    private val _allAppDataUnzipResult = MutableLiveData<Result<Boolean>>()
    val allAppDataUnzipResult: LiveData<Result<Boolean>>
        get() = _allAppDataUnzipResult

    private val _changedAppDataDownloadProgress = MutableLiveData<Int>()
    val changedAppDataDownloadProgress: LiveData<Int>
        get() = _changedAppDataDownloadProgress

    private val _changedAppDataDownloadResult = MutableLiveData<Result<File>>()
    val changedAppDataDownloadResult: LiveData<Result<File>>
        get() = _changedAppDataDownloadResult

    private val _changedAppDataUnzipProgress = MutableLiveData<Int>()
    val changedAppDataUnzipProgress: LiveData<Int>
        get() = _changedAppDataUnzipProgress

    private val _changedAppDataUnzipResult = MutableLiveData<Result<Boolean>>()
    val changedAppDataUnzipResult: LiveData<Result<Boolean>>
        get() = _changedAppDataUnzipResult

    fun getLatestAppDataVersion() = viewModelScope.launch {
        _latestAppDataVersion.value = preferenceRepository.latestAppDataVersion.first()
    }

    fun setLatestAppDataVersion(version: String) = viewModelScope.launch {
        preferenceRepository.setLatestAppDataVersion(version)
    }

    fun getClubIndex() = viewModelScope.launch {
        _clubIndex.value = preferenceRepository.clubIndex.first()
    }

    fun getDownloadMainUrl() = viewModelScope.launch {
        _downloadMainUrl.value = preferenceRepository.downloadMainUrl.first()
    }

    fun downloadAllAppData(mainUrl: String, outputFile: File) {
        with(FileUtils) {
            val (path, fileName) = getPathAndFileName(removeFileExtension(outputFile))
            if (isFolderExists(path, fileName)) {
                setAllAppdataDownloadCompletedProgress()
            } else {
                downloadFile(makeAllAppDataDownloadUrl(mainUrl), outputFile)
            }
        }
    }

    fun downloadChangedAppData() {

    }

    private fun setAllAppdataDownloadCompletedProgress() {
        viewModelScope.launch {
            delay(COMPLETED_PROGRESS_TIME)
            _allAppDataDownloadProgress.postValue(COMPLETED_PROGRESS)
            delay(COMPLETED_PROGRESS_TIME)
            _allAppDataUnzipProgress.postValue(COMPLETED_PROGRESS)
        }
    }

    private fun downloadFile(url: String, outputFile: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = fileRepository.downloadFile(url, outputFile) { progress ->
                _allAppDataDownloadProgress.postValue(progress.toInt())
            }
            _allAppDataDownloadResult.postValue(result)
        }
    }

    fun unzipFile(zipFile: File, outputFile: File?) {
        viewModelScope.launch(Dispatchers.Default) {
            val result = fileRepository.unzipFile(zipFile, outputFile) { progress ->
                _allAppDataUnzipProgress.postValue(progress.toInt())
            }
            _allAppDataUnzipResult.postValue(result)
        }
    }

    private fun makeAllAppDataDownloadUrl(mainUrl: String): String {
        return mainUrl + APP_UPDATE_SUB_URL + clubIndex.value + ALL_APP_DATA_FILE_NAME
    }

    companion object {
        const val APP_UPDATE_SUB_URL = "app_update/"
        const val ALL_APP_DATA_FILE_NAME = "/allAppData.zip"

        const val COMPLETED_PROGRESS_TIME = 500L
        const val COMPLETED_PROGRESS = 100
    }
}