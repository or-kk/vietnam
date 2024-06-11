package io.orkk.vietnam.utils.extension

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

fun Context.adjustDialogFragment(dialogFragment: DialogFragment, width: Float, height: Float) {
    val windowManager = dialogFragment.requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        @Suppress("DEPRECATION")
        val display = windowManager.defaultDisplay
        val size = Point()

        @Suppress("DEPRECATION")
        display.getSize(size)
        val window = dialogFragment.dialog?.window
        val x = (size.x * width).toInt()
        val y = (size.y * height).toInt()
        window?.setLayout(x, y)
    } else {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds

        val window = dialogFragment.dialog?.window
        val x = (bounds.width() * width).toInt()
        val y = (bounds.height() * height).toInt()
        window?.setLayout(x, y)
    }
}