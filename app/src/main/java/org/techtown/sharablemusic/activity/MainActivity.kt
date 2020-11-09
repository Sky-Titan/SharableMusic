package org.techtown.sharablemusic.activity

import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import org.techtown.sharablemusic.R

class MainActivity(handler: Handler?) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO : Recycler View 생성

    }
}