package com.example.banksampah.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.banksampah.R
import com.example.banksampah.data.remote.response.BeritaItem
import com.example.banksampah.ui.detail.DetailBeritaActivity

class ListBeritaAdapter(private val listBerita: ArrayList<BeritaItem>) : RecyclerView.Adapter<ListBeritaAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBerita: ImageView = itemView.findViewById(R.id.iv_berita)
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judulBerita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_berita, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listBerita.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val berita = listBerita[position]
        holder.tvJudul.text = berita.judul
        Glide.with(holder.itemView.context)
            .load(berita.foto)
//            .placeholder(R.drawable.placeholder)
            .into(holder.imgBerita)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailBeritaActivity::class.java)
            intent.putExtra(DetailBeritaActivity.EXTRA_BERITA, berita)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun setData(newData: List<BeritaItem>) {
        listBerita.clear()
        listBerita.addAll(newData)
        notifyDataSetChanged()
    }
}