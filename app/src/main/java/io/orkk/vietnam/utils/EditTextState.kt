package io.orkk.vietnam.utils

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class EditTextState {
    @Parcelize
    object Loading : EditTextState(), Parcelable

    @Parcelize
    data class Error(@StringRes val resourceId: Int) : EditTextState(), Parcelable

    @Parcelize
    object Success : EditTextState(), Parcelable
}