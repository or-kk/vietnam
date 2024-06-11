package io.orkk.vietnam.screen

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import io.orkk.vietnam.R

abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {

    abstract val layoutId: Int

    private var _dataBinding: T? = null
    protected val dataBinding: T
        get() = _dataBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _dataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return dataBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        initListener()
    }

    override fun onPause() {
        super.onPause()
        removeListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }

    protected open fun initView() {}
    protected open fun initListener() {}
    protected open fun initObserver() {}
    protected open fun removeListener() {}
}