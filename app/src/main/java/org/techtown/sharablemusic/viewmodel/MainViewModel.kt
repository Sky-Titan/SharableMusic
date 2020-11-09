package org.techtown.sharablemusic.viewmodel

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.techtown.sharablemusic.model.Music


class MainViewModel(var contentResolver: ContentResolver) : ViewModel() {

    private var musicMutableLiveData : MutableLiveData<List<Music>> = MutableLiveData()
    private var mObserver : ContentObserver

    init {

        mObserver = object : ContentObserver(Handler()){
            override fun deliverSelfNotifications(): Boolean {
                return super.deliverSelfNotifications()
            }

            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)

                musicMutableLiveData.postValue(loadMusic())
            }

            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
            }
        }

        //컨텐트 리졸버 옵저버 등록
        contentResolver.registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mObserver)
    }

    override fun onCleared() {
        super.onCleared()
        contentResolver.unregisterContentObserver(mObserver)

    }

    fun getMusicLiveData() : LiveData<List<Music>> = musicMutableLiveData

    private fun loadMusic() : List<Music>
    {
        var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        var sortOrder = MediaStore.Audio.Media.TITLE +" ASC"
        var cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, null, sortOrder)

        cursor?.moveToFirst()

        var list = ArrayList<Music>()

        while(cursor?.moveToNext()?:false)
        {
            var track_id = cursor?.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            var albumId = cursor?.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            var title = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            var album = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            var artist = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            var mDuration = cursor?.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            var datapath = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            list.add(Music(track_id!!, albumId!!, title!!, album!!, artist!!, mDuration!!, datapath!!))
        }

        return list

    }
}