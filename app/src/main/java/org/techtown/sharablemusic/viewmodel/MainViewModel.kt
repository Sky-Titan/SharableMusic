package org.techtown.sharablemusic.viewmodel

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.techtown.sharablemusic.model.Music
import java.io.FileNotFoundException
import java.io.InputStream


class MainViewModel(var contentResolver: ContentResolver, val DEFAULT_ALBUM_ART : Drawable) : ViewModel() {

    private val TAG = "MainViewModel"
    private var musicMutableLiveData : MutableLiveData<List<Music>> = MutableLiveData()
    private var mObserver : ContentObserver

    init {
        getNewData()
        mObserver = object : ContentObserver(Handler()){
            override fun deliverSelfNotifications(): Boolean {
                return super.deliverSelfNotifications()
            }

            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)

                getNewData()
            }

            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
            }
        }

        //컨텐트 리졸버 옵저버 등록
        contentResolver.registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mObserver)
    }


    //비동기 처리
    private fun getNewData()
    {
        CoroutineScope(Dispatchers.Default).launch {
            var list = loadMusic()
            musicMutableLiveData.postValue(list)
        }
    }

    override fun onCleared() {
        super.onCleared()
        contentResolver.unregisterContentObserver(mObserver)
    }

    fun getMusicLiveData() : LiveData<List<Music>> = musicMutableLiveData

    private fun loadMusic() : List<Music>
    {
        var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        //정렬기준 : 추가된 날짜
        var sortOrder = MediaStore.Audio.Media.DATE_ADDED +" DESC"
        //TODO : 썸네일 로딩 시간 줄이기
        var cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, null, sortOrder)

        cursor?.moveToFirst()

        var list = ArrayList<Music>()

        do
        {
            var track_id = cursor?.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            var albumId = cursor?.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            var title = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            var artist = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            var album = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            var mDuration = cursor?.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            var datapath = cursor?.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

            var artworkUri = Uri.parse("content://media/external/audio/albumart")
            var uri = ContentUris.withAppendedId(artworkUri, albumId!!)

            var albumArt = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                BitmapDrawable(contentResolver.loadThumbnail(uri, Size(80, 80), null))
            } else {
                try {
                    BitmapDrawable(BitmapFactory.decodeStream(contentResolver.openInputStream(uri)))
                }
                catch (e: FileNotFoundException)
                {
                    DEFAULT_ALBUM_ART
                }


            }
            //Log.d(TAG, title)
            list.add(Music(track_id!!, albumId!!, title!!, artist!!, album!!, mDuration!!, datapath!!, albumArt))
        }while(cursor?.moveToNext()?:false)

        cursor?.close()

        return list
    }
}