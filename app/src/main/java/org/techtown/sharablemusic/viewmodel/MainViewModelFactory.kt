package org.techtown.sharablemusic.viewmodel

import android.content.ContentResolver
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(var contentResolver: ContentResolver, var DEFAULT_ALBUM_ART : Drawable) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(contentResolver, DEFAULT_ALBUM_ART) as T
        return null as T
    }
}