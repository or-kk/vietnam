package io.orkk.vietnam.screen

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.orkk.vietnam.databinding.DialogLoadingGolfBinding

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : Fragment() {
    private var _dataBinding: T? = null
    protected val dataBinding: T
        get() = _dataBinding!!

    private val loadingDialog: AlertDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setView(DialogLoadingGolfBinding.inflate(LayoutInflater.from(requireContext()), null, false).root)
            .setCancelable(false)
            .setBackground(ColorDrawable(Color.TRANSPARENT))
            .create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        initListener()
    }

    override fun onPause() {
        super.onPause()
        removeListener()
    }

    protected open fun initView() {}
    protected open fun initListener() {}
    protected open fun initObserver() {}
    protected open fun removeListener() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }

    fun showLoading() {
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    fun hideLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}