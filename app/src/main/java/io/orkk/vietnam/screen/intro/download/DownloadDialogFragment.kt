package io.orkk.vietnam.screen.intro.download

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import io.orkk.vietnam.R
import io.orkk.vietnam.databinding.DialogDownloadBinding
import io.orkk.vietnam.screen.BaseDialogFragment
import io.orkk.vietnam.utils.extension.adjustDialogFragment
import io.orkk.vietnam.utils.extension.launchAndRepeatWithViewLifecycle
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@AndroidEntryPoint
class DownloadDialogFragment() : BaseDialogFragment<DialogDownloadBinding>() {
    override val layoutId: Int = R.layout.dialog_download

    private val downloadViewModel: DownloadViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        downloadViewModel.getClubIndex()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = downloadViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        context?.adjustDialogFragment(this@DownloadDialogFragment, 0.8f, 0.45f)
        isCancelable = false
    }

    override fun initView() {
        super.initView()
    }

    override fun initObserver() {
        super.initObserver()

        launchAndRepeatWithViewLifecycle {
            launch {
                downloadViewModel.clubIndex.observe(viewLifecycleOwner, Observer {
                    downloadViewModel.getDownloadMainUrl()
                })
            }

            launch {
                downloadViewModel.downloadMainUrl.observe(viewLifecycleOwner, Observer {
                    val outputFile = File(activity?.getExternalFilesDir(null), ALL_APP_DATA_OUTPUT_FILE)
                    downloadViewModel.downloadAppdata(it, outputFile)
                })
            }

            launch {
                downloadViewModel.allAppDataDownloadResult.observe(viewLifecycleOwner, Observer { result ->
                    result.onSuccess { file ->
                        Timber.d("download success all app data, file size -> $file")
                        downloadViewModel.unzipFile(File(ALL_APP_DATA_OUTPUT_FILE), null)
                    }.onFailure { exception ->
                        Timber.e("download all app data exception -> ${exception.message}")
                        Toasty.error(requireActivity(), R.string.download_all_app_data_fail_message, Toast.LENGTH_SHORT, false).show()
                    }
                })
            }

            launch {
                downloadViewModel.allAppDataUnzipResult.observe(viewLifecycleOwner, Observer { result ->
                    result.onSuccess { isUnzip ->
                        if (isUnzip) {
                            Timber.d("unzip all app data")
                        } else {
                            Timber.e("unzip all app data error")
                        }
                    }
                })
            }
        }
    }

    companion object {
        const val ALL_APP_DATA_OUTPUT_FILE = "GolfAppData.zip"
    }
}