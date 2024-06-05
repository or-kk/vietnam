package io.orkk.vietnam.screen.intro.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.orkk.vietnam.data.remote.file.FileRepository
import io.orkk.vietnam.screen.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val fileRepository: FileRepository
) : BaseViewModel() {

    private val _downloadProgress = MutableLiveData<Float>()
    val downloadProgress: LiveData<Float> get() = _downloadProgress

    private val _downloadResult = MutableLiveData<Result<File>>()
    val downloadResult: LiveData<Result<File>> get() = _downloadResult

    fun downloadFile(url: String, outputFile: File) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = fileRepository.downloadFile(url, outputFile) { progress ->
                _downloadProgress.postValue(progress)
            }
            _downloadResult.postValue(result)
        }
    }
}