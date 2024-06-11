package io.orkk.vietnam.utils.bindingadapter

import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import io.orkk.vietnam.model.config.ClubInfo
import io.orkk.vietnam.screen.intro.initial.adapter.InitialSettingGolfCourseSpinnerAdapter

@BindingAdapter("app:setGolfClubList")
fun setItems(view: AppCompatSpinner, items: List<ClubInfo>?) {
    items?.let {
        val adapter = InitialSettingGolfCourseSpinnerAdapter(view.context, android.R.layout.simple_spinner_item, items.map { it.clubName })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.adapter = adapter
    }
}