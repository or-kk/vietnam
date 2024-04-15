package io.orkk.vietnam.utils

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebounceTextWatcher(
    private val debouncePeriod: Long = 300L,
    private val onEditTextChange: (String) -> Unit,
    private val onEditTextStateChange: ((EditTextState) -> Unit)? = null
) : TextWatcher {
    private var debounceJob: Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Main).launch {
            if (!text.isNullOrEmpty()) {
                onEditTextStateChange?.invoke(EditTextState.Loading)
                delay(debouncePeriod)
            }
            onEditTextChange(text.toString())
        }
    }

    override fun afterTextChanged(s: Editable?) = Unit
}