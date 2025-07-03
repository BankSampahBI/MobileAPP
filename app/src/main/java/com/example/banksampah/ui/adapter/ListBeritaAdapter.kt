package com.example.banksampah.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.ui.model.Berita
import com.example.banksampah.ui.detail.DetailBeritaActivity

class ListBeritaAdapter(private val listBerita: ArrayList<Berita>) : RecyclerView.Adapter<ListBeritaAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgBerita: ImageView = itemView.findViewById(R.id.iv_berita)
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judulBerita)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_berita, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listBerita.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (foto, judul) = listBerita[position]
        holder.imgBerita.setImageResource(foto)
        holder.tvJudul.text = judul
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailBeritaActivity::class.java)
            intentDetail.putExtra("key_berita", listBerita[holder.adapterPosition])
            holder.itemView.context.startActivity(intentDetail)
        }
    }
}