package org.techtown.sharablemusic.viewmodel

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build.*
import android.os.Handler
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.techtown.sharablemusic.model.Music
import java.io.FileNotFoundException
import java.util.function.BiFunction


class MainViewModel(var contentResolver: ContentResolver, val DEFAULT_ALBUM_ART : Drawable) : ViewModel() {

    private val TAG = "MainViewModel"
    private var musicMutableLiveData : MutableLiveData<List<Music>> = MutableLiveData()
    private var mObserver : ContentObserver

    init
    {
        getNewData()

        mObserver = object : ContentObserver(Handler()){
            override fun onChange(selfChange: Boolean) {
                getNewData()
            }
        }

        //컨텐트 리졸버 옵저버 등록
        contentResolver.registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mObserver)
    }

    //비동기 처리
    private fun getNewData()
    {
        CoroutineScope(Dispatchers.IO).launch {
            var list = loadMusic()
            musicMutableLiveData.postValue(list)
        }
    }

    override fun onCleared() {
        super.onCleared()
        contentResolver.unregisterContentObserver(mObserver)
    }

    fun getMusicLiveData() : LiveData<List<Music>> = musicMutableLiveData

    @WorkerThread
    private fun loadMusic() : List<Music>
    {
        var selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"

        //정렬기준 : 추가된 날짜
        var sortOrder = MediaStore.Audio.Media.DATE_ADDED +" DESC"

        //TODO : 썸네일 로딩 시간 줄이기
        var cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, selection, null, sortOrder)

        cursor?.moveToFirst()

        var artworkUri = Uri.parse("content://media/external/audio/albumart")


        var list = ArrayList<Music>()

        var trackIdIndex = cursor?.getColumnIndex(MediaStore.Audio.Media._ID)
        var albumIdIndex = cursor?.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
        var titleIndex = cursor?.getColumnIndex(MediaStore.Audio.Media.TITLE)
        var artistIndex = cursor?.getColumnIndex(MediaStore.Audio.Media.ARTIST)
        var albumIndex = cursor?.getColumnIndex(MediaStore.Audio.Media.ALBUM)
        var durationIndex = cursor?.getColumnIndex(MediaStore.Audio.Media.DURATION)
        var dataPathIndex = cursor?.getColumnIndex(MediaStore.Audio.Media.DATA)
        var albumArtIndex = cursor?.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)

        do
        {
            var track_id = cursor?.getLong(trackIdIndex!!);
            var albumId = cursor?.getLong(albumIdIndex!!);
            var title = cursor?.getString(titleIndex!!);
            var artist = cursor?.getString(artistIndex!!);
            var album = cursor?.getString(albumIndex!!);
            var mDuration = cursor?.getLong(durationIndex!!);
            var datapath = cursor?.getString(dataPathIndex!!);
            //var albumArt = cursor?.getString(albumArtIndex!!)
            //var uri = ContentUris.withAppendedId(artworkUri, albumId!!)

        /*    var albumArt : Drawable = if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                BitmapDrawable(contentResolver.loadThumbnail(uri, Size(80, 80), null))
            }
            //30보다 낮은 API 일 시
            else
            {

                try
                {
                    //해당 앨범의 앨범아트가 존재
                    BitmapDrawable(BitmapFactory.decodeFileDescriptor(contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor))
                }
                catch (e: FileNotFoundException)
                {
                    //앨범 아트 존재하지 않으면 DEFAULT
                    DEFAULT_ALBUM_ART
                }

            }*/

            //Log.d(TAG, title)
            list.add(Music(track_id!!, albumId!!, title!!, artist!!, album!!, mDuration!!, datapath!!))
        }while(cursor?.moveToNext()?:false)

        cursor?.close()




        return list
    }
}