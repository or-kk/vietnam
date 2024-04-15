package io.orkk.vietnam.utils.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.textfield.TextInputLayout
import io.orkk.vietnam.utils.EditTextState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun TextInputLayout.setTextInputError(editTextState: EditTextState) {
    if (editTextState is EditTextState.Error) {
        isErrorEnabled = true
        error = context.getString(editTextState.resourceId)
    } else if (editTextState is EditTextState.Success) {
        isErrorEnabled = false
    }
}

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            action()
        }
    }
}