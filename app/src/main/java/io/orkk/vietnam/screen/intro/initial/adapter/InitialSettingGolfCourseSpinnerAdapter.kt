package io.orkk.vietnam.screen.intro.initial.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import io.orkk.vietnam.R

class InitialSettingGolfCourseSpinnerAdapter(
    context: Context,
    resource: Int,
    objects: List<String>
) : ArrayAdapter<String>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        if (position == 0) {
            view.setTextColor(ContextCompat.getColor(context, R.color.color_gray))
        } else {
            view.setTextColor(ContextCompat.getColor(context, R.color.color_black))
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        if (position == 0) {
            view.setTextColor(ContextCompat.getColor(context, R.color.color_gray))
        } else {
            view.setTextColor(ContextCompat.getColor(context, R.color.color_black))
        }
        return view
    }
}