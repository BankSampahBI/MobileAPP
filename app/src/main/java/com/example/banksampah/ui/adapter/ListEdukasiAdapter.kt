package com.example.banksampah.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.banksampah.R
import com.example.banksampah.ui.Model.Edukasi
import com.example.banksampah.ui.detail.DetailEdukasiActivity

class ListEdukasiAdapter(
    private val fullList: ArrayList<Edukasi>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showAll = false
    private val PREVIEW_COUNT = 3
    private val TYPE_ITEM = 0
    private val TYPE_VIEW_MORE = 1

    override fun getItemViewType(position: Int): Int {
        return if (!showAll && position == minOf(PREVIEW_COUNT, fullList.size)) {
            TYPE_VIEW_MORE
        } else {
            TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_edukasi, parent, false)
            EdukasiViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_more, parent, false)
            ViewMoreViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (showAll || fullList.size <= PREVIEW_COUNT) {
            fullList.size
        } else {
            PREVIEW_COUNT + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EdukasiViewHolder) {
            val edukasi = fullList[position]
            holder.ivSampah.setImageResource(edukasi.gambar)
            holder.tvJenis.text = edukasi.jenis
            holder.tvHarga.text = edukasi.harga
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailEdukasiActivity::class.java)
                intent.putExtra("key_edukasi", edukasi)
                holder.itemView.context.startActivity(intent)
            }
        } else if (holder is ViewMoreViewHolder) {
            holder.tvViewMore.setOnClickListener {
                showAll = true
                notifyDataSetChanged()
            }
        }
    }

    class EdukasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSampah: ImageView = itemView.findViewById(R.id.iv_sampah)
        val tvJenis: TextView = itemView.findViewById(R.id.tv_jenisSampah)
        val tvHarga: TextView = itemView.findViewById(R.id.tv_harga)
    }

    class ViewMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvViewMore: TextView = itemView.findViewById(R.id.tv_view_more)
    }
}
