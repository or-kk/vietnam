package io.orkk.vietnam.utils.bindingadapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

@SuppressLint("SetTextI18n")
@BindingAdapter("app:progressText")
fun setProgressText(textView: TextView, progress: Int?) {
    textView.text = "${progress ?: 0}%"
}

@BindingAdapter("isGone")
fun setIsGone(view: View, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}