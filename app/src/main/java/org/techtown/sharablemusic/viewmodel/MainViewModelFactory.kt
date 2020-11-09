package org.techtown.sharablemusic.viewmodel

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(var contentResolver: ContentResolver) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(contentResolver) as T
        return null as T
    }
}