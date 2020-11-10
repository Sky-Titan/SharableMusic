package org.techtown.sharablemusic.activity

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

import org.techtown.sharablemusic.R
import org.techtown.sharablemusic.adapter.MusicListAdapter
import org.techtown.sharablemusic.viewmodel.MainViewModel
import org.techtown.sharablemusic.viewmodel.MainViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var adapter : MusicListAdapter
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //권한 체크 -> 허용 안된 경우
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            //Toast.makeText(this, "권한 필요",Toast.LENGTH_SHORT).show();
            //거부 한적 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("권한 요청").setMessage("저장소 권한을 사용하지 않으면 앱을 이용할 수 없습니다.")
                builder.setPositiveButton("확인") { dialogInterface: DialogInterface?, i: Int ->
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            0)
                }.setNegativeButton("종료") { dialogInterface: DialogInterface?, i: Int -> finish() }.create().show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        0)
            }
        }
        //허용 된 경우
        else
        {
            setRecyclerView()
        }




    }

    private fun setRecyclerView()
    {
        //Recycler View 생성
        adapter = MusicListAdapter()
        //구분선 적용
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerview_main.addItemDecoration(dividerItemDecoration)
        recyclerview_main.adapter = adapter

        setViewModel()
    }

    private fun setViewModel()
    {
        viewModel = ViewModelProvider(this, MainViewModelFactory(contentResolver, getDrawable(R.drawable.ic_baseline_audiotrack_24)!!)).get(MainViewModel::class.java)

        viewModel.getMusicLiveData().observe(this, {
            adapter.submitList(it)
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> if (grantResults.size > 0) {
                //권한허용
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "저장소 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()

                    setRecyclerView()
                }
                else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("권한 거절").setMessage("저장소 권한을 사용하지 않으면 앱을 이용할 수 없습니다.")
                    builder.setPositiveButton("종료") { dialogInterface: DialogInterface?, i: Int -> finish() }.create().show()
                }
            }
        }
    }
}