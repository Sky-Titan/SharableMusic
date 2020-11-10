package org.techtown.sharablemusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import org.techtown.sharablemusic.MyViewHolder
import org.techtown.sharablemusic.R
import org.techtown.sharablemusic.databinding.MusicItemLayoutBinding
import org.techtown.sharablemusic.model.Music

class MusicListAdapter : ListAdapter<Music, MyViewHolder<MusicItemLayoutBinding>>(Music.DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<MusicItemLayoutBinding> {
        val inflater =LayoutInflater.from(parent.context)

        return MyViewHolder(inflater.inflate(R.layout.music_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder<MusicItemLayoutBinding>, position: Int) {

        holder.binding().item = currentList[position]
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}