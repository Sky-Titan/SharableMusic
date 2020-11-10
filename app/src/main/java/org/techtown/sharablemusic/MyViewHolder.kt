package org.techtown.sharablemusic

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder <T : ViewDataBinding>(v: View) : RecyclerView.ViewHolder(v) {

    private val binding: T
    fun binding(): T {
        return binding
    }

    init {
        binding = DataBindingUtil.bind<ViewDataBinding>(v) as T
    }

}