package io.orkk.vietnam.utils

//noinspection SuspiciousImport
import android.R
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import io.orkk.vietnam.screen.signin.SignInViewModel

@BindingAdapter("dropdownItems")
fun setDropdownItems(textInputLayout: TextInputLayout, viewModel: SignInViewModel) {
    val adapter = ArrayAdapter(
        textInputLayout.context,
        R.layout.simple_dropdown_item_1line,
        viewModel.loginTypes
    )
    val exposedDropdownMenu = AppCompatAutoCompleteTextView(textInputLayout.context).apply {
        setAdapter(adapter)
        setOnItemClickListener { _, _, position, _ ->
            viewModel.setSelectedLoginType(viewModel.loginTypes[position])
        }
    }
    textInputLayout.addView(exposedDropdownMenu)
}