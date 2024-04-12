package io.orkk.vietnam.utils.event

import androidx.lifecycle.Observer
import timber.log.Timber

open class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<Event<T>> {

    override fun onChanged(value: Event<T>) {
        Timber.e("onChanged")
        value.getContentIfNotHandled()?.let { event ->
            onEventUnhandledContent(event)
        }
    }
}
