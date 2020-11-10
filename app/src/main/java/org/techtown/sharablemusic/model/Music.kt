package org.techtown.sharablemusic.model

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

data class Music(var mId : Long, var albumId : Long, var title : String, var artist : String, var album : String, var duration : Long, var dataPath : String) {
     // mId 오디오 고유 ID
    //AlbumId 오디오 앨범아트 ID
    //Title 타이틀 정보
    //Artist 아티스트 정보
    // Album 앨범 정보
    // Duration  재생시간
    // DataPath 실제 데이터 위치
    // albumArt 앨범아트

    lateinit var albumArt : String

    constructor(mId : Long, albumId : Long, title : String, artist : String, album : String, duration : Long, dataPath : String, albumArt : String) : this(mId, albumId, title, artist, album, duration, dataPath)
    {
        this.albumArt = albumArt

    }


     companion object {
         @JvmField
         var DIFF_CALLBACK: DiffUtil.ItemCallback<Music> = object : DiffUtil.ItemCallback<Music>() {
             override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                 return oldItem.mId == newItem.mId
             }

             override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                 return oldItem == newItem
             }
         }
     }
}