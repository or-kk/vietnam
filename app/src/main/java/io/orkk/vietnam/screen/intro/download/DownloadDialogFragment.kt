package io.orkk.vietnam.screen.intro.download

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.DialogDownloadBinding
import io.orkk.vietnam.screen.BaseDialogFragment
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class DownloadDialogFragment() : BaseDialogFragment<DialogDownloadBinding>(){
    override val layoutId: Int = R.layout.dialog_download

    private val downloadViewModel: DownloadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = downloadViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    override fun initView() {
        super.initView()
        val downloadUrl = "http://119.205.217.251:8091/app_update/926/app_version.txt"
        val outputFile = File(activity?.getExternalFilesDir(null), "downloaded_file")
        downloadViewModel.downloadFile(downloadUrl, outputFile)
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            downloadViewModel.downloadResult.observe(viewLifecycleOwner, Observer { result ->
                result.onSuccess { file ->
                    Timber.d("download success file size -> $file")
                }.onFailure { exception ->
                    Timber.e("download fail -> ${exception.message}")
                }
            })

            downloadViewModel.downloadProgress.observe(viewLifecycleOwner, Observer { progress ->
                dataBinding.pbDownload.progress = progress.toInt()
            })
        }
    }
}